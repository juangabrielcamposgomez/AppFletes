package com.oriundo.appfletes.data.daos



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.oriundo.appfletes.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<User?>
}