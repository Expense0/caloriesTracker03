package cn.itcast.caloriestracker03.presentation.utils.cameraScreen.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleCameraPermission(
    permissionState: PermissionState,
    onPermissionGranted: @Composable () -> Unit
) {
    when {
        permissionState.status.isGranted -> {
            onPermissionGranted()
        }
        !permissionState.status.shouldShowRationale -> {
            ShowPermissionDeniedUI(permissionState)
        }
        else -> {
            ShowPermissionRequestUI(permissionState)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ShowPermissionDeniedUI(permissionState: PermissionState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "相机权限被拒绝，请前往设置开启",
            modifier = Modifier.align(Alignment.Center)
        )
        val context = LocalContext.current
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(64.dp)
        ) {
            Text("前往设置")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ShowPermissionRequestUI(permissionState: PermissionState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "需要相机权限",
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = { permissionState.launchPermissionRequest() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(64.dp)
        ) {
            Text("请求权限")
        }
    }
}