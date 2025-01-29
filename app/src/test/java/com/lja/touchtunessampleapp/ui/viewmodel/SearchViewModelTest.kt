package com.lja.touchtunessampleapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lja.touchtunessampleapp.data.mapper.toSearchResulEntity
import com.lja.touchtunessampleapp.data.model.SearchResultDto
import com.lja.touchtunessampleapp.domain.usecase.ISearchUseCase
import com.lja.touchtunessampleapp.ui.stateMachine.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchUseCase: ISearchUseCase

    private lateinit var searchViewModel: SearchViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchViewModel = SearchViewModel(searchUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search query succeed`() = runTest {
        //given
        val result = listOf(createFakeSearchResultEntity())

        //when
        Mockito.`when`(searchUseCase(query = any())).thenReturn(result)

        //then
        val states = mutableListOf<State>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            searchViewModel.observeState().toList(states)
        }

        searchViewModel.onSearchQueryChanged("aaa")
        assertEquals(states[0], SearchViewModel.SearchState.Loading)
        assertEquals(
            states[1],
            SearchViewModel.SearchState.Success(result)
        )

        collectJob.cancel()
    }

    @Test
    fun `search query failed`() = runTest {
        //given
        val result = RuntimeException()

        //when
        Mockito.`when`(searchUseCase(query = any())).thenThrow(result)

        //then
        val states = mutableListOf<State>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            searchViewModel.observeState().toList(states)
        }

        searchViewModel.onSearchQueryChanged("aaa")
        assertEquals(states[0], SearchViewModel.SearchState.Loading)
        assertEquals(
            states[1],
            SearchViewModel.SearchState.Error
        )

        collectJob.cancel()
    }

    private fun createFakeSearchResultEntity() =
        SearchResultDto(
            amgArtistId = 372703,
            artistId = 79789358,
            artistName = "AAA",
            artistViewUrl = "https://music.apple.com/us/artist/aaa/79789358?uo=4",
            artworkUrl100 = "https://music.apple.com/us/artist/aaa/79789358?uo=4",
            artworkUrl60 = "https://music.apple.com/us/artist/aaa/79789358?uo=4",
            collectionCensoredName = "All",
            collectionExplicitness = "notExplicit",
            collectionId = 209133438,
            collectionName = "All",
            collectionPrice = 14.99,
            collectionType = "Album",
            collectionViewUrl = "collectionViewUrl",
            contentAdvisoryRating = "contentAdvisoryRating",
            copyright = "℗ 2007 AVEX ENTERTAINMENT INC.",
            country = "USA",
            currency = "currency",
            primaryGenreName = "J-Pop",
            releaseDate = "2007-01-01T08:00:00Z",
            trackCount = 13,
            wrapperType = "collection"
        ).toSearchResulEntity()
}