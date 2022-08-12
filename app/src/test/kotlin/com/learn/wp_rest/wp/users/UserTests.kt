package com.learn.wp_rest.wp.users;

import com.learn.wp_rest.repository.wp.users.UsersRepository
import okhttp3.Credentials
import org.junit.Assert
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserTests {
    // 테스트용 학생 계정
    private val username = "student8793"
    private val password = "@ejrghk3865"

    // auth credential for the Basic scheme
    private val basicAuth = Credentials.basic(username, password)

    private val usersRepository = UsersRepository(basicAuth)

    @Test
    @Order(1)
    fun whoAmI() {
        val response = usersRepository.whoAmI()
        println("whoAmI : ${response.first}, ${response.second}")
        Assert.assertEquals("200", response.first)
    }
}
