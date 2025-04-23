package com.hwang.kindergarden.ui.screens.place

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition

@Composable
fun PlaceMapScreen() {
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(37.5665, 126.9780), 15f) // 서울 좌표를 기본값으로 사용
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> 
            hasLocationPermission = granted
            if (granted) {
                // 권한을 얻자마자 현재 위치 즉시 요청
                getCurrentLocation(context) { location ->
                    userLocation = location
                    // 위치가 업데이트되면 카메라도 업데이트
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)
                }
            }
        }
    )

    // 앱 시작 시 권한 요청
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // 이미 권한이 있으면 바로 위치 요청
            getCurrentLocation(context) { location ->
                userLocation = location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)
            }
        }
    }

    if (hasLocationPermission) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = true
            )
        ) {
            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "현재 위치"
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }) {
                Text(text = "위치 권한 허용하기")
            }
        }
    }
}

// 현재 위치를 가져오는 함수 분리
private fun getCurrentLocation(context: android.content.Context, onLocationReceived: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    
    try {
        // 먼저 마지막으로 알려진 위치 확인 (빠른 응답)
        fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
            if (lastLocation != null) {
                onLocationReceived(LatLng(lastLocation.latitude, lastLocation.longitude))
            } else {
                // 마지막 위치가 없으면 최신 위치 한 번 요청 (더 정확하지만 느림)
                val cancellationToken = CancellationTokenSource()
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
                    .addOnSuccessListener { location ->
                        location?.let {
                            onLocationReceived(LatLng(it.latitude, it.longitude))
                        }
                    }
            }
        }
    } catch (e: SecurityException) {
        // 위치 권한이 없는 경우
    }
} 