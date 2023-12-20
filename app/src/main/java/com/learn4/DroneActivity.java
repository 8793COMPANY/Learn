package com.learn4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DroneActivity extends AppCompatActivity implements View.OnClickListener {



    public static final String TAG = "Controll";

    //ARM MSG
    public static final int ARM = 1;
    public static final int MSG = 2;
    public static final int BAT = 3;
    public static final int RECEIVED = 4;
    public static final int DEBUG = 254;


    //
    public static int cont_version = 2;

    //handle to deal message from drone
    MyHandler myHandler;

    //controller
    ConstraintLayout layout_controller;
    RelativeLayout layout_cont_left;
    ImageView left_btn;
    ImageView left_circle;
    RelativeLayout layout_cont_right;
    ImageView right_btn;
    ImageView right_circle;

    //func btn
    RelativeLayout layout_function;
    // 드론 시동
    Button btn_arm, btn_disarm;
    //    Button btn_chat;
    Button btn_cal;
    Button btn_wifi;
    Button btn_setting;

    //tri
    Button btn_trim_forward, btn_trim_left, btn_trim_right, btn_trim_backward;
    Button btn_x12510;
    Button btn_version;
    Button btn_rgb_id;
    SeekBar seek_r, seek_g, seek_b;
    Button btn_optic;
    Button btn_debug;

    //text
    RelativeLayout layout_text;
    TextView txt_throttle, txt_yaw, txt_roll, txt_pitch;
    TextView txt_bat;

    //debug
    RelativeLayout layout_debug;
    TextView txt_debug_0;
    TextView txt_debug_1;
    TextView txt_debug_2;
    TextView txt_debug_3;

    //
    int x125 = 1;

    //레이아웃 크기
    int width = 0;
    int height = 0;

    //
    int fir_X = 125;
    int fir_Y = 200;
    int right_locate = 400;

    // button, circle size
    int w_left_btn = 60;
    int h_left_btn = 60;
    int w_left_btn_rad = 30;
    int h_left_btn_rad = 30;
    int w_left_circle = 176;
    int h_left_circle = 176;
    int w_left_circle_rad = 88;
    int h_left_circle_rad = 88;

    int cen_w_left_btn = 0;
    int cen_h_left_btn = 0;
    int cen_w_left_circle = 0;
    int cen_h_left_circle = 0;

    //
    int gap = 0;

    //손 뗸 후 다시 붙일 떄 조절
    int down_left_X_to_yaw_origin = 0;
    int down_left_Y_to_throttle_origin = 0;
    int down_right_X_to_roll_origin = 0;
    int down_right_Y_to_pitch_origin = 0;
    int fin_left_X_diff = 0;
    //    int fin_left_Y_diff = -(h_left_circle/2);
    int fin_left_Y_diff = 0;
    int fin_right_X_diff = 0;
    int fin_right_Y_diff = 0;

    public byte seek_r_val = (byte) 0;
    public byte seek_g_val = (byte) 0;
    public byte seek_b_val = (byte) 0;
    public byte rgb_id_val = 2;

    // communicate
    public static final String ip = "192.168.4.1";
    public static final int port = 5000;

    public boolean SW = true;
    public boolean STOP = false;
    public boolean SAFE_LANDING = false;

    public byte pitch = 0x7d;
    public byte roll = 0x7d;
    public byte yaw = 0x7d;
    public byte throttle = 0x7d;

    public byte checkSum = (byte) 0;

    public boolean arm = false;
    public boolean first_arm = true;
    public boolean disarm = false;
    public boolean cal = false;
    public boolean setting = false;
    public boolean trim_forward = false;
    public boolean trim_backward = false;
    public boolean trim_left = false;
    public boolean trim_right = false;
    public boolean rgb = false;
    public boolean optic = true;

    public boolean connected = false;
    public boolean rgbNcal = false; // 너무 빨라서 안되나? 아니면 cali 동안 rgb가 씹힘?
    public boolean debug = false;

    TabLayout drone_menu_tabs;

    TextView forward, left, right, backward;

    public Button arm_btn, disarm_btn,  rgb_version_change_btn, trim_setting_btn;

    ConstraintLayout controller_section, layout_trim;

    UDPClient client;
    DatagramSocket socket;

    public int batCount = 0;
    public int debugCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_test);

        registerReceiver(rssiReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

        Log.e(TAG, "onCreate");

        if (cont_version == 1) throttle = 0x00;

        //
        myHandler = new MyHandler();



        //controller
        layout_controller = findViewById(R.id.layout_controller);
        controller_section = findViewById(R.id.controller_section);
        layout_trim = findViewById(R.id.layout_trim);
//        layout_cont_left = (RelativeLayout) findViewById(R.id.layout_cont_left);
        left_btn = (ImageView) findViewById(R.id.left_btn);
        left_circle = (ImageView) findViewById(R.id.left_circle);
        right_btn = (ImageView) findViewById(R.id.right_btn);
        right_circle = (ImageView) findViewById(R.id.right_circle);

        arm_btn = findViewById(R.id.arm_btn);
        disarm_btn = findViewById(R.id.disarm_btn);

        rgb_version_change_btn = findViewById(R.id.rgb_version_change_btn);
        trim_setting_btn = findViewById(R.id.trim_setting_btn);


        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        txt_roll = (TextView) findViewById(R.id.txt_roll2);
        txt_pitch = (TextView) findViewById(R.id.txt_pitch2);
        txt_yaw = (TextView) findViewById(R.id.txt_yaw2);
        txt_throttle = (TextView) findViewById(R.id.txt_throttle2);

        txt_roll.setText(Byte.toString(roll));
        txt_pitch.setText(Byte.toString(pitch));
        txt_yaw.setText(Byte.toString(yaw));
        txt_throttle.setText(Byte.toString(throttle));

        seek_r = (SeekBar) findViewById(R.id.red_seekbar);
        seek_g = (SeekBar) findViewById(R.id.green_seekbar);
        seek_b = (SeekBar) findViewById(R.id.blue_seekbar);


        arm_btn.setOnClickListener(this);
        disarm_btn.setOnClickListener(this);
        rgb_version_change_btn.setOnClickListener(this);
        trim_setting_btn.setOnClickListener(this);

        drone_menu_tabs = findViewById(R.id.drone_menu_tabs);

        LinearLayout linearLayout = (LinearLayout)drone_menu_tabs.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(this, R.color.black));
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(45);
        linearLayout.setDividerDrawable(drawable);




        drone_menu_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                switch (tab.getPosition()){
                    case 0:
                        Log.e("touch", "home");
                        finish();
                        break;
                    case 1:
                        cal = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rgbNcal = true; // 됨
                            }
                        }, 500);
                        Log.e("touch", "calibration");

                        break;
                    case 2:
                        optic = !optic;
                        if (optic) tab.setText("optical");
                        else tab.setText("flow");
                        Log.e("touch", "optical");
                        break;
                    case 3:
                        if (cont_version == 1) {
                            cont_version = 2;
                            left_btn.setTranslationY(0);
//                            left_circle.setTranslationY(0);
                            throttle = (byte) 0x7d;
                            tab.setIcon(R.drawable.drone_version2);
                        } else {
                            cont_version = 1;
                            left_btn.setTranslationY(0);
//                            left_circle.setTranslationY(0);
                            fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY() + gap);
                            throttle = (byte) 0;
                            tab.setIcon(R.drawable.drone_version1);
                        }
