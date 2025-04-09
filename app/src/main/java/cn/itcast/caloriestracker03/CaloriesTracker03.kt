package cn.itcast.caloriestracker03

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class CaloriesTracker03 : Application() {
    companion object {
        @Volatile
        private var instance: CaloriesTracker03? = null

        // 双重检查锁单例模式[7,8](@ref)
        fun getAppContext(): Context {
            return instance?.applicationContext
                ?: throw IllegalStateException("Application not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}