package cn.itcast.caloriestracker03.presentation.screens.onboarding.pager_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import cn.itcast.caloriestracker03.R


@Composable
fun WeightPagerScreen(
    weight: Float,
    onNextClicked: (weight: Float) -> Unit,
    onPreviousClicked: () -> Unit,
) {

    val enteredWeight = rememberSaveable() {
        mutableStateOf(weight.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { onPreviousClicked() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = null, //✨ M3强制要求显式声明
                    tint = MaterialTheme.colorScheme.primary //✨ 主题色应用
                )
            }
            Text(
                text = stringResource(R.string.title_weight_kg),
                style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center), //✨ h4→headlineMedium
                modifier = Modifier.align(Alignment.TopCenter),
                color = MaterialTheme.colorScheme.onSurface //✨ 文本颜色适配
            )
        }
        // 修改2：使用M3的TextField
        TextField(
            value = enteredWeight.value,
            onValueChange = { enteredWeight.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), //✨ 简化配置
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, //✨ 容器颜色
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            shape = MaterialTheme.shapes.medium //✨ 形状主题应用
        )
        // 修改3：更新浮动按钮实现
        ExtendedFloatingActionButton( //✨ 替换为M3扩展按钮
            onClick = {
                if (enteredWeight.value.isNotBlank()) {
                    onNextClicked(enteredWeight.value.toFloat())
                } else {
                    onNextClicked(0.0f)
                }
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer, //✨ 颜色方案更新
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = stringResource(R.string.desc_navigate_to_previous)
            )
        }
    }

}