package com.learn.wp_rest.data.wp.users

/**
 * 학생 data class
 * @author  두동근
 * @param   id              학생의 고유 번호(User ID)
 * @param   name            학생의 이름
 * @param   url             학생의 프로필 이미지 URL
 * @param   acf             [UserMeta] 클래스
 */
data class User(val id : String,
                val name : String,
                val url : String,
                val acf : List<UserMeta>)
/**
 * 학생 Meta data
 * @author  두동근
 * @param   id          id
 * @param   key         key
 * @param   value       value
 */
data class UserMeta(val id : Int?,
                     val key : String?,
                     val value : Any?)