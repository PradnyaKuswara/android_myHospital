package com.example.ugd3_kelompok15.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val namaLengkap: String,
        val username: String,
        val Email: String,
        val nomorTelepon: String,
        val password: String
)
