package cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.components

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    // 创建应用私有目录
    val storageDir = context.getDir("my_app_photos", Context.MODE_PRIVATE)
    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }
    val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    // 使用 Log.d 打印文件路径
    Log.d("CameraViewModel", "Saved image to: ${file.absolutePath}")
    return file
}