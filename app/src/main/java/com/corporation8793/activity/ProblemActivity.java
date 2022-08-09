package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.corporation8793.MySharedPreferences;
import com.corporation8793.R;
import com.corporation8793.fragment.Step1;
import com.corporation8793.fragment.Step2;
import com.corporation8793.fragment.Step3;
import com.corporation8793.fragment.Step4;

public class ProblemActivity extends AppCompatActivity {

    private View decorView;
    private int	uiOption;

    Button back_btn, next_btn;

    TextView title, title2;

    String [] titles = {"준비물","전체 회로도","회로도 구성","회로도 구성"};
    String [] contents = {"LED 깜박이기","LED 1초간 껐다 켜기","LED 3개 깜박이기"};
    int pos = 0;

    ConstraintLayout background;
    LinearLayout title_background;
    String chapter_step = "default";
    int diagram_img = R.drawable.all_diagram_img;
    String contents_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        background = findViewById(R.id.problem_background);
        title_background = findViewById(R.id.problem_title_background);

        title = findViewById(R.id.title);
        title2 = findViewById(R.id.title2);

        back_btn = findViewById(R.id.back_btn);
        next_btn = findViewById(R.id.next_btn);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        int step = getIntent().getIntExtra("step",1);
        String id = getIntent().getStringExtra("id");

        Log.e("id",id);
        contents_name = contents[step-1];

        Log.e("check",step +"");
        if (step != 1){
            chapter_step = "deep";
            background.setBackgroundColor(Color.WHITE);
            title2.setVisibility(View.VISIBLE);
            title_background.setVisibility(View.INVISIBLE);
        }

        if (step == 3){
            diagram_img = R.drawable.all_diagram_img2;
        }

        replaceFragment(0);


        back_btn.setOnClickListener(v->{
            next_btn.setVisibility(View.VISIBLE);
            pos--;
            replaceFragment(pos);
            Log.e("pos",pos+"");

            if (pos ==0){
                if (chapter_step.equals("deep")){
                    background.setBackgroundColor(Color.WHITE);
                    title2.setVisibility(View.VISIBLE);
                    title_background.setVisibility(View.INVISIBLE);
                }else{
                    title.setText(titles[pos]);
                }
                back_btn.setVisibility(View.INVISIBLE);
            }else{
                title_background.setVisibility(View.VISIBLE);
                title.setText(titles[pos]);
                title2.setVisibility(View.INVISIBLE);
            }

        });

        next_btn.setOnClickListener(v->{

            if (MySharedPreferences.getInt(getApplicationContext(),contents_name) < 3) {
                if (pos < 3) {
                    Log.e("hello",MySharedPreferences.getInt(getApplicationContext(),contents_name)+"");
                        pos++;
                    if (MySharedPreferences.getInt(getApplicationContext(), contents_name) == 0 && chapter_step.equals("deep")) {
                        pos = 0;
                    }

                    if (pos <3)
                            title.setText(titles[pos]);


                }
                if (MySharedPreferences.getInt(getApplicationContext(),contents_name) == pos) {
                    Log.e("chapter_step",chapter_step);
                    if (MySharedPreferences.getInt(getApplicationContext(), contents_name) == 0 && chapter_step.equals("deep")) {
                    } else {
                        back_btn.setVisibility(View.VISIBLE);
                        replaceFragment(pos);

                    }
                }

            }

            if(MySharedPreferences.getInt(getApplicationContext(),contents_name) ==3){
                Intent intent = new Intent(ProblemActivity.this, MainActivity.class);
                intent.putExtra("contents_name",contents_name);
                intent.putExtra("id",id);
                Log.e("contents_name",contents_name);
                Log.e("id",id);
                startActivity(intent);
                finish();
            } else{
                if (chapter_step.equals("deep") && pos ==1){
                    title2.setVisibility(View.INVISIBLE);
                    title_background.setVisibility(View.VISIBLE);
                    background.setBackgroundColor(Color.parseColor("#f7f7f7"));
                }else if (MySharedPreferences.getInt(getApplicationContext(),contents_name) ==2 && pos ==3){

                        Toast.makeText(getApplicationContext(),"사진을 업로드해주세요",Toast.LENGTH_SHORT).show();
                }
            }


            Log.e("pos",pos+"");

        });

    }


    public void replaceFragment(int pos){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (pos == 0){
            if (chapter_step.equals("default")){
                transaction.replace(R.id.fragment, new Step1(contents_name));
            }else{
                transaction.replace(R.id.fragment, new Step4(contents_name));
            }

        }else if (pos ==1){
            transaction.replace(R.id.fragment, new Step2(contents_name,diagram_img));
        }else if (pos ==2){
            transaction.replace(R.id.fragment, new Step3(contents_name));
        }

        transaction.commit();

    }

}