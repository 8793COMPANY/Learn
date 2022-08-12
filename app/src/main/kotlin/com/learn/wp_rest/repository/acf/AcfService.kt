package com.learn.wp_rest.repository.acf

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
                              @Query("fields[lesson_name]") lesson_name : String,
                              @Query("fields[circuit_img]") circuit_img : String,
                              @Query("fields[block_img]") block_img : String) : Call<UploadReportJson>
}