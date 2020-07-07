package com.veyselyenilmez.masterdetailcasestudy.data.network;

import com.veyselyenilmez.masterdetailcasestudy.data.model.Game;
import com.veyselyenilmez.masterdetailcasestudy.data.model.GamesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface APIService {

    @Headers("Content-Type: application/json")
    @GET("games?page_size=10&page=1")
    Call<GamesList> getData();


    @Headers("Content-Type: application/json")
    @GET("games/{id}")
    Call<Game> getDetailedDataById(@Path("id") String id);

}