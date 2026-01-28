package com.example.aquacontrol.data.repository

import android.util.Log
import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class MonitoringRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MonitoringRepository {

    private val collection = firestore.collection("monitoring_points")

    override suspend fun registerMonitoringPoint(point: MonitoringPoint): Result<Unit> = try {
        val doc = collection.document()
        collection.document(doc.id).set(point.copy(id = doc.id)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllMonitoringPoints(): Result<List<MonitoringPoint>> = try {
        val snapshot = collection.get().await()
        val points = snapshot.toObjects(MonitoringPoint::class.java)
        Result.success(points)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateMonitoringPoint(point: MonitoringPoint): Result<Unit> =
        try {
            collection.document(point.id).set(point).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun deleteMonitoringPoint(id: String): Result<Unit> =
        try {
            collection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getSensorReadings(pointId: String): Flow<List<SensorReading>> = callbackFlow {
        val subscription = firestore.collection("monitoring_points")
            .document(pointId)
            .collection("readings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(100) // Limitar a últimas 100 lecturas
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val readings = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(SensorReading::class.java)
                } ?: emptyList()
                trySend(readings)
            }
        awaitClose { subscription.remove() }
    }
    override fun getMonitoringPointsFlow(): Flow<List<MonitoringPoint>> = callbackFlow {
        val subscription = firestore.collection("monitoring_points")
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val points = snapshot?.toObjects(MonitoringPoint::class.java) ?: emptyList()
                Log.d("MonitoringRepo", "Puntos recibidos: ${points.size}")
                trySend(points)
            }
        awaitClose { subscription.remove() }
    }
}
