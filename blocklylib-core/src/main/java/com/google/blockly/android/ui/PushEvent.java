package com.google.blockly.android.ui;

public class PushEvent
{
    // the pushlist object being sent using the bus
    private int pos;

    public PushEvent(int pushPosition)
    {
        this.pos = pushPosition;
    }

    public int getPos() {
        return pos;
    }
}