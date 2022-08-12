package com.learn.wp_rest.repository.wp.posts

import com.learn.wp_rest.RestClient
import com.learn.wp_rest.data.wp.media.Media
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
}