package com.lja.touchtunessampleapp.di

import com.lja.touchtunessampleapp.BuildConfig
import com.lja.touchtunessampleapp.ui.viewmodel.SearchViewModel
import com.lja.touchtunessampleapp.domain.usecase.ISearchUseCase
import com.lja.touchtunessampleapp.domain.usecase.SearchUseCase
import com.lja.touchtunessampleapp.data.datasource.repository.ISearchRepository
import com.lja.touchtunessampleapp.data.datasource.repository.SearchRepository
import com.lja.touchtunessampleapp.data.datasource.api.SearchApi
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
            .create(SearchApi::class.java)
    }

    factory<ISearchUseCase> {
        SearchUseCase(get())
    }

    factory<ISearchRepository> {
        SearchRepository(get())
    }

    viewModel {
        SearchViewModel(
            searchUseCase = get()
        )
    }
}