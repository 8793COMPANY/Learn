package com.learn.wp_rest.repository.acf

import com.learn.wp_rest.data.acf.QuizReportJson
import com.learn.wp_rest.data.acf.UploadReportJson
import com.learn.wp_rest.data.wp.posts.UploadReport
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * ACF(Advanced Custom Fields) 인터페이스
 * @author  두동근
 */
interface AcfService {
    /**
     * [UploadReport] ACF(Advanced Custom Fields) 수정
     * @author  두동근
     */
    @POST("/wp-json/acf/v3/posts/{id}")
    fun updateUploadReportAcf(@Header("Authorization") h1 : String,
                              @Path("id") id : String,
                              @Query("fields[chapter]") chapter : Int,
                              @Query("fields[content]") content : Int,
                              @Query("fields[circuit_img]") circuit_img : String,
                              @Query("fields[block_img]") block_img : String) : Call<UploadReportJson>
    /**
     * [QuizReport] ACF(Advanced Custom Fields) 수정
     * @author  두동근
     */
    @POST("/wp-json/acf/v3/posts/{id}")
    fun updateQuizReportAcf(@Header("Authorization") h1 : String,
                            @Path("id") id : String,
                            @Query("fields[chapter]") chapter : Int,
                            @Query("fields[answer_1]") answer_1 : Int,
                            @Query("fields[answer_2]") answer_2 : Int,
                            @Query("fields[answer_3]") answer_3 : Int,
                            @Query("fields[answer_4]") answer_4 : Int,
                            @Query("fields[answer_5]") answer_5 : Int,
    ) : Call<QuizReportJson>
}