package com.corporation8793.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.corporation8793.R;

public class FinishDialog extends Dialog {

    private LinearLayout confirm_btn, cancel_btn;

    private View.OnClickListener Confirm_Btn;
    private View.OnClickListener Cancel_Btn;


    public FinishDialog(@NonNull Context context, View.OnClickListener Confirm_Btn, @Nullable View.OnClickListener Cancel_Btn) {
        super(context);
        this.Confirm_Btn = Confirm_Btn;
        this.Cancel_Btn = Cancel_Btn;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_finish);

        confirm_btn = findViewById(R.id.confirm);
        cancel_btn = findViewById(R.id.cancel);

        confirm_btn.setOnClickListener(Confirm_Btn);
        if (Cancel_Btn != null) {
            cancel_btn.setOnClickListener(Cancel_Btn);
        } else {
            cancel_btn.setVisibility(View.GONE);
        }

    }
}
