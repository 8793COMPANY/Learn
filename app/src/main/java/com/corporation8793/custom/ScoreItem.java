package com.corporation8793.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.corporation8793.R;

public class ScoreItem extends ConstraintLayout {
    private ImageView background, stroke,answer_correct_check_img;
    private boolean correct = false; // 선택된 번호
    String title="";
    private TextView problem_number;
    public ScoreItem(@NonNull Context context) {
        super(context);
        initializeViews(context, null);
    }

    public ScoreItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, null);
    }

    public ScoreItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, null);
    }

    public ScoreItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, null);
    }


    // 객체 초기화 될때 호출 된다.
    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_score_itemview, this);
        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다 즉 (attrs.xml에서 발생된 selected 속성이
            // 발생되어 private void setSelected(int select, boolean force)를 호출하게 된다
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScoreItem);
            Log.e("a count",a.length()+"");
            correct = a.getBoolean(R.styleable.ScoreItem_correct,false);
            title = a.getString(R.styleable.ScoreItem_title);
            a.recycle(); // 이용이 끝났으면 recycle() 호출
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        background = (ImageView) findViewById(R.id.background);
        problem_number =  findViewById(R.id.problem_number);
        answer_correct_check_img = findViewById(R.id.answer_correct_check_img);
        // 처음에는 XML의 지정을 반영하고자 0번째 인수인 force를 true로 한다
        setTitle(title);
        setCorrect(correct);
    }

    public void setTitle(String title){
        this.title = title;
        problem_number.setText(this.title);
    }


    public void setCorrect(boolean correct) {
        this.correct = correct;
        if (correct){
            answer_correct_check_img.setBackgroundResource(R.drawable.answer_correct_img);
        }else{
            answer_correct_check_img.setBackgroundResource(R.drawable.answer_incorrect_img);
        }
    }

    public boolean getCorrect() {
        return correct;
    }


}
