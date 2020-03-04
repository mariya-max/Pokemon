package ru.maria.pokemon;

import android.os.Bundle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.maria.pokemon.Model.Data;
import ru.maria.pokemon.Model.PokemonInfo;

public interface DataApi {

    @GET("pokemon")
    Call<Data> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{id}/")
    Call<PokemonInfo> getPokemon(@Path("id") String id);

}
