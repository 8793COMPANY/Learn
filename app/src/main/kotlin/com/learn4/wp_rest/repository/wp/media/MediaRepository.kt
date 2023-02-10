package com.learn.wp_rest.repository.wp.media

import com.learn.wp_rest.RestClient
import com.learn.wp_rest.data.wp.media.Media
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * [MediaService]의 구현 클래스
 * @author  두동근
 * @see     MediaService
 */
class MediaRepository(private val basicAuth : String) {
    /**
     * 이미지를 업로드합니다.
     * @author  두동근
     * @param   file        이미지 [File]
     * @return  responseCode (expected : "201"), [Media]
     * @see     Media
     * @see     Pair
     * @see     File
     */
    fun uploadMedia(file : File) : Pair<String, Media?> {
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("image/*"), file)
        )

        val response = RestClient.mediaService.uploadMedia(basicAuth, filePart).execute()

        return Pair(response.code().toString(), response.body())
    }
    /**
     * id([mediaId])가 일치하는 미디어를 검색합니다.
     * * (id가 일치하는 미디어는 1개(One)입니다.)
     * @author  두동근
     * @param   mediaId  미디어 id
     * @return  responseCode (expected : "200"), [Media]
     * @see     Media
     * @see     Pair
     */
    fun retrieveMedia(mediaId : String?) : Pair<String, Media?> {
        val response = RestClient.mediaService.retrieveMedia(mediaId).execute()

        return Pair(response.code().toString(), response.body())
    }
}