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
import com.lja.touchtunessampleapp.domain.model.SearchResultEntity
import com.lja.touchtunessampleapp.ui.stateMachine.State
import com.lja.touchtunessampleapp.ui.components.AlbumDialog
import com.lja.touchtunessampleapp.ui.components.CircularProgressBar
import com.lja.touchtunessampleapp.ui.theme.TouchTunesSampleAppTheme
import com.lja.touchtunessampleapp.ui.viewmodel.SearchViewModel


@Composable
fun SearchScreen(
    state: State,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openDialog = remember { mutableStateOf(false) }
    val itemIdClicked = remember { mutableStateOf(0) }

    SearchContent(
        modifier = modifier,
        state = state,
        onSearchQueryChanged = onSearchQueryChanged,
        onSearchItemClicked = { id ->
            itemIdClicked.value = id
            openDialog.value = true
        }
    )

    if (openDialog.value && state is SearchViewModel.SearchState.Success) {
        state.results.firstOrNull { it.id == itemIdClicked.value }?.let { item ->
            AlbumDialog(
                onConfirmationClicked = { openDialog.value = false },
                genre = item.genreName,
                price = item.price,
                currency = item.currency,
                copyright = item.copyright
            )
        }
    }
}

@Composable
fun SearchContent(
    state: State,
    onSearchQueryChanged: (query: String) -> Unit,
    onSearchItemClicked: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchBar(onSearchQueryChanged = onSearchQueryChanged)

        when (state) {
            is SearchViewModel.SearchState.Loading -> {
                CircularProgressBar()
            }

            is SearchViewModel.SearchState.Success -> {
                SearchList(
                    results = state.results,
                    onSearchItemClicked = onSearchItemClicked,
                )
            }

            is SearchViewModel.SearchState.Error -> {
                Toast.makeText(LocalContext.current, R.string.search_error_text, Toast.LENGTH_LONG)
                    .show()
            }

            is SearchViewModel.SearchState.None -> {}
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textFieldState = remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = textFieldState.value,
        onValueChange = { value ->
            textFieldState.value = value
            onSearchQueryChanged(value.text)
        },
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
                        onSearchQueryChanged(textFieldState.value.text)
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
    onSearchItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trainings = remember { results }
    LazyVerticalGrid(
        modifier = modifier,
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
    onSearchItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(dimensionResource(R.dimen.margin_padding_size_small))
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.search_grid_eight)),
        onClick = { onSearchItemClicked(training.id) },
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
                    text = training.name,
                    style = typography.titleSmall,
                    maxLines = 2
                )
                Text(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.margin_padding_size_micro)),
                    text = training.releaseDate,
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
        SearchBar(onSearchQueryChanged = {})
    }
}
