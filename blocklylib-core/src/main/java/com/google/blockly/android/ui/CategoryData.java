package com.google.blockly.android.ui;

import android.view.View;
import android.widget.Button;

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

    View setup_btn = null;
    View loop_btn = null;
    View upload_btn = null;

    View method_btn = null;

    View home_btn = null;

    View reset_btn = null;





    //드론 관련

    View drone_coding_btn = null;

    View drone_cali_btn = null;

    View drone_wifi_btn = null;

    public View getDrone_start_btn() {
        return drone_start_btn;
    }

    public void setDrone_start_btn(View drone_start_btn) {
        this.drone_start_btn = drone_start_btn;
    }

    View drone_start_btn = null;

    public View getDrone_wifi_btn() {
        return drone_wifi_btn;
    }

    public void setDrone_wifi_btn(View drone_wifi_btn) {
        this.drone_wifi_btn = drone_wifi_btn;
    }

    public View getDrone_battery_btn() {
        return drone_battery_btn;
    }

    public void setDrone_battery_btn(View drone_battery_btn) {
        this.drone_battery_btn = drone_battery_btn;
    }

    View drone_battery_btn = null;

    public View getDrone_coding_btn() {
        return drone_coding_btn;
    }

    public void setDrone_coding_btn(View drone_coding_btn) {
        this.drone_coding_btn = drone_coding_btn;
    }

    public View getDrone_cali_btn() {
        return drone_cali_btn;
    }

    public void setDrone_cali_btn(View drone_cali_btn) {
        this.drone_cali_btn = drone_cali_btn;
    }

    public View getDrone_upload_btn() {
        return drone_upload_btn;
    }

    public void setDrone_upload_btn(View drone_upload_btn) {
        this.drone_upload_btn = drone_upload_btn;
    }

    View drone_upload_btn = null;

    Button simulator_btn = null;

    WorkspacePoint rtp, ptp, ntp;

    public View getUpload_btn() {
        return upload_btn;
    }

    public void setSetup_btn(View setup_btn) {
        this.setup_btn = setup_btn;
    }
    public void setUpload_btn(View upload_btn) {
        this.upload_btn = upload_btn;
    }

    public View getHome_btn() {
        return home_btn;
    }

    public void setHome_btn(View home_btn) {
        this.home_btn = home_btn;
    }
    public View getReset_btn() {
        return reset_btn;
    }
    public View getSetUp_btn() {
        return setup_btn;
    }

    public void setSimulator_btn(Button simulator_btn) {
        this.simulator_btn = simulator_btn;
    }
    public View getSimulator_btn() {
        return simulator_btn;
    }

    public void setReset_btn(View reset_btn) {
        this.reset_btn = reset_btn;
    }



    public View getLoop_btn() {
        return loop_btn;
    }

    public void setLoop_btn(View loop_btn) {
        this.loop_btn = loop_btn;
    }



    public View getMethod_btn() {
        return method_btn;
    }

    public void setMethod_btn(View method_btn) {
        this.method_btn = method_btn;
    }



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