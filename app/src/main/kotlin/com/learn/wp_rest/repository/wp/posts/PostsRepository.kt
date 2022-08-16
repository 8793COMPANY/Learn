package com.learn.wp_rest.repository.wp.posts

import com.learn.wp_rest.RestClient
import com.learn.wp_rest.data.wp.media.Media
import com.learn.wp_rest.data.wp.posts.QuizReport
import com.learn.wp_rest.data.wp.posts.UploadReport

class PostsRepository(private val basicAuth : String) {
    /**
     * [UploadReport]를 생성합니다.
     * @author  두동근
     * @param   title               제목
     * @param   circuit_img         회로도 사진 URL
     * @param   block_img           블록 사진 URL
     * @return  responseCode (expected : "201"), [UploadReport]
     */
    fun createUploadReport(title : String, circuit_img : String, block_img : String) : Pair<String, UploadReport?> {
        val htmlTemplate =
            """
                <div class="app-report-row">
                  <div class="app-report-column">
                    <a href="$circuit_img" target="_blank">
                    <h2>회로도</h2>
                    <img src="$circuit_img" alt="circuit" style="width:100%">
                    </a>
                  </div>
                  <div class="app-report-column">
                    <a href="$block_img" target="_blank">
                    <h2>블록</h2>
                    <img src="$block_img" alt="block" style="width:100%">
                    </a>
                  </div>
                </div>
            """.trimIndent()

        val response = RestClient.postsService.createUploadReport(
            h1 = basicAuth,
            title = title,
            content = htmlTemplate,
            status = "publish",
            categories = RestClient.POST_CATEGORY_UPLOAD_REPORT
        ).execute()

        return Pair(response.code().toString(), response.body())
    }
    /**
     * [QuizReport]를 생성합니다.
     * @author  두동근
     * @param   title               제목
     * @return  responseCode (expected : "201"), [QuizReport]
     */
    fun createQuizReport(title : String,
                         answer_1 : Int,
                         answer_2 : Int,
                         answer_3 : Int,
                         answer_4 : Int,
                         answer_5 : Int) : Pair<String, QuizReport?> {
        val htmlTemplate =
            """
                <table>
                    <thead>
                        <tr>
                            <th colspan="5">답안지</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1번</td>
                            <td>2번</td>
                            <td>3번</td>
                            <td>4번</td>
                            <td>5번</td>
                        </tr>
                        <tr>
                            <td>${answer_1}</td>
                            <td>${answer_2}</td>
                            <td>${answer_3}</td>
                            <td>${answer_4}</td>
                            <td>${answer_5}</td>
                        </tr>
                    </tbody>
                </table>
            """.trimIndent()

        val response = RestClient.postsService.createQuizReport(
            h1 = basicAuth,
            title = title,
            content = htmlTemplate,
            status = "publish",
            categories = RestClient.POST_CATEGORY_QUIZ_REPORT
        ).execute()

        return Pair(response.code().toString(), response.body())
    }
}