package com.example.aquacontrol.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.aquacontrol.data.model.User

private val DrawerBackground = Color(0xFF0A74DA) // Azul profundo
private val DrawerTextColor = Color.White
private val DrawerIconColor = Color.White
private val AccentColor = Color(0xFF4ECDC4) // Verde agua

@Composable
fun NavigationDrawer(
    role: User.UserRole,
    selectedItem: String,
    onItemClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showMonitoringSubItems by remember { mutableStateOf(false) }

    // 👇 Aquí se controla el ancho exacto del Drawer para evitar fondo sobrante
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(DrawerBackground)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = if (role == User.UserRole.ADMIN) "Menú Administrador" else "Menú",
                style = MaterialTheme.typography.titleMedium,
                color = DrawerTextColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (role == User.UserRole.ADMIN) {
                DrawerExpandableItem(
                    title = "Puntos de Monitoreo",
                    icon = Icons.Default.LocationOn,
                    expanded = showMonitoringSubItems,
                    onClick = { showMonitoringSubItems = !showMonitoringSubItems }
                )

                if (showMonitoringSubItems) {
                    SubDrawerItem("Registrar Punto", "registrar_punto", selectedItem, onItemClick)
                    SubDrawerItem("Gestionar Puntos", "gestion_puntos", selectedItem, onItemClick)
                }


            }

            DrawerItem("Visualizar Datos", "visualizar_datos", Icons.Default.BarChart, selectedItem, onItemClick)
            //DrawerItem("Historial Alertas", "historial_alertas", Icons.Default.Notifications, selectedItem, onItemClick)
            DrawerItem("Reportes", "reportes", Icons.Default.FileDownload, selectedItem, onItemClick)
            DrawerItem("Analisis", "visualizar_predicciones", Icons.Default.ShowChart, selectedItem, onItemClick)
        }

        DrawerItem("Cerrar Sesión", "logout", Icons.Default.ExitToApp, "", onClick = { onLogout() })
    }
}

@Composable
fun DrawerExpandableItem(
    title: String,
    icon: ImageVector,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = DrawerIconColor)
        Spacer(Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, color = DrawerTextColor)
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = null,
            tint = DrawerIconColor
        )
    }
}

@Composable
fun SubDrawerItem(
    text: String,
    key: String,
    selectedItem: String,
    onClick: (String) -> Unit
) {
    val isSelected = key == selectedItem
    val backgroundColor = if (isSelected) AccentColor.copy(alpha = 0.15f) else Color.Transparent
    val textColor = if (isSelected) AccentColor else DrawerTextColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick(key) }
            .padding(start = 32.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Circle, contentDescription = null, tint = textColor, modifier = Modifier.size(8.dp))
        Spacer(Modifier.width(12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = textColor)
    }
}

@Composable
fun DrawerItem(
    text: String,
    key: String,
    icon: ImageVector,
    selectedItem: String,
    onClick: (String) -> Unit
) {
    val isSelected = key == selectedItem
    val backgroundColor = if (isSelected) AccentColor.copy(alpha = 0.15f) else Color.Transparent
    val textColor = if (isSelected) AccentColor else DrawerTextColor
    val iconTint = if (isSelected) AccentColor else DrawerIconColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick(key) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = iconTint)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = textColor)
    }
}


