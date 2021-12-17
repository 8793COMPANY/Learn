package com.corporation8793.dialog;

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
import androidx.annotation.Nullable;

import com.corporation8793.R;

public class UploadDialog extends Dialog {

    private Button Confirm;

    private View.OnClickListener Confirm_Btn;

    public TextView Body;
    public TextView Title;

    public String title;
    public String body;

    public UploadDialog(@NonNull Context context, View.OnClickListener Confirm_Btn, String title, String body) {
        super(context);
        this.Confirm_Btn = Confirm_Btn;
        this.title = title;
        this.body = body;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_upload);
        Confirm=(Button)findViewById(R.id.Confirm);

        Title = findViewById(R.id.title);
        Body = findViewById(R.id.body);

        Confirm.setOnClickListener(Confirm_Btn);

        //타이틀과 바디의 글씨 재셋팅
        Title.setText(this.title);
        Body.setText(this.body);
    }
}
