package com.learn4.view.contents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.blockly.android.BlockClickDialog;
import com.learn4.R;
import com.learn4.util.Application;
import com.learn4.util.DataSetting;
import com.learn4.data.dto.Chapter;
import com.learn4.data.dto.Level;
import com.learn4.view.custom.dialog.ModeSelectDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentsActivity extends AppCompatActivity {

    private RecyclerView rvSubject;
    private LevelAdapter levelAdapter;
    private ArrayList<Level> subjects;
    List<Chapter> chapters;
    private View decorView;
    private int	uiOption;

    Button back_btn, contents_mode_select_btn;

    MediaPlayer mediaPlayer;

    DataSetting setting;

    ModeSelectDialog modeSelectDialog;

    private View.OnClickListener cancel_listener;
    private View.OnClickListener confirm_listener;
    private View.OnClickListener default_mode_listener;
    private View.OnClickListener jikco_mode_listener;

    int mode = 1;
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

        Application.mode = 1;

        subjects = prepareData();

        rvSubject = findViewById(R.id.level_list);
        back_btn = findViewById(R.id.back_btn);
        contents_mode_select_btn= findViewById(R.id.contents_mode_select_btn);

        cancel_listener = new View.OnClickListener() {
            public void onClick(View v) {
                modeSelectDialog.dismiss();
            }
        };

        confirm_listener = new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), mode+"", Toast.LENGTH_SHORT).show();
//                levelAdapter.levels.clear();
                levelAdapter.deco_check = false;
                if (mode == 1){
                    levelAdapter.levels = prepareData();
                    levelAdapter.notifyDataSetChanged();
                    contents_mode_select_btn.setText("기본모드");
                }else{
                    levelAdapter.levels = prepareData(2);
                    levelAdapter.notifyDataSetChanged();
                    contents_mode_select_btn.setText("직코모드");
                }

                modeSelectDialog.dismiss();
            }
        };

        default_mode_listener = new View.OnClickListener() {
            public void onClick(View v) {
                modeSelectDialog.jikco_mode.setChecked(false);
                mode = 1;
                Application.mode = 1;
            }
        };

        jikco_mode_listener = new View.OnClickListener() {
            public void onClick(View v) {
                modeSelectDialog.default_mode.setChecked(false);
                mode = 2;
                Application.mode = 2;
            }
        };

        contents_mode_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelectDialog = new ModeSelectDialog(ContentsActivity.this,cancel_listener,confirm_listener,default_mode_listener,jikco_mode_listener,mode);
                modeSelectDialog.show();
            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int devicewidth = (int) (displaymetrics.widthPixels / 4.9);
        levelAdapter = new LevelAdapter(ContentsActivity.this, subjects, devicewidth);
        LinearLayoutManager manager = new LinearLayoutManager(ContentsActivity.this);
//        LinearLayoutManager manager = new LinearLayoutManager(ContentsActivity.this);
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
            Log.e("check 2 ",setting.chapter_list.get("1").get(i).id+"");
            level.chapters.add(setting.chapter_list.get("1").get(i));
        }


        for (int i =0; i<setting.chapter_list.get("2").size(); i++){
            Log.e("check 2 ",setting.chapter_list.get("2").get(i).id+"");
            level2.chapters.add(setting.chapter_list.get("2").get(i));
        }

        for (int i =0; i<setting.chapter_list.get("3").size(); i++){
            Log.e("check 3 ",setting.chapter_list.get("3").get(i).id+"");
            level3.chapters.add(setting.chapter_list.get("3").get(i));
        }



        /*Chapter chapter7 = new Chapter();
        chapter7.id = 7;
        chapter7.chapterName = "none";
        chapter7.image = Color.parseColor("#ffdca2");



        level.chapters.add(chapter7);*/

        subjects.add(level);

        //두번째 subject 추가


        subjects.add(level2);

        //세번째 subject 추가


//        level3.chapters.add(chapter6);
//        level3.chapters.add(chapter7);

//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter7);
//        level3.chapters.add(chapter5);


        subjects.add(level3);


        return subjects;
    }


    private ArrayList<Level> prepareData(int number) {
        String [] con = setting.project_contents_list.get(number-2).contents_list.split(",");
        for (int i=0; i < con.length; i++){
            Log.e("con",con[i]);
        }
        Log.e("check len",con.length+"");
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
            if (Arrays.asList(con).contains(setting.chapter_list.get("1").get(i).id+""))
                level.chapters.add(setting.chapter_list.get("1").get(i));
        }


        for (int i =0; i<setting.chapter_list.get("2").size(); i++){
            Log.e("check 2 ",setting.chapter_list.get("2").get(i).id+"");
            if (Arrays.asList(con).contains(setting.chapter_list.get("2").get(i).id+""))
                level2.chapters.add(setting.chapter_list.get("2").get(i));
        }

        for (int i =0; i<setting.chapter_list.get("3").size(); i++){
            Log.e("check 3 ",setting.chapter_list.get("3").get(i).id+"");
            if (Arrays.asList(con).contains(setting.chapter_list.get("3").get(i).id+""))
                level3.chapters.add(setting.chapter_list.get("3").get(i));
        }


        subjects.add(level);

        subjects.add(level2);


        subjects.add(level3);


        return subjects;
    }




}
