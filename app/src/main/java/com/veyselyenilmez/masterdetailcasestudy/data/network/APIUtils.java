package com.veyselyenilmez.masterdetailcasestudy.data.network;

public class APIUtils {

    private APIUtils() {}

    public static final String BASE_URL = "https://api.rawg.io/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}