package com.learn.wp_rest

import com.learn.wp_rest.repository.acf.AcfService
import com.learn.wp_rest.repository.auth.AuthService
import com.learn.wp_rest.repository.wp.media.MediaService
import com.learn.wp_rest.repository.wp.posts.PostsService
import com.learn.wp_rest.repository.wp.users.UsersService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


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
     * 게시물 카테고리(에러 리포트)
     */
    const val POST_CATEGORY_ERROR_REPORT = "418"

    /**
     * baseUrl 정보로 초기화된 [Retrofit]
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //ssl인증서 관련 에러가 났을 때 추가해주면 됨
    //.client(getUnsafeOkHttpClient()?.build())



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

    /**
     * [retrofit]의 [AcfService]
     */
    val acfService = retrofit.create(AcfService::class.java)

    fun getUnsafeOkHttpClient(): OkHttpClient.Builder? {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}