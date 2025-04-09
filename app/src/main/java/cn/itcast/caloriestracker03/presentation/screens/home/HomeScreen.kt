package cn.itcast.caloriestracker03.presentation.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.presentation.components.CircularProgressBar
import cn.itcast.caloriestracker03.presentation.components.NutrientCard
import cn.itcast.caloriestracker03.presentation.theme.Green
import cn.itcast.caloriestracker03.presentation.theme.Orange
import cn.itcast.caloriestracker03.presentation.theme.Pink
import cn.itcast.caloriestracker03.presentation.theme.Turquoise
import cn.itcast.caloriestracker03.utils.getCurrentDateString
import cn.itcast.caloriestracker03.utils.getTomorrowDateString
import cn.itcast.caloriestracker03.utils.getYesterdayDateString


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
) {
    val state = homeViewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        HeaderSection(calsConsumed = state.cals, state.dailyCals)
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        DaysSection(
            date = state.date,
            onDateChange = { date ->
                homeViewModel.onEvent(HomeEvent.ChangeDate(date))
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        NutrientsSection(
            calories = state.cals,
            proteins = state.proteins,
            carbs = state.carbs,
            fat = state.fat,
            dailyCalories = state.dailyCals,
            dailyProteins = state.dailyProteins,
            dailyCarbs = state.dailyCarbs,
            dailyFat = state.dailyFat
        )
    }

}


@Composable
private fun HeaderSection(
    calsConsumed: Float,
    calsToConsumeInTotal: Float,
) {
    val progress = remember(calsConsumed, calsToConsumeInTotal) {
        calsConsumed / calsToConsumeInTotal
    }

    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(
                text = stringResource(R.string.count_calories, calsConsumed.toInt()),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            )
            Text(
                text = stringResource(R.string.text_consumed),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primaryContainer,
            )
        }
        CircularProgressBar(
            percentage = progress,
            radius = 40.dp,
            strokeWidth = 6.dp,
            color = Brush.horizontalGradient(
                colors = listOf(
                    Turquoise,
                    Green,
                )
            ),
        )
    }

}

@Composable
private fun DaysSection(
    date: String,
    onDateChange: (date: String) -> Unit,
) {
    val todaysDate = remember {
        getCurrentDateString()
    }
    val yesterdaysDate = remember {
        getYesterdayDateString()
    }
    val tomorrowsDate = remember {
        getTomorrowDateString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .horizontalScroll(rememberScrollState(1)),
        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        TextButton(onClick = { onDateChange(yesterdaysDate) }) {
            Text(
                text = stringResource(R.string.title_yesterday),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                color = if (date == yesterdaysDate) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            )
        }
        TextButton(onClick = { onDateChange(todaysDate) }) {
            Text(
                text = stringResource(R.string.title_today),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                color = if (date == todaysDate) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            )
        }
        TextButton(onClick = { onDateChange(tomorrowsDate) }) {
            Text(
                text = stringResource(R.string.title_tomorrow),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                color = if (date == tomorrowsDate)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            )
        }
    }
}


@Composable
private fun NutrientsSection(
    calories: Float,
    proteins: Float,
    carbs: Float,
    fat: Float,
    dailyCalories: Float,
    dailyProteins: Float,
    dailyCarbs: Float,
    dailyFat: Float,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(unbounded = true)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.width(3.dp))
        NutrientCard(
            name = stringResource(id = R.string.title_proteins),
            count = proteins,
            countInTotal = dailyProteins,
            modifier = Modifier,
            progressBarColor = Pink
        )
        NutrientCard(
            name = stringResource(id = R.string.title_carbs),
            count = carbs,
            countInTotal = dailyCarbs,
            modifier = Modifier,
            progressBarColor = Green
        )
        NutrientCard(
            name = stringResource(id = R.string.title_fat),
            count = fat,
            countInTotal = dailyFat,
            modifier = Modifier,
            progressBarColor = Orange
        )
        NutrientCard(
            name = stringResource(id = R.string.title_calories),
            count = calories,
            countInTotal = dailyCalories,
            modifier = Modifier,
            progressBarColor = Turquoise
        )
        Spacer(modifier = Modifier.width(3.dp))
    }
}