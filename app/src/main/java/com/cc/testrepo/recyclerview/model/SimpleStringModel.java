package com.cc.testrepo.recyclerview.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleStringModel {

    public String content;
    public int viewType;

    public SimpleStringModel(String content, int viewType) {
        this.content = content;
        this.viewType = viewType;
    }

    public static class SimpleStringModelProvider {

        public static int sPositionOffset;

        private static final int COUNT = 20;

        public List<SimpleStringModel> get() {
            return get(COUNT);
        }

        public List<SimpleStringModel> get(int count) {
            List<SimpleStringModel> result = new ArrayList<>(count);
            for (int i=0; i<count; ++i) {
                result.add(new SimpleStringModel("Position : " + (i + sPositionOffset), getItemViewType(i)));
            }
            sPositionOffset += count;
            return result;
        }

        private int getItemViewType(int position) {
            return position % 10 - 5;
        }

    }
}