//                        txt_throttle.setText(Byte.toString(throttle));
                        tab.setText("Version" + Integer.toString(cont_version));
                        drone_menu_tabs.setClickable(true);

                        Log.e("touch", "version");
                        break;
                    case 4:
                        Log.e("touch", "battery");
                        break;
                    case 5:
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        Log.e("touch", "wifi");
                        break;
                    case 6:
                        setting = !setting;
                        if (setting) {
                            controller_section.setVisibility(View.INVISIBLE);
                            layout_trim.setVisibility(View.VISIBLE);
//
                        } else {
                            controller_section.setVisibility(View.VISIBLE);
                            layout_trim.setVisibility(View.INVISIBLE);
                        }
                        Log.e("touch", "setting");
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Log.e("touch", "home");
                        finish();
                        break;
                    case 1:
                        cal = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rgbNcal = true; // 됨
                            }
                        }, 500);
                        Log.e("touch", "calibration");

                        break;
                    case 2:
                        optic = !optic;
                        if (optic) tab.setText("optical");
                        else tab.setText("flow");
                        Log.e("touch", "optical");
                        break;
                    case 3:
                        if (cont_version == 1) {
                            cont_version = 2;
                            left_btn.setTranslationY(0);
//                            left_circle.setTranslationY(0);
                            throttle = (byte) 0x7d;
                            tab.setIcon(R.drawable.drone_version2);
                        } else {
                            cont_version = 1;
                            left_btn.setTranslationY(w_left_circle_rad);
//                            left_circle.setTranslationY(0);
                            fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY() + gap);
                            throttle = (byte) 0;
                            tab.setIcon(R.drawable.drone_version1);
                        }
//                        txt_throttle.setText(Byte.toString(throttle));
                        tab.setText("Version" + Integer.toString(cont_version));
                        drone_menu_tabs.setClickable(true);

                        Log.e("touch", "version");
                        break;
                    case 4:
                        Log.e("touch", "battery");
                        break;
                    case 5:
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        Log.e("touch", "wifi");
                        break;
                    case 6:
                        setting = !setting;
                        if (setting) {
                            controller_section.setVisibility(View.INVISIBLE);
                            layout_trim.setVisibility(View.VISIBLE);
//
                        } else {
                            controller_section.setVisibility(View.VISIBLE);
                            layout_trim.setVisibility(View.INVISIBLE);
                        }
                        Log.e("touch", "setting");
                        break;

                }
            }
        });

        // button과 circle 크기 맞추기
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.e(TAG, Integer.toString(metrics.densityDpi));
        Log.e(TAG, Integer.toString(metrics.widthPixels));
        Log.e(TAG, Integer.toString(metrics.heightPixels));
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        fir_X = (int) (metrics.widthPixels / 4);
        fir_Y = (int) (metrics.heightPixels / 2);
//        Log.e(TAG + "firx", Integer.toString(fir_X));
        right_locate = (int) (metrics.widthPixels / 2);

        Log.e(TAG, "hi" + Integer.toString(metrics.densityDpi));

        w_left_btn = (int) (width * 0.08);
        w_left_circle = (int) (width * 0.24);

        w_left_btn_rad = w_left_btn / 2;
        h_left_btn_rad = h_left_btn / 2;
        w_left_circle_rad = w_left_circle / 2;
        h_left_circle_rad = h_left_circle / 2;

        //보정값
        cen_w_left_btn = fir_X - (w_left_btn / 2); //대략 400
        cen_h_left_btn = fir_Y - (h_left_btn / 2); //대략 400
        cen_w_left_circle = fir_X - (w_left_circle / 2);
        cen_h_left_circle = fir_Y - (h_left_circle / 2);

        gap = w_left_circle / 2 - w_left_btn / 2;
        fin_left_Y_diff = -(h_left_circle / 2);
        Log.e(TAG, "cen_w_left_btn : " + cen_w_left_btn);
        Log.e(TAG, "cen_w_left_circle : " + cen_w_left_circle);

