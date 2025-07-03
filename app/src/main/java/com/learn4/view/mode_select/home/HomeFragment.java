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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.learn4.util.MySharedPreferences;
import com.learn4.view.bluetooth.BluetoothActivity;
import com.learn4.view.bluetooth.BluetoothActivity2;
import com.learn4.view.drone.DroneActivity;
import com.learn4.util.Application;
import com.learn4.R;
import com.learn4.view.contents.ContentsActivity;
import com.learn4.view.MainActivity;
import com.learn4.view.dictionary.BlockDictionaryActivity2;
import com.learn4.view.drone.DroneBlockActivity;
import com.learn4.view.drone.DroneTestActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    LinearLayout free_mode,content_mode;
    Application myApplication;
    Button free_btn,contents_btn,dictionary_btn,drone_btn,drone_block_btn;
    TextView user_name;

    Button bluetooth_btn, bluetooth_btn2;

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
        drone_block_btn = root.findViewById(R.id.drone_block_btn);
//        user_name = root.findViewById(R.id.user_name);

        bluetooth_btn = root.findViewById(R.id.bluetooth_btn);
        bluetooth_btn2 = root.findViewById(R.id.bluetooth_btn2);

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

//        if (MySharedPreferences.getBoolean(getActivity(),"coupon_register_check")){
//            changeBtnLock(true);
//        }else{
//            changeBtnLock(false);
//        }



        free_btn.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("contents_name","none");
            intent.putExtra("id","0");
            myApplication.showLoadingScreen(getContext());
            startActivity(intent);
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

        drone_block_btn.setOnClickListener(v->{
            Log.e("???? why ","dont go droneblockactivity");

                Intent intent = new Intent(getActivity(), DroneBlockActivity.class);
                startActivity(intent);


        });

        bluetooth_btn.setOnClickListener(v -> {

                Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                startActivity(intent);

        });

        bluetooth_btn2.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), BluetoothActivity2.class);
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

        // 2025 디지털 새싹 설문지 팝업
        Dialog surveyDialog = new Dialog(getContext());
        surveyDialog.setContentView(R.layout.dialog_survey_select);
        surveyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        surveyDialog.setCanceledOnTouchOutside(false);
        surveyDialog.show();
        
        // 레이아웃 요소 참조
        GridLayout categoryGrid = surveyDialog.findViewById(R.id.categoryGrid);
        LinearLayout surveyListGroup = surveyDialog.findViewById(R.id.surveyListGroup);
        GridLayout surveyGrid = surveyDialog.findViewById(R.id.surveyGrid);
        TextView btnBack = surveyDialog.findViewById(R.id.btn_back);
        Button btnClose = surveyDialog.findViewById(R.id.close_btn);
        TextView surveyCategoryTitle = surveyDialog.findViewById(R.id.surveyCategoryTitle);
        
        // 설문 링크 정의
        Map<String, String[]> surveyLinks = new HashMap<>();

        // 일반 학생 대상 설문 링크
        surveyLinks.put("general", new String[]{
                "https://walla.my/survey/rfqN5esCwrkC75lpBEV5",
                "https://walla.my/survey/lZ1yOf5siUZKhck7ZaDD",
                "https://walla.my/survey/zrYr9JXuWAN0DfW6AtTp",
                "https://walla.my/survey/bcJmGCNHTWDymeWBvKsg"
        });

        // 사회적 배려자(다문화) 학생 대상 설문 링크
        surveyLinks.put("multicultural", new String[]{
                "https://walla.my/survey/aGexTTdfdmjxTFqvisyH",
                "https://walla.my/survey/VdH0wmNX7gZt4iZbhr2g",
                "https://walla.my/survey/dkdM8525mAQsrb5wqFat",
                "https://walla.my/survey/iSWyFPAH2ZLcCfclHf7u"
        });

        // 사회적 배려자(도서벽지) 학생 대상 설문 링크
        surveyLinks.put("remote", new String[]{
                "https://walla.my/survey/nBSl8e76FvDAcyawS04d",
                "https://walla.my/survey/4vGxKWJiodbo0Ss4bZyN",
                "https://walla.my/survey/5joOslh4II4ackOgW7Wq",
                "https://walla.my/survey/iVhuqrpwokd7RKq1eehq"
        });

        // 사회적 배려자(특수교육) 학생 대상 설문 링크
        surveyLinks.put("special", new String[]{
                "https://walla.my/survey/pXI8aA93KI2wsdafjsQe",
                "https://walla.my/survey/uml7dlLNLkmgdBFKocF4",
                "https://walla.my/survey/Z1WfkvnmoBjwo0LckIEg",
                "https://walla.my/survey/eM7ldHU9rzfTO9aoNqeF"
        });
        
        // 공통 라벨
        String[] labels = {
                "초등학생\n사전 설문", "초등학생\n사후 설문",
                "중 · 고등학생\n사전 설문", "중 · 고등학생\n사후 설문"
        };
        
        // 설문 버튼을 동적으로 그리드에 추가
        View.OnClickListener categoryClickListener = v -> {
            String categoryKey = null;
            String categoryLabel = "";  // 사용자에게 보여줄 이름

            if (v.getId() == R.id.btn_general) {
                categoryKey = "general";
                categoryLabel = "일반 학생 대상 설문";
            } else if (v.getId() == R.id.btn_multicultural) {
                categoryKey = "multicultural";
                categoryLabel = "사회적 배려자(다문화) 학생 대상 설문";
            } else if (v.getId() == R.id.btn_remote) {
                categoryKey = "remote";
                categoryLabel = "사회적 배려자(도서벽지) 학생 대상 설문";
            } else if (v.getId() == R.id.btn_special) {
                categoryKey = "special";
                categoryLabel = "사회적 배려자(특수교육) 학생 대상 설문";
            }

            // 텍스트 뷰 업데이트
            surveyCategoryTitle.setText(categoryLabel);

            // 화면 전환
            categoryGrid.setVisibility(View.GONE);
            surveyListGroup.setVisibility(View.VISIBLE);
            surveyGrid.removeAllViews();

            String[] links = surveyLinks.get(categoryKey);
            if (links == null) return;

            for (int i = 0; i < links.length; i++) {
                Button btn = new Button(getContext());
                btn.setText(labels[i]);
                btn.setTextSize(18f);
                btn.setTextColor(Color.parseColor("#af6400"));
                btn.setBackgroundResource(R.drawable.tutor_dialog_title_custom);
                btn.setPadding(16, 16, 16, 16);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(i % 2, 1f);  // 2열 균등 분배
                params.setMargins(8, 8, 8, 8);
                btn.setLayoutParams(params);

                String url = links[i];
                btn.setOnClickListener(linkView -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    getContext().startActivity(intent);
                });

                surveyGrid.addView(btn);
            }
        };
        
        // 카테고리 선택 버튼들 리스너 연결
        surveyDialog.findViewById(R.id.btn_general).setOnClickListener(categoryClickListener);
        surveyDialog.findViewById(R.id.btn_multicultural).setOnClickListener(categoryClickListener);
        surveyDialog.findViewById(R.id.btn_remote).setOnClickListener(categoryClickListener);
        surveyDialog.findViewById(R.id.btn_special).setOnClickListener(categoryClickListener);
        
        // 뒤로가기 버튼 동작
        btnBack.setOnClickListener(v -> {
            surveyListGroup.setVisibility(View.GONE);
            categoryGrid.setVisibility(View.VISIBLE);
        });
        
        // 닫기 버튼 동작
        btnClose.setOnClickListener(v -> surveyDialog.dismiss());

        
        // 설문지 팝업(이전 디싹)
            Dialog dialog01;
            dialog01 = new Dialog(getContext());
            dialog01.setCanceledOnTouchOutside(false);
            dialog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog01.setContentView(R.layout.dialog_survey);

            // 이 부분 주석해서 다이얼로그를 안보이게 함
