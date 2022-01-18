package com.corporation8793;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.corporation8793.dialog.ProgressDialog;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class Application extends android.app.Application {
    public static int indicator_index = -1;
    Context context;
    private LogHelper logHelper;
    private ProgressDialog loadingDialog;
    Excel excel;

    public com.corporation8793.Application getInstance(Context context){
        this.context = context;
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logHelper = new LogHelper();
        Thread.setDefaultUncaughtExceptionHandler(logHelper);
        excel = new Excel(context, "contents_mode_info.xls");
        excel.readComponent();
    }


    public void showLoadingScreen(Context context){
        if(context != null){
            loadingDialog = new ProgressDialog(context);
            loadingDialog.setContentView(R.layout.dialog_progress);
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loadingDialog.show();
        }
    }

    public void hideLoadingScreen(){
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }






}