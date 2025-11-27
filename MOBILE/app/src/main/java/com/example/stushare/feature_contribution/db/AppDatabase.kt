// File: app/src/main/java/com/example/stushare/feature_contribution/db/AppDatabase.kt

package com.example.stushare.feature_contribution.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Import các Entity
import com.example.stushare.feature_contribution.db.UserProfileEntity
import com.example.stushare.feature_contribution.db.SavedDocumentEntity
import com.example.stushare.feature_contribution.db.NotificationEntity
import com.example.stushare.feature_contribution.db.UserDao
import com.example.stushare.feature_contribution.db.SavedDocumentDao
import com.example.stushare.feature_contribution.db.NotificationDao

@Database(
    entities = [
        UserProfileEntity::class,
        SavedDocumentEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun savedDocumentDao(): SavedDocumentDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stushare_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}