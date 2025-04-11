package cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen

import android.content.Context
import android.net.Uri
import android.util.Log
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
import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo
import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.components.createImageFile
import cn.itcast.caloriestracker03.utils.HUNDRED_GRAMS
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
    ) {
        //相机提供者
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        //监听器
        cameraProviderFuture.addListener({
            //获取相机提供者实例
            cameraProvider = cameraProviderFuture.get()
            //获取相机选择器
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            //创建预览
            preview = Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }

            //创建图像捕获
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            //绑定相机到生命周期
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

    fun takePicture(context: Context, onImageCaptured: (MealFoodProduct) -> Unit) {
        //获取图像捕获实例
        imageCapture?.let { capture ->
            //创阿金照片文件
            val photoFile = createImageFile(context)
            //创建输出选项
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            //设置目标旋转
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            capture.targetRotation = windowManager.defaultDisplay.rotation

            //拍照并且保存图像
            capture.takePicture(
                outputOptions,
                Executors.newCachedThreadPool(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        viewModelScope.launch {
                            output.savedUri?.let { uri ->
                                // 使用 Log 打印 URI
                                Log.d("CameraViewModel", "Image captured: $uri")
                                val mealFoodProduct = handleImageUri(uri)
                                onImageCaptured(mealFoodProduct)
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
        bindCameraLifecycle(context, lifecycleOwner, previewView)
    }

    public override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }

    // 新方法，用于处理 URI 并返回 MealFoodProduct
    private suspend fun handleImageUri(uri: Uri): MealFoodProduct {
        // 在这里处理 URI，例如显示图像或导航到下一个屏幕
        Log.d("CameraViewModel", "Handling image URI: $uri")
        return foodProductRepository.getMealFoodProduct(uri.toString())
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