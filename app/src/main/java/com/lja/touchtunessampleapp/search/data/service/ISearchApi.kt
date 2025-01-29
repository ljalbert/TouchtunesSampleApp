package com.lja.touchtunessampleapp.search.data.service

import com.lja.touchtunessampleapp.search.data.model.SearchResultDto

interface ISearchApi {
    suspend fun search(query: String): List<SearchResultDto>
}