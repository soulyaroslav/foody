package com.jerdoul.foody.presentation.search.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.identifier
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
import com.jerdoul.foody.ui.theme.OnError
import com.jerdoul.foody.utils.extensions.clickableSingle


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchDishItem(
    dish: Dish,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onDishSelected: (Dish) -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp)
            )
            .clickableSingle { onDishSelected(dish) }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = dish.identifier()),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                ),
            painter = painterResource(id = R.drawable.logo_dish),
            contentDescription = null
        )
        Text(
            text = dish.name,
            style = MaterialTheme.typography.headlineMedium,
            color = FieldTextColor
        )
        Text(
            text = dish.shortDescription,
            style = MaterialTheme.typography.bodySmall,
            color = FieldTextHintColor
        )
        Text(
            text = stringResource(R.string.calories, dish.calories),
            style = MaterialTheme.typography.bodySmall,
            color = OnError
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${dish.price}",
                style = MaterialTheme.typography.headlineLarge,
                color = FieldTextColor
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}