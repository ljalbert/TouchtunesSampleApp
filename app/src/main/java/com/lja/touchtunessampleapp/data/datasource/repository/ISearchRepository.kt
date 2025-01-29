package com.lja.touchtunessampleapp.data.datasource.repository

import com.lja.touchtunessampleapp.domain.model.SearchResultEntity

interface ISearchRepository {
    suspend operator fun invoke(query: String): List<SearchResultEntity>
}