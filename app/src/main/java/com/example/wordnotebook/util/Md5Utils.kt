package com.example.wordnotebook.util

import java.security.MessageDigest

object Md5Utils {
    fun md5(text: String): String {
        val digest = MessageDigest.getInstance("MD5").digest(text.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }
}
