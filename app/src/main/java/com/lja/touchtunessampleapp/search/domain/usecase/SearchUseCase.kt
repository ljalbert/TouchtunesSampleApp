package com.lja.touchtunessampleapp.search.domain.usecase

import com.lja.touchtunessampleapp.search.data.model.SearchResultDto
import com.lja.touchtunessampleapp.search.data.service.ISearchApi

class SearchUseCase(private val searchApi: ISearchApi) : ISearchUseCase {

    override suspend fun execute(query: String): List<SearchResultDto> {
        return searchApi.search(query)
    }
}