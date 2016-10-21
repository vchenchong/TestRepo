package com.cc.testrepo.phoneview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.cc.testrepo.R;
import com.cc.testrepo.base.Logger;

public class PhoneView extends EditText {

    public static final int FORMATTER_TYPE_DIVIDER = 0;

    private int mFormatterType;
    private String mDividerString;

    public PhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PhoneView, 0, 0);
        for (int i=0, size=a.length(); i<size; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.PhoneView_formatter:
                    mFormatterType = a.getInt(R.styleable.PhoneView_formatter, FORMATTER_TYPE_DIVIDER);
                    break;
                case R.styleable.PhoneView_formatter_divider:
                    mDividerString = a.getString(R.styleable.PhoneView_formatter_divider);
                    break;
            }
        }
        a.recycle();

        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_PHONE);
        setSingleLine(true);
        setFilters(new InputFilter[] {new InputFilter.LengthFilter(13)});

        switch (mFormatterType) {
            case FORMATTER_TYPE_DIVIDER:
                addTextChangedListener(new DividerPhoneNumberFormattingTextWatcher(mDividerString));
                break;
        }
    }

    public String getPhoneNumber() {
        Editable text = getText();
        if (text == null) {
            return "";
        }
        return text.toString().replaceAll("[^\\d]", "");
    }

    private class DividerPhoneNumberFormattingTextWatcher implements TextWatcher {

        private String mDivider;

        private boolean mIsAddDigit;

        private boolean mIsJustEdit;

        private int mDigitCountAfterCursor;

        DividerPhoneNumberFormattingTextWatcher(String divider) {
            mDivider = divider;
            if (mDivider == null) {
                mDivider = "";
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mDigitCountAfterCursor = s.length() - getSelectionStart();
            mIsAddDigit = after > count;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mIsJustEdit) {
                String phone = s.toString().replaceAll("[^\\d]", "");
                int length = phone.length();
                if (mIsAddDigit) {
                    if (length > 7) {
                        mIsJustEdit = true;
                        String formatted = phone.substring(0, 3) + mDivider + phone.substring(3, 7)
                                + mDivider + phone.substring(7, length);
                        setText(formatted);
                        setSelection(length + 2 - mDigitCountAfterCursor);
                    } else if (length > 3) {
                        mIsJustEdit = true;
                        String formatted = phone.substring(0, 3) + mDivider + phone.substring(3, length);
                        setText(formatted);
                        setSelection(length + 1 - mDigitCountAfterCursor);
                    }
                }

                Logger.log("afterTextChanged : " + s);
            } else {
                mIsJustEdit = false;
            }
        }
    }

}
