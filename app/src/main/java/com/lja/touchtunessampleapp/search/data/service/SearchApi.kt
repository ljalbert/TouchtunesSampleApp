package com.lja.touchtunessampleapp.search.data.service

import com.lja.touchtunessampleapp.search.data.model.SearchResultDto

class SearchApi(private val searchService: SearchService) : ISearchApi {
    override suspend fun search(query: String): List<SearchResultDto> {
        return searchService.getSearchResults(term = query).results
    }
}