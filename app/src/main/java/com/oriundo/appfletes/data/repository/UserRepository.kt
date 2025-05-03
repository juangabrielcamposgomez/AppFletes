package com.oriundo.appfletes.data.repository

import com.oriundo.appfletes.data.User
import com.oriundo.appfletes.data.daos.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    fun getUserByEmail(email: String): Flow<User?> {
        return userDao.getUserByEmail(email)
    }
}