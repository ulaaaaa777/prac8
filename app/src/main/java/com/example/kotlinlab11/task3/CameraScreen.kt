package com.example.kotlinlab11.task3

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity

    var isGranted by remember { mutableStateOf(checkCameraPermission(context)) }
    var shouldShowRationale by remember { mutableStateOf(false) }
    var requestCount by remember { mutableIntStateOf(0) }

    // ON_RESUME: Sozlamalardan qaytganda yoki fonga o'tib qaytganda holatni yangilaymiz
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        isGranted = checkCameraPermission(context)
        activity?.let {
            shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isGranted = granted
        requestCount++
        activity?.let {
            shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "3-topshiriq: Kamera Ruxsati (3 holat)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            // SDK < 23 bo'lsa, o'rnatishda avtomatik ruxsat berilgan
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📷 Kamera tayyor (API < 23)",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Android 6.0 dan past versiyalarda ruxsat o'rnatish vaqtida berilgan.")
                    }
                }
            }

            // 1-BRANCH: Ruxsat berilgan
            isGranted -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📷 Kamera Ruxsati Berilgan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Kamera funksiyasidan foydalanishingiz mumkin.",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 2-BRANCH: Bir marta rad etilgan (Rationale ko'rsatiladi)
            shouldShowRationale -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚠️ Kamera Nima Uchun Kerak?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ilova rasmga olish funksiyasini bajarishi uchun kamera ruxsati shart. Iltimos, ruxsat bering.",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                            Text(text = "Ruxsat So'rash")
                        }
                    }
                }
            }

            // 3-BRANCH: Hali so'ralmagan yoki Ikki marta rad etilgan (Permanently denied)
            else -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "❌ Kamera Ruxsati Yo'q",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (requestCount >= 2 || (requestCount > 0 && !shouldShowRationale)) {
                            // Ikki marta rad etilgan holat
                            Text(
                                text = "Ruxsat bir necha marta rad etilgan. Funksiyadan foydalanish uchun Sozlamalardan kiring va ruxsatni qo'lda yoqing.",
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                openAppSettings(context)
                            }) {
                                Text(text = "Sozlamalarni Ochish")
                            }
                        } else {
                            // Hali so'ralmagan holat
                            Text(
                                text = "Rasmga olish uchun kamera ruxsatini berishingiz kerak.",
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                                Text(text = "Kamera Ruxsatini So'rash")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

private fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}
