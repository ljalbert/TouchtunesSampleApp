package com.lja.touchtunessampleapp.data.mapper

import com.lja.touchtunessampleapp.data.model.SearchResultDto
import com.lja.touchtunessampleapp.domain.model.SearchResultEntity
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun List<SearchResultDto>.toSearchResulEntities() : List<SearchResultEntity> =
    this.map { it.toSearchResulEntity() }

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

