package com.learn.wp_rest.wp.media

import com.learn.wp_rest.repository.wp.media.MediaRepository
import okhttp3.Credentials
import org.junit.Assert
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.io.File

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class MediaTests {
    // 테스트용 학생 계정
    private val username = "student8793"
    private val password = "@ejrghk3865"

    // auth credential for the Basic scheme
    private val basicAuth = Credentials.basic(username, password)

    private val mediaRepository = MediaRepository(basicAuth)

    @Test
    @Order(1)
    fun uploadMedia() {
        val circuit_img_test = File("src/test/kotlin/com/learn/wp_rest/wp/media/circuit_img_test.jpg")
        val block_img_test = File("src/test/kotlin/com/learn/wp_rest/wp/media/block_img_test.jpg")

        val response_ci = mediaRepository.uploadMedia(circuit_img_test)
        println("uploadMedia : ${response_ci.first}, ${response_ci.second}")
        Assert.assertEquals("201", response_ci.first)

        val response_bi = mediaRepository.uploadMedia(block_img_test)
        println("uploadMedia : ${response_bi.first}, ${response_bi.second}")
        Assert.assertEquals("201", response_bi.first)
    }
}