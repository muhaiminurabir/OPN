package com.misfit.opnmart.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient


class Controller {
    companion object {

        //var BASE_URL = "https://c8d92d0a-6233-4ef7-a229-5a91deb91ea1.mock.pstmn.io/"
        var BASE_URL = "https://virtserver.swaggerhub.com/m-tul/opn-mobile-challenge-api/1.0.0/"
        var httpClient = OkHttpClient.Builder()
        fun create(): ApiService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build()
            return retrofit.create(ApiService::class.java)

        }
    }
}