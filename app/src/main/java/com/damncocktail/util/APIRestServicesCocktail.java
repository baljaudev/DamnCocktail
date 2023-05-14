package com.damncocktail.util;

import com.damncocktail.apidata.Cocktail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRestServicesCocktail {

    public static final String BASE_URL = "https://www.thecocktaildb.com/api/json/";

    public static final String CLAVE_KEY = "1";

    @GET("v1/{key}/search.php")
    Call<Cocktail> obtenerCocktailNombre(
            @Path("key") String CLAVE_KEY,
            @Query("s") String nombre);


    @GET("v1/{key}/search.php")
    Call<Cocktail> obtenerCocktailIngrediente(
            @Path("key") String CLAVE_KEY,
            @Query("i") String ingrediente);


    @GET("v1/{key}/search.php")
    Call<Cocktail> obtenerCocktailPrimeraLetra(
            @Path("key") String CLAVE_KEY,
            @Query("f") String letra);


    @GET("v1/{key}/filter.php")
    Call<Cocktail> obtenerCocktailAlcohol(
            @Path("key") String CLAVE_KEY,
            @Query("a") String alcohol);


    @GET("v1/{key}/random.php")
    Call<Cocktail> obtenerCocktailRandom(
            @Path("key") String CLAVE_KEY);

}