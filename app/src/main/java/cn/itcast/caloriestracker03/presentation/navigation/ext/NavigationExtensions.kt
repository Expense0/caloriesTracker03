package cn.itcast.caloriestracker03.presentation.navigation.ext

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator


//Implementation by https://stackoverflow.com/a/75472964/20298826

typealias NavResultCallback<T> = (T) -> Unit

public const val TAG = "NavExtensions"  // 统一日志标签
private const val NAV_RESULT_PREFIX = "NavResult_" // 键前缀避免冲突

// ======================== 核心方法改造 ========================
/**
 * 为指定路由设置导航结果回调
 * @param route 目标路由（必须唯一）
 */
fun <T> NavController.setNavResultCallback(route: String, callback: NavResultCallback<T>) {
    val key = "$NAV_RESULT_PREFIX$route"
    Log.i(TAG, "📌 注册回调 | 路由: $route")
    NavResultContainer.set(key, callback)
}


/**
 * 获取并移除与路由关联的导航结果回调
 * @return 可能为 null（如果回调不存在或类型不匹配）
 */
@Suppress("UNCHECKED_CAST")
fun <T> NavController.getNavResultCallback(route: String): NavResultCallback<T>? {
    val key = "$NAV_RESULT_PREFIX$route"
    return try {
        Log.d(TAG, "🎯 获取路由成功 | 路由: $route")
        NavResultContainer.get(key, NavResultCallback::class.java) as? NavResultCallback<T>
    } catch (e: IllegalStateException) {
        Log.w(TAG, "⚠️ 获取回调失败: ${e.message} | 路由: $route")
        null
    }
}

/**
 * 弹出返回栈并传递结果给上一个页面
 * @param result 要传递的结果数据
 */
fun <T> NavController.popBackStackWithResult(result: T) {
    // 关键修改：获取返回后的目标页面路由（即发起导航的源页面）
    val sourceRoute = currentBackStackEntry?.destination?.route ?: run {
        Log.e(TAG, "❌ 无法获取当前页面路由")
        return
    }

    Log.d(TAG, "🚀 准备返回结果到路由: $sourceRoute | 类型: ${result?.javaClass?.simpleName}")

    getNavResultCallback<T>(sourceRoute)?.invoke(result) ?: run {
        Log.e(TAG, "⚠️ 未找到回调 | 路由: $sourceRoute")
    }

    Log.d(TAG, "↩️ 执行返回操作")
    popBackStack()
}

/**
 * 导航到目标路由并等待结果回调
 * @param route 目标路由
 * @param navResultCallback 结果回调（类型 T 需与返回时类型一致）
 */
fun <T> NavController.navigateForResult(
    route: String,
    navResultCallback: NavResultCallback<T>,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    Log.i(TAG, "🛫 启动导航到: $route")
    setNavResultCallback(route, navResultCallback)
    navigate(route, navOptions, navigatorExtras)
}

