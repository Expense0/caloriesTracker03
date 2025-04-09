package cn.itcast.caloriestracker03.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.*
import cn.itcast.caloriestracker03.utils.formatDateToString
import cn.itcast.caloriestracker03.utils.formatDateToMillis
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DatePickerDialog(
    selectedDate: String,
    onDismiss: () -> Unit,
    onDateChanged: (date: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            onDismiss()
        },
    ) {
        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            CalendarCompose(
                modifier = Modifier.fillMaxWidth(),
                selectedDate = Date(selectedDate.formatDateToMillis()),
                onDateChange = { y, m, d ->
                    onDateChanged(formatDateToString(dayOfMonth = d, month = m, year = y))
                }
            )
        }

    }
}