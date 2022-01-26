package com.google.blockly.android.ui;

import android.view.View;

import com.google.blockly.model.WorkspacePoint;

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

    WorkspacePoint rtp, ptp, ntp;

    public WorkspacePoint getRtp() {
        return rtp;
    }

    public void setRtp(WorkspacePoint rtp) {
        this.rtp = rtp;
    }

    public WorkspacePoint getPtp() {
        return ptp;
    }

    public void setPtp(WorkspacePoint ptp) {
        this.ptp = ptp;
    }

    public WorkspacePoint getNtp() {
        return ntp;
    }

    public void setNtp(WorkspacePoint ntp) {
        this.ntp = ntp;
    }

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