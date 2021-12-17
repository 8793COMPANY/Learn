package com.corporation8793.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;

import android.widget.EditText;
import android.widget.ImageView;

import com.corporation8793.R;


public class LoginActivity extends AppCompatActivity {
    EditText login_id_input_box;
    ImageView auto_login;
    ImageButton login_btn;
//
//    ImageButton login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        login_btn = findViewById(R.id.login_btn);

        login_id_input_box = findViewById(R.id.login_id_input_box);
        auto_login = findViewById(R.id.auto_login);
        login_btn = findViewById(R.id.login_btn);

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
            Intent intent = new Intent(this, ChapterActivity.class);
            startActivity(intent);
            finish();

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