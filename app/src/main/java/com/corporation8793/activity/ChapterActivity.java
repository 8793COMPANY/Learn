package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corporation8793.MySharedPreferences;
import com.corporation8793.custom.CustomView;
import com.corporation8793.R;

public class ChapterActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;
    CustomView chapter1, chapter2, chapter3;
    Button back_btn, block_bot_btn;

    MediaPlayer mediaPlayer;

    @Override
    protected void onDestroy() {
        // 웰컴 메시지 재생 종료
        block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_normal));
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // 웰컴 메시지 재생 종료
        block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_normal));
        mediaPlayer.release();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        chapter1 = findViewById(R.id.chapter1);
        chapter2 = findViewById(R.id.chapter2);
        chapter3 = findViewById(R.id.chapter3);

        back_btn = findViewById(R.id.back_btn);
        block_bot_btn = findViewById(R.id.block_bot_btn);

        // 웰컴 메시지 재생
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_test_lv1_led_into_contents_1_select);
        mediaPlayer.start();
        block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_speech));

        chapter2.setEnabled(false);
        chapter3.setEnabled(false);


        chapter1.setGroup(1);
        chapter2.setGroup(2);
        chapter3.setGroup(3);

        initContent(chapter1,"LED 깜박이기",true, R.drawable.chapter_content1, MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX"));
        initContent(chapter2,"LED 1초간 껐다 켜기",false,R.drawable.chapter_content2,MySharedPreferences.getInt(getApplicationContext(), "LED 1초간 껐다 켜기 MAX"));
        initContent(chapter3,"LED 3개 깜박이기",false,R.drawable.chapter_content3,MySharedPreferences.getInt(getApplicationContext(), "LED 3개 깜박이기 MAX"));
        if (MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX") == 5){
            chapter2.setOpen(true,false);
            chapter2.setEnabled(true);
        }

        if(MySharedPreferences.getInt(getApplicationContext(),"LED 1초간 껐다 켜기 MAX")==5){
            chapter3.setOpen(true,false);
            chapter3.setEnabled(true);
        }



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

        block_bot_btn.setOnClickListener(v -> {
            // 봇 메시지 초기화
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            // 웰컴 메시지 재생
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_test_lv1_led_into_contents_1_select);
            mediaPlayer.start();
            block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_speech));

            // 웰컴 메시지 재생 완료
            mediaPlayer.setOnCompletionListener(mp -> {
                block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_normal));
                mp.release();
            });
        });

        // 웰컴 메시지 재생 완료
        mediaPlayer.setOnCompletionListener(mp -> {
            block_bot_btn.setBackground(getResources().getDrawable(R.drawable.bot_test_2_normal));
            mp.release();
        });
    }

    public void initContent(CustomView view, String title, boolean open, int chapterImg, int step){
        view.setTitle(title);
        view.setOpen(open,false);
        view.setBackground(chapterImg);
        view.setPercentage(step);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX") == 5){
            chapter2.setOpen(true,false);
        }
        if(MySharedPreferences.getInt(getApplicationContext(),"LED 1초간 껐다 켜기 MAX")==5){
            chapter3.setOpen(true,false);
        }
        chapter1.setPercentage(MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX"));
        chapter2.setPercentage(MySharedPreferences.getInt(getApplicationContext(), "LED 1초간 껐다 켜기 MAX"));
        chapter3.setPercentage(MySharedPreferences.getInt(getApplicationContext(), "LED 3개 깜박이기 MAX"));
    }
}