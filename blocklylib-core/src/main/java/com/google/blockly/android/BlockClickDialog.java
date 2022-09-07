package com.google.blockly.android;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class BlockClickDialog extends Dialog {

    private Button retake_btn;
    private Button cancel_btn;

    private View.OnClickListener retake_listener;
    private View.OnClickListener cancel_listener;

    public TextView Body;
    public TextView Title;

    public String title;
    public String body;

    public BlockClickDialog(@NonNull Context context, View.OnClickListener retake_listener, View.OnClickListener cancel_listener) {
        super(context);
        this.retake_listener = retake_listener;
        this.cancel_listener = cancel_listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_block_click);
//        retake_btn =(Button)findViewById(R.id.retake);
//        cancel_btn = findViewById(R.id.cancel);
//
//
//        retake_btn.setOnClickListener(retake_listener);
//        cancel_btn.setOnClickListener(cancel_listener);



        //타이틀과 바디의 글씨 재셋팅
//        Title.setText(this.title);
//        Body.setText(this.body);
    }
}
