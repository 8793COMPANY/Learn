package com.corporation8793.problem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.corporation8793.R

class RightAnswerActivity : AppCompatActivity() {
    var right_answers = arrayOf("3","2","2","3","3")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_right_answer)

        var result_text = findViewById<TextView>(R.id.result_text)
        var result = findViewById<TextView>(R.id.result)


        var results = intent.getStringExtra("results")!!.split(" ")

        var count =0
        var right_answer_count =0
        var check = ""

        results.forEach{
            if (right_answers[right_answer_count] == it) {
                check += (count+1).toString()+": 정답! "
                right_answer_count++;
            }else{
                check += (count+1).toString()+": 땡! "
            }
            count++
        }

        result.setText(check)

        if (right_answer_count <2){
            result_text.setText("더 열심히 공부해주세요.")
        }else if(right_answer_count == 2){
            result_text.setText("아직 포텐 터지기 전.")
        }else if(right_answer_count == 3){
            result_text.setText("이제 절반 넘었네.")
        }else if(right_answer_count == 4){
            result_text.setText("고지가 눈앞입니다.")
        }else if(right_answer_count == 5){
            result_text.setText("더 이상 배울 게 없어요.")
        }

    }
}