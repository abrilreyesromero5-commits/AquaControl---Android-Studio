package com.example.aquacontrol.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aquacontrol.data.model.MonitoringPoint
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapComponent(
    points: List<MonitoringPoint>,
    modifier: Modifier = Modifier,
    onPointSelected: (MonitoringPoint) -> Unit
) {
    // Centro aproximado de México
    val defaultLocation = LatLng(23.6345, -102.5528)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 5f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        points.forEach { point ->
            val markerPosition = LatLng(point.latitude, point.longitude)
            Marker(
                state = MarkerState(position = markerPosition),
                title = point.locality,
                snippet = "${point.state} - ${point.municipality}",
                onClick = {
                    onPointSelected(point)
                    true // Consume el evento para que el marcador muestre info
                }
            )
        }
    }
}
