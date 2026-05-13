package com.example.travellikeasigma.data.repository

import com.example.travellikeasigma.domain.AuthRepository
import com.example.travellikeasigma.domain.AuthUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun getCurrentUser(): AuthUser? =
        firebaseAuth.currentUser?.let { u ->
            AuthUser(
                uid = u.uid,
                email = u.email ?: "",
                isEmailVerified = u.isEmailVerified,
                displayName = u.displayName
            )
        }

    override suspend fun login(email: String, password: String): AuthUser {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = result.user!!
        user.reload().await()
        val fresh = firebaseAuth.currentUser!!
        return AuthUser(
            uid = fresh.uid,
            email = fresh.email ?: "",
            isEmailVerified = fresh.isEmailVerified,
            displayName = fresh.displayName
        )
    }

    override suspend fun register(email: String, password: String, displayName: String): AuthUser {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user!!
        user.updateProfile(userProfileChangeRequest { this.displayName = displayName }).await()
        user.sendEmailVerification().await()
        return AuthUser(
            uid = user.uid,
            email = user.email ?: "",
            isEmailVerified = user.isEmailVerified,
            displayName = displayName
        )
    }

    override suspend fun reloadCurrentUser() {
        firebaseAuth.currentUser?.reload()?.await()
    }

    override suspend fun sendEmailVerification() {
        firebaseAuth.currentUser?.sendEmailVerification()?.await()
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
