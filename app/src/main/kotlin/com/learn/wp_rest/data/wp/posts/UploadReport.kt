package com.learn.wp_rest.data.wp.posts

/**
 * [UploadReport] data class
 * @author  두동근
 * @param   id                          [UploadReport]의 고유 번호(Post ID)
 * @param   status                      [UploadReport]의 상태
 * @param   date                        [UploadReport]의 작성 일자
 * @param   author                      [UploadReport]를 작성한 회원
 * @param   categories                  [UploadReport] 카테고리
 * @param   title                       [UploadReportTitle] 클래스
 * @param   content                     [UploadReportContent] 클래스
 * @param   acf                         [UploadReportAcf] 클래스
 */
data class UploadReport(val id : String?,
                        val status : String?,
                        val date : String?,
                        val author : String?,
                        val categories : Array<String>,
                        val title : UploadReportTitle,
                        val content : UploadReportContent,
                        val acf : UploadReportAcf)
/**
 * [UploadReport.title]
 * @author  두동근
 * @param   rendered    [UploadReport] 이름
 */
data class UploadReportTitle(val rendered : String)
/**
 * [UploadReport.content]
 * @author  두동근
 * @param   rendered    [UploadReport] 소개
 */
data class UploadReportContent(val rendered : String)
/**
 * [UploadReport] ACF(Advanced Custom Fields)
 * @param   chapter             챕터 번호
 * @param   content             콘텐츠 번호
 * @param   lesson_name         레슨 이름
 * @param   circuit_img         회로도 사진 URL
 * @param   block_img           블록 사진 URL
 * @author  두동근
 */
data class UploadReportAcf(val chapter : Int?,
                           val content : Int?,
                           val lesson_name : String?,
                           val circuit_img : String?,
                           val block_img : String?)