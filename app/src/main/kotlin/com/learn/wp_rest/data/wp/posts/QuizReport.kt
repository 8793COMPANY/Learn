package com.learn.wp_rest.data.wp.posts

/**
 * [QuizReport] data class
 * @author  두동근
 * @param   id                          [QuizReport]의 고유 번호(Post ID)
 * @param   status                      [QuizReport]의 상태
 * @param   date                        [QuizReport]의 작성 일자
 * @param   author                      [QuizReport]를 작성한 회원
 * @param   categories                  [QuizReport] 카테고리
 * @param   title                       [QuizReportTitle] 클래스
 * @param   content                     [QuizReportContent] 클래스
 * @param   acf                         [QuizReportAcf] 클래스
 */
data class QuizReport(val id : String?,
                        val status : String?,
                        val date : String?,
                        val author : String?,
                        val categories : Array<String>,
                        val title : QuizReportTitle,
                        val content : QuizReportContent,
                        val acf : QuizReportAcf)
/**
 * [QuizReport.title]
 * @author  두동근
 * @param   rendered    [QuizReport] 이름
 */
data class QuizReportTitle(val rendered : String)
/**
 * [QuizReport.content]
 * @author  두동근
 * @param   rendered    [QuizReport] 소개
 */
data class QuizReportContent(val rendered : String)
/**
 * [QuizReport] ACF(Advanced Custom Fields)
 * @param   chapter             챕터 번호
 * @param   content             콘텐츠 번호
 * @param   lesson_name         레슨 이름
 * @param   answer_1             1번 답안
 * @param   answer_2             2번 답안
 * @param   answer_3             3번 답안
 * @param   answer_4             4번 답안
 * @param   answer_5             5번 답안
 * @author  두동근
 */
data class QuizReportAcf(val chapter : Int?,
                         val content : Int?,
                         val lesson_name : String?,
                         val answer_1 : Int?,
                         val answer_2 : Int?,
                         val answer_3 : Int?,
                         val answer_4 : Int?,
                         val answer_5 : Int?)