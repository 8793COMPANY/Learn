package com.corporation8793;

public class Application extends android.app.Application {
    public static int indicator_index = -1;
//    private LogHelper logHelper;

    @Override
    public void onCreate() {
        super.onCreate();
//        logHelper = new LogHelper();
//        Thread.setDefaultUncaughtExceptionHandler(logHelper);
    }
}