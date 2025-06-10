package com.jerdoul.foody.presentation.details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jerdoul.foody.R
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor

@Composable
fun DishDetailsContent(modifier: Modifier, dishDescription: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.details),
            style = MaterialTheme.typography.headlineMedium,
            color = FieldTextColor
        )
        Text(
            text = dishDescription,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 24.sp,
            color = FieldTextHintColor
        )
    }
}