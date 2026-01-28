package com.example.aquacontrol.domain.usecase

import com.example.aquacontrol.data.repository.AuthRepository

class AuthUseCases(private val repository: AuthRepository) {
    suspend fun register(email: String, password: String) = repository.register(email, password)
    suspend fun login(email: String, password: String) = repository.login(email, password)
    suspend fun getUserRole(uid: String) = repository.getUserRole(uid)
    fun logout() = repository.logout()
    fun isUserLoggedIn() = repository.isUserLoggedIn()
}