package com.example.kotlinlab11.task5

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

enum class LocationPermissionState {
    FINE, COARSE, NONE
}

@Composable
fun LocationScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var locationState by remember {
        mutableStateOf(getLocationPermissionState(context))
    }

    // ON_RESUME da holatni qayta tekshiramiz ("Only this time" - fonga o'tib qaytganda yangilanishi uchun)
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        locationState = getLocationPermissionState(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        locationState = getLocationPermissionState(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "5-topshiriq: Bir Martalik Joylashuv Ruxsati",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (locationState) {
                    LocationPermissionState.FINE -> Color(0xFFE8F5E9)
                    LocationPermissionState.COARSE -> Color(0xFFFFF3E0)
                    LocationPermissionState.NONE -> Color(0xFFFFEBEE)
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (locationState) {
                        LocationPermissionState.FINE -> "🎯 Aniq Joylashuv (Fine Location)"
                        LocationPermissionState.COARSE -> "📍 Taxminiy Joylashuv (Coarse Location)"
                        LocationPermissionState.NONE -> "🚫 Joylashuv Ruxsati Yo'q"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when (locationState) {
                        LocationPermissionState.FINE -> Color(0xFF2E7D32)
                        LocationPermissionState.COARSE -> Color(0xFFE65100)
                        LocationPermissionState.NONE -> Color(0xFFC62828)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }) {
                    Text(text = "Joylashuv Ruxsatini So'rash")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = "Kuzatuv: Dialogda 'Only this time' (Faqat bu safar) tanlansa va ilova fonga olib chiqilib qaytilsa, ON_RESUME chaqiruvi tufayli UI ruxsat yo'qligini ('NONE') avtomatik ko'rsatadi.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

private fun getLocationPermissionState(context: Context): LocationPermissionState {
    val fineGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return when {
        fineGranted -> LocationPermissionState.FINE
        coarseGranted -> LocationPermissionState.COARSE
        else -> LocationPermissionState.NONE
    }
}
