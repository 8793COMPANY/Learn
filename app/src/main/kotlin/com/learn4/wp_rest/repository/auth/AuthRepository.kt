package com.learn.wp_rest.repository.auth

import com.learn.wp_rest.RestClient
import com.learn.wp_rest.data.auth.AuthCookie
import com.learn.wp_rest.data.auth.Nonce
import com.learn.wp_rest.data.auth.Validation
import retrofit2.Call
import retrofit2.http.Query
import java.util.*

/**
 * [AuthService]의 구현 클래스
 * @author  두동근
 * @see     AuthService
 */
class AuthRepository {
    /**
     * [AuthCookie]생성용 [Nonce]를 초기화합니다.
     * @author  두동근
     * @return  responseCode (expected : "200"), [Nonce]
     */
    fun getNonce() : Pair<String, Nonce?> {
        val response = RestClient.authService.getNonce().execute()

        return Pair(response.code().toString(), response.body())
    }

    /**
     * [AuthCookie]를 초기화합니다.
     * * 유효 기간 : 2시간
     * @author  두동근
     * @return  responseCode (expected : "200"), [AuthCookie]
     * @param   nonce           [AuthCookie]생성용 [Nonce]
     * @param   username        회원의 로그인 아이디
     * @param   password        회원의 로그인 패스워드
     * @see     getNonce
     * @see     Pair
     * @see     AuthCookie
     */
    fun getAuthCookie(nonce: String?,
                      username: String,
                      password: String
    ) : Pair<String, AuthCookie?> {
        val currentTime = System.currentTimeMillis()
        val currentDate = Date(currentTime)
        println("authCookieGenerateTime : $currentTime")
        println("authCookieGenerateDate : $currentDate")

        val response = RestClient.authService.getAuthCookie(nonce, username, password, "$currentTime").execute()

        return Pair(response.code().toString(), response.body())
    }

    /**
     * [AuthCookie]를 검증합니다.
     * @author  두동근
     * @return  responseCode (expected : "200"), [Boolean]
     * @param   cookie        [AuthCookie]
     * @see     getAuthCookie
     * @see     Pair
     */
    fun isValidCookie(cookie: String?) : Pair<String, Boolean> {
        val currentTime = System.currentTimeMillis()
        val currentDate = Date(currentTime)
        println("authCookieValidateTime : $currentTime")
        println("authCookieValidateDate : $currentDate")

        val response = RestClient.authService.isValidCookie(cookie, "$currentTime").execute()

        return Pair(response.code().toString(), response.body()!!.valid)
    }
}