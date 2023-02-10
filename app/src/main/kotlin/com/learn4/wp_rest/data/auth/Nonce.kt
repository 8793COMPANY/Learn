package com.learn.wp_rest.data.auth

/**
 * Nonce data class
 * @author  두동근
 * @param   nonce    WP용 암호화 임시값(Nonce)
 */
data class Nonce(val nonce : String)