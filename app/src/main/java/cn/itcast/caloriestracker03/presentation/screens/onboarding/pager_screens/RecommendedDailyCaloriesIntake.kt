package cn.itcast.caloriestracker03.presentation.screens.onboarding.pager_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.user.GoalType


@Composable
fun RecommendedDailyCaloriesIntake(
    fat: Float,
    proteins: Float,
    carbs: Float,
    calories: Int,
    goalType: GoalType,
    onSaveClicked: (fat: Float, proteins: Float, carbs: Float, calories: Int) -> Unit,
    onPreviousClicked: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onPreviousClicked() },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.title_recom_cal_intake), //✨ M3强制内容描述
                    tint = MaterialTheme.colorScheme.primary //✨ 主题色应用
                )
            }
            Text(
                text = stringResource(R.string.title_recom_cal_intake),
                style = MaterialTheme.typography.headlineMedium, //✨ h4→headlineMedium
                modifier = Modifier.align(Alignment.TopCenter),
                color = MaterialTheme.colorScheme.onSurface //✨ 文本颜色适配
            )
        }
        ExplanationText(goalType = goalType)
        NutritionSection(
            fat,
            proteins,
            carbs,
            calories
        )
        Text(
            text = stringResource(R.string.text_you_can_change_cal_intake_in_profile),
            color = MaterialTheme.colorScheme.onSurface, // 颜色系统更新
            style = MaterialTheme.typography.bodyMedium,  // 排版系统更新
            modifier = Modifier.padding(10.dp)
        )
        Button(
            onClick = { onSaveClicked(fat, proteins, carbs, calories) },
            shape = MaterialTheme.shapes.large, //✨ 使用M3预设形状
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer, //✨ 颜色槽更新
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation( //✨ 海拔系统更新
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            )
        ) {
            Text(text = stringResource(R.string.title_save))
        }
    }

}


@Composable
private fun ExplanationText(
    goalType: GoalType,
) {
    val text = when (goalType) {
        is GoalType.KeepWeight -> stringResource(R.string.explanation_keep_weight)
        is GoalType.LoseWeight -> stringResource(R.string.explanation_lose_weight)
        is GoalType.GainWeight -> stringResource(R.string.explanation_gain_weight)
        else -> throw Exception("Unexpected GoalType: ${goalType.type}")
    }
    Text(
        text = text,
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
private fun NutritionSection(
    fat: Float,
    proteins: Float,
    carbs: Float,
    calories: Int,
) {
    Column() {
        NutritionItem(
            value = proteins,
            name = stringResource(id = R.string.title_proteins)
        )
        NutritionItem(
            value = fat,
            name = stringResource(id = R.string.title_fat)
        )
        NutritionItem(
            value = carbs,
            name = stringResource(id = R.string.title_carbs)
        )
        NutritionItem(
            value = calories.toFloat(),
            name = stringResource(id = R.string.title_calories)
        )
    }
}

@Composable
private fun NutritionItem(
    value: Float,
    name: String,
) {
    Card(
        modifier = Modifier.padding(10.dp),
        colors = CardDefaults.cardColors( //✨ M3颜色配置
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium //✨ 形状主题应用
    ) {
        Text(
            text = stringResource(id = R.string.name_count_grams, name, value),
            style = MaterialTheme.typography.bodyMedium, //✨ body2→bodyMedium
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant, //✨ 强调文本颜色
            textAlign = TextAlign.Center
        )
    }
}