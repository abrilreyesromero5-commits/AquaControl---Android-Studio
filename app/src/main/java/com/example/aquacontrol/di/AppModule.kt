package com.example.aquacontrol.di

import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.repository.AuthRepository
import com.example.aquacontrol.data.repository.AuthRepositoryImpl
import com.example.aquacontrol.data.repository.MonitoringPointRepository
import com.example.aquacontrol.data.repository.MonitoringPointRepositoryImpl
import com.example.aquacontrol.data.repository.ReportRepository
import com.example.aquacontrol.data.repository.ReportRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(auth, firestore)

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val monitoringPointRepository: MonitoringPointRepository by lazy {
        MonitoringPointRepositoryImpl(firestoreInstance)
    }

    @Provides
    @Singleton
    fun provideMonitoringPointRepository(
        firestore: FirebaseFirestore
    ): MonitoringPointRepository = MonitoringPointRepositoryImpl(firestore)

    @Provides
    fun provideReportRepository(firestore: FirebaseFirestore): ReportRepository =
        ReportRepositoryImpl(firestore)

}