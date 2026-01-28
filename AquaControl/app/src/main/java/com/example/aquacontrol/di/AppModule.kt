package com.example.aquacontrol.di

import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.repository.AuthRepository
import com.example.aquacontrol.data.repository.AuthRepositoryImpl
import com.example.aquacontrol.data.repository.MonitoringRepository
import com.example.aquacontrol.data.repository.MonitoringRepositoryImpl
import com.example.aquacontrol.domain.usecase.MonitoringUseCases
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

    @Provides
    @Singleton
    fun provideMonitoringRepository(firestore: FirebaseFirestore): MonitoringRepository =
        MonitoringRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideMonitoringUseCases(monitoringRepository: MonitoringRepository): MonitoringUseCases {
        return MonitoringUseCases(
            register = { monitoringRepository.registerMonitoringPoint(it) },
            getAll = { monitoringRepository.getAllMonitoringPoints() },
            update = { monitoringRepository.updateMonitoringPoint(it) },
            delete = { monitoringRepository.deleteMonitoringPoint(it) },
            getMonitoringPoints = monitoringRepository::getMonitoringPointsFlow,
            getSensorReadings = monitoringRepository::getSensorReadings
        )
    }

}