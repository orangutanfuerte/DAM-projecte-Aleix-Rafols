package com.example.travellikeasigma.data.repository

import com.example.travellikeasigma.data.room.UserDao
import com.example.travellikeasigma.data.room.UserEntity
import com.example.travellikeasigma.domain.User
import com.example.travellikeasigma.domain.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun getUserById(uid: String): User? =
        userDao.getUserById(uid)?.toDomain()

    private fun User.toEntity() = UserEntity(
        userId = uid,
        email = email,
        username = username,
        dateOfBirth = dateOfBirth,
        language = preferences.language,
        theme = preferences.theme,
        notificationsEnabled = preferences.notificationsEnabled
    )

    private fun UserEntity.toDomain() = User(
        uid = userId,
        name = username,
        email = email,
        username = username,
        dateOfBirth = dateOfBirth,
        preferences = com.example.travellikeasigma.domain.Preferences(
            language = language,
            theme = theme,
            notificationsEnabled = notificationsEnabled
        )
    )
}
