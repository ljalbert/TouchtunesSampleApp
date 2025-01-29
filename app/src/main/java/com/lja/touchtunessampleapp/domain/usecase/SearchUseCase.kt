package com.lja.touchtunessampleapp.domain.usecase

import com.lja.touchtunessampleapp.data.datasource.repository.ISearchRepository
import com.lja.touchtunessampleapp.domain.model.SearchResultEntity

class SearchUseCase(
    private val searchRepository: ISearchRepository
) : ISearchUseCase {

    override suspend operator fun invoke(query: String): List<SearchResultEntity> {
        return searchRepository(query)
    }
}