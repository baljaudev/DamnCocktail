package com.damncocktail.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.apidata.DrinkList;
import com.damncocktail.recyclerutil.CocktailAdapter;
import com.damncocktail.recyclerutil.OnCocktailClickListener;
import com.damncocktail.util.APIRestServicesCocktail;
import com.damncocktail.util.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SiNoAlcoholFragment extends Fragment implements View.OnClickListener, OnCocktailClickListener {

    public static final String CLAVE_KEY = "1";
    public static final String ALCOHOL = "Alcoholic";
    public static final String NO_ALCOHOL = "Non_Alcoholic";

    String filtro;
    Button btnFiltrar;
    RadioGroup radioGroupFiltro;
    RadioButton radioButtonAlcoholicas;
    RadioButton radioButtonNoAlcoholicas;
    CocktailAdapter cocktailAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvCocktails;

    public SiNoAlcoholFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void consultarCocktail() {
        Retrofit r = RetrofitClient.getClient(APIRestServicesCocktail.BASE_URL);
        APIRestServicesCocktail ars = r.create(APIRestServicesCocktail.class);
        Call<DrinkList> drinkListCall = ars.obtenerCocktailAlcohol(CLAVE_KEY,filtro);

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
        linearLayoutManager = new LinearLayoutManager(getActivity());
        cocktailAdapter = new CocktailAdapter((ArrayList<Cocktail>) cocktail);
        cocktailAdapter.setOnCocktailClickListener(this);
        rvCocktails.setHasFixedSize(true);
        rvCocktails.setLayoutManager(linearLayoutManager);
        rvCocktails.setAdapter(cocktailAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_si_no_alcohol, container, false);
        radioGroupFiltro = vista.findViewById(R.id.radioGroupFiltro);
        radioButtonAlcoholicas = vista.findViewById(R.id.radioButtonAlcoholicas);
        radioButtonNoAlcoholicas = vista.findViewById(R.id.radioButtonNoAlcoholicas);
        rvCocktails = vista.findViewById(R.id.rvCocktails);
        cocktailAdapter = new CocktailAdapter(new ArrayList<>());
        rvCocktails.setAdapter(cocktailAdapter);
        rvCocktails.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnFiltrar = vista.findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this);
        return vista;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onClick(View v) {
        filtro = radioGroupFiltro.getCheckedRadioButtonId() == R.id.radioButtonAlcoholicas ? ALCOHOL : NO_ALCOHOL;
        if (isNetworkAvailable()) {
            consultarCocktail();
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
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