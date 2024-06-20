package com.learn4.view.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.learn4.R;
import com.learn4.util.DisplaySize;

public class CouponInputDialog extends Dialog {

    TextView word_input_text;
    public EditText word_input_box;
    Button confirm_btn;

    View.OnClickListener confirm_listener;
    public int num;

    public String coupon_num = "";

    public CouponInputDialog(@NonNull Context context, View.OnClickListener confirm_listener) {
        super(context);

        this.confirm_listener = confirm_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_word_change);

        word_input_text = findViewById(R.id.word_input_text);
        word_input_box = findViewById(R.id.word_input_box);
        confirm_btn = findViewById(R.id.confirm_btn);

        word_input_text.setText("쿠폰번호를 입력하세요");

        word_input_text.setTextSize(DisplaySize.font_size_y_29);
        word_input_box.setTextSize(DisplaySize.font_size_y_29);
        word_input_box.setPadding((int) DisplaySize.font_size_x_20,0,0,0);
        confirm_btn.setTextSize(DisplaySize.font_size_y_28);

        word_input_box.setText("2024-AGFA-FE01-1208");

        confirm_btn.setOnClickListener(confirm_listener);
    }


}
