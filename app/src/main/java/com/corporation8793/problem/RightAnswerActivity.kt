package com.corporation8793.problem

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.corporation8793.R
import com.corporation8793.custom.ScoreItem
import com.learn.wp_rest.repository.acf.AcfRepository
import com.learn.wp_rest.repository.wp.posts.PostsRepository
import okhttp3.Credentials

class RightAnswerActivity : AppCompatActivity() {
    var right_answers = arrayOf("3","2","2","3","3")
    var score_text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_right_answer)

        var result_text = findViewById<TextView>(R.id.result_text)

        var problem1 = findViewById<ScoreItem>(R.id.problem1)
        var problem2 = findViewById<ScoreItem>(R.id.problem2)
        var problem3 = findViewById<ScoreItem>(R.id.problem3)
        var problem4 = findViewById<ScoreItem>(R.id.problem4)
        var problem5 = findViewById<ScoreItem>(R.id.problem5)
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
//        Thread {
//            Log.e("in", "thread")
//            val basic =
//                Credentials.basic("student8793", "@ejrghk3865")
//            val postsRepository = PostsRepository(basic)
//            val acfRepository = AcfRepository(basic)
//            val post_id = postsRepository.createQuizReport(
//                "LED 깜박이기",
//                1,
//                2,
//                3,
//                4,
//                5
//            ).second!!.id
//            val check = acfRepository.updateQuizReportAcf(
//                post_id!!,
//                1,
//                1,
//                "LED 깜박이기",
//                1,
//                2,
//                3,
//                4,
//                5
//            ).second.toString()
//            Log.e("end", "thread")
//            Log.e("upload_check", check)
//        }.start()

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
}