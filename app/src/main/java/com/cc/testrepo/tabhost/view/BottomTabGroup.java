package com.cc.testrepo.tabhost.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenchong on 16/11/29.
 */

public class BottomTabGroup extends LinearLayout {

    private int INVALID_SELECTED_TAB = -1;

    private OnClickListener mCommonOnClickListener;
    private OnTagSelectListener mOnTagSelectListener;
    private int mCurrentSelectedTab = INVALID_SELECTED_TAB;

    public BottomTabGroup(Context context) {
        this(context, null);
    }

    public BottomTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setClipChildren(false);

        mCommonOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTagSelectListener != null) {
                    int position = indexOfChild(v);
                    if (position != mCurrentSelectedTab) {
                        View currentSelectedTabView = getChildAt(mCurrentSelectedTab);
                        if (currentSelectedTabView != null) {
                            currentSelectedTabView.setSelected(false);
                        }
                        v.setSelected(true);
                        mCurrentSelectedTab = position;
                        mOnTagSelectListener.onTagSelect(v, position);
                    }
                }
            }
        };
    }

    public void setTag(List<Drawable> drawableList, List<String> textList) {
        removeAllViews();
        if (drawableList != null && textList != null && drawableList.size() == textList.size()) {
            int size = drawableList.size();
            Context context = getContext();
            for (int i=0; i!=size; ++i) {
                TextView tag = new TextView(context);
                tag.setGravity(Gravity.CENTER);
                tag.setText(textList.get(i));
                Drawable drawable = drawableList.get(i);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tag.setCompoundDrawables(null, drawable, null, null);
                BadgeViewGroup badgeWrapper = new BadgeViewGroup(context);
                badgeWrapper.addView(tag);
                LinearLayout.LayoutParams lp = new LayoutParams(0, -1, 1);
                addView(badgeWrapper, lp);

                badgeWrapper.setOnClickListener(mCommonOnClickListener);
            }
        }
    }

    public void setOnTagSelectListener(OnTagSelectListener listener) {
        mOnTagSelectListener = listener;
    }

    public interface OnTagSelectListener {
        void onTagSelect(View view, int position);
    }
}
