package cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal

import android.util.Log
import cn.itcast.caloriestracker03.presentation.navigation.ext.NavResultCallback
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.FloatingActionButton

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.presentation.components.CountGramsAndTextItem
import cn.itcast.caloriestracker03.presentation.components.DialogExit
import cn.itcast.caloriestracker03.presentation.components.EditProductWeightDialog
import cn.itcast.caloriestracker03.presentation.components.MealProductCard
import cn.itcast.caloriestracker03.presentation.components.TimePickerDialog
import cn.itcast.caloriestracker03.utils.formatTimeToString

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AddMealScreen(
    viewModel: AddMealViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSearchFood: (NavResultCallback<MealFoodProduct>) -> Unit,
) {

    val state = viewModel.state

    val lazyListState = rememberLazyListState()

    val showExitDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is AddMealUiEvent.MealSaved -> {
                    onNavigateBack()
                }
                is AddMealUiEvent.ShowSnackbar -> {
                    //Cant override default value of SnackbarDuration so use this method
                    launch {
                        val job = launch {
                            snackbarHostState.showSnackbar(
                                uiEvent.message,
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(1000)
                        cancel()
                    }
                }
            }
        }
    }

    BackHandler() {
        showExitDialog.value = true
    }

    if (showExitDialog.value) {
        DialogExit(
            onDismiss = { showExitDialog.value = false },
            onConfirm = {
                showExitDialog.value = false
                onNavigateBack()
            },
            title = stringResource(id = R.string.title_cancel_creation),
            text = stringResource(R.string.text_cancel_new_meal)
        )
    }

    if (state.showEditProductWeightDialog) {
        state.productToEdit?.let { product ->
            EditProductWeightDialog(
                product = product,
                onConfirm = { weight, _ ->
                    viewModel.onEvent(AddMealEvent.HideEditProductWeightDialog)
                    viewModel.onEvent(AddMealEvent.ChangeMealProductGrams(product, weight))
                },
                onDismiss = {
                    viewModel.onEvent(AddMealEvent.HideEditProductWeightDialog)
                }
            )
        }
    }


    TimePickerDialog(
        showDialog = state.showTimePickerDialog,
        onCancelled = {
            viewModel.onEvent(AddMealEvent.ShowTimePickerDialog(false))
        },
        onTimePicked = { timeSeconds ->
            viewModel.onEvent(AddMealEvent.ShowTimePickerDialog(false))
            viewModel.onEvent(AddMealEvent.ChangeTime(timeSeconds))
        }
    )


    Scaffold(
        topBar = {
            HeaderSection(
                state.mealProducts,
                state.name,
                state.timeSeconds,
                onNameChanged = { name ->
                    viewModel.onEvent(AddMealEvent.ChangeMealName(name))
                },
                onClockClicked = {
                    viewModel.onEvent(AddMealEvent.ShowTimePickerDialog(true))
                },
                onNavigateBack = {
                    showExitDialog.value = true
                }
            )
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                ) {
                    TextButton(
                        onClick = {
                            showExitDialog.value = true
                        }
                    ) {
                        Text(text = stringResource(R.string.title_cancel))
                    }
                    TextButton(
                        onClick = {
                            viewModel.onEvent(AddMealEvent.SaveMeal)
                        }
                    ) {
                        Text(text = stringResource(R.string.title_save))
                    }
                }
            }

        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MealsProductsSection(
                products = state.mealProducts,
                lazyListState = lazyListState,
                onDeleteProductClicked = { product ->
                    viewModel.onEvent(AddMealEvent.DeleteMealProduct(product))
                },
                onEditProductWeightClicked = { mealFoodProduct ->
                    viewModel.onEvent(AddMealEvent.ShowEditProductWeightDialog(mealFoodProduct))
                }
            )
            AnimatedVisibility(
                visible = !lazyListState.isScrollInProgress,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 15.dp, end = 15.dp),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        // 记录按钮点击事件
                        Log.d("AddMealScreen", "FloatingActionButton clicked: Navigating to search food screen")

                        onNavigateToSearchFood { mealFoodProduct ->
                            // 记录导航到搜索页面
                            Log.d("AddMealScreen", "Navigating to search food screen")

                            // 记录选择的产品信息
                            Log.d("AddMealScreen", "Selected product: ${mealFoodProduct.name} (ID: ${mealFoodProduct.id})")

                            viewModel.onEvent(AddMealEvent.AddMealProduct(mealFoodProduct))
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.desc_add_meal_icon)
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealsProductsSection(
    products: List<MealFoodProduct>,
    lazyListState: LazyListState,
    onDeleteProductClicked: (MealFoodProduct) -> Unit,
    onEditProductWeightClicked: (MealFoodProduct) -> Unit,
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            start = 10.dp,
            end = 10.dp,
            top = 20.dp,
            bottom = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        items(products) { product ->
            MealProductCard(
                modifier = Modifier.animateItemPlacement(tween(300)),
                product = product,
                onProductClicked = {},
                onDeleteProductClicked = {
                    onDeleteProductClicked(product)
                },
                onEditProductWeightClicked = {
                    onEditProductWeightClicked(product)
                }
            )
        }
    }

}


@Composable
private fun HeaderSection(
    products: List<MealFoodProduct>,
    name: String,
    timeSeconds: Long,
    onNameChanged: (String) -> Unit,
    onClockClicked: () -> Unit,
    onNavigateBack: () -> Unit,
) {

    val calories = remember(products) {
        products.sumOf { it.cals.toDouble() }
    }

    val proteins = remember(products) {
        products.sumOf { it.proteins.toDouble() }
    }

    val carbs = remember(products) {
        products.sumOf { it.carbs.toDouble() }
    }

    val fat = remember(products) {
        products.sumOf { it.fat.toDouble() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(15.dp))
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
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.desc_navigate_to_previous),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Text(
                text = stringResource(R.string.title_new_meal),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSecondary
            ),
            value = name,
            onValueChange = { newName ->
                onNameChanged(newName)
            },
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.text_meal_name),
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
                )
            },

            )
        Spacer(modifier = Modifier.height(15.dp))
        IconButton(
            onClick = {
                onClockClicked()
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.desc_set_meal_time),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    text = timeSeconds.formatTimeToString(),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CountGramsAndTextItem(
                count = proteins.toFloat(),
                text = stringResource(id = R.string.title_proteins),
                color = MaterialTheme.colorScheme.onSecondary,
                countFontSize = 17.sp,
                textFontSize = 13.sp
            )
            CountGramsAndTextItem(
                count = fat.toFloat(),
                text = stringResource(id = R.string.title_fats),
                color = MaterialTheme.colorScheme.onSecondary,
                countFontSize = 17.sp,
                textFontSize = 13.sp
            )
            CountGramsAndTextItem(
                count = carbs.toFloat(),
                text = stringResource(id = R.string.title_carbs),
                color = MaterialTheme.colorScheme.onSecondary,
                countFontSize = 17.sp,
                textFontSize = 13.sp
            )
            CountGramsAndTextItem(
                count = calories.toFloat(),
                text = stringResource(id = R.string.title_calories),
                color = MaterialTheme.colorScheme.onSecondary,
                countFontSize = 17.sp,
                textFontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
    }


}