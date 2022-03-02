package com.artworkspace.github.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artworkspace.github.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "note_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}