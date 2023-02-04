package com.ted.roomwordssample.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey (autoGenerate = true) var id: Int = 0,
    @ColumnInfo (name = "word") var word: String
)