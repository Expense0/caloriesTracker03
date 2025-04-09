package cn.itcast.caloriestracker03.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // 更新为 Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cn.itcast.caloriestracker03.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionResultDialog(
    onDismiss: () -> Unit,
    onSaveNutrition: (calories: Int, proteins: Float, fats: Float, carbs: Float) -> Unit,
    modifier: Modifier = Modifier,
    calories: Int,
    proteins: Float,
    fats: Float,
    carbs: Float,
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier,
//            elevation = 5.dp,这可不对哦
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.text_your_daily_calorie_intake),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(id = R.string.count_calories, calories),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    CountGramsAndTextItem(
                        count = proteins,
                        text = stringResource(id = R.string.title_proteins),
                    )
                    CountGramsAndTextItem(
                        count = fats,
                        text = stringResource(id = R.string.title_fats),
                    )
                    CountGramsAndTextItem(
                        count = carbs,
                        text = stringResource(id = R.string.title_carbs),
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .weight(1f, fill = false),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(text = stringResource(R.string.title_cancel))
                    }
                    TextButton(
                        onClick = {
                            onSaveNutrition(calories, proteins, fats, carbs)
                        }
                    ) {
                        Text(text = stringResource(R.string.text_save_nutrition))
                    }
                }
            }
        }
    }

}