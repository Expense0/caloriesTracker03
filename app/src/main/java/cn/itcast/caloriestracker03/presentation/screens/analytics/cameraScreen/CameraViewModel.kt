package cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen

import android.content.Context
import android.net.Uri
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.itcast.caloriestracker03.data.repositoryImpl.FoodProductRepositoryImpl
import cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.components.createImageFile
import cn.itcast.caloriestracker03.presentation.screens.analytics.search_product.SearchProductViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class CameraViewModel(
    private val foodProductRepository: FoodProductRepositoryImpl,
) : ViewModel() {
    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    fun bindCameraLifecycle(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onImageCaptured: (Uri) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            preview = Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun takePicture(context: Context, onImageCaptured: (Uri) -> Unit) {
        imageCapture?.let { capture ->
            val photoFile = createImageFile(context)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            capture.targetRotation = windowManager.defaultDisplay.rotation

            capture.takePicture(
                outputOptions,
                Executors.newCachedThreadPool(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        viewModelScope.launch {
                            output.savedUri?.let { uri ->
                                onImageCaptured(uri)
                            }
                        }
                    }

                    override fun onError(exc: ImageCaptureException) {
                        exc.printStackTrace()
                    }
                }
            )
        }
    }

    fun switchCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
    }

    fun rebindCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        cameraProvider?.unbindAll()
        bindCameraLifecycle(context, lifecycleOwner, previewView) {
        /* Ignore callback here */
        }
    }

    public override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }
}


class CameraViewModelFactory(
    private val foodProductRepository: FoodProductRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CameraViewModel(foodProductRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}