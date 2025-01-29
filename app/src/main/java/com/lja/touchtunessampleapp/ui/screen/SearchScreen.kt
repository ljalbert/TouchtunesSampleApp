package com.lja.touchtunessampleapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.lja.touchtunessampleapp.R
import com.lja.touchtunessampleapp.common.Event
import com.lja.touchtunessampleapp.common.State
import com.lja.touchtunessampleapp.search.domain.model.SearchResultEntity
import com.lja.touchtunessampleapp.search.domain.viewmodel.SearchViewModel
import com.lja.touchtunessampleapp.ui.components.AlbumDialog
import com.lja.touchtunessampleapp.ui.components.CircularProgressBar
import com.lja.touchtunessampleapp.ui.theme.TouchTunesSampleAppTheme


@Composable
fun SearchScreen(
    state: State,
    event: Event,
    onSearchTextChanged: (String) -> Unit,
    onSearchItemClicked: (Int?) -> Unit,
    onDetailDialogPositiveButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SearchView(onSearchTextChanged = onSearchTextChanged)

        when (state) {
            is SearchViewModel.SearchState.Loading -> {
                CircularProgressBar()
            }

            is SearchViewModel.SearchState.Success -> {
                SearchList(
                    results = state.results,
                    onSearchItemClicked = onSearchItemClicked
                )
            }

            is SearchViewModel.SearchState.Error -> {
                Toast.makeText(LocalContext.current, R.string.search_error_text, Toast.LENGTH_LONG)
                    .show()
            }

            is SearchViewModel.SearchState.None -> {}
        }


        when (event) {
            is SearchViewModel.SearchEvent.Detail -> {
                with(event.resultDetail) {
                    AlbumDialog(
                        onDialogPositiveButtonClicked = onDetailDialogPositiveButtonClicked,
                        genre = genreName,
                        price = price,
                        currency = currency,
                        copyright = copyright
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchView(onSearchTextChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textFieldState = remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        value = textFieldState.value,
        onValueChange = { value ->
            textFieldState.value = value
            onSearchTextChanged(value.text)
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "search icon",
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.margin_padding_size_medium))
                    .size(dimensionResource(R.dimen.search_view_icon_size))
            )
        },
        trailingIcon = {
            if (textFieldState.value.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        textFieldState.value = TextFieldValue("")
                        onSearchTextChanged(textFieldState.value.text)
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "close icon",
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.margin_padding_size_micro))
                            .size(dimensionResource(R.dimen.search_view_icon_size))
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        )
    )
}

@Composable
private fun SearchList(
    results: List<SearchResultEntity>,
    onSearchItemClicked: (Int?) -> Unit
) {
    val trainings = remember { results }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(R.dimen.search_grid_width)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.margin_padding_size_medium)),
        content = {
            items(trainings.size) { index ->
                SearchItem(
                    training = trainings[index],
                    onSearchItemClicked = onSearchItemClicked
                )
            }
        })
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchItem(
    training: SearchResultEntity,
    onSearchItemClicked: (Int?) -> Unit
) {
    Card(
        onClick = { onSearchItemClicked.invoke(training.id) },
        modifier = Modifier
            .padding(dimensionResource(R.dimen.margin_padding_size_small))
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.search_grid_eight))

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            GlideImage(
                model = training.artwork,
                contentDescription = null,
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.search_item_image_height)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_padding_size_small)),
            ) {
                Text(
                    text = training.name.orEmpty(),
                    style = typography.titleSmall,
                    maxLines = 2
                )
                Text(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.margin_padding_size_micro)),
                    text = training.releaseDate.orEmpty(),
                    style = typography.bodySmall,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    TouchTunesSampleAppTheme {
        SearchView {}
    }
}
