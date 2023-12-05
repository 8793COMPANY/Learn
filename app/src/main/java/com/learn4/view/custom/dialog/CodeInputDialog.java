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
import com.learn4.util.MySharedPreferences;

public class CodeInputDialog extends Dialog {

    public EditText code_input_box;
    Button confirm_btn;
    TextView sub_text, sub_text2;

    View.OnClickListener confirm_listener;
    Boolean code_save_check;

    public CodeInputDialog(@NonNull Context context, View.OnClickListener confirm_listener, Boolean code_save_check) {
        super(context);

        this.confirm_listener = confirm_listener;
        this.code_save_check = code_save_check;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_code_input);

        code_input_box = findViewById(R.id.code_input_box);
        confirm_btn = findViewById(R.id.confirm_btn);

        sub_text = findViewById(R.id.sub_text);
        sub_text2 = findViewById(R.id.sub_text2);

        confirm_btn.setOnClickListener(confirm_listener);

        Log.e("testtest", "CodeInputDialog on");

        if (code_save_check) {
            sub_text2.setVisibility(View.VISIBLE);
            code_input_box.setVisibility(View.GONE);
            confirm_btn.setVisibility(View.GONE);

            sub_text.setText("학교 코드가 이미 등록되어 있습니다");
            sub_text2.setText(MySharedPreferences.getString(getContext(), "school_code"));
        } else {
            sub_text2.setVisibility(View.GONE);
            code_input_box.setVisibility(View.VISIBLE);
            confirm_btn.setVisibility(View.VISIBLE);

            sub_text.setText("학교 코드를 입력하세요");
        }
    }
}
