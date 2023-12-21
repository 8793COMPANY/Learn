package com.learn4.view.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.learn4.R;
import com.learn4.WeatherData;
import com.learn4.data.dto.WeatherEx;
import com.learn4.util.DataSetting;
import com.learn4.view.mode_select.ModeSelect;

import org.json.JSONException;

import java.io.IOException;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        hideSystemUI();

        Handler handler = new Handler();

        Log.e("time check", DataSetting.getInstance(this).getTime());


        // TODO: 날씨블록 체크
        //데이터 저장 타입 변경 가능
        String [] date = DataSetting.getInstance(this).getTime().split(" ");
        String [] local = {"서울특별시", "인천광역시", "대전광역시", "대구광역시", "울산광역시", "광주광역시", "부산광역시"};

        new Thread(() -> {
            try {
                Log.e("testtest", "start");
                DataSetting.getInstance(this).setting_weather[0] = "11";
                DataSetting.getInstance(this).setting_weather[1] = "강수없음";
                DataSetting.getInstance(this).setting_weather[2] = "1";
                DataSetting.getInstance(this).setting_weather[3] = "85";
                DataSetting.getInstance(this).setting_weather[4] = "0";
                DataSetting.getInstance(this).setting_weather[5] = "11";
                Log.e("testtest", "end");

                WeatherData weatherData = new WeatherData();
                weatherData.lookUpWeather(date[0],date[1]+"시","광주광역시","기온");
//                DataSetting.getInstance(this).setting_weather[0] = weatherData.getData("기온");
//                DataSetting.getInstance(this).setting_weather[1] = weatherData.getData("강수량");
//                DataSetting.getInstance(this).setting_weather[2] = weatherData.getData("하늘상태");
//                DataSetting.getInstance(this).setting_weather[3] = weatherData.getData("습도");
//                DataSetting.getInstance(this).setting_weather[4] = weatherData.getData("강수형태");
//                DataSetting.getInstance(this).setting_weather[5] = weatherData.getData("풍속");

                //추가하는 부분
                for (int i=0; i < local.length; i++) {
                    weatherData.lookUpWeather(date[0], date[1]+"시", local[i],"기온");
                    DataSetting.getInstance(this).weatherData_list.add(new WeatherEx(date[0], date[1]+"시", local[i], weatherData.getData("기온"),
                            weatherData.getData("강수량"), weatherData.getData("하늘상태"), weatherData.getData("습도"),
                            weatherData.getData("강수형태"), weatherData.getData("풍속")));
                }

                Log.e("testtest", DataSetting.getInstance(this).weatherData_list.size()+"");

                for (int i=0; i < DataSetting.getInstance(this).weatherData_list.size(); i++) {
                    Log.e("testtest", "시작");
                    Log.e("testtest", DataSetting.getInstance(this).weatherData_list.get(i).getLocal()+"");
                    Log.e("testtest", DataSetting.getInstance(this).weatherData_list.get(i).getTMP()+"");
                    Log.e("testtest", DataSetting.getInstance(this).weatherData_list.get(i).getPCP()+"");
                    Log.e("testtest", DataSetting.getInstance(this).weatherData_list.get(i).getSKY()+"");
                    Log.e("testtest", DataSetting.getInstance(this).weatherData_list.get(i).getREH()+"");
                    Log.e("testtest", DataSetting.getInstance(this).weatherData_list.get(i).getPTY()+"");
                    Log.e("testtest", DataSetting.getInstance(this).weatherData_list.get(i).getWSD()+"");
                    Log.e("testtest", "끝");
                }

            }catch (IOException e){
                e.printStackTrace();
                Log.e("testtest", "IOException : " + e.getMessage());

            }catch (JSONException e){
                e.printStackTrace();
                Log.e("testtest", "JSONException : " + e.getMessage());
            }
        }).start();


        
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, ModeSelect.class);
            startActivity(intent);
            finish();
        }, 2500);
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