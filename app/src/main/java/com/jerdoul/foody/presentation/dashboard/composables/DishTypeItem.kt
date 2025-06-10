package com.jerdoul.foody.presentation.dashboard.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.utils.extensions.clickableSingle

@Composable
fun DishTypeItem(
    dishType: DishType,
    itemPosition: Int,
    selectedItemPosition: Int,
    onItemSelected: (DishType?, Int) -> Unit
) {
    var animate by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animate = true
    }

    AnimatedVisibility(
        visible = animate,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = itemPosition * 100,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Row(
            modifier = Modifier
                .then(
                    if (selectedItemPosition == itemPosition) {
                        Modifier.background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                )
                .padding(14.dp)
                .clickableSingle {
                    if (selectedItemPosition != itemPosition) {
                        onItemSelected(dishType, itemPosition)
                    } else {
                        onItemSelected(null, -1)
                    }

                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.logo_dish),
                contentDescription = null
            )
            Text(
                text = dishType.type,
                style = MaterialTheme.typography.headlineSmall,
                color = FieldTextColor
            )
        }
    }
}