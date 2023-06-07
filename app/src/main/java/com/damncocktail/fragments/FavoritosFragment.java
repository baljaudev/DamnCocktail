package com.damncocktail.fragments;

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

import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.apidata.DrinkList;
import com.damncocktail.recyclerutil.CocktailAdapter;
import com.damncocktail.recyclerutil.OnCocktailClickListener;
import com.damncocktail.util.APIRestServicesCocktail;
import com.damncocktail.util.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoritosFragment extends Fragment implements OnCocktailClickListener {

    public static final String CLAVE_KEY = "1";

    LinearLayoutManager linearLayoutManager;
    CocktailAdapter cocktailAdapter;
    RecyclerView rvCocktails;

    public FavoritosFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_favoritos, container, false);
        rvCocktails = vista.findViewById(R.id.rvCocktails);


        linearLayoutManager = new LinearLayoutManager(getActivity());
        cocktailAdapter = new CocktailAdapter(new ArrayList<>());
        cocktailAdapter.setOnCocktailClickListener(this);
        rvCocktails.setHasFixedSize(true);
        rvCocktails.setLayoutManager(linearLayoutManager);
        rvCocktails.setAdapter(cocktailAdapter);

        return vista;
    }

    private void consultarCocktail(String id) {
        Retrofit r = RetrofitClient.getClient(APIRestServicesCocktail.BASE_URL);
        APIRestServicesCocktail ars = r.create(APIRestServicesCocktail.class);
        Call<DrinkList> drinkListCall = ars.obtenerCocktailPorId(CLAVE_KEY, id);

        drinkListCall.enqueue(new Callback<DrinkList>() {
            @Override
            public void onResponse(Call<DrinkList> call, Response<DrinkList> response) {
                if (response.isSuccessful()) {
                    DrinkList cocktails = response.body();
                    Log.d("URL", response.raw().request().url().toString());
                    cocktailAdapter.addCocktail(cocktails.getDrinks().get(0));
                    Log.d("xxx", "" + cocktailAdapter.getItemCount());
                    cocktailAdapter.notifyDataSetChanged();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String sUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseDatabase.getInstance().getReference("users").child(sUserId).child("favorites").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            GenericTypeIndicator<ArrayList<String>> arrayListGenericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                            ArrayList<String> favoritos = task.getResult().getValue(arrayListGenericTypeIndicator);
                            if (favoritos == null) {
                                favoritos = new ArrayList<String>();
                            }

                            for (String favIdDrink : favoritos) {
                                consultarCocktail(favIdDrink);
                            }
                        } else {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                    }
                });
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