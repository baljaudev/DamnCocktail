package com.damncocktail.fragments;



import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.util.APIRestServicesCocktail;
import com.damncocktail.util.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NombreFragment extends Fragment implements View.OnClickListener {
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

    EditText etBuscarCocktail;

    Button btnBuscarCocktail;



    public NombreFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void consultarCocktail(String nombre) {
        Retrofit r = RetrofitClient.getClient(APIRestServicesCocktail.BASE_URL);
        APIRestServicesCocktail ars = r.create(APIRestServicesCocktail.class);
        Call<Cocktail> cocktailCall = ars.obtenerCocktailNombre(CLAVE_KEY, nombre);

        cocktailCall.enqueue(new Callback<Cocktail>() {
            @Override
            public void onResponse(Call<Cocktail> call, Response<Cocktail> response) {
                if (response.isSuccessful()) {
                    Cocktail cocktail = response.body();
                    if (cocktail != null) {
                        cargarDatos(cocktail);
                    }
                } else {
                    Log.e("ERROR", response.message());
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
        // La imagen se saca de la url https://www.thecocktaildb.com/images/ingredients/XXXXXX-Medium.png
        // donde XXXXXX es el nombre del ingrediente.
        cocktail.getNumIngredientes();

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
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ingredient = textViewsIngredientes.get(index).getText().toString();
                    Toast.makeText(getActivity(), "Clic en el ingrediente: " + ingredient, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nombre, container, false);

        nombreCocktail = rootView.findViewById(R.id.nombreCocktail);
        fotoCocktail = rootView.findViewById(R.id.fotoCocktail);
        parejasIngredientes1 = rootView.findViewById(R.id.parejasIngredientesCocktail1);
        parejasIngredientes2 = rootView.findViewById(R.id.parejasIngredientesCocktail2);
        parejasIngredientes3 = rootView.findViewById(R.id.parejasIngredientesCocktail3);
        medidasIngredientesCocktail1 = rootView.findViewById(R.id.medidasIngredientesCocktail1);
        medidasIngredientesCocktail2 = rootView.findViewById(R.id.medidasIngredientesCocktail2);
        medidasIngredientesCocktail3 = rootView.findViewById(R.id.medidasIngredientesCocktail3);
        instruccionesCocktail = rootView.findViewById(R.id.instruccionesCocktail);
        etBuscarCocktail = rootView.findViewById(R.id.et_busca_cocktail);
        btnBuscarCocktail = rootView.findViewById(R.id.btn_busca_cocktail);

        btnBuscarCocktail.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_busca_cocktail) {
            if (isNetworkAvailable()) {
                String nombre = etBuscarCocktail.getText().toString();
                consultarCocktail(nombre);
            } else {
                Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
            }
    }
    }
}