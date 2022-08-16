package com.learn.wp_rest.data.wp.media

/**
 * 미디어 data class
 * @author  두동근
 * @param   id                  미디어의 고유 번호(Media ID)
 * @param   date                미디어의 업로드 일자
 * @param   guid                [Guid] 클래스
 * @param   media_type          타입 (expected : "image")
 */
data class Media(val id : String?,
                 val date : String,
                 val guid : Guid,
                 val media_type : String)
/**
 * [Media.guid]
 * @author  두동근
 * @param   rendered    이미지의 HTTP URL
 */
data class Guid(val rendered : String)