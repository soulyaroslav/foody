package com.jerdoul.foody.presentation.cart.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jerdoul.foody.R
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.ui.composable.Counter
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor

@Composable
fun SwipeContent(
    dish: Dish,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    selectedCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            val (imageRef, titleRef, descriptionRef, priceRef, counterRef) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top, 14.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom, 14.dp)
                    }
                    .size(150.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.logo_dish),
                contentDescription = null
            )
            Text(
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(imageRef.end, 24.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                text = dish.name,
                style = MaterialTheme.typography.headlineSmall,
                color = FieldTextColor
            )
            Text(
                modifier = Modifier.constrainAs(descriptionRef) {
                    top.linkTo(titleRef.bottom, 8.dp)
                    start.linkTo(titleRef.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                text = dish.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = FieldTextHintColor
            )
            Row(
                modifier = Modifier
                    .constrainAs(priceRef) {
                        top.linkTo(descriptionRef.bottom, 8.dp)
                        start.linkTo(descriptionRef.start)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
            Counter(
                modifier = Modifier
                    .constrainAs(counterRef) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                onDecrement = onDecrement,
                onIncrement = onIncrement,
                selectedCount = selectedCount
            )
        }
    }
}