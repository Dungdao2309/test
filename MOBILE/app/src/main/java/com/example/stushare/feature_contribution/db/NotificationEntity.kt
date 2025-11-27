package com.example.stushare.feature_contribution.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String, 
    val message: String,
    val timestamp: Long,
    val type: String,
    val isRead: Boolean = false
)