package com.learn.wp_rest.repository.wp.users

import com.learn.wp_rest.RestClient
import com.learn.wp_rest.data.auth.AuthCookie
import com.learn.wp_rest.data.auth.Nonce
import com.learn.wp_rest.data.wp.users.User
import com.learn.wp_rest.repository.auth.AuthService
import retrofit2.Call
import retrofit2.http.Header

/**
 * [UsersService]의 구현 클래스
 * @author  두동근
 * @see     UsersService
 */
class UsersRepository(private val basicAuth : String) {
    /**
     * [User]를 확인합니다.
     * @author  두동근
     * @return  responseCode (expected : "200"), [User]
     */
    fun whoAmI() : Pair<String, User?> {
        val response = RestClient.usersService.whoAmI(basicAuth).execute()

        return Pair(response.code().toString(), response.body())
    }
}