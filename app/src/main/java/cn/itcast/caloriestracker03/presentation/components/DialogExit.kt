package cn.itcast.caloriestracker03.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cn.itcast.caloriestracker03.R


@Composable
fun DialogExit(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.title_exit),
    text: String = stringResource(R.string.warning_exit),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(text = stringResource(R.string.title_no))
                    }
                    TextButton(
                        onClick = {
                            onConfirm()
                        }
                    ) {
                        Text(text = stringResource(R.string.title_yes))
                    }
                }
            }
        }
    }

}