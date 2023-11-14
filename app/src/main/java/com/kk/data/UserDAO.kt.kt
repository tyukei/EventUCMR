package com.kk.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(user: List<User>)

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Int): User?

    // select password from User where email = :email
    @Query("SELECT password FROM User WHERE email = :email")
    fun getPasswordByEmail(email: String): String?

    // get next id
    @Query("SELECT MAX(id) FROM User")
    fun getNextId(): Int

    // get id by email
    @Query("SELECT id FROM User WHERE email = :email")
    fun getIdByEmail(email: String): Int?

    @Query("DELETE FROM Event")
    fun clearAllEvents()

}
