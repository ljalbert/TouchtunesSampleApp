package com.lja.touchtunessampleapp.search.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lja.touchtunessampleapp.common.Action
import com.lja.touchtunessampleapp.common.Effect
import com.lja.touchtunessampleapp.common.Event
import com.lja.touchtunessampleapp.common.State
import com.lja.touchtunessampleapp.search.data.model.SearchResultDto
import com.lja.touchtunessampleapp.search.domain.model.SearchResultDetailEntity
import com.lja.touchtunessampleapp.search.domain.model.SearchResultEntity
import com.lja.touchtunessampleapp.search.domain.usecase.ISearchUseCase
import com.lja.touchtunessampleapp.search.mapper.toSearchResulDetailEntity
import com.lja.touchtunessampleapp.search.mapper.toSearchResulEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val searchUseCase: ISearchUseCase) : ViewModel() {

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "aaa"
    }

    private val state = MutableStateFlow<State>(SearchState.None)
    private val event = MutableSharedFlow<Event>()

    fun observeState(): StateFlow<State> = state
    fun observeEvent(): SharedFlow<Event> = event

    sealed class SearchState : State {
        object None : SearchState()
        object Loading : SearchState()
        data class Success(val results: List<SearchResultEntity>) : SearchState()
        object Error : SearchState()
    }

    sealed class SearchAction : Action {
        data class Search(val query: String) : SearchAction()
        data class Success(val results: List<SearchResultDto>) : SearchAction()
        data class Error(val exception: Exception?) : SearchAction()
    }

    sealed class SearchEffect : Effect {
        object None : SearchEffect()
        data class Detail(val resultDetailId: Int?) : SearchEffect()
    }

    sealed class SearchEvent : Event {
        object None : SearchEvent()
        data class Detail(val resultDetail: SearchResultDetailEntity) : SearchEvent()
        object Error : SearchEvent()
    }

    init {
        onSearchQueryChanged(DEFAULT_SEARCH_QUERY)
    }

    private fun reduceAction(action: SearchAction) {
        val newState = when (action) {
            is SearchAction.Search -> action.reduce()
            is SearchAction.Success -> action.reduce(action.results)
            is SearchAction.Error -> action.reduce()
        }

        state.value = newState
    }

    @Suppress("UNUSED")
    private fun SearchAction.Search.reduce(): State {
        viewModelScope.launch { search(query) }
        return SearchState.Loading
    }

    @Suppress("UNUSED")
    private fun SearchAction.Success.reduce(data: List<SearchResultDto>): State {
        return SearchState.Success(data.map { dto -> dto.toSearchResulEntity() })
    }

    @Suppress("UNUSED")
    private fun SearchAction.Error.reduce(): State {
        return SearchState.Error
    }

    @Suppress("UNUSED")
    private fun executeEffect(effect: SearchEffect) {
        val newEvent = when (effect) {
            is SearchEffect.None -> effect.execute()
            is SearchEffect.Detail -> effect.execute(effect.resultDetailId)
        }

        viewModelScope.launch {
            event.emit(newEvent)
        }
    }

    @Suppress("UNUSED")
    private fun SearchEffect.None.execute(): Event = SearchEvent.None

    @Suppress("UNUSED")
    private fun SearchEffect.Detail.execute(resultDetailId: Int?): Event {
        return (state.value as? SearchState.Success)?.results?.firstOrNull { resultDetailId == it.id }
            ?.let { result ->
                SearchEvent.Detail(result.toSearchResulDetailEntity())
            } ?: SearchEvent.Error
    }

    private suspend fun search(query: String) {
        try {
            val searchResults = if (query.isBlank()) {
                searchUseCase.execute(DEFAULT_SEARCH_QUERY)
            } else {
                searchUseCase.execute(query)
            }

            reduceAction(SearchAction.Success(searchResults))
        } catch (e: Exception) {
            reduceAction(SearchAction.Error(e))
        }
    }

    fun onSearchQueryChanged(query: String) {
        reduceAction(SearchAction.Search(query))
    }

    fun onSearchItemClicked(resultDetailId: Int?) {
        executeEffect(SearchEffect.Detail(resultDetailId))
    }

    fun onDetailDialogPositiveButtonClicked() {
        executeEffect(SearchEffect.None)
    }
}