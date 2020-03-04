package ru.maria.pokemon;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    String BASE_URL = "https://pokeapi.co/api/v2/";

    private static DataApi dataApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dataApi = retrofit.create(DataApi.class);
    }

    public static DataApi getApi() {
        return dataApi;
    }
}
