package com.learn.wp_rest.repository.wp.posts

import com.learn.wp_rest.data.wp.posts.QuizReport
import com.learn.wp_rest.data.wp.posts.UploadReport
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 게시물(Posts) 인터페이스
 * @author  두동근
 */
interface PostsService {
    // UploadReport
    /**
     * [UploadReport]를 생성합니다.
     * @author  두동근
     */
    @FormUrlEncoded
    @POST("wp-json/wp/v2/posts")
    fun createUploadReport(@Header("Authorization") h1 : String,
                           @Field("status") status : String,
                           @Field("title") title : String,
                           @Field("content") content : String,
                           @Field("categories") categories : String) : Call<UploadReport>

    // QuizReport
    /**
     * [QuizReport]를 생성합니다.
     * @author  두동근
     */
    @FormUrlEncoded
    @POST("wp-json/wp/v2/posts")
    fun createQuizReport(@Header("Authorization") h1 : String,
                         @Field("status") status : String,
                         @Field("title") title : String,
                         @Field("content") content : String,
                         @Field("categories") categories : String) : Call<QuizReport>
}