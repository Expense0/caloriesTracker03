package cn.itcast.caloriestracker03.presentation.screens.onboarding.pager_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight

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
import cn.itcast.caloriestracker03.domain.user.ActivityLevel

import cn.itcast.caloriestracker03.R


@Composable
fun ActivityLevelPagerScreen(
    activityLevel: ActivityLevel,
    onNextClicked: (ActivityLevel) -> Unit,
    onPreviousClicked: () -> Unit,
) {

    val selectedActivityLevel = rememberSaveable(key = activityLevel.type) {
        mutableStateOf(activityLevel.type)
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
                onClick = {
                    onPreviousClicked()
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, null)
            }
            Text(
                text = stringResource(id = R.string.title_activity_level),
                style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center),
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
        ActivityLevelsSection(
            selectedActivityType = selectedActivityLevel.value,
            onActivityLevelClicked = { level ->
                selectedActivityLevel.value = level.type
            }
        )
        ExtendedFloatingActionButton(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                onNextClicked(ActivityLevel.fromString(selectedActivityLevel.value))
            },
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

@Composable
private fun ActivityLevelsSection(
    selectedActivityType: String,
    onActivityLevelClicked: (ActivityLevel) -> Unit,
) {
    val levels = listOf(
        ActivityLevel.Low,
        ActivityLevel.Medium,
        ActivityLevel.High,
        ActivityLevel.SuperHigh
    )
    Column() {
        levels.forEach { level ->
            ActivityLevelItem(
                isSelected = selectedActivityType == level.type,
                activityLevel = level,
                onActivityLevelClicked = {
                    onActivityLevelClicked(level)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityLevelItem(
    isSelected: Boolean,
    activityLevel: ActivityLevel,
    onActivityLevelClicked: (ActivityLevel) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
            else Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onActivityLevelClicked(activityLevel)
        },
    ) {
        Text(
            text = stringResource(id = activityLevel.nameResId),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}