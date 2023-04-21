package com.learn4.view.chapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.learn4.data.room.AppDatabase;
import com.learn4.data.room.dao.ContentsDao;
import com.learn4.util.MySharedPreferences;
import com.learn4.view.custom.dialog.ContinueDialog;
import com.learn4.view.recyclerview.RecyclerDecoration_Height;
import com.learn4.view.custom.view.CustomView;
import com.learn4.R;
import com.learn4.data.dto.Subject;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;
    CustomView chapter1, chapter2, chapter3;
    Button back_btn;
    RecyclerView chapter_list;
    ChapterAdapter chapterAdapter;
    ArrayList<Subject> subject_list = new ArrayList<>();
    Boolean lock_check = false;

    AppDatabase db = null;
    public ContentsDao contentsDao;
    List<com.learn4.data.room.entity.Contents> contentsList = new ArrayList<>();

    String basic_problem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        String chapter_id = getIntent().getStringExtra("id");

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int width = (int)(display.widthPixels / 4);

        db = AppDatabase.getInstance(this);
        contentsDao = db.contentsDao();
        contentsList = contentsDao.findAll();

        // 여기에서 챕터별로 다르게 소챕터들이 나와야함
        uploadChapter(chapter_id);


        //id : chapter_id+"_1",
        //contents 번호는 3부터 시작 / chapter 번호는 2부터 시작
        /*subject_list.add(new Contents(
                "3-2",
                R.drawable.default_problem_image,
                "LED 깜빡이기",
                R.drawable.chapter_content1_1,
                MySharedPreferences.getInt(getApplicationContext(),"LED 깜빡이기 MAX"),true));

        if(MySharedPreferences.getInt(getApplicationContext(),"LED 깜빡이기 MAX") == 5)
            lock_check = true;

        subject_list.add(new Contents(
                "3-3",
                R.drawable.advanced_problem_image,
                "LED 깜빡이는 시간 바꾸기",
                R.drawable.chapter_content1_2,
                MySharedPreferences.getInt(getApplicationContext(),"LED 깜빡이는 시간 바꾸기 MAX"),lock_check));


        subject_list.add(new Contents(
                "3-4",
                R.drawable.advanced_problem_image2,
                "LED 핀 번호 바꾸기",
                R.drawable.chapter_content1_3,
                MySharedPreferences.getInt(getApplicationContext(),"LED 핀 번호 바꾸기 MAX"),lock_check));

        subject_list.add(new Contents(
                "3-5",
                R.drawable.advanced_problem_image3,
                "문제풀이",
                R.drawable.chapter_content1_1,
                MySharedPreferences.getInt(getApplicationContext(),"문제풀이3"),lock_check));*/
        // 문제풀이 +chapter_id

//        chapter1 = findViewById(R.id.chapter1);
//        chapter2 = findViewById(R.id.chapter2);
//        chapter3 = findViewById(R.id.chapter3);

        chapter_list = findViewById(R.id.chapter_list);
        chapterAdapter = new ChapterAdapter(this, subject_list,width,"3");

        chapter_list.setAdapter(chapterAdapter);
        chapter_list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        RecyclerDecoration_Height decoration_height = new RecyclerDecoration_Height(100);
        chapter_list.addItemDecoration(decoration_height);

        back_btn = findViewById(R.id.back_btn);



        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );


        back_btn.setOnClickListener(v->{
            finish();
        });
    }

    public void uploadChapter(String id) {
        // id가 챕터 기준 +2 되어야 함
        //id : chapter_id+"_1",
        //contents 번호는 3부터 시작 / chapter 번호는 2부터 시작

        int num = Integer.parseInt(id);

        String totalNum = Integer.toString(num + (num + 1));
        Log.e("test", totalNum);

        //chapter_content11_2
        int[] rID = new int[3];

        String deep_problem1 = "";
        String deep_problem2 = "";

        for (int i = 0; i < 3; i++) {
            rID[i] = getResources().getIdentifier("chapter_content" + String.valueOf(num) + "_" + String.valueOf(i + 1), "drawable", getPackageName());
        }

        for (int i = 0; i < contentsList.size(); i++) {
            if (i == (num - 1)) {
                basic_problem = contentsList.get(i).getBasic_problem();
                deep_problem1 = contentsList.get(i).getDeep_problem1();
                deep_problem2 = contentsList.get(i).getDeep_problem2();
            }
        }

        // 변경 필요 13 >> 10
        if (num > 10) {
            ContinueDialog continueDialog = new ContinueDialog(this, "준비 중인 콘텐츠입니다");
            continueDialog.show();

            subject_list.add(new Subject(
                    totalNum + "-2",
                    R.drawable.default_problem_image,
                    basic_problem,
                    rID[0],
                    MySharedPreferences.getInt(getApplicationContext(), basic_problem + " MAX"),false));
        } else {
            subject_list.add(new Subject(
                    totalNum + "-2",
                    R.drawable.default_problem_image,
                    basic_problem,
                    rID[0],
                    MySharedPreferences.getInt(getApplicationContext(), basic_problem + " MAX"),true));
        }

        /*subject_list.add(new Subject(
                totalNum + "-2",
                R.drawable.default_problem_image,
                basic_problem,
                rID[0],
                MySharedPreferences.getInt(getApplicationContext(), basic_problem + " MAX"),true));*/

        if(MySharedPreferences.getInt(getApplicationContext(),basic_problem + " MAX") == 5) {
            lock_check = true;
        }/* else {
            lock_check = true;
        }*/

        subject_list.add(new Subject(
                totalNum + "-3",
                R.drawable.advanced_problem_image,
                deep_problem1,
                rID[1],
                MySharedPreferences.getInt(getApplicationContext(),deep_problem1 + " MAX"),lock_check));

        subject_list.add(new Subject(
                totalNum + "-4",
                R.drawable.advanced_problem_image2,
                deep_problem2,
                rID[2],
                MySharedPreferences.getInt(getApplicationContext(),deep_problem2 + " MAX"),lock_check));

        subject_list.add(new Subject(
                totalNum + "-5",
                R.drawable.advanced_problem_image3,
                "문제풀이",
                rID[0],
                MySharedPreferences.getInt(getApplicationContext(),"문제풀이" + totalNum),false));
    }


    public void addData(int id,String group, String title, int img, int percentage){

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        chapterAdapter.notifyItemChanged(chapterAdapter.current_chapter,"update");
        if (MySharedPreferences.getInt(getApplicationContext(),basic_problem + " MAX") == 5){

            chapterAdapter.notifyItemRangeChanged(1, subject_list.size()-2,"lock_check");
        }
//        if(MySharedPreferences.getInt(getApplicationContext(),"LED 1초간 껐다 켜기 MAX")==5){
//            chapter3.setOpen(true,false);
//        }
//        chapter1.setPercentage(MySharedPreferences.getInt(getApplicationContext(),"LED 깜박이기 MAX"));
//        chapter2.setPercentage(MySharedPreferences.getInt(getApplicationContext(), "LED 1초간 껐다 켜기 MAX"));
//        chapter3.setPercentage(MySharedPreferences.getInt(getApplicationContext(), "LED 3개 깜박이기 MAX"));
    }
}