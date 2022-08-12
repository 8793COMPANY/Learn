package com.learn.wp_rest

import com.learn.wp_rest.repository.auth.AuthService
import com.learn.wp_rest.repository.wp.media.MediaService
import com.learn.wp_rest.repository.wp.posts.PostsService
import com.learn.wp_rest.repository.wp.users.UsersService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor

/**
 * [Retrofit] 사용을 위한 [object] 클래스
 * @author  두동근
 */
object RestClient {
    /**
     * Static IP of AWS Server (배울래? LD-LMS)
     */
    private const val baseUrl = "https://baeulrae.kr/"

    /**
     * 게시물 카테고리(미분류)
     */
    const val POST_CATEGORY_NULL = "1"
    /**
     * 게시물 카테고리(업로드 리포트)
     */
    const val POST_CATEGORY_UPLOAD_REPORT = "413"
    /**
     * 게시물 카테고리(퀴즈 리포트)
     */
    const val POST_CATEGORY_QUIZ_REPORT = "414"

    /**
     * baseUrl 정보로 초기화된 [Retrofit]
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * [retrofit]의 [AuthService]
     */
    val authService = retrofit.create(AuthService::class.java)

    /**
     * [retrofit]의 [UsersService]
     */
    val usersService = retrofit.create(UsersService::class.java)

    /**
     * [retrofit]의 [MediaService]
     */
    val mediaService = retrofit.create(MediaService::class.java)

    /**
     * [retrofit]의 [PostsService]
     */
    val postsService = retrofit.create(PostsService::class.java)
}