package com.learn4.view.custom.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.learn4.R;
import com.learn4.util.Application;
import com.learn4.view.MainActivity;

public class UploadDialog extends Dialog {

    private ConstraintLayout Confirm, submit;

    private View.OnClickListener Confirm_Btn;
    private View.OnClickListener Submit_Btn;

    public TextView Body;
    public TextView Title;

    public String title;
    public String body;

    AdView adView;

    //MainActivity activity;
    MainActivity mainActivity;

    ProgressBar progressImage;

    ImageView enable1, enable2;

    public UploadDialog(@NonNull Context context, View.OnClickListener Confirm_Btn, @Nullable View.OnClickListener Submit_Btn,
                        String title, String body, MainActivity mainActivity) {
        super(context);
        this.Confirm_Btn = Confirm_Btn;
        this.Submit_Btn = Submit_Btn;
        this.title = title;
        this.body = body;
        this.mainActivity = mainActivity;
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
        Title.setText("업로드 중");

        progressImage = findViewById(R.id.progressImage);

        //Log.e("testtest", testFile);
        Log.e("testtest", "onCreate");

        //mainActivity = activity;
        //mainActivity.test2();

//        Confirm.setClickable(false);
//        submit.setClickable(false);

        enable1 = findViewById(R.id.enable1);
        enable2 = findViewById(R.id.enable2);

        adView = findViewById(R.id.adView);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        if (Application.ad_check || Application.payment_check) {
            adView.setVisibility(View.GONE);
        } else {
            adView.setVisibility(View.VISIBLE);
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AdLoader.Builder builder = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110");

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                TemplateView templateView = findViewById(R.id.test);
                templateView.setNativeAd(nativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loading() {
        progressImage.setVisibility(View.GONE);
        Title.setText(this.title);
        Confirm.setClickable(true);
        submit.setClickable(true);

        enable1.setVisibility(View.GONE);
        enable2.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("testtest", "onStart()");
        mainActivity.test2();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("testtest", "onStop()");
        progressImage.setVisibility(View.VISIBLE);
        Title.setText("업로드 중");
        Confirm.setClickable(false);
        submit.setClickable(false);

        enable1.setVisibility(View.VISIBLE);
        enable2.setVisibility(View.VISIBLE);

        if (Application.ad_check) {
            adView.setVisibility(View.GONE);
        } else {
            adView.setVisibility(View.VISIBLE);
        }
    }
}
