package com.learn.wp_rest.repository.acf

import com.learn.wp_rest.RestClient
import com.learn.wp_rest.data.acf.UploadReportJson
import com.learn.wp_rest.data.wp.posts.UploadReport

/**
 * [AcfService]의 구현 클래스
 * @author  두동근
 * @see     AcfService
 */
class AcfRepository(private val basicAuth : String) {
    /**
     * [UploadReport] ACF(Advanced Custom Fields) 수정
     * @author  두동근
     */
    fun updatePostAcf(id : String,
                      chapter : Int,
                      content : Int,
                      lesson_name : String,
                      circuit_img : String,
                      block_img : String) : Pair<String, UploadReportJson?> {
        val response = RestClient.acfService.updateUploadReportAcf(
            h1 = basicAuth,
            id = id,
            chapter = chapter,
            content = content,
            lesson_name = lesson_name,
            circuit_img = circuit_img,
            block_img = block_img)
            .execute()

        return Pair(response.code().toString(), response.body())
    }
}