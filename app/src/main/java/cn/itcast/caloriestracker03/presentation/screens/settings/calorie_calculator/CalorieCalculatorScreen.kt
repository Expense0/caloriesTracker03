package cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.caloriestracker03.presentation.components.NutritionResultDialog

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.user.GoalType


@Composable
fun CalorieCalculatorScreen(
    viewModel: CaloriesCalculatorViewModel,
    onNavigateBack: () -> Unit,
) {

    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() } // Material3 的 SnackbarHostState
//    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is CalorieCalculatorUiEvent.ShowSnackbar -> {
                    launch {
                        val job = launch {
                            //这是Material3的Snackbar参数
                            snackbarHostState.showSnackbar(
                                uiEvent.message,
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(1000)
                        job.cancel()
                    }
                }
            }
        }
    }

    if (state.showResultDialog) {
        NutritionResultDialog(
            onDismiss = {
                viewModel.onEvent(CalorieCalculatorEvent.ShowResultDialog(false))
            },
            onSaveNutrition = { calories, proteins, fats, carbs ->
                viewModel.onEvent(
                    CalorieCalculatorEvent.SaveNutrition(
                        calories, proteins, fats, carbs
                    )
                )
                viewModel.onEvent(CalorieCalculatorEvent.ShowResultDialog(false))
            },
            calories = state.calories,
            proteins = state.proteins.toFloat(),
            fats = state.fats.toFloat(),
            carbs = state.carbs.toFloat()
        )
    }

    Scaffold(
//        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = {
                        onNavigateBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                        contentDescription = stringResource(id = R.string.desc_navigate_to_previous),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = stringResource(R.string.title_calorie_calculator),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                InfoSection(
                    state.gender,
                    state.ageText,
                    state.heightText,
                    state.weightText,
                    onGenderChanged = { gender ->
                        viewModel.onEvent(CalorieCalculatorEvent.GenderChanged(gender))
                    },
                    onAgeTextChanged = { age ->
                        viewModel.onEvent(CalorieCalculatorEvent.AgeTextChanged(age))
                    },
                    onHeightTextChanged = { height ->
                        viewModel.onEvent(CalorieCalculatorEvent.HeightTextChanged(height))
                    },
                    onWeightTextChanged = { weight ->
                        viewModel.onEvent(CalorieCalculatorEvent.WeightTextChanged(weight))
                    }
                )
                ActivitySection(
                    activityLevel = state.activityLevel,
                    goalType = state.goalType,
                    onActivityLevelChanged = { activityLevel ->
                        viewModel.onEvent(CalorieCalculatorEvent.ActivityLevelChanged(activityLevel))
                    },
                    onGoalTypeChanged = { goalType ->
                        viewModel.onEvent(CalorieCalculatorEvent.GoalTypeChanged(goalType))
                    }
                )
                NutritionRatioSection(
                    proteinsRatioText = state.proteinsRatioText,
                    fatsRatioText = state.fatsRatioText,
                    carbsRatioText = state.carbsRatioText,
                    onProteinsRatioTextChanged = { proteinsRatio ->
                        viewModel.onEvent(
                            CalorieCalculatorEvent.ProteinsRatioTextChanged(
                                proteinsRatio
                            )
                        )
                    },
                    onFatsRatioTextChanged = { fatsRatio ->
                        viewModel.onEvent(
                            CalorieCalculatorEvent.FatsRatioTextChanged(
                                fatsRatio
                            )
                        )
                    },
                    onCarbsRatioTextChanged = { carbsRatio ->
                        viewModel.onEvent(
                            CalorieCalculatorEvent.CarbsRatioTextChanged(
                                carbsRatio
                            )
                        )
                    }
                )
                Button(
                    onClick = {
                        viewModel.onEvent(CalorieCalculatorEvent.Calculate)
                    }
                ) {
                    Text(text = stringResource(R.string.title_calculate))
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoSection(
    gender: Gender,
    ageText: String,
    heightText: String,
    weightText: String,
    onGenderChanged: (Gender) -> Unit,
    onAgeTextChanged: (age: String) -> Unit,
    onHeightTextChanged: (height: String) -> Unit,
    onWeightTextChanged: (weight: String) -> Unit,
) {

    val dropdownMenuExpanded = rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.title_gender))
            TextButton(
                onClick = {
                    dropdownMenuExpanded.value = true
                }
            ) {
                Text(text = stringResource(id = gender.nameResId))
                DropdownMenu(
                    expanded = dropdownMenuExpanded.value,
                    onDismissRequest = { dropdownMenuExpanded.value = false },
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.title_male)) },
                        onClick = {
                            dropdownMenuExpanded.value = false
                            onGenderChanged(Gender.Male)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.title_female)) },
                        onClick = {
                            dropdownMenuExpanded.value = false
                            onGenderChanged(Gender.Female)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                disabledTextColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f),
            value = ageText,
            onValueChange = { ageString ->
                onAgeTextChanged(ageString)
            },
            label = {
                Text(
                    text = stringResource(R.string.text_age_in_years),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                disabledTextColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f),
            value = heightText,
            onValueChange = { heightString ->
                onHeightTextChanged(heightString)
            },
            label = {
                Text(
                    text = stringResource(R.string.text_height_in_cm),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                disabledTextColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f),
            value = weightText,
            onValueChange = { weightString ->
                onWeightTextChanged(weightString)
            },
            label = {
                Text(
                    text = stringResource(R.string.text_weight_in_kg),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivitySection(
    activityLevel: ActivityLevel,
    goalType: GoalType,
    onActivityLevelChanged: (ActivityLevel) -> Unit,
    onGoalTypeChanged: (GoalType) -> Unit,
) {

    val activityLevelDropdownMenuExpanded = rememberSaveable {
        mutableStateOf(false)
    }

    val goalDropdownMenuExpanded = rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.title_activity_level))
            TextButton(
                onClick = {
                    activityLevelDropdownMenuExpanded.value = true
                }
            ) {
                Text(text = stringResource(id = activityLevel.nameResId))
                DropdownMenu(
                    expanded = activityLevelDropdownMenuExpanded.value,
                    onDismissRequest = { activityLevelDropdownMenuExpanded.value = false },
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.title_activity_low)) },
                        onClick = {
                            activityLevelDropdownMenuExpanded.value = false
                            onActivityLevelChanged(ActivityLevel.Low)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.title_activity_medium)) },
                        onClick = {
                            activityLevelDropdownMenuExpanded.value = false
                            onActivityLevelChanged(ActivityLevel.Medium)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.title_activity_high)) },
                        onClick = {
                            activityLevelDropdownMenuExpanded.value = false
                            onActivityLevelChanged(ActivityLevel.High)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.title_activity_superhigh)) },
                        onClick = {
                            activityLevelDropdownMenuExpanded.value = false
                            onActivityLevelChanged(ActivityLevel.SuperHigh)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.title_goal))
        TextButton(
            onClick = {
                goalDropdownMenuExpanded.value = true
            }
        ) {
            Text(text = stringResource(id = goalType.nameResId))
            DropdownMenu(
                expanded = goalDropdownMenuExpanded.value,
                onDismissRequest = { goalDropdownMenuExpanded.value = false },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 4.dp,
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.title_lose_weight)) },
                    onClick = {
                        goalDropdownMenuExpanded.value = false
                        onGoalTypeChanged(GoalType.LoseWeight)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.title_keep_weight)) },
                    onClick = {
                        goalDropdownMenuExpanded.value = false
                        onGoalTypeChanged(GoalType.KeepWeight)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.title_gain_weight)) },
                    onClick = {
                        goalDropdownMenuExpanded.value = false
                        onGoalTypeChanged(GoalType.GainWeight)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}


@Composable
private fun NutritionRatioSection(
    proteinsRatioText: String,
    fatsRatioText: String,
    carbsRatioText: String,
    onProteinsRatioTextChanged: (text: String) -> Unit,
    onFatsRatioTextChanged: (text: String) -> Unit,
    onCarbsRatioTextChanged: (text: String) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                disabledTextColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f),
            value = proteinsRatioText,
            onValueChange = { proteins ->
                onProteinsRatioTextChanged(proteins)
            },
            label = {
                Text(
                    text = stringResource(R.string.text_proteins_in_percentage),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                disabledTextColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f),
            value = fatsRatioText,
            onValueChange = { fats ->
                onFatsRatioTextChanged(fats)
            },
            label = {
                Text(
                    text = stringResource(R.string.text_fats_in_percentage),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                disabledTextColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f),
            value = carbsRatioText,
            onValueChange = { carbs ->
                onCarbsRatioTextChanged(carbs)
            },
            label = {
                Text(
                    text = stringResource(R.string.text_carbs_in_percentage),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }

}