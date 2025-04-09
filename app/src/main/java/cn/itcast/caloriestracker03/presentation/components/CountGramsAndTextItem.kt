package cn.itcast.caloriestracker03.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.utils.round

@Composable
fun CountGramsAndTextItem(
    count: Float,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    countFontSize: TextUnit = 20.sp,
    textFontSize: TextUnit = 16.sp,
) {

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.count_grams_short_2f, count.round(1)),
            style = MaterialTheme.typography.titleSmall.copy(fontSize = countFontSize),
            color = color,
            textAlign = TextAlign.Center
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = textFontSize),
            color = color,
            textAlign = TextAlign.Center,
        )
    }

}