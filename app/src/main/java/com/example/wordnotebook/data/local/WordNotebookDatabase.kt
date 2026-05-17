package com.example.wordnotebook.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WordRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WordNotebookDatabase : RoomDatabase() {
    abstract fun wordRecordDao(): WordRecordDao

    companion object {
        @Volatile
        private var INSTANCE: WordNotebookDatabase? = null

        fun getInstance(context: Context): WordNotebookDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WordNotebookDatabase::class.java,
                    "word_notebook.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
