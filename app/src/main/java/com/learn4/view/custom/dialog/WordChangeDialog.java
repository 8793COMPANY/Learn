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

public class WordChangeDialog extends Dialog {

    TextView word_input_text;
    public EditText word_input_box;
    Button confirm_btn;

    View.OnClickListener confirm_listener;
    public int num;

    public WordChangeDialog(@NonNull Context context, View.OnClickListener confirm_listener, int num) {
        super(context);

        this.confirm_listener = confirm_listener;
        this.num = num;
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

        word_input_text.setTextSize(DisplaySize.font_size_y_29);
        word_input_box.setTextSize(DisplaySize.font_size_y_29);
        word_input_box.setPadding((int) DisplaySize.font_size_x_20,0,0,0);
        confirm_btn.setTextSize(DisplaySize.font_size_y_28);

        confirm_btn.setOnClickListener(confirm_listener);
    }


}
