package com.sebasiao.pruebaandroidserempre.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiData {
    @GET("posts")
    Observable<Response<ResponseBody>> getPosts();
}
