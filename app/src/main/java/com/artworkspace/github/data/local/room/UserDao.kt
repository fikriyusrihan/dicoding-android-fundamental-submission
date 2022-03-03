package com.artworkspace.github.data.local.room

import androidx.room.*
import com.artworkspace.github.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND is_favorite = 1)")
    fun isFavoriteUser(id: String): Flow<Boolean>
}