package com.jerdoul.foody.presentation.details.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.jerdoul.foody.R

@Composable
fun DishCharacteristicsContent(
    modifier: Modifier,
    dishRating: String,
    dishCalories: Int,
    dishCookTime: String
) {
    Row(modifier = modifier) {
        CharacteristicItem(
            title = dishRating,
            vectorPainter = rememberVectorPainter(Icons.Filled.Star)
        )
        CharacteristicItem(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.calories, dishCalories),
            vectorPainter = rememberVectorPainter(Icons.Filled.Info)
        )
        CharacteristicItem(
            title = dishCookTime,
            vectorPainter = rememberVectorPainter(Icons.Filled.Notifications)
        )
    }
}