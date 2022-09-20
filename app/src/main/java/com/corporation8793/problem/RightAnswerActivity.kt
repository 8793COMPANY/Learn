package com.corporation8793.problem

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.corporation8793.R
import com.corporation8793.custom.ScoreItem

class RightAnswerActivity : AppCompatActivity() {
    var right_answers = arrayOf("3","2","2","3","3")
    var score_text = ""
    var chapter_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_right_answer)

        hideSystemUI()

        chapter_id = intent.getStringExtra("chapter_id")!!

        var result_text = findViewById<TextView>(R.id.result_text)

        var problem1 = findViewById<ScoreItem>(R.id.problem1)
        var problem2 = findViewById<ScoreItem>(R.id.problem2)
        var problem3 = findViewById<ScoreItem>(R.id.problem3)
        var problem4 = findViewById<ScoreItem>(R.id.problem4)
        var problem5 = findViewById<ScoreItem>(R.id.problem5)

        var back_to_solve_problem_btn  = findViewById<Button>(R.id.back_to_solve_problem_btn)
        var finish_btn = findViewById<Button>(R.id.finish_btn)

        back_to_solve_problem_btn.setOnClickListener {
            var intent = Intent(this, SolvingProblem::class.java)
            intent.putExtra("chapter_id",chapter_id)
            startActivity(intent)
            finish()
        }

        finish_btn.setOnClickListener {
            finish()
        }
//        var result = findViewById<TextView>(R.id.result)

        problem1.setTitle("Q1")
        problem2.setTitle("Q2")
        problem3.setTitle("Q3")
        problem4.setTitle("Q4")
        problem5.setTitle("Q5")
        var list = listOf<ScoreItem>(problem1,problem2,problem3,problem4,problem5)


        var results = intent.getStringExtra("results")!!.split(" ")

        var count =0
        var right_answer_count =0
        var check = ""

        results.forEach{
            if (right_answers[right_answer_count] == it) {
                check += (count+1).toString()+": 정답! "
                list.get(count).correct = true
                right_answer_count++;
            }else{
                check += (count+1).toString()+": 땡! "
                list.get(count).correct = false
            }
            count++
        }

//        result.setText(check)

        if (right_answer_count <2){
            score_text = "더 열심히 공부해주세요.\n"
//            result_text.setText()
        }else if(right_answer_count == 2){
            score_text = "아직 포텐 터지기 전.\n"
//            result_text.setText("아직 포텐 터지기 전.")
        }else if(right_answer_count == 3){
            score_text = "이제 절반 넘었네.\n"
//            result_text.setText("이제 절반 넘었네.")
        }else if(right_answer_count == 4){
            score_text = "고지가 눈앞입니다.\n"
//            result_text.setText("고지가 눈앞입니다.")
        }else if(right_answer_count == 5){
            score_text = "더 이상 배울 게 없어요.\n"
//            result_text.setText("더 이상 배울 게 없어요.")
        }

        score_text += (right_answer_count *20).toString()+"점"
        changeScoreTextStyle(result_text)

        //업로드


    }

    fun changeScoreTextStyle(textView: TextView) {
        var start = score_text.indexOf("\n")+1
        var end = score_text.indexOf("점")
        val spannableStringBuilder = SpannableStringBuilder(score_text)

// startIndex ~ endIndexExclusive 에 어떤 색상을 입혀주세요~ 라는 역할을 한다.
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(Color.parseColor("#af6401")),
            start,
            score_text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
//        spannableStringBuilder.setSpan(
//            AbsoluteSizeSpan(56),
//            startIndex,
//            endIndexExclusive-1,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
//        )

        spannableStringBuilder.setSpan(
            RelativeSizeSpan(1.9f),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        spannableStringBuilder.setSpan(
            RelativeSizeSpan(1.2f),
            end,
            score_text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

// 위에서 span 을 입힌 [spannableStringBuilder] 을 textView.text 에 넣어준다


        textView.setText(spannableStringBuilder)
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}