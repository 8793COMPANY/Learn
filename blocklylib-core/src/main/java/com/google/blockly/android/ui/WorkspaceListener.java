package com.google.blockly.android.ui;

import android.view.MotionEvent;
import android.view.View;

import com.google.blockly.android.control.BlocklyController;

public class WorkspaceListener implements View.OnTouchListener {

    private BlocklyController mController;

    public WorkspaceListener(BlocklyController controller) {
        mController = controller;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
