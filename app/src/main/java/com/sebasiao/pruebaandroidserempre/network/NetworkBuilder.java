package com.sebasiao.pruebaandroidserempre.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkBuilder {
    Retrofit retrofit;
    ApiData apiData;
    public NetworkBuilder() {
        retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        apiData = retrofit.create(ApiData.class);
    }

    public ApiData getApiData () {
        return apiData;
    }
}
