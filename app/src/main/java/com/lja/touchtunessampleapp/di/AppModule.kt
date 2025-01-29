package com.lja.touchtunessampleapp.di

import com.lja.touchtunessampleapp.BuildConfig
import com.lja.touchtunessampleapp.search.domain.viewmodel.SearchViewModel
import com.lja.touchtunessampleapp.search.domain.usecase.ISearchUseCase
import com.lja.touchtunessampleapp.search.domain.usecase.SearchUseCase
import com.lja.touchtunessampleapp.search.data.service.ISearchApi
import com.lja.touchtunessampleapp.search.data.service.SearchApi
import com.lja.touchtunessampleapp.search.data.service.SearchService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * Koin modules
 */
val appModule = module {

    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
    }.build()

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
            .create(SearchService::class.java)
    }

    factory<ISearchUseCase> {
        SearchUseCase(get())
    }

    factory<ISearchApi> {
        SearchApi(get())
    }

    viewModel {
        SearchViewModel(
            searchUseCase = get()
        )
    }
}