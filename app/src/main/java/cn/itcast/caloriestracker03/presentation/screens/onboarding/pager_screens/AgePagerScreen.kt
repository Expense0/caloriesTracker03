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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.R


@Composable
fun AgePagerScreen(
    age: Int,
    onNextClicked: (age: Int) -> Unit,
    onPreviousClicked: () -> Unit,
) {

    val enteredAge = rememberSaveable() {
        mutableStateOf(age.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onPreviousClicked() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = null, // 🗳️ M3要求必须有内容描述
                    tint = MaterialTheme.colorScheme.primary // 🗳️ 使用主题色
                )
            }
            Text(
                text = stringResource(R.string.title_age),
                style = MaterialTheme.typography.headlineMedium, // 🗳️ 更新排版样式
                modifier = Modifier.align(Alignment.TopCenter),
                color = MaterialTheme.colorScheme.primary // 🗳️ 应用主题颜色
            )
        }
        TextField(
            value = enteredAge.value,
            onValueChange = { newAge ->
                enteredAge.value = newAge
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        // 修改4：更新浮动按钮实现
        ExtendedFloatingActionButton( // 🗳️ 替换为M3扩展按钮
            onClick = {
                if (enteredAge.value.isNotBlank()) {
                    onNextClicked(enteredAge.value.toInt())
                } else {
                    onNextClicked(0)
                }
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer, // 🗳️ 颜色方案
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 10.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                stringResource(R.string.desc_navigate_to_previous)
            )
        }
    }
}