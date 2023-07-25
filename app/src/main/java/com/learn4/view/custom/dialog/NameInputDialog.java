package com.learn4.view.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.learn4.R;


public class NameInputDialog extends Dialog {

    private Button confirm_btn;

    private View.OnClickListener confirm_listener;


    public EditText name_input;


    public String name;

    public NameInputDialog(@NonNull Context context, View.OnClickListener confirm_listener) {
        super(context);
        this.confirm_listener = confirm_listener;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_file_name_save);
        confirm_btn =findViewById(R.id.confirm_btn);
        name_input = findViewById(R.id.link_input_box);

        confirm_btn.setOnClickListener(confirm_listener);


    }
}
