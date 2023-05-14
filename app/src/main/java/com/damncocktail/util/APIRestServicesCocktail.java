package com.damncocktail.util;

import com.damncocktail.apidata.Cocktail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIRestServicesCocktail {

    public static final String BASE_URL = "www.thecocktaildb.com/api/json/v1/1/search.php";

    public static final String CLAVE_KEY = "1";

    @GET("{key}")
    Call<Cocktail> obtenerCines(
            @Path("key") String key);
}


/*    @GET("{key}/{latitud},{longitud}")
    Call<WeatherRes> obtenerTiempo(
            @Path("key") String key,
            @Path("latitud") double latitud,
            @Path("longitud") double longitud,
            @Query("exclude") String exclude, // Va detrás de un '?param=' en la URL
            @Query("lang") String lang); // Va detrás de un '&param=' en la URL
*/