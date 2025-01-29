package com.lja.touchtunessampleapp.search.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultEntity(
    val id: Int?,
    val artwork: String,
    val name: String,
    val releaseDate: String,
    val genreName: String,
    val price: String,
    val currency: String,
    val copyright: String
)