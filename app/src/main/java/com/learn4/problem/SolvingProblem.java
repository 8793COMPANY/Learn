package com.learn4.problem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.learn4.Application;
import com.learn4.MySharedPreferences;
import com.learn4.R;
import com.learn.wp_rest.data.acf.QuizReportJson;

import kotlin.Pair;

public class SolvingProblem extends AppCompatActivity {

    private View decorView;
    private int	uiOption;

    Button back_btn, next_btn;

    TextView title, title2, problem_progress_text;
    ProgressBar problem_progress;

    String [] titles = {"준비물","전체 회로도","회로도 구성","회로도 구성"};
    String [] contents = {"LED 깜빡이기","LED 깜빡이는 시간 바꾸기","LED 핀 번호 바꾸기","문제풀이",};
    int pos = 1;

    ConstraintLayout background;
//    LinearLayout title_background;
    String chapter_step = "default";
    int diagram_img = R.drawable.all_diagram_img;
    String contents_name = "",chapter_id = "";
    int [] answers = {0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solving_problem);

        chapter_id = getIntent().getStringExtra("chapter_id");

        background = findViewById(R.id.problem_background);
        problem_progress_text = findViewById(R.id.problem_progress_text);
        problem_progress = findViewById(R.id.problem_progress);
//        title_background = findViewById(R.id.problem_title_background);

//        title = findViewById(R.id.title);
//        title2 = findViewById(R.id.title2);

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
//        String id = getIntent().getStringExtra("id");
//
//        Log.e("id",id);
        contents_name = contents[step-1];

        Log.e("check",step +"");
        if (step != 1){
            chapter_step = "deep";
            background.setBackgroundColor(Color.WHITE);
            title2.setVisibility(View.VISIBLE);
//            title_background.setVisibility(View.INVISIBLE);
        }

        if (step == 3){
            diagram_img = R.drawable.all_diagram_img2;
        }

//        replaceFragment(0);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment, new VerticalAnswer(contents_name));
//        transaction.commit();
        replaceFragment(1);
        back_btn.setOnClickListener(v->{
            next_btn.setVisibility(View.VISIBLE);
            next_btn.setEnabled(true);
            if (pos != 1)
                pos--;
            replaceFragment(pos);
//            replaceFragment(pos);
            Log.e("pos",pos+"");

            if (pos ==1){
                problem_progress_text.setText(pos+"/5");
                problem_progress.setProgress(pos*20);
                back_btn.setEnabled(false);
            }else{
//                title_background.setVisibility(View.VISIBLE);
//                title.setText(titles[pos]);
                problem_progress_text.setText(pos+"/5");
                problem_progress.setProgress(pos*20);
//                title2.setVisibility(View.INVISIBLE);
            }

        });

        next_btn.setOnClickListener(v->{
            MySharedPreferences.setInt(getApplicationContext(),"문제풀이3",pos);
            if (pos == 5){
                String results = answers[0]+" "+answers[1]+" "+answers[2]+" "+answers[3]+" "+answers[4];
                if (MySharedPreferences.getBoolean(this,"solving_problem"+chapter_id)){
                    Log.e("data","in");
                }else{
                    Log.e("data","none");
                    try {
                        new Thread(()->{
                            String post_id = Application.postsRepository.createQuizReport(
                                    chapter_id+"-5. "+"LED 문제",
                                    Application.user.getId(),
                                    answers[0],
                                    answers[1],
                                    answers[2],
                                    answers[3],
                                    answers[4]
                            ).getSecond().getId();
                            Pair<String, QuizReportJson> check = Application.acfRepository.updateQuizReportAcf(
                                    post_id,
                                    3,
                                    answers[0],
                                    answers[1],
                                    answers[2],
                                    answers[3],
                                    answers[4]
                            );
                            Log.e("end", "thread");
                            Log.e("upload_check", check.getFirst());
                            Log.e("upload_check", check.getSecond().getAcf().toString());
                            MySharedPreferences.setBoolean(this,"solving_problem"+chapter_id,true);
                        }).start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                Intent intent = new Intent(this, RightAnswerActivity.class);
                intent.putExtra("results",results);
                intent.putExtra("chapter_id",chapter_id);
                startActivity(intent);
                finish();
            }else{
            back_btn.setVisibility(View.VISIBLE);
            back_btn.setEnabled(true);
            pos++;
            replaceFragment(pos);
            if (pos != 6){
                problem_progress_text.setText(pos+"/5");
                problem_progress.setProgress(pos*20);
            }
//            if (pos ==5){
//
//
//            }else{
////                title_background.setVisibility(View.VISIBLE);
////                title.setText(titles[pos]);
//                problem_progress_text.setText(pos+"/5");
//                problem_progress.setProgress(pos*20);
////                title2.setVisibility(View.INVISIBLE);
//            }

        }
        });






    }

    public void addValue(int pos,int answer){
        answers[pos] = answer;
        if (!next_btn.isEnabled())
            next_btn.setEnabled(true);

        if (answers[pos] != 0 && next_btn.isEnabled())
            next_btn.setBackgroundResource(R.drawable.problem_next_btn);
    }

    public void printAnswer(){
        for (int i =0; i<answers.length; i++){
            Log.e(i+"",answers[i]+"");
        }
    }

    public void initBtn(){
        next_btn.setBackgroundResource(R.drawable.problem_next_btn_off);
        next_btn.setEnabled(false);
    }


    public void replaceFragment(int pos){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (pos == 1){
            transaction.replace(R.id.fragment, new HorizontalAnswer(1,"다음 중 LED센서는 무엇일까요?"));
        }else if (pos ==2){
            transaction.replace(R.id.fragment, new VerticalAnswer(2,"pinMode의 설명과 일치하는 것은 무엇일까요?"));
        }else if (pos ==3){
            transaction.replace(R.id.fragment, new HorizontalAnswer(3,"다음 중 스위치는 무엇일까요?"));
        }else if (pos ==4){
            transaction.replace(R.id.fragment, new VerticalAnswer(4,"Digitalread의 설명과 일치하는 것은 무엇일까요?"));
        }else{
            transaction.replace(R.id.fragment, new VerticalAnswer(5,"analogWrite의 설명과 일치하는 것은 무엇일까요?"));
        }


        transaction.commit();

    }

}