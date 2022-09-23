package com.example.ugd3_kelompok15.room

import androidx.room.*

@Dao
interface JanjiTemuDao {
    @Insert
    suspend fun addJanjiTemu(janjitemu: JanjiTemu)
    @Update
    suspend fun updateJanjiTemu(janjitemu: JanjiTemu)
    @Delete
    suspend fun deleteJanjiTemu(janjitemu: JanjiTemu)
    @Query("SELECT * FROM janjitemu")
    suspend fun getJanjis() : List<JanjiTemu>
    @Query("SELECT * FROM janjitemu WHERE id =:janji_id")
    suspend fun getJanji(janji_id: Int) : List<JanjiTemu>
}