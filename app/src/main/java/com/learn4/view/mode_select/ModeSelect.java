package com.learn4.view.mode_select;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

import com.learn4.R;
import com.google.android.material.navigation.NavigationView;
import com.learn4.WeatherData;
import com.learn4.util.Application;
import com.learn4.util.DataSetting;
import com.learn4.util.MySharedPreferences;
import com.learn4.view.custom.dialog.CouponCancelDialog;
import com.learn4.view.custom.dialog.CouponInputDialog;
import com.learn4.view.mode_select.home.HomeFragment;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.Executors;

public class ModeSelect extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private View decorView;
    private int	uiOption;

    DrawerLayout drawer_layout;

    NavigationView nav_view;
    CouponInputDialog couponInputDialog;
    CouponCancelDialog couponCancelDialog;

    String coupon_value = "2024-AGFA-FE01-1208";

    NavHostFragment navHostFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_select);
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout  drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
        DataSetting setting = DataSetting.getInstance(getApplicationContext());
        setting.dataCheck();

//        nav_view.setNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.nav_coupon_register:
//                    drawer_layout.closeDrawers();
//                    couponInputDialog = new CouponInputDialog(ModeSelect.this,coupon_input_ok_listener);
//                    couponInputDialog.show();
//
//                    Window window = couponInputDialog.getWindow();
//
//                    int x = (int) (Application.displaySize_X * 0.43f);
//                    int y = (int) (Application.displaySize_Y * 0.278f);
//                    window.setLayout(x, y);
//                    break;
//
//                case R.id.nav_coupon_cancel:
//                    if (MySharedPreferences.getBoolean(ModeSelect.this,"coupon_register_check")){
//                        drawer_layout.closeDrawers();
//                        couponCancelDialog = new CouponCancelDialog(ModeSelect.this,coupon_cancel_ok_listener, coupon_cancel_listener);
//                        couponCancelDialog.show();
//
//                        Window window2 = couponCancelDialog.getWindow();
//
//                        int x2 = (int) (Application.displaySize_X * 0.43f);
//                        int y2 = (int) (Application.displaySize_Y * 0.278f);
//                        window2.setLayout(x2, y2);
//                    }else{
//                        Toast.makeText(getApplicationContext(),"등록된 쿠폰이 없습니다.",Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//            }
//
//
//            return true;
//        });



        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
//        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);
//
//        actionBar.setCustomView(actionbar);
//
//        getSupportActionBar().getCustomView().findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Log.e("hi","in!");
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });
        findViewById(R.id.btnMenu).setOnClickListener(v->{
            drawer.openDrawer(GravityCompat.START);
        });



        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);


//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        NewThread nt = new NewThread() ;
//        nt.start() ;

    }


    private final View.OnClickListener coupon_input_ok_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("couponInputDialog clicklistener",couponInputDialog.word_input_box.getText().toString());

            if (couponInputDialog.word_input_box.getText().toString().trim().equals(coupon_value)){
                MySharedPreferences.setBoolean(getApplicationContext(),"coupon_register_check", true);
                ((HomeFragment)navHostFragment.getChildFragmentManager().getFragments().get(0)).changeBtnLock(true);
                Toast.makeText(getApplicationContext(),"쿠폰 등록이 완료되었습니다.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"사용할 수 없는 쿠폰입니다. 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
            }

            couponInputDialog.dismiss();


        }
    };


    private final View.OnClickListener coupon_cancel_ok_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            couponCancelDialog.dismiss();
            MySharedPreferences.setBoolean(getApplicationContext(),"coupon_register_check", false);
            ((HomeFragment)navHostFragment.getChildFragmentManager().getFragments().get(0)).changeBtnLock(false);
        }
    };


    private final View.OnClickListener coupon_cancel_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            couponCancelDialog.dismiss();


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.mode_select, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hideSystemUI();
    }

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



    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterNetworkCallback();
//    }

    class NewThread extends Thread {
        WeatherData weatherData;
        NewThread() {
            weatherData = new WeatherData();
        }

        public void run() {
            Log.e("run","start");
            try {
                weatherData.lookUpWeather("hi","hi","hi","기온");
                String st = weatherData.getTmperature();

                runOnUiThread(new Runnable() {
                    public void run() {



                    }
                });



//                Log.e("temp check",weatherData.getTmperature());


            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }


}