package com.example.ugd3_kelompok15.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ugd3_kelompok15.ui.janjitemu.JanjiTemuActivity

@Database(
    entities = [JanjiTemu::class],
    version = 1
)

abstract class JanjiTemuDB: RoomDatabase() {
    abstract fun janjiTemuDao() : JanjiTemuDao

    companion object {
        @Volatile private var instance : JanjiTemuDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            JanjiTemuDB::class.java,
            "janjitemu12345.db"
        ).build()
    }
}