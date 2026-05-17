package com.example.wordnotebook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WordRecordDao {
    @Query("SELECT * FROM word_records WHERE word = :word LIMIT 1")
    suspend fun getByWord(word: String): WordRecordEntity?

    @Query("SELECT * FROM word_records WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun observeFavorites(): Flow<List<WordRecordEntity>>

    @Query("SELECT * FROM word_records ORDER BY lastQueriedAt DESC LIMIT :limit")
    fun observeRecentQueries(limit: Int): Flow<List<WordRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: WordRecordEntity)

    @Transaction
    suspend fun saveOrUpdate(word: String, meaning: String, phonetic: String, now: Long) {
        val existing = getByWord(word)
        val updated = if (existing == null) {
            WordRecordEntity(
                word = word,
                meaning = meaning,
                phonetic = phonetic,
                isFavorite = false,
                queryCount = 1,
                lastQueriedAt = now,
                updatedAt = now
            )
        } else {
            existing.copy(
                meaning = meaning,
                phonetic = phonetic,
                queryCount = existing.queryCount + 1,
                lastQueriedAt = now,
                updatedAt = now
            )
        }
        upsert(updated)
    }

    @Transaction
    suspend fun toggleFavorite(word: String, fallbackMeaning: String, fallbackPhonetic: String, now: Long): Boolean {
        val existing = getByWord(word)
        val updated = if (existing == null) {
            WordRecordEntity(
                word = word,
                meaning = fallbackMeaning,
                phonetic = fallbackPhonetic,
                isFavorite = true,
                queryCount = 0,
                lastQueriedAt = now,
                updatedAt = now
            )
        } else {
            existing.copy(
                isFavorite = !existing.isFavorite,
                meaning = fallbackMeaning.ifBlank { existing.meaning },
                phonetic = fallbackPhonetic.ifBlank { existing.phonetic },
                updatedAt = now
            )
        }
        upsert(updated)
        return updated.isFavorite
    }
}
