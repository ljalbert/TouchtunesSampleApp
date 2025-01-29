package com.lja.touchtunessampleapp.search.mapper

import com.lja.touchtunessampleapp.search.data.model.SearchResultDto
import com.lja.touchtunessampleapp.search.domain.model.SearchResultDetailEntity
import com.lja.touchtunessampleapp.search.domain.model.SearchResultEntity
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun SearchResultDto.toSearchResulEntity(): SearchResultEntity {
    val releaseYear =
        OffsetDateTime.parse(releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()

    return SearchResultEntity(
        id = collectionId,
        artwork = artworkUrl100.orEmpty(),
        name = collectionName.orEmpty(),
        releaseDate = releaseYear,
        genreName = primaryGenreName.orEmpty(),
        price = collectionPrice?.toString().orEmpty(),
        currency = currency.orEmpty(),
        copyright = copyright.orEmpty()
    )
}

fun SearchResultEntity.toSearchResulDetailEntity(): SearchResultDetailEntity {
    return SearchResultDetailEntity(
        id = id,
        genreName = genreName,
        price = price,
        currency = currency,
        copyright = copyright
    )
}

