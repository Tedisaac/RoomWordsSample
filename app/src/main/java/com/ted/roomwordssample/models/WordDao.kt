package com.ted.roomwordssample.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDao {

    @Insert
    suspend fun insert(word: Word)

    @Query("DELETE FROM words")
    suspend fun deleteAll()

    @Query("DELETE FROM words WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("SELECT * FROM words ORDER BY word ASC")
    fun getAllWords() : LiveData<List<Word>>
}