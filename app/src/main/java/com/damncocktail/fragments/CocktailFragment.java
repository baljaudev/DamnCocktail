package com.damncocktail.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.apidata.DrinkList;
import com.damncocktail.util.APIRestServicesCocktail;
import com.damncocktail.util.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CocktailFragment extends Fragment {

    public static final String CLAVE_KEY = "1";
    TextView nombreCocktail;
    ImageView fotoCocktail;
    LinearLayout parejasIngredientes1;
    LinearLayout parejasIngredientes2;
    LinearLayout parejasIngredientes3;
    LinearLayout medidasIngredientesCocktail1;
    LinearLayout medidasIngredientesCocktail2;
    LinearLayout medidasIngredientesCocktail3;
    TextView instruccionesCocktail;
    public CocktailFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cocktail, container, false);
        Bundle args = getArguments();
        nombreCocktail = rootView.findViewById(R.id.nomCocktail);
        fotoCocktail = rootView.findViewById(R.id.imgCocktail);
        parejasIngredientes1 = rootView.findViewById(R.id.bloqueIngredientesCocktail1);
        parejasIngredientes2 = rootView.findViewById(R.id.bloqueIngredientesCocktail2);
        parejasIngredientes3 = rootView.findViewById(R.id.bloqueIngredientesCocktail3);
        medidasIngredientesCocktail1 = rootView.findViewById(R.id.medIngredientesCocktail1);
        medidasIngredientesCocktail2 = rootView.findViewById(R.id.medIngredientesCocktail2);
        medidasIngredientesCocktail3 = rootView.findViewById(R.id.medIngredientesCocktail3);
        instruccionesCocktail = rootView.findViewById(R.id.instCocktail);
        if (args != null && args.containsKey("nombreCocktail")) {
            String nombCocktail = args.getString("nombreCocktail");
            comprobarAPI(nombCocktail);
        }
        return rootView;
    }

    private void comprobarAPI(String nombre) {
        if (isNetworkAvailable()) {
            consultarCocktail(nombre);
        } else {
            Toast.makeText(getActivity(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarCocktail(String nombre) {
        Retrofit r = RetrofitClient.getClient(APIRestServicesCocktail.BASE_URL);
        APIRestServicesCocktail ars = r.create(APIRestServicesCocktail.class);
        Call<DrinkList> drinkListCall = ars.obtenerCocktailNombre(CLAVE_KEY, nombre);

        drinkListCall.enqueue(new Callback<DrinkList>() {
            @Override
            public void onResponse(Call<DrinkList> call, Response<DrinkList> response) {
                if (response.isSuccessful()) {
                    DrinkList drinkList = response.body();
                    Log.d("URL", response.raw().request().url().toString());
                    if (drinkList != null && drinkList.getDrinks() != null) {
                        Cocktail cocktail = drinkList.getDrinks().get(0);
                        cargarDatos(cocktail);
                    }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void cargarDatos(Cocktail cocktail) {
        nombreCocktail.setText(cocktail.getStrDrink());
        Glide.with(this)
                .load(cocktail.getStrDrinkThumb())
                .into(fotoCocktail);
        cargarIngredientes(cocktail);
        cargarMedidas(cocktail);
        instruccionesCocktail.setText(cocktail.getStrInstructions());
    }

    private void cargarMedidas(Cocktail cocktail) {
        List<TextView> textViewsMedidas = new ArrayList<>();

        for (int i = 1; i <= cocktail.getNumIngredientes(); i++) {
            String medida = cocktail.getMedida(i);
            if (medida != null && !medida.isEmpty()) {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView.setText(medida);
                textViewsMedidas.add(textView);
            }
        }

        for (int i = 0; i < textViewsMedidas.size(); i++) {
            TextView textView = textViewsMedidas.get(i);
            if (i < 5) {
                medidasIngredientesCocktail1.addView(textView);
            } else if (i < 10) {
                medidasIngredientesCocktail2.addView(textView);
            } else {
                medidasIngredientesCocktail3.addView(textView);
            }
        }
    }
    private void cargarIngredientes(Cocktail cocktail) {
        List<TextView> textViewsIngredientes = new ArrayList<>();

        for (int i = 1; i <= cocktail.getNumIngredientes(); i++) {
            String ingrediente = cocktail.getIngredient(i);
            if (ingrediente != null && !ingrediente.isEmpty()) {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView.setText(ingrediente);
                textViewsIngredientes.add(textView);
            }
        }

        for (int i = 0; i < textViewsIngredientes.size(); i++) {
            TextView textView = textViewsIngredientes.get(i);
            if (i < 5) {
                parejasIngredientes1.addView(textView);
            } else if (i < 10) {
                parejasIngredientes2.addView(textView);
            } else {
                parejasIngredientes3.addView(textView);
            }

            final int index = i;
            textView.setOnClickListener(v -> {
                String ingredient = textViewsIngredientes.get(index).getText().toString();
                Toast.makeText(getActivity(), "Clic en el ingrediente: " + ingredient, Toast.LENGTH_SHORT).show();
            });
        }
    }
}