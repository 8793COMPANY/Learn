package com.google.blockly.utils;

import android.content.Context;

public class TestApplication {

    private static TestApplication instance = null;

    public static TestApplication getInstance(Context context) {
        if (instance == null) {
            instance = new TestApplication();
        }
        return instance;
    }

    public static String chapter_check = "none";

    public static String getChapter_check() {
        return chapter_check;
    }
}
