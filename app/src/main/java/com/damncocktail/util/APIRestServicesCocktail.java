package com.damncocktail.util;

import com.damncocktail.apidata.Cocktail;
import com.damncocktail.apidata.DrinkList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRestServicesCocktail {

    public static final String BASE_URL = "https://www.thecocktaildb.com/api/json/";

    @GET("v1/{key}/search.php")
    Call<DrinkList> obtenerCocktailNombre(
            @Path("key") String key,
            @Query("s") String nombre);


    @GET("v1/{key}/search.php")
    Call<DrinkList> obtenerCocktailIngrediente(
            @Path("key") String key,
            @Query("i") String ingrediente);


    @GET("v1/{key}/search.php")
    Call<DrinkList> obtenerCocktailPrimeraLetra(
            @Path("key") String key,
            @Query("f") String letra);


    @GET("v1/{key}/filter.php")
    Call<DrinkList> obtenerCocktailAlcohol(
            @Path("key") String key,
            @Query("a") String alcohol);


    @GET("v1/{key}/random.php")
    Call<DrinkList> obtenerCocktailRandom(
            @Path("key") String key);

}