package com.learn4.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.learn4.Application;
import com.learn4.MySharedPreferences;
import com.learn4.R;
import com.learn4.Setting;
import com.learn4.dialog.ProgressDialog;
import com.learn.wp_rest.repository.auth.AuthRepository;

import okhttp3.Credentials;


public class LoginActivity extends AppCompatActivity {
    EditText login_id_input_box, login_pw_input_box;
    ImageView auto_login;
    ImageButton login_btn;

    ProgressDialog customProgressDialog;
//
//    ImageButton login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_login);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.e("check pixel width",displayMetrics.widthPixels+"");
        Log.e("check pixel height",displayMetrics.heightPixels+"");
        Log.e("check dpi",displayMetrics.densityDpi+"");

        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;

        Log.e("check width",width+"");
        Log.e("check height",height+"");

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.setContentView(R.layout.dialog_progress);

        customProgressDialog.setCancelable(false);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//        Setting setting = new Setting(getApplicationContext());
        Setting setting = Setting.getInstance(getApplicationContext());
        setting.dataCheck();

//        setting.settingChapterByLevel(1);
//        setting.settingChapterByLevel(2);
//        setting.settingChapterByLevel(3);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        login_btn = findViewById(R.id.login_btn);

        login_id_input_box = findViewById(R.id.login_id_input_box);
        login_pw_input_box = findViewById(R.id.login_pw_input_box);
        auto_login = findViewById(R.id.auto_login);
        login_btn = findViewById(R.id.login_btn);

        if (MySharedPreferences.getBoolean(this,"auto_login")){
            auto_login.setSelected(true);
            auto_login.setBackground(getResources().getDrawable(R.drawable.auto_login_));
            login_id_input_box.setText(MySharedPreferences.getString(this,"login_id"));
            login_pw_input_box.setText(MySharedPreferences.getString(this,"login_pw"));
        }

        auto_login.setOnClickListener(v -> {
            runOnUiThread(() -> {
                if (!v.isSelected()) {
                    v.setBackground(getResources().getDrawable(R.drawable.auto_login_));
                    v.setSelected(true);
                } else {
                    v.setBackground(getResources().getDrawable(R.drawable.auto_login_off));
                    v.setSelected(false);
                }
            });
        });

        login_btn.setOnClickListener(v -> {
            customProgressDialog.show();
            new Thread(()->{
                AuthRepository auth = new AuthRepository();
                String nonce = auth.getNonce().getSecond().getNonce();
                String cookie = auth.getAuthCookie(nonce,login_id_input_box.getText().toString(),login_pw_input_box.getText().toString()).getSecond().getCookie();
                String validation = auth.isValidCookie(cookie).getFirst();
                Boolean validation_check = auth.isValidCookie(cookie).getSecond();
                Log.e("validation",validation);
                Log.e("validation check",validation_check+"");
                Handler handler = new Handler(Looper.getMainLooper());
                if(validation_check) {
                    Log.e("auto login check",auto_login.isSelected()+"");
                    if (auto_login.isSelected()) {
                        MySharedPreferences.setBoolean(this, "auto_login",true);
                        MySharedPreferences.setString(this, "login_id", login_id_input_box.getText().toString());
                        MySharedPreferences.setString(this, "login_pw", login_pw_input_box.getText().toString());
                    }else
                        MySharedPreferences.setBoolean(this, "auto_login",false);
                    customProgressDialog.dismiss();
                    Application application = Application.getInstance(this);
                    Log.e("login id",login_id_input_box.getText().toString());
                    Log.e("login pw",login_pw_input_box.getText().toString());
                    application.setAuth(Credentials.basic(login_id_input_box.getText().toString(),login_pw_input_box.getText().toString()));
                    Intent intent = new Intent(this, ModeSelect.class);
                    startActivity(intent);
                    finish();
                }else {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            customProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "로그인 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }, 0);
                }
            }).start();
        });





    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hideSystemUI();
    }

    //
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }




}