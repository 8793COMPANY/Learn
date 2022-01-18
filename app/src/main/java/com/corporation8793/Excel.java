package com.corporation8793;

import android.content.Context;
import android.util.Log;

import com.google.blockly.model.Input;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class Excel {
    Context context;
    InputStream is;
    Workbook workbook;

    public Excel(Context context, String excelName){
        this.context = context;
        try {
            is = context.getResources().getAssets().open("contents_mode_info.xls");
            workbook = Workbook.getWorkbook(is);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void readContents(){
        try{
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

    public void readComponent(){
        try{
            Log.e("in","readComponent");
            if (workbook != null){
                Log.e("in","workbook");
                Sheet sheet = workbook.getSheet(1);
                if (sheet != null){
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 1;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    Log.e("??","in");
                    for (int row = rowIndexStart; row<rowTotal; row++){
                        Log.e("??","in");
                        sb = new StringBuilder();

                        for (int col=2; col<colTotal; col++){
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