//        //왼쪽 초기 위치 설정
//        left_btn.setTranslationX(cen_w_left_btn);
//        if (cont_version == 1) left_btn.setTranslationY(cen_h_left_btn + w_left_circle_rad);
//        else left_btn.setTranslationY(cen_h_left_btn);
//        left_circle.setTranslationX(cen_w_left_circle);
//        left_circle.setTranslationY(cen_h_left_circle);
//        down_left_X_to_yaw_origin = cen_w_left_btn;
//        if (cont_version == 2) down_left_Y_to_throttle_origin = cen_h_left_btn;
//
//        //오른쪽 초기 위치 설정
//        right_btn.setTranslationX(100);
//        right_btn.setTranslationY(100);
//        right_circle.setTranslationX(cen_w_left_circle + right_locate);
//        right_circle.setTranslationY(cen_h_left_circle);
//        down_right_X_to_roll_origin = cen_w_left_btn + right_locate;
//        down_right_Y_to_pitch_origin = cen_h_left_btn;

        controller_section.setOnTouchListener((view, motionEvent) -> {
            int pCnt = motionEvent.getPointerCount(); // 1개 닿으면 1, 두 개 닿으면 2
//                Log.e("onTouch", Integer.toString(pCnt));

            //getRawX는 무조건 첫 번째로 터치한 손가락만 쫓는다.
            int X = (int) motionEvent.getRawX();
            int Y = (int) motionEvent.getRawY();
//                Log.e(TAG+"X값", Integer.toString(X));
//                Log.e(TAG+"Y값", Integer.toString(Y));

            //btn과 circle의 원점과 손이 닿은 부분까지의 거리를 빼주어 원의 중심으로 이동

            Log.e("x check",left_circle.getX()+"");
            Log.e("Y check",Y+"");
            int LBX = X - w_left_btn_rad;
            int LBY = Y - h_left_btn_rad;
            int LCX = X - w_left_circle_rad;
            int LCY = Y - h_left_circle_rad;

            //error
//                int X1 = (int) event.getX(1);

            int point_0_index = 0;

            int left_circle_x_pos = (int) ((width *0.16) + w_left_circle_rad - w_left_btn_rad);
            int left_circle_y_pos = (int) ((height * 0.15) + ((height - (height * 0.15)) / 2) - w_left_btn);

            int left_btn_x_pos = (int) ((width *0.15) + w_left_btn_rad);

            int right_circle_x_pos = width - left_circle_x_pos;
            int right_circle_y_pos = (int) ((height * 0.15) + ((height - (height * 0.15)) / 2) - w_left_btn);
            Log.e("check circle position",right_circle_x_pos+"");


            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // 1손가락만 닿았을 때
                    if (X < width / 2) { // 왼쪽에 닿았을 때
                        Log.e("ACTION_DOWN", "1손 왼 down");
                        down_left_X_to_yaw_origin = LBX;
                        if (cont_version == 2) down_left_Y_to_throttle_origin = LBY;

                        if (Y < left_circle.getY()){
                            Y = (int) (left_circle_y_pos -Y);
                        }else {
                            Y = (int) -(left_circle_y_pos -Y);
                        }


                        // 왼쪽 circle과 btn은 손이 닿은 곳으로 이동
//                        left_circle.setTranslationX(-(left_circle_x_pos-X));

//                        if (cont_version == 1)
//                            left_circle.setTranslationY(Y);
//                        else left_circle.setTranslationY(Y);
                        left_btn.setTranslationX(-(left_circle_x_pos-X));
                        left_btn.setTranslationY(Y);

                    } else {
                        Log.e("ACTION_DOWN", "1손 오른 down");
                        down_right_X_to_roll_origin = LBX;
                        down_right_Y_to_pitch_origin = LBY;
                        if (Y < right_circle.getY()){
                            Y = (int) (right_circle_y_pos -Y);
                        }else {
                            Y = (int) -(right_circle_y_pos -Y);
                        }


                        // 오른쪽 circle과 btn을 손이 닿은 곳으로 이동
//                        right_circle.setTranslationX(-(right_circle_x_pos-X));
//                        right_circle.setTranslationY(Y);
                        right_btn.setTranslationX(-(right_circle_x_pos-X));
                        right_btn.setTranslationY(Y);

                    }

//                        point_0_index = event.getPointerId(0);

                    break;
                case MotionEvent.ACTION_UP: //손가락 땠을 때 요,롤,피치 원상복구
                    Log.e("ACTION_UP", "1손 up");

                    left_btn.setTranslationX(0);
                    if (cont_version == 2) left_btn.setTranslationY(0);
                    else left_btn.setTranslationY(cen_h_left_btn - fin_left_Y_diff);
//                    left_circle.setTranslationX(0);
//                    left_circle.setTranslationY(0);

                    right_btn.setTranslationX(0);
                    right_btn.setTranslationY(0);
//                    right_circle.setTranslationX(0);
//                    right_circle.setTranslationY(0);
                    break;


                case MotionEvent.ACTION_POINTER_DOWN: //pcnt로 나눌 필요가 없다. 여기에 들어올 땐 무조건 2번째인놈만 들어옴
                    // 2번째 손가락도 눌렸을 때
                    int point_index = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
//                        Log.e(TAG, "point_index : " + point_index);

//                        Log.e(TAG, "MY event.getRawY : " + event.getRawY());
//                        Log.e(TAG, "MY event.getRawY : " + event.getRawY());
//                        Log.e(TAG, "MY event.getRawX : " + event.getRawX());
//                        Log.e(TAG, "MY event.getRawY : " + event.getRawY());
//                        Log.e(TAG, "MY event.getX1 : " + event.getX(1));
//                        Log.e(TAG, "MY event.getY1 : " + event.getY(1));

                    int X1 = (int) motionEvent.getX(1); // 두 번째 닿은 손의 좌표
//                        Log.e("X1", Integer.toString(X1));


                    if (X1 < width / 2) { // 두 번째 눌린 손가락이 왼쪽일 때
                        Log.e("ACTION_POINTER_DOWN", "2손 왼 down이거나 down중일 때");
                        if (point_0_index == point_index) {//첫 번째 손가락이 다시 닿았을 때(오른 손이 닿았을 때), 왼쪽은 계속 닿아있는 상태
                            Log.e("ACTION_POINTER_DOWN", "1 손 오른 down");
                            down_right_X_to_roll_origin = (LBX);
                            down_right_Y_to_pitch_origin = (LBY);

                            if (Y < right_circle.getY()){
                                Y = (int) (right_circle_y_pos -Y);
                            }else {
                                Y = (int) -(right_circle_y_pos -Y);
                            }

//                            right_circle.setTranslationX(-(left_circle_x_pos-X));
//                            right_circle.setTranslationY(Y);
                            right_btn.setTranslationX(-(left_circle_x_pos-X));
                            right_btn.setTranslationY(Y);
                        } else { //오른쪽을 안 떼고, 왼쪽 손이 다시 닿았을 때
                            Log.e("ACTION_POINTER_DOWN", "2손 왼 down");
                            down_left_X_to_yaw_origin = (int) (motionEvent.getX(1) - w_left_btn_rad);
                            if (cont_version == 2)
                                down_left_Y_to_throttle_origin = (int) (motionEvent.getY(1) - h_left_btn_rad);

                            if (motionEvent.getY(1) < left_circle.getY()){
                                Y = (int) (left_circle_y_pos - motionEvent.getY(1));
                            }else {
                                Y = (int) -(left_circle_y_pos -motionEvent.getY(1));
                            }

//                            left_circle.setTranslationX(-(left_circle_x_pos-motionEvent.getX(1)));
//                            if (cont_version == 1)
//                                left_circle.setTranslationY(Y);
//                            else left_circle.setTranslationY(Y);
                            Log.e("ACTION Y",Y+"");
                            left_btn.setTranslationX(-(left_circle_x_pos-motionEvent.getX(1)));
                            left_btn.setTranslationY(Y);
                        }
                    } else { // 두 번째 닿은 손이 오른쪽일 때
                        Log.e("ACTION_POINTER_DOWN", "2손 오른 down이거나  down중일 때");
                        if (point_0_index == point_index) { //오른쪽 안 떼고, 왼쪽이 다시 닿음
                            Log.e("ACTION_POINTER_DOWN", "1손 왼 down");
                            down_left_X_to_yaw_origin = (LBX);
                            down_left_Y_to_throttle_origin = (LBY);

                            if (Y < left_circle.getY()){
                                Y = (int) (left_circle_y_pos -Y);
                            }else {
                                Y = (int) -(left_circle_y_pos -Y);
                            }

//                            left_circle.setTranslationX(-(left_circle_x_pos-X));
//                            if (cont_version == 1)
//                                left_circle.setTranslationY(Y);
//                            else left_circle.setTranslationY(Y);
                            left_btn.setTranslationX(-(left_circle_x_pos-X));
                            left_btn.setTranslationY(Y);
                        } else { //왼쪽 안 떼고, 오른쪽 다시 닿음
                            Log.e("ACTION_POINTER_DOWN", "2손 오른 down");
                            down_right_X_to_roll_origin = (int) (motionEvent.getX(1) - w_left_btn_rad);
                            down_right_Y_to_pitch_origin = (int) (motionEvent.getY(1) - h_left_btn_rad);

                            if (motionEvent.getY(1) < right_circle.getY()){
                                Y = (int) (right_circle_y_pos -motionEvent.getY(1));
                            }else {
                                Y = (int) -(right_circle_y_pos -motionEvent.getY(1));
                            }

//                            right_circle.setTranslationX(-(right_circle_x_pos- motionEvent.getX(1)));
//                            right_circle.setTranslationY(Y);
                            right_btn.setTranslationX(-(right_circle_x_pos- motionEvent.getX(1)));
                            right_btn.setTranslationY(Y);

                        }

                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP: //떨어졌을 때
                    point_index = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
//                        Log.e(TAG, "MY id : " + point_index);

                    X1 = (int) motionEvent.getX(1);

                    if (X1 < width / 2) {
                        Log.e("ACTION_POINTER_UP", "2손이 왼일 때");
                        if (point_0_index == point_index) {
                            Log.e("ACTION_POINTER_UP", "1손 오른 up");
                            right_btn.setTranslationX(0);
                            right_btn.setTranslationY(0);
//                            right_circle.setTranslationX(0);
//                            right_circle.setTranslationY(0);
                        } else {
                            Log.e("ACTION_POINTER_UP", "2손 왼 up");
                            left_btn.setTranslationX(0);
                            if (cont_version == 2) left_btn.setTranslationY(0);
                            else left_btn.setTranslationY(0);
//                            left_circle.setTranslationX(0);
//                            left_circle.setTranslationY(0);
                        }
                    } else {
                        Log.e("ACTION_POINTER_UP", "2손이 오른일 때");
                        if (point_0_index == point_index) {
                            Log.e("ACTION_POINTER_UP", "1손 왼 up");
                            left_btn.setTranslationX(0);
                            if (cont_version == 2) left_btn.setTranslationY(0);
                            else left_btn.setTranslationY(0);
//                            left_circle.setTranslationX(0);
//                            left_circle.setTranslationY(0);
                        } else {
                            Log.e("ACTION_POINTER_UP", "2손 오른 up");
                            right_btn.setTranslationX(0);
                            right_btn.setTranslationY(0);
//                            right_circle.setTranslationX(0);
//                            right_circle.setTranslationY(0);
                        }

                    }
                    break;
                case MotionEvent.ACTION_MOVE: //움직일 때
                    Log.e("ACTION move","in");
                    if (pCnt == 1) {
                        Log.e("ACTION y",Y+", "+left_circle.getY());
                        if (X < width / 2) {
                            if (Y < left_btn.getY()){
                                Y = (int) (left_circle_y_pos - motionEvent.getY());
                            }else {
                                Y = (int) -(left_circle_y_pos -motionEvent.getY());
                            }
                            left_btn.setTranslationX(-(left_circle_x_pos-X));
                            left_btn.setTranslationY(Y);
                        } else {
                            if (Y < right_btn.getY()){
                                Y = (int) (right_circle_y_pos - motionEvent.getY());
                            }else {
                                Y = (int) -(right_circle_y_pos -motionEvent.getY());
                            }
                            right_btn.setTranslationX(-(right_circle_x_pos-X));
                            right_btn.setTranslationY(Y);
                        }
                        Log.e("ACTION y",Y+"");
                    } else {
                        if (X < width / 2) {
                            int YR = 0;
                            int YL = 0;
                            if (Y < left_btn.getY()){
                                YL = (int) (left_circle_y_pos - motionEvent.getY());
                            }else {
                                YL = (int) -(left_circle_y_pos -motionEvent.getY());
                            }
                            if (motionEvent.getY(1) < right_btn.getY()){
                                YR = (int) (right_circle_y_pos - motionEvent.getY(1));
                            }else {
                                YR = (int) -(right_circle_y_pos -motionEvent.getY(1));
                            }

                            left_btn.setTranslationX(-(left_circle_x_pos-X));
                            left_btn.setTranslationY(YL);
                            right_btn.setTranslationX(-(right_circle_x_pos-motionEvent.getX(1)));
                            right_btn.setTranslationY(YR);
                            Log.e("ACTION y",YR+","+YL);
                        } else {

                            int YR = 0;
                            int YL = 0;
                            if (motionEvent.getY(1) < left_btn.getY()){
                                YL = (int) (left_circle_y_pos - motionEvent.getY(1));
                            }else {
                                YL = (int) -(left_circle_y_pos -motionEvent.getY(1));
                            }
                            if (Y < right_btn.getY()){
                                YR = (int) (right_circle_y_pos - motionEvent.getY());
                            }else {
                                YR = (int) -(right_circle_y_pos -motionEvent.getY());
                            }


                            right_btn.setTranslationX(-(right_circle_x_pos-X));
                            right_btn.setTranslationY(YR);
                            left_btn.setTranslationX(-(left_circle_x_pos-motionEvent.getX(1)));
                            left_btn.setTranslationY(YL);
                            Log.e("ACTION y",YR+","+YL);
                        }
                    }

                    break;

            } //switch


            fin_left_X_diff = (int) -(left_circle.getTranslationX() - left_btn.getTranslationX());
            fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY());
            fin_right_X_diff = (int) -(right_circle.getTranslationX() - right_btn.getTranslationX());
            fin_right_Y_diff = (int) (right_circle.getTranslationY() - right_btn.getTranslationY());

            if (fin_left_Y_diff > w_left_circle_rad) {
                fin_left_Y_diff = w_left_circle_rad;
            } else if (fin_left_Y_diff < -w_left_circle_rad) {
                fin_left_Y_diff = -w_left_circle_rad;
            }

            Log.e("check left_circle getTranslationX", fin_left_X_diff+"");
            Log.e("check left_btn getTranslationX", fin_left_Y_diff+"");

            int[] cont = new int[4]; // roll, pitch, yaw, throttle
            cont[2] = ((fin_left_X_diff + w_left_circle_rad) * 250) / w_left_circle;
            cont[3] = ((fin_left_Y_diff + w_left_circle_rad) * 250) / w_left_circle;
            cont[1] = ((fin_right_Y_diff + w_left_circle_rad) * 250) / w_left_circle;
            cont[0] = ((fin_right_X_diff + w_left_circle_rad) * 250) / w_left_circle;

//                Log.e("rpyt", Integer.toString(cont[3]));
//            Log.e("check controller first throttle",cont[3]+"");
            for (int i = 0; i < 4; i++) {
                if (cont[i] > 250){
                    cont[i] = 250;
                }
                else if (cont[i] < 0){
                    cont[i] = 0;
                    if (i == 3)
                        Log.e("check controller throttle in ","0!!!!!!!!!!!!!!!!!!!");
                }
                if (i != 3) cont[i] = fineTuning(cont[i]);
            }
            if (cont_version == 2) cont[3] = fineTuning(cont[3]);

            roll = (byte) cont[0];
            pitch = (byte) cont[1];
            yaw = (byte) cont[2];
            throttle = (byte) cont[3];

            txt_roll.setText(String.valueOf(cont[0]));
            txt_pitch.setText(String.valueOf(cont[1]));
            txt_yaw.setText(String.valueOf(cont[2]));
            txt_throttle.setText(String.valueOf(cont[3]));

            return true;
        });


        seek_r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek_r_val = (byte) seekBar.getProgress();
                rgb = true;
