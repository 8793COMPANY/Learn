package com.corporation8793;

import android.content.Context;

public class Application extends android.app.Application {
    public static int indicator_index = -1;
    Context context;
    private LogHelper logHelper;

    public com.corporation8793.Application getInstance(Context context){
        this.context = context;
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logHelper = new LogHelper();
        Thread.setDefaultUncaughtExceptionHandler(logHelper);


    }


}