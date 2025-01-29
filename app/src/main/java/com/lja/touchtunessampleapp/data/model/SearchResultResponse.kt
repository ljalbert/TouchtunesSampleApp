package com.lja.touchtunessampleapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultResponse(
    var resultCount: Int,
    var results: List<SearchResultDto>,
)