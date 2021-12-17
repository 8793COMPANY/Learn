package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corporation8793.R;
import com.corporation8793.data.Chapter;
import com.corporation8793.data.Level;
import com.corporation8793.recyclerview.LevelAdapter;

import java.util.ArrayList;

public class ContentsActivity extends AppCompatActivity {

    private RecyclerView rvSubject;
    private LevelAdapter levelAdapter;
    private ArrayList<Level> subjects;
    private View decorView;
    private int	uiOption;

    Button back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        subjects = prepareData();

        rvSubject = findViewById(R.id.level_list);
        back_btn = findViewById(R.id.back_btn);


        levelAdapter = new LevelAdapter(ContentsActivity.this, subjects);
        LinearLayoutManager manager = new LinearLayoutManager(ContentsActivity.this);
        rvSubject.setLayoutManager(manager);
        rvSubject.setAdapter(levelAdapter);

        back_btn.setOnClickListener(v->{
            finish();
        });
    }


    private ArrayList<Level> prepareData() {
        ArrayList<Level> subjects = new ArrayList<Level>();

        //첫번째 subject 추가
        Level level = new Level();
        level.id = 1;
        level.level = "Level 1";
        level.chapters = new ArrayList<Chapter>();

        Chapter chapter1 = new Chapter();
        chapter1.id = 1;
        chapter1.chapterName = "LED 깜박이기";
        chapter1.image = R.drawable.chapter1;

        Chapter chapter2 = new Chapter();
        chapter2.id = 2;
        chapter2.chapterName = "조도센서 사용하기";
        chapter2.image = R.drawable.chapter2;

        Chapter chapter3 = new Chapter();
        chapter3.id = 3;
        chapter3.chapterName = "부저 사용하기";
        chapter3.image = R.drawable.chapter3;

        Chapter chapter4 = new Chapter();
        chapter4.id = 4;
        chapter4.chapterName = "초음파 센서 사용하기";
        chapter4.image = R.drawable.chapter4;


        Chapter chapter5 = new Chapter();
        chapter5.id = 5;
        chapter5.chapterName = "스위치 사용하기";
        chapter5.image = R.drawable.chapter5;

        Chapter chapter6 = new Chapter();
        chapter6.id = 6;
        chapter6.chapterName = "3색 LED 깜박이기";
        chapter6.image = R.drawable.chapter6;

        Chapter chapter7 = new Chapter();
        chapter7.id = 7;
        chapter7.chapterName = "none";
        chapter7.image = Color.parseColor("#ffdca2");


        level.chapters.add(chapter1);
        level.chapters.add(chapter2);
        level.chapters.add(chapter3);
        level.chapters.add(chapter7);

        subjects.add(level);

        //두번째 subject 추가
        Level level2 = new Level();
        level2.id = 2;
        level2.level = "Level 2";
        level2.chapters = new ArrayList<Chapter>();

        level2.chapters.add(chapter4);
        level2.chapters.add(chapter5);
        level2.chapters.add(chapter7);
        level2.chapters.add(chapter7);
//        level2.chapters.add(chapter7);
//        level2.chapters.add(chapter7);
//        level2.chapters.add(chapter7);

//        level2.chapters.add(chapter5);


        subjects.add(level2);

        //세번째 subject 추가
        Level level3 = new Level();
        level3.id = 2;
        level3.level = "Level 3";
        level3.chapters = new ArrayList<Chapter>();

        level3.chapters.add(chapter6);
        level3.chapters.add(chapter7);
        level3.chapters.add(chapter7);
        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter5);


        subjects.add(level3);


        return subjects;
    }
}
