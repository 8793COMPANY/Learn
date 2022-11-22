package com.learn4.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.learn4.R;

public class UploadDialog extends Dialog {

    private ConstraintLayout Confirm, submit;

    private View.OnClickListener Confirm_Btn;
    private View.OnClickListener Submit_Btn;

    public TextView Body;
    public TextView Title;

    public String title;
    public String body;

    public UploadDialog(@NonNull Context context, View.OnClickListener Confirm_Btn, @Nullable View.OnClickListener Submit_Btn, String title, String body) {
        super(context);
        this.Confirm_Btn = Confirm_Btn;
        this.Submit_Btn = Submit_Btn;
        this.title = title;
        this.body = body;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_upload);
        Confirm = findViewById(R.id.Confirm);
        submit = findViewById(R.id.submit);

        Title = findViewById(R.id.title);

        Confirm.setOnClickListener(Confirm_Btn);
        if (Submit_Btn != null) {
            submit.setOnClickListener(Submit_Btn);
        } else {
            submit.setVisibility(View.GONE);
        }

        //타이틀과 바디의 글씨 재셋팅
        Title.setText(this.title);
    }
}
