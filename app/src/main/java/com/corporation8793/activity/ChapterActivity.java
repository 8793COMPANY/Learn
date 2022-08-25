package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.corporation8793.MySharedPreferences;
import com.corporation8793.custom.CustomView;
import com.corporation8793.R;
import com.corporation8793.dto.Contents;

import java.sql.Array;
import java.util.ArrayList;

public class ChapterActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;
    CustomView chapter1, chapter2, chapter3;
    Button back_btn;
    RecyclerView chapter_list;
    ChapterAdapter chapterAdapter;
    ArrayList<Contents> contents_list = new ArrayList<>();
    Boolean lock_check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        String chapter_id = getIntent().getStringExtra("id");

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int width = (int)(display.widthPixels / 4);

        //id : chapter_id+"_1",
        contents_list.add(new Contents(
                "3-2",
                R.drawable.default_problem_image,
                "LED 깜빡이기",
                R.drawable.chapter_content1,
                MySharedPreferences.getInt(getApplicationContext(),"LED 깜빡이기 MAX"),true));

        if(MySharedPreferences.getInt(getApplicationContext(),"LED 깜빡이기 MAX") == 5)
            lock_check = true;

        contents_list.add(new Contents(
                "3-3",
                R.drawable.advanced_problem_image,
                "LED 깜빡이는 시간 바꾸기",
                R.drawable.chapter_content2,
                MySharedPreferences.getInt(getApplicationContext(),"LED 깜빡이는 시간 바꾸기 MAX"),lock_check));


        contents_list.add(new Contents(
                "3-4",
                R.drawable.advanced_problem_image2,
                "LED 핀 번호 바꾸기",
                R.drawable.chapter_content3,
                MySharedPreferences.getInt(getApplicationContext(),"LED 핀 번호 바꾸기 MAX"),lock_check));

        contents_list.add(new Contents(
                "3-5",
                R.drawable.advanced_problem_image,
                "문제풀이",
                R.drawable.chapter_content1,
                MySharedPreferences.getInt(getApplicationContext(),"문제풀이3"),lock_check));
        // 문제풀이 +chapter_id

//        chapter1 = findViewById(R.id.chapter1);
//        chapter2 = findViewById(R.id.chapter2);
//        chapter3 = findViewById(R.id.chapter3);

        chapter_list = findViewById(R.id.chapter_list);
        chapterAdapter = new ChapterAdapter(this,contents_list,width,"3");

        chapter_list.setAdapter(chapterAdapter);
        chapter_list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        RecyclerDecoration_Height decoration_height = new RecyclerDecoration_Height(100);
        chapter_list.addItemDecoration(decoration_height);

        back_btn = findViewById(R.id.back_btn);

//        chapter2.setEnabled(false);
//        chapter3.setEnabled(false);
//
//
//        chapter1.setGroup(1);
//        chapter2.setGroup(2);
//        chapter3.setGroup(3);
//
//        initContent(chapter1,"LED 깜박이기",true, R.drawable.chapter_content1, MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX"),chapter_id+"_"+"1");
//        initContent(chapter2,"LED 1초간 껐다 켜기",false,R.drawable.chapter_content2,MySharedPreferences.getInt(getApplicationContext(), "LED 1초간 껐다 켜기 MAX"),chapter_id+"_"+"2");
//        initContent(chapter3,"LED 3개 깜박이기",false,R.drawable.chapter_content3,MySharedPreferences.getInt(getApplicationContext(), "LED 3개 깜박이기 MAX"),chapter_id+"_"+"3");
//        if (MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX") == 5){
//            chapter2.setOpen(true,false);
//            chapter2.setEnabled(true);
//        }
//
//        if(MySharedPreferences.getInt(getApplicationContext(),"LED 1초간 껐다 켜기 MAX")==5){
//            chapter3.setOpen(true,false);
//            chapter3.setEnabled(true);
//        }



        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

//        chapter1.setOnClickListener(v->{
//            Intent intent = new Intent(ChapterActivity.this, ProblemActivity.class);
//            intent.putExtra("step",1);
//            startActivity(intent);
//        });

//        chapter2.setOnClickListener(v->{
//            Intent intent = new Intent(ChapterActivity.this, ProblemActivity.class);
//            intent.putExtra("step",2);
//            startActivity(intent);
//        });
//
//        chapter3.setOnClickListener(v->{
//            Intent intent = new Intent(ChapterActivity.this, ProblemActivity.class);
//            intent.putExtra("step",3);
//            startActivity(intent);
//        });

        back_btn.setOnClickListener(v->{
            finish();
        });
    }

    public void initContent(CustomView view, String title, boolean open, int chapterImg, int step, String id){
        view.setTitle(title);
        view.setOpen(open,false);
        view.setBackground(chapterImg);
        view.setPercentage(step);
        view.setID(id);
    }

    public void addData(int id,String group, String title, int img, int percentage){

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        chapterAdapter.notifyItemChanged(chapterAdapter.current_chapter,"update");
        if (MySharedPreferences.getInt(getApplicationContext(),"LED 깜빡이기 MAX") == 5){
            //여기 코드 작성해주세요 ~
            chapterAdapter.notifyItemRangeChanged(1,contents_list.size()-1,"lock_check");
        }
//        if(MySharedPreferences.getInt(getApplicationContext(),"LED 1초간 껐다 켜기 MAX")==5){
//            chapter3.setOpen(true,false);
//        }
//        chapter1.setPercentage(MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX"));
//        chapter2.setPercentage(MySharedPreferences.getInt(getApplicationContext(), "LED 1초간 껐다 켜기 MAX"));
//        chapter3.setPercentage(MySharedPreferences.getInt(getApplicationContext(), "LED 3개 깜박이기 MAX"));
    }
}