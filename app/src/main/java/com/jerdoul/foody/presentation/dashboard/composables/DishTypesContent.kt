package com.jerdoul.foody.presentation.dashboard.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.domain.pojo.DishType

@Composable
fun DishTypesContent(
    modifier: Modifier,
    dishTypes: List<DishType>,
    onFilterTypes: (DishType?) -> Unit
) {
    var selectedItemPosition by remember { mutableIntStateOf(-1) }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        itemsIndexed(dishTypes) { itemPosition, foodType ->
            DishTypeItem(
                dishType = foodType,
                itemPosition = itemPosition,
                selectedItemPosition = selectedItemPosition,
                onItemSelected = { dishType, position ->
                    selectedItemPosition = position
                    onFilterTypes(dishType)
                }
            )
        }
    }
}