package com.lja.touchtunessampleapp.search.domain.usecase

import com.lja.touchtunessampleapp.search.data.model.SearchResultDto

interface ISearchUseCase {
    suspend fun execute(query: String): List<SearchResultDto>
}