//            dialog01.show();

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

    public void changeBtnLock(boolean check){
        if (check){
            contents_btn.setBackgroundResource(R.drawable.content_mode_icon);
            dictionary_btn.setBackgroundResource(R.drawable.dictionary_mode_icon);
            drone_btn.setBackgroundResource(R.drawable.drone_mode_icon);
            drone_block_btn.setBackgroundResource(R.drawable.drone_coding_mode_icon);
            bluetooth_btn.setBackgroundResource(R.drawable.bluetooth_controller_btn);
            bluetooth_btn2.setBackgroundResource(R.drawable.bluetooth_controller2_btn);
        }else{
            contents_btn.setBackgroundResource(R.drawable.content_mode_icon_lock);
            dictionary_btn.setBackgroundResource(R.drawable.dictionary_mode_icon_lock);
            drone_btn.setBackgroundResource(R.drawable.drone_mode_icon_lock);
            drone_block_btn.setBackgroundResource(R.drawable.drone_coding_mode_icon_lock);
            bluetooth_btn.setBackgroundResource(R.drawable.bluetooth_controller_btn_lock);
            bluetooth_btn2.setBackgroundResource(R.drawable.bluetooth_controller2_btn_lock);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("hello ~", "zz");
    }

    @Override
    public void onStop() {
        super.onStop();
        myApplication.hideLoadingScreen();
    }
}