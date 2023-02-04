package com.ted.roomwordssample.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {

        private fun buildDatabase(context: Context) : WordRoomDatabase {
            return Room.databaseBuilder(context, WordRoomDatabase::class.java, "word.db")
                .build()
        }

        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context): WordRoomDatabase {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = buildDatabase(context)
                }
            }

            return INSTANCE!!
        }
    }

}