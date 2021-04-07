package com.google.blockly.android.ui;

public class PushEvent
{
    // the pushlist object being sent using the bus
    private int pos;
    private boolean isClose;

    public PushEvent(int pushPosition, boolean isClose)
    {
        this.pos = pushPosition;
        this.isClose = isClose;
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