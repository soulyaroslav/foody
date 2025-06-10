package com.jerdoul.foody.presentation.cart.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.ui.composable.BaseFilledButton

@Composable
fun BottomCartContent(
    modifier: Modifier,
    isLoading: Boolean,
    itemCount: Int,
    itemsPrice: Double,
    totalPrice: Double,
    onCheckout: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            CartPriceItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.items_count, itemCount),
                price = itemsPrice
            )
            Spacer(modifier = Modifier.height(14.dp))
            CartPriceItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.total_price),
                price = totalPrice
            )
            Spacer(modifier = Modifier.height(24.dp))
            BaseFilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                onClick = {
                    onCheckout()
                },
                isLoading = isLoading,
                loadingContent = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                content = {
                    Text(
                        text = stringResource(R.string.checkout),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    }
}