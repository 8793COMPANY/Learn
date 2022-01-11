package com.google.blockly.android.ui;

import android.view.View;

public class CategoryData {
    private static CategoryData categoryData = new CategoryData();

    boolean closed = true;

    private CategoryData() {}

    public static CategoryData getInstance() {
        return categoryData;
    }

    int position = -1;
    boolean selection = false;

    public View getUpload_btn() {
        return upload_btn;
    }

    public void setUpload_btn(View upload_btn) {
        this.upload_btn = upload_btn;
    }

    View upload_btn = null;

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public boolean isSelection() {
        return selection;
    }

    public int getPosition() {
        return position;
    }


    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed){
        this.closed = closed;
    }

}