package com.learn4.view.mode_select.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.learn4.DroneActivity;
import com.learn4.util.Application;
import com.learn4.R;
import com.learn4.util.DisplaySize;
import com.learn4.view.ContentsWorkspace;
import com.learn4.view.contents.ContentsActivity;
import com.learn4.view.MainActivity;
import com.learn4.view.dictionary.BlockDictionaryActivity2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    LinearLayout free_mode,content_mode;
    Application myApplication;
    Button free_btn,contents_btn,dictionary_btn,drone_btn;
    TextView user_name, mode_select_text;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myApplication = (Application)getActivity().getApplication();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        free_mode = root.findViewById(R.id.free_mode);
        content_mode = root.findViewById(R.id.content_mode);
        contents_btn = root.findViewById(R.id.contents_btn);
        free_btn = root.findViewById(R.id.free_btn);
        dictionary_btn = root.findViewById(R.id.dictionary_btn);
        drone_btn = root.findViewById(R.id.drone_btn);

        user_name = root.findViewById(R.id.user_name);
        user_name.setTextSize(DisplaySize.font_size_y_27);

        mode_select_text = root.findViewById(R.id.mode_select_text);
        mode_select_text.setTextSize(DisplaySize.font_size_y_26);

//        new Thread(() -> {
//                WeatherData weatherData = new WeatherData();
//                try {
//                    weatherData.lookUpWeather();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//
//        }).start();



//        user_name.setText(Application.user.getName());

        free_btn.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("contents_name","none");
            intent.putExtra("id","0");
            myApplication.showLoadingScreen(getContext());
            startActivity(intent);

//            Intent intent = new Intent(getActivity(), ContentsWorkspace.class);
//            intent.putExtra("contents_name","none");
//            intent.putExtra("id","0");
//            myApplication.showLoadingScreen(getContext());
//            startActivity(intent);
        });

        content_mode.setOnClickListener(v->{
            Log.e("in!","!!");

        });

        contents_btn.setOnClickListener(v->{
            Log.e("in!","!!");
            Intent intent = new Intent(getActivity(), ContentsActivity.class);
            startActivity(intent);
        });

        dictionary_btn.setOnClickListener(v->{
            /*ContinueDialog continueDialog = new ContinueDialog(getActivity(), "사전을 만들고 있는 중입니다");
            continueDialog.show();*/
            Intent intent = new Intent(getActivity(), BlockDictionaryActivity2.class);
            startActivity(intent);

        });

        drone_btn.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), DroneActivity.class);
            startActivity(intent);
        });

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        String current = format.format(currentTime);
        Log.e("current day",current);

//        if (!current.equals(MySharedPreferences.getString(getContext(),"notice_check_day"))){
//            MySharedPreferences.setBoolean(getContext(), "notice_today_check", false);
//        }


//        if (!MySharedPreferences.getBoolean(getContext(),"notice_today_check")){
            Dialog dialog01;
            dialog01 = new Dialog(getContext());
            dialog01.setCanceledOnTouchOutside(false);
            dialog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog01.setContentView(R.layout.dialog_survey);

            dialog01.show();

            TextView st_beforehand_survey_write = dialog01.findViewById(R.id.st_beforehand_survey_write);
            TextView st_beforehand_survey_write_set = dialog01.findViewById(R.id.st_beforehand_survey_write_set);
            TextView st_after_survey_write = dialog01.findViewById(R.id.st_after_survey_write);
            TextView st_after_survey_write_set = dialog01.findViewById(R.id.st_after_survey_write_set);
            TextView st_satisfaction_survey_elementary_write = dialog01.findViewById(R.id.st_satisfaction_survey_elementary_write);
//            TextView st_satisfaction_survey_middle_write = dialog01.findViewById(R.id.st_satisfaction_survey_middle_write);
//            TextView st_satisfaction_survey_high_write = dialog01.findViewById(R.id.st_satisfaction_survey_high_write);
            TextView tr_after_survey_write = dialog01.findViewById(R.id.tr_after_survey_write);
            ScrollView surveys = dialog01.findViewById(R.id.surveys);

            surveys.setAlwaysDrawnWithCacheEnabled(true);

//            CheckBox today_check = dialog01.findViewById(R.id.today_check);

            st_beforehand_survey_write.setOnClickListener(view -> {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://st-beforehand.p-e.kr/"));
                startActivity(intentUrl);
            });

            st_beforehand_survey_write_set.setOnClickListener(view -> {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://st-beforehand-set.p-e.kr/"));
                startActivity(intentUrl);
            });

            st_after_survey_write.setOnClickListener(view -> {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://st-after.p-e.kr/"));
                startActivity(intentUrl);
            });

            st_after_survey_write_set.setOnClickListener(view -> {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://st-after-set.p-e.kr/"));
                startActivity(intentUrl);
            });

            //만족도 조사 초중고 안 나누고 하나로 통합
            st_satisfaction_survey_elementary_write.setOnClickListener(view -> {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://st-satisfaction.p-e.kr/"));
                startActivity(intentUrl);
            });

//            st_satisfaction_survey_middle_write.setOnClickListener(view -> {
//                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://st-m-satisfaction.p-e.kr/"));
//                startActivity(intentUrl);
//            });
//
//            st_satisfaction_survey_high_write.setOnClickListener(view -> {
//                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://st-h-satisfaction.p-e.kr/"));
//                startActivity(intentUrl);
//            });

            tr_after_survey_write.setOnClickListener(view -> {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tr-after.p-e.kr/"));
                startActivity(intentUrl);
            });

//            today_check.setOnCheckedChangeListener((compoundButton, b) -> {
//                Log.e("b",b+"");
//                if (b) {
//
//                    MySharedPreferences.setString(getContext(), "notice_check_day", current);
//                    MySharedPreferences.setBoolean(getContext(), "notice_today_check", true);
//                }else {
//                    MySharedPreferences.setBoolean(getContext(), "notice_today_check", false);
//                }
//            });


            dialog01.findViewById(R.id.close_btn).setOnClickListener(v->{
                dialog01.dismiss();
            });
//        }


//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    @Override
    public void onStop() {
        super.onStop();
        myApplication.hideLoadingScreen();
    }
}