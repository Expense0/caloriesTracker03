package cn.itcast.caloriestracker03.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.R

@Composable
fun SettingsScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToCalorieCalculator: () -> Unit,
    onNavigateToCalorieIntake: () -> Unit,
    viewModel: SettingsViewModel,
) {
    val isDarkTheme = viewModel.isDarkTheme.collectAsState(initial = false)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.title_settings),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    viewModel.changeDarkTheme(!isDarkTheme.value)
                }
            ) {
                Icon(
                    imageVector = if (isDarkTheme.value) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.desc_theme_icon)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
        UnderlinedTextItem(
            text = stringResource(R.string.title_calorie_calculator),
            icon = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToCalorieCalculator()
                }
                .padding(start = 10.dp, end = 10.dp)
                .background(androidx.compose.material.MaterialTheme.colors.background),
        )
        UnderlinedTextItem(
            text = stringResource(R.string.title_daily_calorie_intake),
            icon = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToCalorieIntake()
                }
                .padding(start = 10.dp, end = 10.dp)
                .background(androidx.compose.material.MaterialTheme.colors.background),
        )
        UnderlinedTextItem(
            text = stringResource(R.string.title_change_goal),
            icon = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToOnboarding()
                }
                .padding(start = 10.dp, end = 10.dp)
                .background(androidx.compose.material.MaterialTheme.colors.background),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .background(androidx.compose.material.MaterialTheme.colors.background),
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material.Text(
                    text = stringResource(R.string.title_dark_theme),
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = isDarkTheme.value,
                    onCheckedChange = { checked ->
                        viewModel.changeDarkTheme(checked)
                    },
                    colors = SwitchDefaults.colors()
                )
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun UnderlinedTextItem(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material.Text(text = text, style = MaterialTheme.typography.titleMedium)
            Icon(imageVector = icon, contentDescription = text)
        }
        HorizontalDivider()
    }
}