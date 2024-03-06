package com.learn4.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.learn4.R;
import com.learn4.data.dto.LearningObjective;
import com.learn4.view.custom.dialog.ProgressDialog;
import com.google.blockly.android.ui.CategoryData;
import com.learn.wp_rest.data.wp.users.User;
import com.learn.wp_rest.repository.acf.AcfRepository;
import com.learn.wp_rest.repository.auth.AuthRepository;
import com.learn.wp_rest.repository.wp.media.MediaRepository;
import com.learn.wp_rest.repository.wp.posts.PostsRepository;
import com.learn.wp_rest.repository.wp.users.UsersRepository;
import com.physicaloid.lib.Physicaloid;

import java.util.ArrayList;

public class Application extends android.app.Application  {
    public static int indicator_index = -1;
    Context context;
    // TODO : log4j Exception 발생 시 CE 로 전송
    private LogHelper logHelper;
    private ProgressDialog loadingDialog;

    private static Application instance = null;

    public static AcfRepository acfRepository;
    public static User user;
    public static UsersRepository usersRepository;
    public static AuthRepository authRepository;
    public static MediaRepository mediaRepository;
    public static PostsRepository postsRepository;

    public static boolean usb_check = false;
    public static boolean wifi_check = false;
    public static boolean all_check = false;
    public static Physicaloid mPhysicaloid ;
    public static CategoryData categoryData;
    public static ArrayList<LearningObjective> learningObjectives;
    public static int mode = 1;
    public static boolean translate_check = false;

    public static MediaPlayer mediaPlayer;

    public static String sttString="";

    public static Application getInstance(Context context){
//        this.context = context;
        if (instance == null){
            instance = new Application();
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("application","oncreate");
//        db = AppDatabase.Companion.getInstance(getApplicationContext());
        mPhysicaloid = new Physicaloid(getApplicationContext());
        learningObjectives = new ArrayList<>();
        this.registerReceiver(uploadEventReceiver, new IntentFilter("android.hardware.usb.action.USB_DEVICE_ATTACHED"));
        this.registerReceiver(uploadEventReceiver, new IntentFilter("android.hardware.usb.action.USB_DEVICE_DETACHED"));



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

        //log 수집
//        logHelper = new LogHelper();
//        Thread.setDefaultUncaughtExceptionHandler(logHelper);


    }


    public void setAuth(String auth){
        usersRepository = new UsersRepository(auth);
        user = usersRepository.whoAmI().getSecond();
//        Log.e("user",user.getName());
        acfRepository = new AcfRepository(auth);
        mediaRepository = new MediaRepository(auth);
        postsRepository = new PostsRepository(auth);
    }

    public static void setSimulatorEnabled(boolean check){
        if (categoryData == null)
            categoryData = CategoryData.getInstance();

        if (categoryData.getSimulator_btn() != null){
            if (check){
                categoryData.getSimulator_btn().setSelected(true);
                categoryData.getSimulator_btn().setEnabled(true);
            }else{
                categoryData.getSimulator_btn().setSelected(false);
                categoryData.getSimulator_btn().setEnabled(false);
            }
        }

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


    BroadcastReceiver uploadEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("action",action);
            if (categoryData == null)
                categoryData = CategoryData.getInstance();

            //Toast.makeText(getApplicationContext(), "hey", Toast.LENGTH_SHORT).show();


            if(action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                // USB was connected
                Log.e("USB 감지 : ", "연결연결");
                Toast.makeText(getApplicationContext(), "USB 연결", Toast.LENGTH_SHORT).show();
                mPhysicaloid.open();
                Log.e("mphysicaloid", mPhysicaloid.isOpened()+"");
                usb_check = true;
                try {
                    if (categoryData.getUpload_btn() != null) {
                        checkUploadBtn();
                    }
                }catch (NullPointerException e){
                    Log.e("upload btn","null!");
                }
            }

            if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                // USB was disconnected
                Log.e("USB 감지 : ", "해제해제");
                Toast.makeText(getApplicationContext(), "USB 해제", Toast.LENGTH_SHORT).show();
                mPhysicaloid.close();
                usb_check = false;

                try {
                    if (categoryData.getUpload_btn() != null) {
                        checkUploadBtn();
                    }
                }catch (NullPointerException e){
                    Log.e("upload btn","null!");
                }
            }

        }
    };



    public static void checkUploadBtn(){
        if (categoryData == null)
            categoryData = CategoryData.getInstance();

        if (wifi_check&& usb_check){
            Log.e("checkUploadBtn","true1");
            categoryData.getUpload_btn().setBackgroundResource(R.drawable.upload_btn_on);
            categoryData.getUpload_btn().setSelected(true);
            categoryData.getUpload_btn().setEnabled(true);

            all_check = true;
        }else{
            Log.e("checkUploadBtn","false1");
            categoryData.getUpload_btn().setBackgroundResource(R.drawable.upload_btn_false);
            categoryData.getUpload_btn().setSelected(false);
            //categoryData.getUpload_btn().setEnabled(false);
            all_check = false;
        }
    }



}