package com.corporation8793;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import androidx.room.Room;

import com.corporation8793.dialog.ProgressDialog;
import com.corporation8793.room.AppDatabase;
import com.corporation8793.room.entity.Contents;
import com.google.blockly.model.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import jxl.Sheet;
import jxl.Workbook;

public class Application extends android.app.Application {
    public static int indicator_index = -1;
    Context context;
    private LogHelper logHelper;
    private ProgressDialog loadingDialog;





    public com.corporation8793.Application getInstance(Context context){
        this.context = context;

        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("application","oncreate");
//        db = AppDatabase.Companion.getInstance(getApplicationContext());


//        Contents contents = new Contents(0,3," 초음파 센서 사용하기",
//                "초음파 센서로 거리 측정하기","거리에 따라 LED 빠르게 깜빡이기","거리에 따라 피에조 부저 소리 조정하기",
//                "0, 1, 11, 12","0, 1, 2-1, 11, 12","0, 1, 6, 11, 12");
//
//        insertData(contents);

//        Observable.fromCallable(() -> db.contentsDao().insertContents(contents))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        longSingle -> {
//                            printList();
//                        }
//                );



//        readComponent();
//        readContents();
//        AppDatabase db = AppDatabase.getInstance(context);
        logHelper = new LogHelper();
        Thread.setDefaultUncaughtExceptionHandler(logHelper);



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