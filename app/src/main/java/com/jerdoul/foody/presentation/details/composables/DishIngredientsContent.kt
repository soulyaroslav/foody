package com.jerdoul.foody.presentation.details.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.IngredientType
import com.jerdoul.foody.ui.theme.FieldTextColor

fun IngredientType.toDrawable() = when (this) {
    IngredientType.VEGETABLE,
    IngredientType.FRUIT,
    IngredientType.MEAT,
    IngredientType.SEAFOOD,
    IngredientType.DAIRY,
    IngredientType.GRAIN,
    IngredientType.NUT,
    IngredientType.LEGUME,
    IngredientType.SPICE,
    IngredientType.HERB,
    IngredientType.SWEETENER,
    IngredientType.OIL,
    IngredientType.SAUCE,
    IngredientType.EGG,
    IngredientType.BEVERAGE,
    IngredientType.MUSHROOM,
    IngredientType.PLANT_BASED,
    IngredientType.PROCESSED,
    IngredientType.OTHER -> R.drawable.steak
}

@Composable
fun DishIngredientsContent(modifier: Modifier, ingredients: List<IngredientType>) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.ingredients),
            style = MaterialTheme.typography.headlineMedium,
            color = FieldTextColor
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ingredients) { ingredient ->
                val painter = remember { ingredient.toDrawable() }
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Image(
                        painter = painterResource(painter),
                        contentDescription = null
                    )
                }
            }
        }
    }
}