package com.damncocktail.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.apidata.DrinkList;
import com.damncocktail.util.APIRestServicesCocktail;
import com.damncocktail.util.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    ToggleButton toggleButtonFavorito;
    private ArrayList<String> favoritos = new ArrayList<String>();
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
        toggleButtonFavorito = rootView.findViewById(R.id.toggleButtonFavorito);
        instruccionesCocktail = rootView.findViewById(R.id.instCocktail);
        if (args != null && args.containsKey("nombreCocktail")) {
            String nombCocktail = args.getString("nombreCocktail");
            comprobarAPI(nombCocktail);
        }
        return rootView;
    }


    private void consultarEsFavorito(Cocktail cocktail) {

        String sUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseDatabase.getInstance().getReference("users").child(sUserId).child("favorites").get()
            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        GenericTypeIndicator<ArrayList<String>> arrayListGenericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                        favoritos = task.getResult().getValue(arrayListGenericTypeIndicator);
                        if (favoritos == null) {
                            favoritos = new ArrayList<String>();
                        }

                        if (favoritos.contains(cocktail.getIdDrink())) {
                           toggleButtonFavorito.setChecked(true);
                        }
                        else {
                            toggleButtonFavorito.setChecked(false);
                        }


                    }
                    else {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
        });
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
                        consultarEsFavorito(cocktail);
                        cargarDatos(cocktail);
                        toggleButtonFavorito.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isChecked = toggleButtonFavorito.isChecked();
                                String sUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                                if (isChecked) {
                                    // meterle como favorito
                                    favoritos.add(cocktail.getIdDrink());
                                }
                                else {
                                    // quitarle de favorito
                                    favoritos.remove(cocktail.getIdDrink());
                                }

                                FirebaseDatabase.getInstance()
                                        .getReference("users")
                                        .child(sUserId)
                                        .child("favorites")
                                        .setValue(favoritos);
                            }
                        });
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
                String ingrediente = textViewsIngredientes.get(index).getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("ingrediente", ingrediente);
                Fragment fragment = new IngredienteFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContenedor, fragment)
                        .addToBackStack(null)
                        .commit();
                Log.d("Cocktail selected", cocktail.getStrDrink());
            });
        }
    }
}