//                Log.e(TAG, "hihiw");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seek_g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek_g_val = (byte) seekBar.getProgress();
                rgb = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seek_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek_b_val = (byte) seekBar.getProgress();
                rgb = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        

    }

    public int fineTuning(int x) {
        int center = 125;

        if (x < center + 5 && x > center) {
            x = center;
        } else if (x >= center + 5) {
            x = x - 5;
        }
        if (x > center - 5 && x < center) {
            x = center;
        } else if (x <= center - 5) {
            x = x + 5;
        }
//        if (x > center + 40) {
//            x = center + 40;
//        } else if (x < center - 40) {
//            x = center - 40;
//        }
        return x;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ARM:
//                    Log.e(TAG+"ARM", (String)msg.obj);
                    armPosition();
                    break;
                case MSG:
//                    Log.e(TAG + "MSG", (String) msg.obj);
//                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case BAT:
//                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, (String) msg.obj);
//                    if (msg.arg1 == 2) drone_menu_tabs.getTabAt(4).setText()
//                    else txt_bat.setTextColor(Color.WHITE);
//                    txt_bat.setText((String) msg.obj);

                    drone_menu_tabs.getTabAt(4).setText((String) msg.obj);
                    break;
                case DEBUG:
                    int[] a = (int[]) msg.obj;
                    int[] b = new int[4];
                    for (int i = 0; i < 4; i++) {
                        int c = 0;
                        if(a[2 * i] < 0) c = 256;
                        b[i] = a[2 * i] + c + (a[2 * i + 1] << 8);
//                        Log.e("a0", Integer.toString(a[2*i]));
//                        Log.e("a1", Integer.toString((a[2 * i + 1] << 8)));
                    }
//                    Log.e("a00", Integer.toString(a[0]));
//                    Log.e("a01", Integer.toString(a[1]));
//                    Log.e("a10", Integer.toString(a[2]));
//                    Log.e("a11", Integer.toString(a[3]));
//                    Log.e("a20", Integer.toString(a[4]));
//                    Log.e("a21", Integer.toString(a[5]));
//                    Log.e("a30", Integer.toString(a[6]));
//                    Log.e("a31", Integer.toString(a[7]));
//                    txt_bat.setText((String) msg.obj);
                    txt_debug_0.setText(Integer.toString(b[0]));
                    txt_debug_1.setText(Integer.toString(b[1]));
                    txt_debug_2.setText(Integer.toString(b[2]));
                    txt_debug_3.setText(Integer.toString(b[3]));
                    break;
                case RECEIVED:
//                    if (msg.arg1 == 2) txt_bat.setTextColor(Color.RED);
//                    else txt_bat.setTextColor(Color.WHITE);
//                    txt_bat.setText((String) msg.obj);
                    IntentFilter rssiFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                    registerReceiver(rssiReceiver, rssiFilter);
                    WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiMan.startScan();
                    drone_menu_tabs.getTabAt(4).setText("Battery:"+(String) msg.obj);
                    break;
            }
        }
    }

    private BroadcastReceiver rssiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiMan.startScan();
            int linkspeed = wifiMan.getConnectionInfo().getLinkSpeed();
            int newRssi = wifiMan.getConnectionInfo().getRssi();
            int level = wifiMan.calculateSignalLevel(newRssi, 10);
            int percentage = (int) ((level/10.0)*100);
            String macAdd = wifiMan.getConnectionInfo().getBSSID();
            Log.e("linkspeed",linkspeed+"");
            drone_menu_tabs.getTabAt(5).setText(percentage+"");
            //debugtext.setText("링크 스피드 : " + linkspeed + " / 신호 감도 : " + percentage + " / 맥어드레스 : " + macAdd );
        }
    };

    public class UDPClient extends Thread {

        InetAddress serverAddr;

        public UDPClient() {
            try {
                // Retrieve the servername
                serverAddr = InetAddress.getByName(ip);
                Log.e("UDP", "C: Connecting...");

                GetUDPMSG getUDPMSG = new GetUDPMSG(socket);
                getUDPMSG.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                Log.e("UDPClient", "run");
                while (true) {
                    while (STOP) {
//                        Log.e("my", "STOP");
                        if (!SW) {
                            break;
                        }
                    }
                    if (!SW) {
                        break;
                    }
                    /* Prepare some data to be sent. */
//                    byte[] buf = ("Hello from Client").getBytes();

                    /*                    ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
                    buff.order(ByteOrder.LITTLE_ENDIAN);
                    buff.putInt(150);
                    temp[4] = buff.get();*/


//                    byte[] buf = ("$M<" + "5" + "150" + Integer.toString(pitch) + Integer.toString(roll) + Integer.toString(yaw) + Integer.toString(throttle) + "5" + Integer.toString(checkSum)).getBytes();
//                    byte[] buf = ("$M<" + "150" + Integer.toString(pitch) + Integer.toString(roll) + Integer.toString(yaw) + Integer.toString(throttle) + "5" + Integer.toString(checkSum)).getBytes();


                    if (arm) {
                        Log.e("handler arm","in");
                        Message msg = myHandler.obtainMessage();
                        msg.what = ARM;
                        msg.arg1 = 4;
                        msg.obj = "hi";
                        myHandler.sendMessage(msg);

                        sendFunction(socket, serverAddr, (byte) 0x97);
//                        sendSignal(socket, serverAddr, (byte)1500, (byte)1500, (byte)2000, (byte)1000); // fail for 2.4
                    } else if (disarm) {
                        Log.e("handler disarm","in");
                        sendFunction(socket, serverAddr, (byte) 0x98);
                    } else if (cal) {
                        sendFunction(socket, serverAddr, (byte) 0xcd);
                    } else if (trim_forward) {
                        sendFunction(socket, serverAddr, (byte) 0x99);
                    } else if (trim_backward) {
                        sendFunction(socket, serverAddr, (byte) 0x9A);
                    } else if (trim_left) {
                        sendFunction(socket, serverAddr, (byte) 0x9B);
                    } else if (trim_right) {
                        sendFunction(socket, serverAddr, (byte) 0x9C);
                    } else if (rgb) {
                        Log.e("send","rgb in");
                        sendRGB(socket, serverAddr, (byte) 0x8D);
                    } else if (!STOP) {
//                        Log.e("send","stop in");
//                        Log.e("send value", roll+","+pitch+","+yaw+","+throttle);
                        sendSignal(socket, serverAddr, roll, pitch, yaw, throttle);
//                        Log.e(TAG+"sendSignal", Integer.toString(cont_version));
                    }

                    // bat
                    if (!connected) {
                        Message msg = myHandler.obtainMessage();
                        msg.what = BAT;
                        msg.arg1 = 2;
                        msg.obj = "not received";
                        myHandler.sendMessage(msg);
                    }
                    batCount++;
                    if (batCount % 20 == 0) {
                        if (batCount > 100) {
                            batCount = 0;
                            connected = false;
                        }
                        sendFunction(socket, serverAddr, (byte) 0xA3); // bat
//                        sendFunction(socket, serverAddr, (byte) 0xfe);// 0xA3); // debug0
//                        Log.e(TAG, "send bat");
                    }

                    debugCount++;
                    if (debugCount % 5 == 0) {
                        debugCount = 0;
//                        sendFunction(socket, serverAddr, (byte) 0xfe); // debug
                    }

//                    else if (SAFE_LANDING) {
////                    } else if (SAFE_LANDING && (throttle & 0xff) > 150) {
//                        for (int i = 5; i > 0; i--) {
//                            for (int j = 0; j < 30; j++) {
//                                sendSignal(socket, serverAddr, (byte) 0x7d, (byte) 0x7d, (byte) 0x7d, (byte) (i * 30));
////                                sendSignal(socket, serverAddr, (byte) 0x7d, (byte) 0x7d, (byte) 0x7d, (byte) (100));
//                                Thread.sleep(20);
////                                Log.e("my", Integer.toString(j));
//                            }
//                        }
//                        SAFE_LANDING = false;
//                    }

                    arm = false;
                    disarm = false;
                    cal = false;
                    trim_forward = false;
                    trim_backward = false;
                    trim_left = false;
                    trim_right = false;
                    rgb = false;
                    checkSum = 0;
                    Thread.sleep(20);
                    if (rgbNcal) {
                        rgbNcal = false;
                        rgb = true;
                    }

                    /*            *//* Create UDP-packet with
                     * data & destination(url+port) *//*
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, port);
                    Log.d("UDP", "C: Sending: '" + new String(buf) + "'");
                    Log.d("UDP", "C: Sending:temp '" + new String(temp) + "'");

            *//* Send out the packet *//*
                    socket.send(packet);
                    Log.d("UDP", "C: Sent.");
                    Log.d("UDP", "C: Done.");*/


//                socket.receive(packet);
//                Log.d("UDP", "C: Received: '" + new String(packet.getData()) + "'");


                }

            } catch (Exception e) {
                Log.e("UDP", "C: Error", e);
            }
        }
    }


    void armPosition() {
//        left_btn.setTranslationX(cen_w_left_btn);
//        left_btn.setTranslationX(down_left_X_to_yaw_origin);
//        left_btn.setTranslationY(cen_h_left_btn + w_left_circle_rad);
//        left_circle.setTranslationX(cen_w_left_circle);
//        left_circle.setTranslationY(cen_h_left_circle);

//        fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY() + gap);

        Log.e("check arm in","in!!!!");
        if (cont_version == 1) {
            left_btn.setTranslationY(cen_h_left_btn + w_left_circle_rad);
//            left_circle.setTranslationY(0);
            fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY() + gap);
            throttle = (byte) 0;
        } else throttle = (byte) 0x7d;

