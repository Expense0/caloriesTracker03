package cn.itcast.caloriestracker03.presentation.navigation.ext


import android.util.Log
import java.util.concurrent.ConcurrentHashMap

object NavResultContainer {
    private val callbacks = ConcurrentHashMap<String, Any>()
    private val lock = Any()

    fun <T> set(key: String, value: T) {
        synchronized(lock) {
            callbacks[key] = value as Any
            Log.d(TAG, "✅ 存储键: $key | 类型: ${value::class.java.simpleName}")
        }
    }

    @Throws(IllegalStateException::class)
    fun <T> get(key: String, type: Class<T>): T {
        return synchronized(lock) {
            val value = callbacks.remove(key).also {
                Log.d(TAG, "🔍 查询键: $key | 存在: ${it != null}")
            }

            when {
                value == null -> {
                    Log.e(TAG, "❌ 键不存在: $key")
                    throw IllegalStateException("无数据对应键: $key")
                }
                !type.isInstance(value) -> {
                    Log.e(TAG, "⚠️ 类型不匹配 | 预期: ${type.simpleName} | 实际: ${value::class.java.simpleName}")
                    throw IllegalStateException("类型不匹配")
                }
                else -> type.cast(value).also {
                    Log.d(TAG, "✅ 成功获取键: $key")
                }
            }
        }
    }

    fun clear() {
        synchronized(lock) {
            callbacks.clear()
            Log.i(TAG, "♻️ 已清空所有存储数据")
        }
    }
}