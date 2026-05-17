package com.example.wordnotebook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_records")
data class WordRecordEntity(
    @PrimaryKey val word: String,
    val meaning: String,
    val phonetic: String = "",
    val isFavorite: Boolean = false,
    val queryCount: Int = 0,
    val lastQueriedAt: Long = 0L,
    val updatedAt: Long = 0L
)
