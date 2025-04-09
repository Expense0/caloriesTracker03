package cn.itcast.caloriestracker03.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 删除旧版 Material2 的 darkColors/lightColors，改用 Material3 的 ColorScheme
private val DarkColorScheme = darkColorScheme(
    primary = White,            // 主色调（浅色模式为深蓝，深色模式为白）
    onPrimary = DarkBlue,       // 主色上的内容色（反色）
    primaryContainer = Gray,    // 替代旧版 primaryVariant
    surface = Gray,             // 容器表面色（卡片、对话框等）
    onSurface = White,          // 表面上的内容色
    background = DarkBlue,      // 背景色（深色模式为深蓝）
    onBackground = White,       // 背景上的内容色
    secondary = Purple,         // 次色调
    secondaryContainer = Gray,  // 替代旧版 secondaryVariant
    onSecondary = White         // 次色上的内容色
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue,
    onPrimary = White,
    primaryContainer = GrayLight,
    surface = WhiteDark,
    onSurface = DarkBlue,
    background = White,
    onBackground = DarkBlue,
    secondary = Purple,
    secondaryContainer = Gray,
    onSecondary = White
)

@Composable
fun CaloriesTracker03Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // 添加动态颜色开关
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, // 使用动态或静态方案
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}