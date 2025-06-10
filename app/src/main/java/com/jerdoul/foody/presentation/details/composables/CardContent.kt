package com.jerdoul.foody.presentation.details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.domain.pojo.IngredientType

@Composable
fun CardContent(
    modifier: Modifier,
    dishName: String,
    dishPrice: Double,
    dishRating: String,
    dishCalories: Int,
    dishCookTime: String,
    dishDescription: String,
    dishIngredients: List<IngredientType>
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        DishNameContent(
            modifier = Modifier.fillMaxWidth(),
            dishName = dishName,
            price = dishPrice
        )
        DishCharacteristicsContent(
            modifier = Modifier.fillMaxWidth(),
            dishRating = dishRating,
            dishCalories = dishCalories,
            dishCookTime = dishCookTime
        )
        DishDetailsContent(
            modifier = Modifier.fillMaxWidth(),
            dishDescription = dishDescription
        )
        DishIngredientsContent(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            ingredients = dishIngredients
        )
    }
}