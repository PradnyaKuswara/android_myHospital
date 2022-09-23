package com.example.ugd3_kelompok15.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ugd3_kelompok15.RegisterActivity
import com.example.ugd3_kelompok15.ui.profile.FragmentProfile

@Database(
    entities = [User::class],
    version = 1
)

abstract class UserDB: RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object {
        @Volatile private var instance : UserDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDB::class.java,
            "user12345.db"
        ).build()
    }
}