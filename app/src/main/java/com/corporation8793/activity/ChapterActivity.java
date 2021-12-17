package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corporation8793.CustomView;
import com.corporation8793.R;

public class ChapterActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;
    CustomView chapter1, chapter2, chapter3;
    Button back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        chapter1 = findViewById(R.id.chapter1);
        chapter2 = findViewById(R.id.chapter2);
        chapter3 = findViewById(R.id.chapter3);

        back_btn = findViewById(R.id.back_btn);

        chapter1.setGroup(R.drawable.default_problem_image);
        chapter2.setGroup(R.drawable.advanced_problem_image);
        chapter3.setGroup(R.drawable.advanced_problem_image2);

        initContent(chapter1,"LED 깜박이기",true, R.drawable.chapter_content1,1);
        initContent(chapter2,"LED 1초간 껐다 켜기",false,R.drawable.chapter_content2,0);
        initContent(chapter3,"LED 3개 깜박이기",false,R.drawable.chapter_content3,0);


        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        chapter1.setOnClickListener(v->{
            Intent intent = new Intent(ChapterActivity.this, ProblemActivity.class);
            startActivity(intent);
        });

        back_btn.setOnClickListener(v->{
            finish();
        });
    }

    public void initContent(CustomView view, String title, boolean open, int chapterImg, int step){
        view.setTitle(title);
        view.setOpen(open,false);
        view.setBackground(chapterImg);
        view.setPercentage(step);
    }
}