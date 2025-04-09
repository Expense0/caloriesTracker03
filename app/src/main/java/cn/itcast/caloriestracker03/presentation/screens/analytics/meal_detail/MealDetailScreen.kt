package cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail

import cn.itcast.caloriestracker03.presentation.navigation.ext.NavResultCallback
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.presentation.components.EditProductWeightDialog
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.presentation.components.CountGramsAndTextItem
import cn.itcast.caloriestracker03.presentation.components.MealProductCard
import cn.itcast.caloriestracker03.utils.formatTimeToString

@Composable
fun MealDetailScreen(
    viewModel: MealDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSearchFood: (NavResultCallback<MealFoodProduct>) -> Unit,
) {

    val state = viewModel.state

    val lazyListState = rememberLazyListState()

    if (state.showEditProductWeightDialog) {
        state.productToEdit?.let { product ->
            EditProductWeightDialog(
                product = product,
                onConfirm = { weight, _ ->
                    viewModel.onEvent(MealDetailEvent.HideEditProductWeightDialog)
                    viewModel.onEvent(MealDetailEvent.ChangeMealProductGrams(weight))
                },
                onDismiss = {
                    viewModel.onEvent(MealDetailEvent.HideEditProductWeightDialog)
                }
            )
        }
    }

    Scaffold(
        topBar = {
            HeaderSection(
                state.mealProducts,
                state.name,
                state.timeSeconds,
                onNavigateBack = onNavigateBack
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MealsProductsSection(
                products = state.mealProducts,
                lazyListState = lazyListState,
                onEditProductWeightClicked = { mealFoodProduct ->
                    viewModel.onEvent(MealDetailEvent.ShowEditProductWeightDialog(mealFoodProduct))
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
                        onNavigateToSearchFood { mealFoodProduct ->
                            viewModel.onEvent(MealDetailEvent.AddMealProduct(mealFoodProduct))
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
    onNavigateBack: () -> Unit
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
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.desc_navigate_to_previous),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Text(
                text = stringResource(R.string.title_analytics),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSecondary
            ),
            value = name,
            onValueChange = { },
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
            onClick = {}
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