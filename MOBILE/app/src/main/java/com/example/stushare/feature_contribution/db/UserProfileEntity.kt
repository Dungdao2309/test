package com.example.stushare.feature_contribution.db // <--- Đã sửa package

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val userId: String,
    val fullName: String,
    val email: String,
    val phone: String?,
    val dateOfBirth: String?,
    val gender: String?,
    val score: Int = 0
)