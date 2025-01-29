package com.lja.touchtunessampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lja.touchtunessampleapp.search.domain.viewmodel.SearchViewModel
import com.lja.touchtunessampleapp.ui.screen.SearchScreen
import com.lja.touchtunessampleapp.ui.theme.TouchTunesSampleAppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TouchTunesSampleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = Color.Black
                ) {
                    val searchViewModel = koinViewModel<SearchViewModel>()

                    SearchScreen(
                        state = searchViewModel.observeState().collectAsStateWithLifecycle().value,
                        event = searchViewModel.observeEvent()
                            .collectAsStateWithLifecycle(initialValue = SearchViewModel.SearchEvent.None).value,
                        onSearchTextChanged = searchViewModel::onSearchQueryChanged,
                        onSearchItemClicked = searchViewModel::onSearchItemClicked,
                        onDetailDialogPositiveButtonClicked = searchViewModel::onDetailDialogPositiveButtonClicked
                    )
                }
            }
        }
    }
}
