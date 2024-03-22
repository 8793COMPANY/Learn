package com.learn4.view.custom.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.learn4.R;
import com.learn4.util.Application;


public class BuildBotDialog extends Dialog {

    private LinearLayout confirm_btn;
    private LinearLayout cancel_btn;

    private View.OnClickListener confirm_listener;
    private View.OnClickListener cancel_listener;
    private View.OnClickListener learning_goal_listener;
    private View.OnClickListener exercise_listener;
    private View.OnClickListener code_save_listener;
    private View.OnClickListener code_load_listener;

    public TextView Body;
    public TextView Title;

    public Button learning_goal_btn, exercise_btn, code_save_btn, code_load_btn, cancel_btn2;

    public String title;
    public String body;

    public BuildBotDialog(@NonNull Context context,  View.OnClickListener cancel_listener,
                          View.OnClickListener learning_goal_listener, View.OnClickListener exercise_listener,
                          View.OnClickListener code_save_listener, View.OnClickListener code_load_listener) {
        super(context);
        this.confirm_listener = confirm_listener;
        this.cancel_listener = cancel_listener;
        this.learning_goal_listener = learning_goal_listener;
        this.exercise_listener = exercise_listener;
        this.code_save_listener = code_save_listener;
        this.code_load_listener = code_load_listener;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_buildbot_click);
        //confirm_btn =findViewById(com.google.blockly.android.R.id.confirm);
        //cancel_btn = findViewById(com.google.blockly.android.R.id.cancel);

        learning_goal_btn = findViewById(R.id.learning_goal_btn);
        exercise_btn = findViewById(R.id.exercise_btn);
        code_save_btn = findViewById(R.id.code_save_btn);
        code_load_btn = findViewById(R.id.code_load_btn);
        cancel_btn2 = findViewById(R.id.cancel_text2);

//        learning_goal_btn.setTextSize((float) (Application.standardSize_Y / 24));
//        exercise_btn.setTextSize((float) (Application.standardSize_Y / 24));
//        code_save_btn.setTextSize((float) (Application.standardSize_Y / 24));
//        code_load_btn.setTextSize((float) (Application.standardSize_Y / 24));

        //confirm_btn.setOnClickListener(confirm_listener);
        //cancel_btn.setOnClickListener(cancel_listener);
        cancel_btn2.setOnClickListener(cancel_listener);

        learning_goal_btn.setOnClickListener(learning_goal_listener);
        exercise_btn.setOnClickListener(exercise_listener);
        code_save_btn.setOnClickListener(code_save_listener);
        code_load_btn.setOnClickListener(code_load_listener);

    }
}
