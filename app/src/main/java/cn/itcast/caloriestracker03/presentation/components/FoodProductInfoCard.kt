package cn.itcast.caloriestracker03.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo
import cn.itcast.caloriestracker03.presentation.theme.Green
import cn.itcast.caloriestracker03.presentation.theme.Orange
import cn.itcast.caloriestracker03.presentation.theme.Pink
import cn.itcast.caloriestracker03.presentation.theme.Turquoise
import cn.itcast.caloriestracker03.utils.round


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodProductInfoCard(
    product: FoodProductInfo,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    background: Color = MaterialTheme.colorScheme.surface,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    onProductClicked: (FoodProductInfo) -> Unit,
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        onClick = {
            onProductClicked(product)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.text_in_100_grams),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NutritionCountItem(
                    name = stringResource(id = R.string.title_proteins),
                    count = product.proteinsIn100Grams,
                    modifier = Modifier.background(Pink, RoundedCornerShape(15.dp))
                )
                NutritionCountItem(
                    name = stringResource(id = R.string.title_fats),
                    count = product.fatIn100Grams,
                    modifier = Modifier.background(Orange, RoundedCornerShape(15.dp))
                )
                NutritionCountItem(
                    name = stringResource(id = R.string.title_carbs),
                    count = product.carbsIn100Grams,
                    modifier = Modifier.background(Green, RoundedCornerShape(15.dp))
                )
            }
            NutritionCountItem(
                name = stringResource(id = R.string.title_calories),
                count = product.caloriesIn100Grams,
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

