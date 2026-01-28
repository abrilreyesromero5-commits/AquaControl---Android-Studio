package com.example.aquacontrol.data.repository

import com.example.aquacontrol.data.model.User
import com.google.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun register(email: String, password: String): AuthResult
    suspend fun login(email: String, password: String): AuthResult
    suspend fun getUserRole(uid: String): User.UserRole
    fun logout()
    fun isUserLoggedIn(): Boolean
}