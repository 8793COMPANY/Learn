package com.learn.wp_rest.repository.auth

import com.learn.wp_rest.data.auth.AuthCookie
import com.learn.wp_rest.data.auth.Nonce
import com.learn.wp_rest.data.auth.Validation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 인증(Auth) 인터페이스
 * @author  두동근
 */
interface AuthService {
    /**
     * [AuthCookie]생성용 [Nonce]를 초기화합니다.
     * @author  두동근
     */
    @GET("api/get_nonce/?controller=auth&method=generate_auth_cookie")
    fun getNonce() : Call<Nonce?>

    /**
     * [AuthCookie]를 초기화합니다.
     * * 유효 기간 : 2시간
     * @author  두동근
     */
    @GET("api/auth/generate_auth_cookie")
    fun getAuthCookie(@Query("nonce") nonce: String?,
                      @Query("username") username: String,
                      @Query("password") password: String,
                      @Query("time") time: String,
                      @Query("seconds") seconds: String = "7200") : Call<AuthCookie>

    /**
     * [AuthCookie]를 검증합니다.
     * @author  두동근
     */
    @GET("api/auth/validate_auth_cookie")
    fun isValidCookie(@Query("cookie") cookie: String?,
                      @Query("time") time: String) : Call<Validation>
}