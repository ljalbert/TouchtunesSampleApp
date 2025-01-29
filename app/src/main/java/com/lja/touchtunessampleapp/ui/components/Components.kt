package com.lja.touchtunessampleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.lja.touchtunessampleapp.R

@Composable
fun AlbumDialog(
    modifier: Modifier = Modifier,
    genre: String,
    price: String,
    currency: String,
    copyright: String,
    onDialogPositiveButtonClicked: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = {},
        title = null,
        text = {
            DialogContent(
                genre = genre,
                price = price,
                currency = currency,
                copyright = copyright,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDialogPositiveButtonClicked?.invoke()
                }
            ) {
                Text(stringResource(R.string.dialog_detail_ok_button))
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_size))
    )
}

@Composable
private fun DialogContent(
    genre: String,
    price: String,
    currency: String,
    copyright: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(
                R.string.dialog_detail_genre_text,
                genre
            )
        )

        Text(
            text = stringResource(
                R.string.dialog_detail_genre_price,
                price,
                currency
            )
        )

        Text(text = copyright)
    }
}


@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}