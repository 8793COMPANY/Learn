package com.learn.wp_rest.repository.wp.users

import com.learn.wp_rest.data.auth.AuthCookie
import com.learn.wp_rest.data.auth.Nonce
import com.learn.wp_rest.data.wp.users.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * 유저(Users) 인터페이스
 * @author  두동근
 */
interface UsersService {
    /**
     * [User]를 확인합니다.
     * @author  두동근
     */
    @GET("wp-json/wp/v2/users/me")
    fun whoAmI(@Header("Authorization") h1 : String) : Call<User?>
}