package com.artworkspace.github.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.artworkspace.github.data.local.entity.User

interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user ORDER BY id ASC")
    suspend fun getAllUsers(): LiveData<List<User>>
}