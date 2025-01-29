package com.lja.touchtunessampleapp.domain.usecase

import com.lja.touchtunessampleapp.domain.model.SearchResultEntity

interface ISearchUseCase {
    suspend operator fun invoke(query: String): List<SearchResultEntity>
}