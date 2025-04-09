package cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.components

import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.CameraViewModel

@Composable
fun CameraPreview(
    viewModel: CameraViewModel,
    onImageCaptured: (Uri) -> Unit,
){
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    DisposableEffect(Unit) {
        viewModel.bindCameraLifecycle(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            onImageCaptured = onImageCaptured
        )
        onDispose {
            viewModel.onCleared()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            { previewView },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = { viewModel.takePicture(context, onImageCaptured) },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("拍照")
        }

        Button(
            onClick = {
                viewModel.switchCamera()
                viewModel.rebindCamera(context, lifecycleOwner, previewView)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(48.dp)
        ) {
            Text("切换")
        }

    }

}