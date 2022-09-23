package com.example.ugd3_kelompok15.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class JanjiTemu (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val rumahSakit: String,
    val tanggal: String,
    val dokter: String,
    val keluhan: String
    )
