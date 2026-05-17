package com.example.wordnotebook.data.remote

import com.example.wordnotebook.BuildConfig
import com.example.wordnotebook.util.Md5Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaiduTranslateRepository(
    private val api: BaiduTranslateApi = Retrofit.Builder()
        .baseUrl("https://fanyi-api.baidu.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BaiduTranslateApi::class.java),
    private val appId: String = BuildConfig.BAIDU_APP_ID,
    private val apiKey: String = BuildConfig.BAIDU_API_KEY
) {
    suspend fun translateEnToZh(word: String): Result<String> = runCatching {
        val trimmedWord = word.trim()
        require(trimmedWord.isNotEmpty()) { "请输入英文单词" }
        require(appId.isNotBlank() && apiKey.isNotBlank()) {
            "请先在 BuildConfig.java 配置 BAIDU_APP_ID 和 BAIDU_API_KEY"
        }

        val salt = System.currentTimeMillis().toString()
        val sign = Md5Utils.md5("$appId$trimmedWord$salt$apiKey")

        val response = api.translate(
            q = trimmedWord,
            from = "en",
            to = "zh",
            appId = appId,
            salt = salt,
            sign = sign
        )

        if (!response.error_code.isNullOrBlank()) {
            error("百度翻译错误 ${response.error_code}: ${response.error_msg ?: "未知错误"}")
        }

        response.trans_result?.firstOrNull()?.dst ?: error("未返回翻译结果")
    }
}
