package com.lja.touchtunessampleapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lja.touchtunessampleapp.ui.stateMachine.Event
import com.lja.touchtunessampleapp.ui.stateMachine.State
import com.lja.touchtunessampleapp.domain.model.SearchResultEntity
import com.lja.touchtunessampleapp.domain.usecase.ISearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: ISearchUseCase
) : ViewModel() {

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "aaa"
    }

    private val state = MutableStateFlow<State>(SearchState.None)
    fun observeState(): StateFlow<State> = state

    sealed class SearchState : State {
        object None : SearchState()
        object Loading : SearchState()
        data class Success(val results: List<SearchResultEntity>) : SearchState()
        object Error : SearchState()
    }

    sealed class SearchEvent : Event {
        data class Search(val query: String) : SearchEvent()
        data class Success(val results: List<SearchResultEntity>) : SearchEvent()
        data class Error(val exception: Exception?) : SearchEvent()
    }

    init {
        onSearchQueryChanged(DEFAULT_SEARCH_QUERY)
    }

    fun onSearchQueryChanged(query: String) {
        reduceEvent(SearchEvent.Search(query))
    }

    private fun reduceEvent(event: SearchEvent) {
        val newState = when (event) {
            is SearchEvent.Search -> event.reduce()
            is SearchEvent.Success -> event.reduce(event.results)
            is SearchEvent.Error -> event.reduce()
        }

        state.value = newState
    }

    private fun SearchEvent.Search.reduce(): State {
        viewModelScope.launch { search(query) }
        return SearchState.Loading
    }

    private fun SearchEvent.Success.reduce(data: List<SearchResultEntity>): State {
        return SearchState.Success(data)
    }

    private fun SearchEvent.Error.reduce(): State {
        return SearchState.Error
    }

    private suspend fun search(query: String) {
        try {
            val searchResults = if (query.isBlank()) {
                searchUseCase(DEFAULT_SEARCH_QUERY)
            } else {
                searchUseCase(query)
            }

            reduceEvent(SearchEvent.Success(searchResults))
        } catch (e: Exception) {
            reduceEvent(SearchEvent.Error(e))
        }
    }
}