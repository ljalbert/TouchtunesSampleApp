package com.lja.touchtunessampleapp.data.datasource.repository

import com.lja.touchtunessampleapp.data.datasource.api.SearchApi
import com.lja.touchtunessampleapp.data.mapper.toSearchResulEntities
import com.lja.touchtunessampleapp.domain.model.SearchResultEntity

class SearchRepository(private val searchApi: SearchApi) : ISearchRepository {
    override suspend operator fun invoke(query: String): List<SearchResultEntity> {
        return searchApi.getSearchResults(term = query).results.toSearchResulEntities()
    }
}