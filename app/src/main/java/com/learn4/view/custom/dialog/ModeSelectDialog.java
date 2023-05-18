package com.learn4.view.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.learn4.R;


public class ModeSelectDialog extends Dialog {

    private LinearLayout confirm_btn;
    private LinearLayout cancel_btn;

    private View.OnClickListener confirm_listener;
    private View.OnClickListener cancel_listener;
    private View.OnClickListener copy_listener;
    private View.OnClickListener delete_listener;

    public TextView Body;
    public TextView Title;

    public RadioButton default_mode, jikco_mode;

    public String title;
    int mode;

    public ModeSelectDialog(@NonNull Context context, View.OnClickListener cancel_listener, View.OnClickListener confirm_listener, View.OnClickListener copy_listener, View.OnClickListener delete_listener, int mode) {
        super(context);
        this.confirm_listener = confirm_listener;
        this.cancel_listener = cancel_listener;
        this.copy_listener = copy_listener;
        this.delete_listener = delete_listener;
        this.mode = mode;
    }

    public ModeSelectDialog(@NonNull Context context) {
        super(context);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_mode_select);
        confirm_btn =findViewById(R.id.confirm);
        cancel_btn = findViewById(R.id.cancel);


        default_mode = findViewById(R.id.default_mode);
        jikco_mode = findViewById(R.id.jikco_mode);


        if (mode == 1){
            default_mode.setChecked(true);
        }else{
            jikco_mode.setChecked(true);
        }

        confirm_btn.setOnClickListener(confirm_listener);
        cancel_btn.setOnClickListener(cancel_listener);
        default_mode.setOnClickListener(copy_listener);
        jikco_mode.setOnClickListener(delete_listener);

    }
}
