package com.google.blockly.android.ui;

import androidx.annotation.Nullable;

public class PushEvent
{
    // the pushlist object being sent using the bus
    private int pos;
    private boolean isClose;
    private boolean selection;

    public PushEvent(int pushPosition, boolean isClose, boolean selection)
    {
        this.pos = pushPosition;
        this.isClose = isClose;
        this.selection = selection;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}