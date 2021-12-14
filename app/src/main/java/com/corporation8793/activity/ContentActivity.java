package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.corporation8793.R;
import com.corporation8793.data.Chapter;
import com.corporation8793.data.Level;
import com.corporation8793.recyclerview.LevelAdapter;
import com.corporation8793.recyclerview.RecyclerDecoration;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {

    private RecyclerView rvSubject;
    private LevelAdapter levelAdapter;
    private ArrayList<Level> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        subjects = prepareData();

        rvSubject = findViewById(R.id.level_list);


        levelAdapter = new LevelAdapter(ContentActivity.this, subjects);
        LinearLayoutManager manager = new LinearLayoutManager(ContentActivity.this);
        rvSubject.setLayoutManager(manager);
        rvSubject.setAdapter(levelAdapter);
    }


    private ArrayList<Level> prepareData() {
        ArrayList<Level> subjects = new ArrayList<Level>();

        //첫번째 subject 추가
        Level level = new Level();
        level.id = 1;
        level.level = "level 1";
        level.chapters = new ArrayList<Chapter>();

        Chapter chapter1 = new Chapter();
        chapter1.id = 1;
        chapter1.chapterName = "LED 깜박이기";
        chapter1.imageUrl = "http://ashishkudale.com/images/phy/atoms.png";

        Chapter chapter2 = new Chapter();
        chapter2.id = 2;
        chapter2.chapterName = "조도센서 사용하기";
        chapter2.imageUrl = "http://ashishkudale.com/images/phy/sigma.png";

        Chapter chapter3 = new Chapter();
        chapter3.id = 3;
        chapter3.chapterName = "부저 사용하기";
        chapter3.imageUrl = "http://ashishkudale.com/images/phy/sigma.png";


        level.chapters.add(chapter1);
        level.chapters.add(chapter2);
        level.chapters.add(chapter3);

        subjects.add(level);

        //두번째 subject 추가
        Level level2 = new Level();
        level2.id = 2;
        level2.level = "Level 2";
        level2.chapters = new ArrayList<Chapter>();

        level2.chapters.add(chapter1);
        level2.chapters.add(chapter2);
        level2.chapters.add(chapter3);


        subjects.add(level2);

        //세번째 subject 추가
        Level level3 = new Level();
        level3.id = 2;
        level3.level = "Level 3";
        level3.chapters = new ArrayList<Chapter>();

        level3.chapters.add(chapter1);
        level3.chapters.add(chapter2);
        level3.chapters.add(chapter3);


        subjects.add(level3);


        return subjects;
    }
}
