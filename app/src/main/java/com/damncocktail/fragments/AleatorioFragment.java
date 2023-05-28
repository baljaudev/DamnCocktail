package com.damncocktail.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.util.APIRestServicesCocktail;
import com.damncocktail.util.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AleatorioFragment extends Fragment implements View.OnClickListener {

    public static final String CLAVE_KEY = "1";
    // Glide Glide;
    TextView nombreCocktail;
    ImageView fotoCocktail;
    //TODO - Ingredientes de la vista
    TextView instruccionesCocktail;

    public AleatorioFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isNetworkAvailable()) {
            consultarCocktail();
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private void consultarCocktail() {
        Retrofit r = RetrofitClient.getClient(APIRestServicesCocktail.BASE_URL);
        APIRestServicesCocktail ars = r.create(APIRestServicesCocktail.class);
        Call<Cocktail> call = ars.obtenerCocktailRandom(CLAVE_KEY);

        call.enqueue(new Callback<Cocktail>() {
            @Override
            public void onResponse(Call<Cocktail> call, Response<Cocktail> response) {
                if (response.isSuccessful()) {
                    Cocktail cocktail = response.body();
                    cargarDatos(cocktail);
                } else {
                    Toast.makeText(getActivity(), R.string.api_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Cocktail> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void cargarDatos(Cocktail cocktail) {
        nombreCocktail.setText(cocktail.getStrDrink());
        Glide.with(this)
                .load(cocktail.getStrDrinkThumb())
                .into(fotoCocktail);
        //TODO - Cargar ingredientes
        instruccionesCocktail.setText(cocktail.getStrInstructions());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aleatorio, container, false);

        nombreCocktail = rootView.findViewById(R.id.nombreCocktail);
        fotoCocktail = rootView.findViewById(R.id.fotoCocktail);
        //TODO - Inicializar demás atributos
        instruccionesCocktail = rootView.findViewById(R.id.instruccionesCocktail);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        //TODO - Implementar onClick para los elementos de la vista
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}