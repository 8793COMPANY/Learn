package com.corporation8793.activity;

import androidx.appcompat.app.AppCompatActivity;

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
    Button back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        String chapter_id = getIntent().getStringExtra("id");

        chapter1 = findViewById(R.id.chapter1);
        chapter2 = findViewById(R.id.chapter2);
        chapter3 = findViewById(R.id.chapter3);

        back_btn = findViewById(R.id.back_btn);

        chapter2.setEnabled(false);
        chapter3.setEnabled(false);


        chapter1.setGroup(1);
        chapter2.setGroup(2);
        chapter3.setGroup(3);

        initContent(chapter1,"LED 깜박이기",true, R.drawable.chapter_content1, MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX"),chapter_id+"_"+"1");
        initContent(chapter2,"LED 1초간 껐다 켜기",false,R.drawable.chapter_content2,MySharedPreferences.getInt(getApplicationContext(), "LED 1초간 껐다 켜기 MAX"),chapter_id+"_"+"2");
        initContent(chapter3,"LED 3개 깜박이기",false,R.drawable.chapter_content3,MySharedPreferences.getInt(getApplicationContext(), "LED 3개 깜박이기 MAX"),chapter_id+"_"+"3");
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
    }

    public void initContent(CustomView view, String title, boolean open, int chapterImg, int step, String id){
        view.setTitle(title);
        view.setOpen(open,false);
        view.setBackground(chapterImg);
        view.setPercentage(step);
        view.setID(id);
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