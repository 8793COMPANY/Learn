package com.learn4.view.custom.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.learn4.R;
import com.learn4.view.problem.basic.ProblemActivity;

public class CustomView extends ConstraintLayout {
    LinearLayout chapter_image;
    private TextView chapter_title;
    private ImageView  lock_check, problem_group;
    private boolean mOpen = false; // 선택된 번호
    private int  step= 0, chapter = 0, group =0;
    private String title = "", id ="";
    private ProgressBar progressBar;
    private TextView percentage;

    public CustomView(@NonNull Context context) {
        super(context);
        initializeViews(context, null);
    }

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, null);
    }

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, null);
    }

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, null);
    }


    // 객체 초기화 될때 호출 된다.
    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chapter_content_layout, this);
        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다 즉 (attrs.xml에서 발생된 selected 속성이
            // 발생되어 private void setSelected(int select, boolean force)를 호출하게 된다
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView);
            Log.e("a count",a.length()+"");
            step = a.getInteger(R.styleable.MyCustomView_percentage,0);
            mOpen = a.getBoolean(0, false);
            title = a.getString(R.styleable.MyCustomView_title);
            step = a.getInteger(R.styleable.MyCustomView_percentage,0);
            chapter = a.getInt(R.styleable.MyCustomView_chapter,0);
            group = a.getInt(R.styleable.MyCustomView_group,1);
            id =  a.getString(R.styleable.MyCustomView_id);

            a.recycle(); // 이용이 끝났으면 recycle() 호출
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        background = (ImageView) findViewById(R.id.background);
        problem_group = findViewById(R.id.problem_group);
        chapter_title = findViewById(R.id.chapter_content_title);
        chapter_image = findViewById(R.id.chapter_content_img);
        lock_check = (ImageView) findViewById(R.id.lock_check);
        progressBar = findViewById(R.id.progress);
        percentage = findViewById(R.id.percentage);
        // 처음에는 XML의 지정을 반영하고자 0번째 인수인 force를 true로 한다
        setOpen(mOpen, false);
//        setGroup(group);

        chapter_image.setOnClickListener(v->{
            if (getOpen()) {
                Log.e("customview id",id);
                Intent intent = new Intent(getContext(), ProblemActivity.class);
                intent.putExtra("step", group);
                intent.putExtra("id", id);
                getContext().startActivity(intent);
            }
        });
    }




    public boolean getSelected() {
        return mOpen;
    }

    public int getPercentage() {
        return step;
    }
    public String getID() {
        return id;
    }

    public boolean getOpen() {
        return mOpen;
    }


    public void setOpen(boolean select, boolean force) {
        mOpen = select;
        if (mOpen) {
//            stroke.setBackgroundResource(R.drawable.board_image_btn_background_on);
            lock_check.setBackgroundResource(0);
        }
    }

    public void setPercentage(int step){
        percentage.setText(step*20+"%");
        progressBar.setProgress(step*20);
    }

    public void setTitle(String title){
        this.title = title;
        chapter_title.setText(title);

    }

    public void setID(String id){
        this.id = id;

    }

    public void setBackground(int chapter){
        setBackground(chapter,R.drawable.chapter_content1);
    }

    private void setBackground(int chapter, int value){
        this.chapter = chapter;
        chapter_image.setBackgroundResource(chapter);
    }

    public void setGroup(int group){
        this.group = group;
        if (group == 1){
            problem_group.setBackgroundResource(R.drawable.default_problem_image);
        }else if(group == 2){
            problem_group.setBackgroundResource(R.drawable.advanced_problem_image);
        }else{
            problem_group.setBackgroundResource(R.drawable.advanced_problem_image2);
        }

    }
}
