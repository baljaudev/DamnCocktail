package com.damncocktail.fragments;

import android.content.Intent;
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


public class AleatorioFragment extends Fragment{

    public static final String CLAVE_KEY = "1";
    TextView nombreCocktail;
    ImageView fotoCocktail;
    LinearLayout parejasIngredientes1;
    LinearLayout parejasIngredientes2;
    LinearLayout parejasIngredientes3;
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
        Call<DrinkList> call = ars.obtenerCocktailRandom(CLAVE_KEY);

        call.enqueue(new Callback<DrinkList>() {
            @Override
            public void onResponse(Call<DrinkList> call, Response<DrinkList> response) {
                if (response.isSuccessful()) {
                    Log.d("RESPUESTA API --> ", response.toString());
                    DrinkList cocktails = response.body();
                    cargarDatos(cocktails);
                } else {
                    Toast.makeText(getActivity(), R.string.api_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DrinkList> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void cargarDatos(DrinkList cocktails) {
        Cocktail cocktail = cocktails.getDrinks().get(0);

        nombreCocktail.setText(cocktail.getStrDrink());
        Glide.with(getActivity()).load(cocktail.getStrDrinkThumb()).into(fotoCocktail);
        instruccionesCocktail.setText(cocktail.getStrInstructions());

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
            /**
             * En el caso de los OnClickListener, se requiere utilizar variables locales finales (variables final)
             * para acceder a variables locales dentro del método anónimo.
             * Dentro del bucle for, estamos creando múltiples instancias de OnClickListener, cada una para un TextView diferente.
             * Sin embargo, cuando se ejecuta el evento onClick(), necesitamos saber a qué TextView específico corresponde ese evento.
             * La variable index se declara como final para que pueda ser accedida desde el interior del método anónimo onClick().
             * Esto nos permite tener una referencia al índice del TextView en ese momento particular,
             * lo cual es útil si queremos realizar alguna acción en función de ese índice específico.
             * Si no usamos final int index = i;, se produciría un error de compilación porque las variables locales no pueden
             * ser accedidas dentro de clases anónimas a menos que sean declaradas como final.
             * En resumen, al usar final int index = i;, estamos asegurando que la variable 'indice'
             * sea accesible dentro del método anónimo onClick() y podamos utilizarla para realizar acciones específicas
             * basadas en el índice del TextView al que se le hace clic.
             */
            final int indice = i; // Variable final para acceder al índice en el evento onClick
            textView.setOnClickListener(v -> {
                String ingredient = textViewsIngredientes.get(indice).getText().toString();
                Log.d("INGREDIENTE", ingredient);
                // TODO: Crear un intent para abrir el Fragment de consulta por ingredientes, mandando el ingrediente como parámetro
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aleatorio, container, false);

        nombreCocktail = rootView.findViewById(R.id.nombreCocktail);
        fotoCocktail = rootView.findViewById(R.id.fotoCocktail);
        parejasIngredientes1 = rootView.findViewById(R.id.parejasIngredientesCocktail1);
        parejasIngredientes2 = rootView.findViewById(R.id.parejasIngredientesCocktail2);
        parejasIngredientes3 = rootView.findViewById(R.id.parejasIngredientesCocktail3);
        instruccionesCocktail = rootView.findViewById(R.id.instruccionesCocktail);

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
