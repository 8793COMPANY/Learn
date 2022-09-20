package com.corporation8793.fragment;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.corporation8793.MySharedPreferences;
import com.corporation8793.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int diagram =0;
    String contents_name ="";

    Context context;
    MediaPlayer mediaPlayer;
    Button block_bot_btn;

    @Override
    public void onDestroy() {
        // 웰컴 메시지 재생 종료
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // 웰컴 메시지 재생 종료
        mediaPlayer.release();
        super.onPause();
    }


    ScaleGestureDetector mScaleGestureDetector;
    float mScaleFactor =1.0f;
    ImageView diagram_img;
    WebView diagram_image_viewer;
    public Step2(){

    }

    public Step2(String contents_name, int diagram_img, MediaPlayer mp, Context context, Button block_bot_btn) {
        // Required empty public constructor
        this.contents_name = contents_name;
        diagram = diagram_img;
        this.mediaPlayer = mp;
        this.context = context;
        this.block_bot_btn = block_bot_btn;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step1.
     */
    // TODO: Rename and change types and number of parameters
    public static Step2 newInstance(String param1, String param2) {
        Step2 fragment = new Step2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step2, container, false);
        // 봇 메시지 초기화
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
//
//        // 웰컴 메시지 재생
//        mediaPlayer = MediaPlayer.create(context, R.raw.bot_test_lv1_led_into_contents_3_circuit);
//        mediaPlayer.start();
//        block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_speech));
//
//
//        // 웰컴 메시지 재생 완료
//        mediaPlayer.setOnCompletionListener(mp -> {
//            block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_normal));
//            mp.release();
//        });



//        block_bot_btn.setOnClickListener(v1 -> {
//            // 봇 메시지 초기화
//            if (mediaPlayer != null) {
//                mediaPlayer.release();
//            }
//
//            // 웰컴 메시지 재생
//            mediaPlayer = MediaPlayer.create(context, R.raw.bot_test_lv1_led_into_contents_3_circuit);
//            mediaPlayer.start();
//            block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_speech));
//
//            // 웰컴 메시지 재생 완료
//            mediaPlayer.setOnCompletionListener(mp -> {
//                block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_normal));
//                mp.release();
//            });
//        });



        if (MySharedPreferences.getInt(getContext(),contents_name+" MAX") < 2) {
            MySharedPreferences.setInt(getContext(), contents_name+" MAX", 2);
        }

        MySharedPreferences.setInt(getContext(),contents_name,2);

        // 웹뷰기반 이미지 뷰어 생성
        diagram_image_viewer = view.findViewById(R.id.diagram_image_viewer);

        WebView webView = new WebView(getContext());
        webView.setBackgroundColor(0xffffffff);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.setInitialScale(0);
        webView.loadDataWithBaseURL("file:///android_res/drawable/", "<img src='all_diagram_img.png' />", "text/html", "utf-8", null);

        diagram_image_viewer.addView(webView, -1, -2);

        // (구)이미지뷰 (확대, 축소만 됨)
        diagram_img = view.findViewById(R.id.diagram_img);
        diagram_img.setBackgroundResource(diagram);

        if (contents_name.equals("LED 핀 번호 바꾸기"))
            diagram_img.setBackgroundResource(R.drawable.all_diagram_img2);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        diagram_img.setOnTouchListener((v, event) -> {
            mScaleGestureDetector.onTouchEvent(event);
            return true;
        });

        RadioButton bigboard_btn = view.findViewById(R.id.bigboard_btn);

        bigboard_btn.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.e("bigboard", "");
            if (b) {
                diagram_img.setBackgroundResource(R.drawable.all_diagram_img2);
//                Log.e("bigboard","true");
//                if (contents_name.equals("LED 핀 번호 바꾸기")) {
//                    Log.e("bigboard","all 핀");
//                    diagram_img.setBackgroundResource(R.drawable.all_diagram_img2);
//                }else {
//                    Log.e("bigboard","all");
//                    diagram_img.setBackgroundResource(R.drawable.all_diagram_img);
//                }
            }else {
                diagram_img.setBackgroundResource(R.drawable.diagram_module_img2);
//                Log.e("bigboard","false");
//                if (contents_name.equals("LED 핀 번호 바꾸기")) {
//                    Log.e("bigboard","module 핀");
//                    diagram_img.setBackgroundResource(R.drawable.diagram_module_img2);
//                }else {
//                    Log.e("bigboard","module");
//                    diagram_img.setBackgroundResource(R.drawable.diagram_module_img);
//                }
            }
        });


        return view;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        //변수로 선언해 놓은 ScaleGestureDetector
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            // ScaleGestureDetector에서 factor를 받아 변수로 선언한 factor에 넣고
            mScaleFactor *= scaleGestureDetector.getScaleFactor();

            // 최대 10배, 최소 10배 줌 한계 설정
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));

            // 이미지뷰 스케일에 적용
            diagram_img.setScaleX(mScaleFactor);
            diagram_img.setScaleY(mScaleFactor);
            return true;
        }
    }



}