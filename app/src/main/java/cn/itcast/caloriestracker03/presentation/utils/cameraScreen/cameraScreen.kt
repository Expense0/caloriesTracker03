package cn.itcast.caloriestracker03.presentation.utils.cameraScreen

import android.Manifest
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cn.itcast.caloriestracker03.presentation.utils.cameraScreen.components.CameraPreview
import cn.itcast.caloriestracker03.presentation.utils.cameraScreen.components.HandleCameraPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    HandleCameraPermission(
        permissionState = cameraPermissionState,
        onPermissionGranted = {
            CameraPreview { uri ->
                capturedImageUri = uri
            }
        }
    )
}

