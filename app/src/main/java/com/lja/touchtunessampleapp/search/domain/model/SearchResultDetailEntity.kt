package com.lja.touchtunessampleapp.search.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDetailEntity(
    val id: Int?,
    val genreName: String,
    val price: String,
    val currency: String,
    val copyright: String
)