//        txt_roll.setText(Byte.toString(roll));
//        txt_pitch.setText(Byte.toString(pitch));
//        txt_yaw.setText(Byte.toString(yaw));
//        txt_throttle.setText(Byte.toString(throttle));
    }


    void sendFunction(DatagramSocket socket, InetAddress serverAddr, byte signal) {
        try {
            if ((trim_forward || trim_backward || trim_left || trim_right) && (x125 == 10))
                signal += (byte) 4;
            byte[] armTemp = new byte[6];
            armTemp[0] = (byte) '$';
            armTemp[1] = (byte) 'M';
            armTemp[2] = (byte) '<';
            armTemp[3] = (byte) 0;
            armTemp[4] = signal;
            armTemp[5] = signal;

            DatagramPacket packet = new DatagramPacket(armTemp, armTemp.length, serverAddr, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendSignal(DatagramSocket socket, InetAddress serverAddr, byte r, byte p, byte y, byte t) {
        try {
            byte[] temp = new byte[11];
            temp[0] = (byte) '$';
            temp[1] = (byte) 'M';
            temp[2] = (byte) '<';
            temp[3] = (byte) 0x05;
            checkSum ^= (byte) 0x05;
            if (cont_version == 2) {
                temp[4] = (byte) 0x95;
                checkSum ^= (byte) 0x95;
            } else {
                temp[4] = (byte) 0x96;
                checkSum ^= (byte) 0x96;
            }
            temp[5] = r;
            checkSum ^= r;
            temp[6] = p;
            checkSum ^= p;
            temp[7] = y;
            checkSum ^= y;
            temp[8] = t;
            checkSum ^= t;
            if (optic) {
                temp[9] = 0x00;
                checkSum ^= 0x00; //AUX
            } else {
                temp[9] = 0x01;
                checkSum ^= 0x01;
            }
            temp[10] = checkSum;
            DatagramPacket packet = new DatagramPacket(temp, temp.length, serverAddr, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendRGB(DatagramSocket socket, InetAddress serverAddr, byte sign) {
        try {
            byte[] temp = new byte[11];
            temp[0] = (byte) '$';
            temp[1] = (byte) 'M';
            temp[2] = (byte) '<';
            temp[3] = (byte) 0x05;
            checkSum ^= (byte) 0x05;
            temp[4] = sign;
            checkSum ^= sign;
            temp[5] = seek_r_val;
            checkSum ^= seek_r_val;
            temp[6] = seek_g_val;
            checkSum ^= seek_g_val;
            temp[7] = seek_b_val;
            checkSum ^= seek_b_val;
            temp[8] = rgb_id_val;
            checkSum ^= rgb_id_val;
            temp[9] = 0x00;
            checkSum ^= 0x00; //AUX
            temp[10] = checkSum;
            DatagramPacket packet = new DatagramPacket(temp, temp.length, serverAddr, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GetUDPMSG extends Thread {

        String text;

        byte[] message = new byte[20];
        DatagramPacket p = new DatagramPacket(message, message.length);

        DatagramSocket mDataGramSocket;

        public GetUDPMSG(DatagramSocket socket) {
            try {
                mDataGramSocket = socket;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (!SW) {
                        break;
                    }
                    try {
//                        Log.e(TAG + "bat", Integer.toString(p.getData()[4]));
                        mDataGramSocket.receive(p);
//                        text = new String(message, 0, p.getLength()); // x
                        Message msg = myHandler.obtainMessage();
                        if (p.getData()[4] == (byte) 0xA3) {
                            Log.e(TAG, "BAT GOT IT");
                            batCount = 1;
                            connected = true;
////                            Log.e(TAG+"getData", "hi");
                            int a = p.getData()[5];
////                            Log.e(TAG + "getData", Integer.toString(a));
                            msg.what = BAT;
                            msg.arg1 = 1;
                            msg.obj = Integer.toString(a);
                            myHandler.sendMessage(msg);
                        } else if (p.getData()[4] == (byte) 0xfe) {
                            msg.what = DEBUG;
                            int[] a = new int[8];
                            for (int i = 0; i < 8; i++) {
                                a[i] = p.getData()[i + 5];
                            }
                            msg.obj = a;

                            myHandler.sendMessage(msg);
//                            Log.e(TAG + "getData5", Integer.toString(a));
                        } else {
                            batCount = 1;
                            connected = true;
                            msg.what = RECEIVED;
                            msg.arg1 = 1;
                            msg.obj = (char) p.getData()[0] + " received";
                            myHandler.sendMessage(msg);
                            Log.e(TAG, "GOT IT");
                        }
//                        String bat = Byte.toString(a); // 굳이 스트링 갈 필요 없다.
                        // If you're not using an infinite loop:
                        //mDataGramSocket.close();
                    } catch (NullPointerException | IOException e) {
                        // no response received after 1 second. continue sending

                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rgb_version_change_btn:
                rgb_id_val++;
                if (rgb_id_val > 2){
                    rgb_id_val = 0;
                }
                rgb_version_change_btn.setText(rgb_id_val+"");
                break;

            case R.id.trim_setting_btn:
                if (x125 == 10){
                    x125 = 1;
                }
                else{
                    x125 = 10;
                }

                trim_setting_btn.setText("X "+x125);
                forward.setText("Forward\nX "+x125);
                backward.setText("Backward\nX "+x125);
                left.setText("left\nX "+x125);
                right.setText("right\nX "+x125);
//                btn_x12510.setText("x" + Integer.toString(x125));
//                btn_trim_forward.setText("forward x " + Integer.toString(x125));
//                btn_trim_backward.setText("backward x " + Integer.toString(x125));
//                btn_trim_left.setText("left x " + Integer.toString(x125));
//                btn_trim_right.setText("right x " + Integer.toString(x125));

                break;

            case R.id.arm_btn:
                arm = true;

                break;

            case R.id.disarm_btn:
                disarm = true;
                if (cont_version == 1) {
                    left_btn.setTranslationY(cen_h_left_btn + w_left_circle_rad);
                    fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY() + gap);
                    throttle = (byte) 0;
                    txt_throttle.setText(Byte.toString(throttle));
                }
                break;

            case R.id.forward:
                trim_forward = true;
                break;

            case R.id.backward:
                trim_forward = true;
                break;

            case R.id.left:
                trim_forward = true;
                break;

            case R.id.right:
                trim_forward = true;
                break;
        }
//        switch (view.getId()) {
//            case R.id.btn_arm:
//                arm = true;
//                break;
//            case R.id.btn_disarm:
//                disarm = true;
//                if (cont_version == 1) {
//                    left_btn.setTranslationY(cen_h_left_btn + w_left_circle_rad);
//                    left_circle.setTranslationY(cen_h_left_circle);
//                    fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY() + gap);
//                    throttle = (byte) 0;
//                    txt_throttle.setText(Byte.toString(throttle));
//                }
//                break;
//            case R.id.btn_cal:
//                cal = true;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        rgbNcal = true; // 됨
//                    }
//                }, 500);
////                rgbNcal = true; // 왜 안돼 > 딜레이 없어서
//                break;
//            case R.id.btn_wifi:
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                break;
//            case R.id.btn_setting:
//                setting = !setting;
//                if (setting) {
//                    layout_controller.setVisibility(View.INVISIBLE);
//                    layout_trim.setVisibility(View.VISIBLE);
//                    btn_arm.setVisibility(View.INVISIBLE);
//                    btn_disarm.setVisibility(View.INVISIBLE);
//                    btn_optic.setVisibility(View.VISIBLE);
////                    btn_debug.setVisibility(View.VISIBLE);
//                    if (debug) {
//                        btn_wifi.setVisibility(View.INVISIBLE);
//                        btn_cal.setVisibility(View.INVISIBLE);
//                        layout_debug.setVisibility(View.VISIBLE);
//                        layout_text.setVisibility(View.INVISIBLE);
//                    }
//                } else {
//                    layout_controller.setVisibility(View.VISIBLE);
//                    layout_trim.setVisibility(View.INVISIBLE);
//                    btn_arm.setVisibility(View.VISIBLE);
//                    btn_disarm.setVisibility(View.VISIBLE);
//                    btn_optic.setVisibility(View.INVISIBLE);
//                    btn_debug.setVisibility(View.INVISIBLE);
//
//                    btn_wifi.setVisibility(View.VISIBLE);
//                    btn_cal.setVisibility(View.VISIBLE);
//                    layout_debug.setVisibility(View.INVISIBLE);
//                    layout_text.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.btn_trim_forward:
//                trim_forward = true;
////                Log.e(TAG, "trim");
//                break;
//            case R.id.btn_trim_backward:
//                trim_backward = true;
//                break;
//            case R.id.btn_trim_left:
//                trim_left = true;
//                break;
//            case R.id.btn_trim_right:
//                trim_right = true;
//                break;
//            case R.id.btn_x12510:
//                if (x125 == 10) x125 = 1;
//                else x125 = 10;
//                btn_x12510.setText("x" + Integer.toString(x125));
//                btn_trim_forward.setText("forward x " + Integer.toString(x125));
//                btn_trim_backward.setText("backward x " + Integer.toString(x125));
//                btn_trim_left.setText("left x " + Integer.toString(x125));
//                btn_trim_right.setText("right x " + Integer.toString(x125));
//                break;
//            case R.id.btn_version:
//                if (cont_version == 1) {
//                    cont_version = 2;
//                    left_btn.setTranslationY(cen_h_left_btn);
//                    left_circle.setTranslationY(cen_h_left_circle);
//                    throttle = (byte) 0x7d;
//                } else {
//                    cont_version = 1;
//                    left_btn.setTranslationY(cen_h_left_btn + w_left_circle_rad);
//                    left_circle.setTranslationY(cen_h_left_circle);
//                    fin_left_Y_diff = (int) (left_circle.getTranslationY() - left_btn.getTranslationY() + gap);
//                    throttle = (byte) 0;
//                }
//                txt_throttle.setText(Byte.toString(throttle));
//                btn_version.setText("version " + Integer.toString(cont_version));
//                break;
//            case R.id.btn_rgb_id:
//                rgb_id_val++;
//                if (rgb_id_val > 2) rgb_id_val = 0;
//                btn_rgb_id.setText(Byte.toString(rgb_id_val));
//                break;
//            case R.id.btn_optic:
//                optic = !optic;
//                if (optic) btn_optic.setText("optical");
//                else btn_optic.setText("flow");
//                break;
//            case R.id.btn_debug:
//                debug = !debug;
//                if (debug) {
//                    btn_trim_forward.setVisibility(View.INVISIBLE);
//                    btn_trim_backward.setVisibility(View.INVISIBLE);
//                    btn_trim_left.setVisibility(View.INVISIBLE);
//                    btn_trim_right.setVisibility(View.INVISIBLE);
//                    btn_x12510.setVisibility(View.INVISIBLE);
//                    btn_rgb_id.setVisibility(View.INVISIBLE);
//                    btn_version.setVisibility(View.INVISIBLE);
//                    btn_wifi.setVisibility(View.INVISIBLE);
//                    btn_cal.setVisibility(View.INVISIBLE);
//                    seek_r.setVisibility(View.INVISIBLE);
//                    seek_g.setVisibility(View.INVISIBLE);
//                    seek_b.setVisibility(View.INVISIBLE);
//                    layout_text.setVisibility(View.INVISIBLE);
//                    layout_debug.setVisibility(View.VISIBLE);
//                } else {
//                    btn_trim_forward.setVisibility(View.VISIBLE);
//                    btn_trim_backward.setVisibility(View.VISIBLE);
//                    btn_trim_left.setVisibility(View.VISIBLE);
//                    btn_trim_right.setVisibility(View.VISIBLE);
//                    btn_x12510.setVisibility(View.VISIBLE);
//                    btn_rgb_id.setVisibility(View.VISIBLE);
//                    btn_version.setVisibility(View.VISIBLE);
//                    btn_wifi.setVisibility(View.VISIBLE);
//                    btn_cal.setVisibility(View.VISIBLE);
//                    seek_r.setVisibility(View.VISIBLE);
//                    seek_g.setVisibility(View.VISIBLE);
//                    seek_b.setVisibility(View.VISIBLE);
//                    layout_text.setVisibility(View.VISIBLE);
//                    layout_debug.setVisibility(View.INVISIBLE);
//                }
//                break;
//        }
    }


    @Override
    protected void onStart() {
        Log.e("my", "onStart");
        armPosition();
        SW = true;
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client = new UDPClient();
        client.start();

        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e("my", "onResume");
        rgb = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        Log.e("my", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e("my", "onStop");

        SW = false;

        //소켓 함부로 닫지 마라
//        if (!socket.isClosed()) {
//            socket.close();
//        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("my", "onDestroy");
        if (!socket.isClosed()) {
            Log.e("my", "!isClosed()");
            socket.close();
            Log.e("my", Boolean.toString(socket.isConnected()));
            Log.e("my", Boolean.toString(socket.isBound()));
            Log.e("my", Boolean.toString(socket.isClosed()));
        }
        unregisterReceiver(rssiReceiver);
        super.onDestroy();
    }
}