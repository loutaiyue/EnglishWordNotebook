package com.example.wordnotebook.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface BaiduTranslateApi {
    @GET("api/trans/vip/translate")
    suspend fun translate(
        @Query("q") q: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("appid") appId: String,
        @Query("salt") salt: String,
        @Query("sign") sign: String
    ): BaiduTranslateResponse
}

data class BaiduTranslateResponse(
    val from: String? = null,
    val to: String? = null,
    val trans_result: List<TranslateResult>? = null,
    val error_code: String? = null,
    val error_msg: String? = null
)

data class TranslateResult(
    val src: String,
    val dst: String
)
