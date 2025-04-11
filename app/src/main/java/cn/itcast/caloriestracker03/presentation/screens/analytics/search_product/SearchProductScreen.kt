package cn.itcast.caloriestracker03.presentation.screens.analytics.search_product

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.presentation.components.AddProductDialog
import cn.itcast.caloriestracker03.presentation.components.FoodProductInfoCard
import cn.itcast.caloriestracker03.utils.EMPTY_STRING


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchProductScreen(
    viewModel: SearchProductViewModel,
    onConfirm: (MealFoodProduct) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val state = viewModel.state

    if (state.showAddProductDialog) {
        state.selectedProduct?.let {
            // 添加日志：记录对话框显示
            Log.d("SearchProductScreen", "AddProductDialog is being shown with product: ${it.name}")

            AddProductDialog(
                productInfo = it,
                onConfirm = { mealFoodProduct ->
                    onConfirm(mealFoodProduct)
                    // 添加日志：记录用户确认添加产品
                    Log.d("SearchProductScreen", "User confirmed adding product: ${mealFoodProduct.name}")
                    viewModel.onEvent(SearchProductEvent.HideAddProductDialog)
                },
                onDismiss = {
                    // 添加日志：记录对话框关闭
                    Log.d("SearchProductScreen", "AddProductDialog dismissed")
                    viewModel.onEvent(SearchProductEvent.HideAddProductDialog)
                }
            )
        }
    }

    Scaffold(
        topBar = {
            HeaderSection(
                onNavigateBack = onNavigateBack,
                query = state.query,
                onQueryChanged = { query ->
                    viewModel.onEvent(SearchProductEvent.ChangeQuery(query))
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 20.dp,
                    bottom = 10.dp
                ),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                items(state.products) { product ->
                    FoodProductInfoCard(
                        modifier = Modifier
                            .animateItemPlacement(tween(300)),
                        product = product,
                        onProductClicked = {
                            viewModel.onEvent(
                                SearchProductEvent.ShowAddProductDialog(product)
                            )
                        }
                    )
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

    }

}

@Composable
fun HeaderSection(
    onNavigateBack: () -> Unit,
    query: String,
    onQueryChanged: (query: String) -> Unit,
) {
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
                text = stringResource(R.string.title_search_products),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSecondary
            ),
            value = query,
            onValueChange = { newQuery ->
                onQueryChanged(newQuery)
            },
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.text_find_food),
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.desc_clean_textfield),
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    onQueryChanged(EMPTY_STRING)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.desc_find_product),
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }

            }
        )
        Spacer(modifier = Modifier.height(15.dp))
    }
}