package com.damncocktail.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.apidata.DrinkList;
import com.damncocktail.recyclerutil.NombreCocktailAdapter;
import com.damncocktail.recyclerutil.OnCocktailClickListener;
import com.damncocktail.util.APIRestServicesCocktail;
import com.damncocktail.util.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PrimeraLetraFragment extends Fragment implements View.OnClickListener, OnCocktailClickListener {

    public static final String CLAVE_KEY = "1";
    String letra;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rvCocktails;

    NombreCocktailAdapter nombreCocktailAdapter;

    EditText etBuscarCocktail;

    Button btnBuscarCocktail;

    public PrimeraLetraFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void consultarCocktail(String letra) {
        Retrofit r = RetrofitClient.getClient(APIRestServicesCocktail.BASE_URL);
        APIRestServicesCocktail ars = r.create(APIRestServicesCocktail.class);
        Call<DrinkList> drinkListCall = ars.obtenerCocktailPrimeraLetra(CLAVE_KEY, letra);

        drinkListCall.enqueue(new Callback<DrinkList>() {
            @Override
            public void onResponse(Call<DrinkList> call, Response<DrinkList> response) {
                if (response.isSuccessful()) {
                    DrinkList cocktails = response.body();
                    Log.d("URL", response.raw().request().url().toString());
                    cargarRV(cocktails.getDrinks());
                } else {
                    Log.e("ERROR", response.message());
                }
            }

            @Override
            public void onFailure(Call<DrinkList> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void cargarRV(List<Cocktail> cocktail) {
        if (cocktail == null) {
            Toast.makeText(getActivity(), R.string.no_ingredient, Toast.LENGTH_SHORT).show();
            return;
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        nombreCocktailAdapter = new NombreCocktailAdapter((ArrayList<Cocktail>) cocktail, getActivity());
        nombreCocktailAdapter.setOnCocktailClickListener(this);
        rvCocktails.setHasFixedSize(true);
        rvCocktails.setLayoutManager(linearLayoutManager);
        rvCocktails.setAdapter(nombreCocktailAdapter);

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_primera_letra, container, false);

        rvCocktails = rootView.findViewById(R.id.rvCocktails);
        nombreCocktailAdapter = new NombreCocktailAdapter(new ArrayList<>(), getActivity());
        rvCocktails.setAdapter(nombreCocktailAdapter);
        rvCocktails.setLayoutManager(new LinearLayoutManager(getActivity()));
        etBuscarCocktail = rootView.findViewById(R.id.et_busca_cocktail);
        btnBuscarCocktail = rootView.findViewById(R.id.btn_busca_cocktail);
        btnBuscarCocktail.setOnClickListener(this);

        if (etBuscarCocktail != null) {
            datosAPI(letra);
        } else {
            Toast.makeText(getActivity(), R.string.no_letra, Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_busca_cocktail) {
            letra = etBuscarCocktail.getText().toString();

            if (letra.isEmpty()) {
                Toast.makeText(getActivity(), R.string.no_letra, Toast.LENGTH_SHORT).show();
                return;
            }
            datosAPI(letra);
        }
    }

    private void datosAPI(String letra) {
        if (isNetworkAvailable()) {
            consultarCocktail(letra);
        } else {
            Toast.makeText(getActivity(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCocktailClick(Cocktail cocktail) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("nombreCocktail", cocktail.getStrDrink());
        Fragment fragment = new CocktailFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContenedor, fragment)
                .addToBackStack(null)
                .commit();
        Log.d("Cocktail selected", cocktail.getStrDrink());
    }
}
