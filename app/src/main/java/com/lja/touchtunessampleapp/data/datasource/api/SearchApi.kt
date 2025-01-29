package com.lja.touchtunessampleapp.data.datasource.api

import com.lja.touchtunessampleapp.data.model.SearchResultResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    companion object {
        const val MEDIA = "music"
        const val ENTITY = "album"
        const val ATTRIBUTE = "mixTerm"
    }

    @GET("search")
    suspend fun getSearchResults(
        @Query("media") media: String? = MEDIA,
        @Query("entity") entity: String? = ENTITY,
        @Query("attribute") attribute: String? = ATTRIBUTE,
        @Query("term") term: String?,
    ): SearchResultResponse
}


//https://itunes.apple.com/search?media=music&entity=album&attribute=mixTerm&term=aaa