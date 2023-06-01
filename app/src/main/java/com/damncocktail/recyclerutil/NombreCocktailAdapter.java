package com.damncocktail.recyclerutil;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damncocktail.apidata.Cocktail;

import java.util.ArrayList;

public class NombreCocktailAdapter  extends RecyclerView.Adapter<NombreCocktailAdapter.NombreCocktailViewHolder> {

    private ArrayList<Cocktail> cocktailList;
    private OnCocktailClickListener cocktailClickListener;

    @NonNull
    @Override
    public NombreCocktailAdapter.NombreCocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NombreCocktailAdapter.NombreCocktailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
