package com.learn4.view.contents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.learn4.R;
import com.learn4.util.DataSetting;
import com.learn4.data.dto.Chapter;
import com.learn4.data.dto.Level;

import java.util.ArrayList;
import java.util.List;

public class ContentsActivity extends AppCompatActivity {

    private RecyclerView rvSubject;
    private LevelAdapter levelAdapter;
    private ArrayList<Level> subjects;
    List<Chapter> chapters;
    private View decorView;
    private int	uiOption;

    Button back_btn;

    MediaPlayer mediaPlayer;

    DataSetting setting;


    @Override
    protected void onDestroy() {
        // 웰컴 메시지 재생 종료
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // 웰컴 메시지 재생 종료
        mediaPlayer.release();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        setting = DataSetting.getInstance(getApplicationContext());
        setting.printChapter();

//        setting.printChapter();



        // 웰컴 메시지 재생
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.into_contents_mode_main);
        mediaPlayer.start();

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

        back_btn.setOnClickListener(v-> finish());

        // 웰컴 메시지 재생 완료
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);


    }


    private ArrayList<Level> prepareData() {
        ArrayList<Level> subjects = new ArrayList<Level>();

        //첫번째 subject 추가
        Level level = new Level();
        level.id = 1;
        level.level = "Level 1";
        level.chapters = new ArrayList<Chapter>();

        Level level2 = new Level();
        level2.id = 2;
        level2.level = "Level 2";
        level2.chapters = new ArrayList<Chapter>();

        Level level3 = new Level();
        level3.id = 2;
        level3.level = "Level 3";
        level3.chapters = new ArrayList<Chapter>();

        for (int i =0; i<setting.chapter_list.get("1").size(); i++){
            Log.e("check",setting.chapter_list.get("1").get(i).id+"");
            level.chapters.add(setting.chapter_list.get("1").get(i));
        }


        for (int i =0; i<setting.chapter_list.get("2").size(); i++){
            Log.e("check",setting.chapter_list.get("2").get(i).id+"");
            level2.chapters.add(setting.chapter_list.get("2").get(i));
        }

        for (int i =0; i<setting.chapter_list.get("3").size(); i++){
            Log.e("check",setting.chapter_list.get("3").get(i).id+"");
            level3.chapters.add(setting.chapter_list.get("3").get(i));
        }



        Chapter chapter7 = new Chapter();
        chapter7.id = 7;
        chapter7.chapterName = "none";
        chapter7.image = Color.parseColor("#ffdca2");



        level.chapters.add(chapter7);

        subjects.add(level);

        //두번째 subject 추가


        subjects.add(level2);

        //세번째 subject 추가


//        level3.chapters.add(chapter6);
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
