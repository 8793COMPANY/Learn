package com.learn4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class LoginActivity extends AppCompatActivity {
    EditText login_id_input_box;
    ImageView auto_login;
    ImageButton login_btn;
//
//    ImageButton login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                             startActivity(intent);
                                             finish();
                                         }

                                     });

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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        });
    }
}