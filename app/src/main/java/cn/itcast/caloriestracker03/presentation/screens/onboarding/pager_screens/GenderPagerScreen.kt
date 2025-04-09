package cn.itcast.caloriestracker03.presentation.screens.onboarding.pager_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import cn.itcast.caloriestracker03.R
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import cn.itcast.caloriestracker03.domain.user.Gender


@Composable
fun GenderPagerScreen(
    gender: Gender,
    onNextClicked: (Gender) -> Unit,
    onPreviousClicked: () -> Unit,
) {
    val selectedGenderType = rememberSaveable(key = gender.type) {
        mutableStateOf(gender.type)
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
                    contentDescription = stringResource(R.string.desc_navigate_to_previous), //🎨 M3强制要求内容描述
                    tint = MaterialTheme.colorScheme.primary //🎨 使用主题颜色
                )
            }
            Text(
                text = stringResource(R.string.title_gender),
                style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center), //🎨 h4改为headlineMedium
                modifier = Modifier.align(Alignment.TopCenter),
                color = MaterialTheme.colorScheme.onSurface //🎨 文本颜色适配主题
            )
        }
        GendersSection(
            selectedGenderType = selectedGenderType.value,
            onGenderClicked = { clickedGender ->
                selectedGenderType.value = clickedGender.type
            }
        )
        ExtendedFloatingActionButton( //🎨 替换为M3扩展按钮
            onClick = { onNextClicked(Gender.fromString(selectedGenderType.value)) },
            containerColor = MaterialTheme.colorScheme.secondaryContainer, //🎨 颜色方案更新
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


@Composable
private fun GendersSection(
    selectedGenderType: String,
    onGenderClicked: (Gender) -> Unit,
) {
    val genders = listOf(
        Gender.Male,
        Gender.Female,
    )
    Column() {
        genders.forEach { gender ->
            GenderItem(
                isSelected = selectedGenderType == gender.type,
                gender = gender,
                onGenderClicked = {
                    onGenderClicked(gender)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderItem(
    isSelected: Boolean,
    gender: Gender,
    onGenderClicked: (Gender) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(  // 🔸 M3 颜色配置方式
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.surfaceContainerHighest  // 🔸 新颜色槽
            else Color.Transparent
        ),
        onClick = { onGenderClicked(gender) },
        shape = MaterialTheme.shapes.medium,  // 🔸 应用形状主题
        elevation = CardDefaults.cardElevation(  // 🔸 新海拔系统
            defaultElevation = if (isSelected) 3.dp else 0.dp
        )
    ) {
        Text(
            text = stringResource(id = gender.nameResId),
            style = MaterialTheme.typography.headlineMedium,  // 🔸 排版层级更新
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 30.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant  // 🔸 强调文本颜色
        )
    }
}