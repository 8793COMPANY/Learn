package com.learn.wp_rest.repository.wp.media

import com.learn.wp_rest.data.wp.media.Media
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

/**
 * 미디어(Media) 인터페이스
 * @author  두동근
 */
interface MediaService {
    /**
     * 이미지를 업로드합니다.
     * @author  두동근
     * @see     Media
     * @see     Pair
     * @see     File
     */
    @Multipart
    @POST("wp-json/wp/v2/media")
    fun uploadMedia(@Header("Authorization") h1 : String,
                    @Part file : MultipartBody.Part) : Call<Media>
    /**
     * id([mediaId])가 일치하는 미디어를 검색합니다.
     * * (id가 일치하는 미디어는 1개(One)입니다.)
     * @author  두동근
     * @see     Media
     * @see     Pair
     */
    @GET("wp-json/wp/v2/media/{id}")
    fun retrieveMedia(@Path("id") id : String?) : Call<Media>
}