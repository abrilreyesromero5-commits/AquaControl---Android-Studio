package com.example.aquacontrol.data.repository

import com.example.aquacontrol.data.model.MonitoringPoint
import com.example.aquacontrol.data.model.SensorReading
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MonitoringPointRepositoryImpl(
    private val firestore: FirebaseFirestore
) : MonitoringPointRepository {

    private val collection = firestore.collection("monitoring_points")

    override suspend fun addMonitoringPoint(point: MonitoringPoint) {
        val docRef = if(point.id.isEmpty()) collection.document() else collection.document(point.id)
        val data = point.copy(registrationDate = Timestamp.now())
        docRef.set(data).await()
    }

    override suspend fun updateMonitoringPoint(point: MonitoringPoint) {
        collection.document(point.id).set(point).await()
    }

    override suspend fun deactivateMonitoringPoint(pointId: String) {
        collection.document(pointId).update("active", false).await()
    }

    override fun getAllMonitoringPoints(): Flow<List<MonitoringPoint>> = callbackFlow {
        val subscription = collection
            .whereEqualTo("active", true)  // ✅ Filtro solo activos
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val points = snapshot?.documents?.mapNotNull {
                    it.toObject(MonitoringPoint::class.java)?.copy(id = it.id)
                } ?: emptyList()
                trySend(points)
            }
        awaitClose { subscription.remove() }
    }

    override fun getMonitoringPointByIdFlow(pointId: String): Flow<MonitoringPoint?> = callbackFlow {
        val subscription = collection.document(pointId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val point = snapshot?.toObject(MonitoringPoint::class.java)?.copy(id = snapshot.id)
            trySend(point).isSuccess
        }
        awaitClose { subscription.remove() }
    }

    override fun getSensorReadingsByPointId(pointId: String): Flow<List<SensorReading>> = callbackFlow {
        val subscription = collection.document(pointId)
            .collection("readings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val readings = snapshot?.documents?.mapNotNull {
                    it.toObject(SensorReading::class.java)
                } ?: emptyList()
                trySend(readings).isSuccess
            }
        awaitClose { subscription.remove() }
    }
}
