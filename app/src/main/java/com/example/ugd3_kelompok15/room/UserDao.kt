package com.example.ugd3_kelompok15.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)
    @Update
    fun updateUser(user: User)
    @Delete
    fun deleteUser(user: User)
    @Query("SELECT * FROM user")
    fun getUsers() : List<User>
    @Query("SELECT * FROM user WHERE id =:user_id")
    fun getUser(user_id: Int) : User
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun checkUser(username: String, password: String): User?
}