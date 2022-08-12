package com.learn.wp_rest.acf

import com.learn.wp_rest.repository.acf.AcfRepository
import com.learn.wp_rest.repository.wp.posts.PostsRepository
import okhttp3.Credentials
import org.junit.Assert
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AcfTests {
    // 테스트용 학생 계정
    private val username = "student8793"
    private val password = "@ejrghk3865"

    // auth credential for the Basic scheme
    private val basicAuth = Credentials.basic(username, password)

    private val acfRepository = AcfRepository(basicAuth)

    @Test
    @Order(1)
    fun updatePostAcf() {
        val testPostId = "436"
        val mediaResponse_ci_guid = "http://baeulrae.kr/wp-content/uploads/2022/08/circuit_img_test.jpg"
        val mediaResponse_bi_guid = "http://baeulrae.kr/wp-content/uploads/2022/08/block_img_test.jpg"

        val response = acfRepository.updatePostAcf(
            id = testPostId,
            chapter = 3,
            content = 1,
            lesson_name = "LED 깜빡이기",
            circuit_img = mediaResponse_ci_guid,
            block_img = mediaResponse_bi_guid
        )
        println("updatePostAcf : ${response.first}, ${response.second}")
        Assert.assertEquals("200", response.first)
    }
}