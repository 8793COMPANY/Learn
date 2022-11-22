package com.learn4.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.learn4.R;

public class TextAnswerItem extends ConstraintLayout {
    private ImageView background, stroke;
    private Button check_btn;
    TextView answer_text;
    private boolean mSelected = false; // 선택된 번호
    String type="", title ="";
    public TextAnswerItem(@NonNull Context context) {
        super(context);
        initializeViews(context, null);
    }

    public TextAnswerItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, null);
    }

    public TextAnswerItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, null);
    }

    public TextAnswerItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, null);
    }


    // 객체 초기화 될때 호출 된다.
    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.text_answer_view, this);
        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다 즉 (attrs.xml에서 발생된 selected 속성이
            // 발생되어 private void setSelected(int select, boolean force)를 호출하게 된다
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnswerCustom);
            Log.e("a count",a.length()+"");
            mSelected = a.getBoolean(R.styleable.TextAnswerCustom_selected, false);
            type = a.getString(R.styleable.TextAnswerCustom_type);
            title = a.getString(R.styleable.TextAnswerCustom_title);
            a.recycle(); // 이용이 끝났으면 recycle() 호출
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        background = (ImageView) findViewById(R.id.background);
        stroke = (ImageView) findViewById(R.id.select_stroke);
        check_btn = (Button) findViewById(R.id.select_check_btn);
        answer_text = findViewById(R.id.answer_text);
        // 처음에는 XML의 지정을 반영하고자 0번째 인수인 force를 true로 한다
        setType(type);
        setSelected(mSelected, true);
    }

    public void setType(String type){
        this.type = type;
    }

    public void setTitle(String title) {
        answer_text.setText(title);
    }


    public void setSelected(boolean select) {
        setSelected(select, false);
    }

    public boolean getSelected() {
        return mSelected;
    }

    private void setSelected(boolean select, boolean force) {
        mSelected = select;
        Log.e("mSelected",mSelected+"");
        if (mSelected) {
            stroke.setBackgroundResource(R.drawable.problem_item_box_select);
            check_btn.setBackgroundResource(R.drawable.answer_check_on_btn);
            stroke.setImageResource(0);
        } else {
//            if (type.equals("search")){
                stroke.setBackgroundResource(R.drawable.problem_item_box_not_select);
                check_btn.setBackgroundResource(R.drawable.answer_check_off_btn);
                stroke.setImageResource(0);
//            }else{
//                stroke.setBackgroundResource(0);
//                stroke.setImageResource(R.drawable.enabled_item_img);
//                check_btn.setBackgroundResource(R.drawable.answer_check_off_btn);
//            }


        }
    }
}
