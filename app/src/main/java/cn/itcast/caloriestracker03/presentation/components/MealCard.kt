package cn.itcast.caloriestracker03.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.model.meal.Meal
import cn.itcast.caloriestracker03.presentation.theme.Green
import cn.itcast.caloriestracker03.presentation.theme.Orange
import cn.itcast.caloriestracker03.presentation.theme.Pink
import cn.itcast.caloriestracker03.presentation.theme.Turquoise
import cn.itcast.caloriestracker03.utils.formatTimeToString
import cn.itcast.caloriestracker03.utils.round


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCard(
    meal: Meal,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    background: Color = MaterialTheme.colorScheme.surface,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    onMealClicked: (Meal) -> Unit,
    onDeleteMealClicked: (Meal) -> Unit,
    onEditMealClicked: (Meal) -> Unit,
) {

    val dropdownMenuExpanded = rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        onClick = {
            onMealClicked(meal)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column() {
                    Text(
                        text = meal.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = stringResource(R.string.desc_icon_alarm),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = meal.timeSeconds.formatTimeToString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            dropdownMenuExpanded.value = !dropdownMenuExpanded.value
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = stringResource(R.string.desc_show_options),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DropdownMenu(
                        expanded = dropdownMenuExpanded.value,
                        onDismissRequest = {
                            dropdownMenuExpanded.value = false
                        }
                    ) {
                        DropdownMenuItem(
                            text =  {
                                Text(
                                    text = stringResource(R.string.title_edit),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                dropdownMenuExpanded.value = false
                                onEditMealClicked(meal)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                text = stringResource(R.string.title_delete),
                                color = MaterialTheme.colorScheme.onSurface
                            )},
                            onClick = {
                                dropdownMenuExpanded.value = false
                                onDeleteMealClicked(meal)
                            }
                        )
                    }

                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NutritionCountItem(
                    name = stringResource(id = R.string.title_proteins),
                    count = meal.proteins,
                    modifier = modifier.background(Pink, RoundedCornerShape(15.dp))
                )
                NutritionCountItem(
                    name = stringResource(id = R.string.title_fats),
                    count = meal.fat,
                    modifier = modifier.background(Orange, RoundedCornerShape(15.dp))
                )
                NutritionCountItem(
                    name = stringResource(id = R.string.title_carbs),
                    count = meal.carbs,
                    modifier = modifier.background(Green, RoundedCornerShape(15.dp))
                )
            }
            NutritionCountItem(
                name = stringResource(id = R.string.title_calories),
                count = meal.calories,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Turquoise, RoundedCornerShape(15.dp))
            )
        }
    }
}

@Composable
private fun NutritionCountItem(
    name: String,
    count: Float,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = count.round(1).toString(),
            modifier = Modifier.padding(start = 5.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = name,
            modifier = Modifier.padding(end = 5.dp)
        )
    }
}

