package com.learn.wp_rest.auth

import android.util.Log
import com.learn.wp_rest.repository.auth.AuthRepository
import org.junit.Assert
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.util.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AuthTests {
    private val authRepository = AuthRepository()

    // 테스트용 학생 계정
    private val username = "student8793"
    private val password = "@ejrghk3865"

    private var nonce: String? = null
    private var cookie: String? = null

    @Test
    @Order(1)
    fun getNonce() {
        val response = authRepository.getNonce()
        println("getNonce : ${response.first}, ${response.second}")
        nonce = response.second?.nonce
        Assert.assertEquals("200", response.first)
    }

    @Test
    @Order(2)
    fun getAuthCookie() {
        val currentTime = System.currentTimeMillis()
        val currentDate = Date(currentTime)
        println("currentTime : $currentTime")
        println("currentDate : $currentDate")

        val authCookie = authRepository.getAuthCookie(nonce, username, password, "$currentTime")
        println("getAuthCookie : ${authCookie.first}, ${authCookie.second}")
        cookie = authCookie.second?.cookie
        Assert.assertEquals("200", authCookie.first)

        val validation = authRepository.isValidCookie(cookie, "$currentTime")
        println("isValidCookie : ${validation.first}, ${validation.second}")
        Assert.assertEquals("200", validation.first)
    }
}