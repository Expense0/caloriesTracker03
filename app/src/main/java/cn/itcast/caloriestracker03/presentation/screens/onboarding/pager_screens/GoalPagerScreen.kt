package cn.itcast.caloriestracker03.presentation.screens.onboarding.pager_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.domain.user.GoalType

import cn.itcast.caloriestracker03.R


@Composable
fun GoalPagerScreen(
    goalType: GoalType,
    onNextClicked: (GoalType) -> Unit,
) {
    val selectedGoalType = rememberSaveable(key = goalType.type) {
        mutableStateOf(goalType.type)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.title_goal),
            style = MaterialTheme.typography.headlineMedium, // 🟢 h4 -> headlineMedium
            color = MaterialTheme.colorScheme.onSurface // 🟢 使用主题文本颜色
        )

        GoalsSection(
            selectedGoalType = selectedGoalType.value,
            onGoalClicked = { goalType ->
                selectedGoalType.value = goalType.type
            }
        )

        ExtendedFloatingActionButton( // 🟢 替换为M3扩展按钮
            onClick = { onNextClicked(GoalType.fromString(selectedGoalType.value)) },
            containerColor = MaterialTheme.colorScheme.secondaryContainer, // 🟢 新颜色方案
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

@OptIn(ExperimentalMaterial3Api::class) // 🟢 更新实验注解
@Composable
private fun GoalItem(
    isSelected: Boolean,
    goalType: GoalType,
    onGoalClicked: (GoalType) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors( // 🟢 M3颜色配置方式
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.surfaceContainerHighest
            else Color.Transparent
        ),
        onClick = { onGoalClicked(goalType) },
        shape = MaterialTheme.shapes.medium, // 🟢 应用形状主题
        elevation = CardDefaults.cardElevation( // 🟢 海拔系统更新
            defaultElevation = if (isSelected) 3.dp else 0.dp
        )
    ) {
        Text(
            text = stringResource(id = goalType.nameResId),
            style = MaterialTheme.typography.headlineMedium, // 🟢 排版层级更新
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 30.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant, // 🟢 强调文本颜色
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GoalsSection(
    selectedGoalType: String,
    onGoalClicked: (GoalType) -> Unit,
) {
    val goalTypes = listOf(
        GoalType.LoseWeight,
        GoalType.KeepWeight,
        GoalType.GainWeight,
    )
    Column() {
        goalTypes.forEach { goalType ->
            GoalItem(
                isSelected = goalType.type == selectedGoalType,
                goalType = goalType,
                onGoalClicked = {
                    onGoalClicked(goalType)
                }
            )
        }
    }
}
