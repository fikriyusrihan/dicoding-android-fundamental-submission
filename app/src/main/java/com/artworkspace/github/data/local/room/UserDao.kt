package com.artworkspace.github.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.artworkspace.github.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<UserEntity>>
}