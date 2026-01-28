package com.example.aquacontrol.data.repository

import com.example.aquacontrol.data.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun register(email: String, password: String): AuthResult {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("UID vacío")

        // Crear documento en Firestore como USER
        val user = User(uid = uid, email = email, role = User.UserRole.USER)
        firestore.collection("users").document(uid).set(user).await()

        return result
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun getUserRole(uid: String): User.UserRole {
        val doc = firestore.collection("users").document(uid).get().await()
        return doc.toObject(User::class.java)?.role ?: User.UserRole.USER
    }

    override fun logout() {
        auth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

}