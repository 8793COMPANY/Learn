package com.learn4.view.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learn4.R;
import com.learn4.util.Application;
import com.learn4.util.MySharedPreferences;
import com.learn4.util.PaymentCheck;
import com.learn4.view.mode_select.ModeSelect;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    String code_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        hideSystemUI();

        PaymentCheck.getInstance(this).initSetting();

        Handler handler = new Handler();
        
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, ModeSelect.class);
            startActivity(intent);
            finish();
        }, 2000);

        arrayList = new ArrayList<>();

        //여기에서 매번 확인하기
        if (MySharedPreferences.getString(getApplicationContext(), "school_code") == null) {
            Log.e("testtest", "school code null");
        } else {
            Log.e("testtest", "school code not null");
            Log.e("testtest", MySharedPreferences.getString(this, "school_code"));

            checkDB();
        }
    }

    private void checkDB() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        arrayList.clear();

        // 어플리케이션으로 넘겨서 배열만 받아오는 것도 방법일 듯
        db.collection("AdTest").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if (documentSnapshot.getData().keySet().toString().equals("[elementary]")) {
                                    code_list = documentSnapshot.getData().get("elementary").toString();
                                    Log.e("testtest", code_list+"");
                                } else {
                                    Log.e("testtest", "no");
                                }

                                if (code_list != null) {
                                    code_list = code_list.replace("[", "");
                                    code_list = code_list.replace("]", "");
                                    code_list = code_list.replace(" ", "");

                                    Log.e("testtest", code_list+"");

                                    String[] splitCodeList = code_list.split(",");

                                    for (int i = 0; i < splitCodeList.length; i++) {
                                        Log.e("testtest", splitCodeList[i]);
                                        arrayList.add(splitCodeList[i]);
                                    }

                                    if (arrayList != null) {
                                        if (arrayList.contains(MySharedPreferences.getString(getApplicationContext(), "school_code"))) {
                                            Application.ad_check = true;
                                        }
//                                        for (int i = 0; i < arrayList.size(); i++) {
//                                            Log.e("testtest", "arrayList : " + arrayList.get(i));
//                                            if (arrayList.get(i).equals(MySharedPreferences.getString(getApplicationContext(), "school_code"))) {
//
//                                            }
//                                        }
                                    } else {
                                        Log.e("testtest", "arrayList null");
                                    }
                                } else {
                                    Log.e("testtest", "code list null");
                                }
                            }
                        }
                    }
                });
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        }
//    }
//
//
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}