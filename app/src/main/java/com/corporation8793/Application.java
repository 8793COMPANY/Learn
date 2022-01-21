package com.corporation8793;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.corporation8793.dialog.ProgressDialog;
import com.google.blockly.model.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import jxl.Sheet;
import jxl.Workbook;

public class Application extends android.app.Application {
    public static int indicator_index = -1;
    Context context;
    private LogHelper logHelper;
    private ProgressDialog loadingDialog;
    InputStream is;
    Workbook workbook;
    public HashMap<String,String[]> components = new HashMap<>();


    public com.corporation8793.Application getInstance(Context context){
        this.context = context;
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logHelper = new LogHelper();
        Thread.setDefaultUncaughtExceptionHandler(logHelper);
        readComponent();
        readContents();

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


    public void readComponent(){
        try{
            is = getBaseContext().getResources().getAssets().open("contents_mode_info.xls");
            workbook = Workbook.getWorkbook(is);
            if (workbook != null){
                Sheet sheet = workbook.getSheet(1);
                if (sheet != null){
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 1;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    String num;

                    for (int row = rowIndexStart; row<rowTotal; row++){
                        num = sheet.getCell(2,row).getContents();

                        String [] component_content = new String[2];
                        for (int col=3; col<5; col++){
                            component_content[col - 3] = sheet.getCell(col,row).getContents();
                            Log.e("content check ",component_content[col - 3]);
                        }
                        components.put(num,component_content);
                    }

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readContents(){
        try{
            is = getBaseContext().getResources().getAssets().open("contents_mode_info.xls");
            workbook = Workbook.getWorkbook(is);
            if (workbook != null){
                Sheet sheet = workbook.getSheet(0);
                if (sheet != null){
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 2;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row<rowTotal; row++){
                        sb = new StringBuilder();

                        for (int col=0; col<colTotal; col++){
                            String contents = sheet.getCell(col,row).getContents();

                            Log.e("col ", contents);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}