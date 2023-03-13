package com.learn4.view.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn4.R;

public class UploadFalseDialog extends Dialog {

    TextView confirm;
    LinearLayout bottom_section;

    public UploadFalseDialog (Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upload_false);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        bottom_section = findViewById(R.id.bottom_section);
        bottom_section.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
