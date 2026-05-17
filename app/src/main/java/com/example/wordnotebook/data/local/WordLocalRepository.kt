package com.example.wordnotebook.data.local

import kotlinx.coroutines.flow.Flow

class WordLocalRepository(
    private val dao: WordRecordDao
) {
    fun observeFavorites(): Flow<List<WordRecordEntity>> = dao.observeFavorites()

    fun observeRecentQueries(limit: Int = 20): Flow<List<WordRecordEntity>> =
        dao.observeRecentQueries(limit)

    suspend fun saveQuery(word: String, meaning: String, phonetic: String = "") {
        dao.saveOrUpdate(word, meaning, phonetic, System.currentTimeMillis())
    }

    suspend fun toggleFavorite(word: String, fallbackMeaning: String, fallbackPhonetic: String = ""): Boolean {
        return dao.toggleFavorite(word, fallbackMeaning, fallbackPhonetic, System.currentTimeMillis())
    }

    suspend fun isFavorite(word: String): Boolean = dao.getByWord(word)?.isFavorite == true
}
