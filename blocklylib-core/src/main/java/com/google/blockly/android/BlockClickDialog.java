package com.google.blockly.android;

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


public class BlockClickDialog extends Dialog {

    private LinearLayout confirm_btn;
    private LinearLayout cancel_btn;

    private View.OnClickListener confirm_listener;
    private View.OnClickListener cancel_listener;
    private View.OnClickListener copy_listener;
    private View.OnClickListener delete_listener;

    public TextView Body;
    public TextView Title;

    public RadioButton copy_btn, delete_btn;

    public String title;
    public String body;

    public BlockClickDialog(@NonNull Context context, View.OnClickListener confirm_listener, View.OnClickListener cancel_listener,View.OnClickListener copy_listener,View.OnClickListener delete_listener) {
        super(context);
        this.confirm_listener = confirm_listener;
        this.cancel_listener = cancel_listener;
        this.copy_listener = copy_listener;
        this.delete_listener = delete_listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_block_click);
        confirm_btn =findViewById(R.id.confirm);
        cancel_btn = findViewById(R.id.cancel);

        copy_btn = findViewById(R.id.copy_btn);
        delete_btn = findViewById(R.id.delete_btn);

        confirm_btn.setOnClickListener(confirm_listener);
        cancel_btn.setOnClickListener(cancel_listener);
        copy_btn.setOnClickListener(copy_listener);
        delete_btn.setOnClickListener(delete_listener);

    }
}
