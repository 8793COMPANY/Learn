package com.learn.wp_rest.repository.acf

import com.learn.wp_rest.RestClient
import com.learn.wp_rest.data.acf.QuizReportJson
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
    fun updateUploadReportAcf(id : String,
                      chapter : Int,
                      content : Int,
                      circuit_img : String,
                      block_img : String) : Pair<String, UploadReportJson?> {
        val response = RestClient.acfService.updateUploadReportAcf(
            h1 = basicAuth,
            id = id,
            chapter = chapter,
            content = content,
            circuit_img = circuit_img,
            block_img = block_img)
            .execute()

        return Pair(response.code().toString(), response.body())
    }
    /**
     * [QuizReport] ACF(Advanced Custom Fields) 수정
     * @author  두동근
     */
    fun updateQuizReportAcf(id : String,
                            chapter : Int,
                            answer_1 : Int,
                            answer_2 : Int,
                            answer_3 : Int,
                            answer_4 : Int,
                            answer_5 : Int
    ) : Pair<String, QuizReportJson?> {
        val response = RestClient.acfService.updateQuizReportAcf(
            h1 = basicAuth,
            id = id,
            chapter = chapter,
            answer_1 = answer_1,
            answer_2 = answer_2,
            answer_3 = answer_3,
            answer_4 = answer_4,
            answer_5 = answer_5)
            .execute()

        return Pair(response.code().toString(), response.body())
    }
}