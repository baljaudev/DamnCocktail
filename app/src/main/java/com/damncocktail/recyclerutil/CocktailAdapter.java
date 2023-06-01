package com.damncocktail.recyclerutil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;

import java.util.ArrayList;

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    private ArrayList<Cocktail> cocktailList;
    private OnCocktailClickListener cocktailClickListener;

    public CocktailAdapter(ArrayList<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }


    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cocktail, parent, false);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailAdapter.CocktailViewHolder holder, int position) {
        holder.bindCocktail(cocktailList.get(position));
    }

    @Override
    public int getItemCount() {
        return cocktailList.size();
    }

    public class CocktailViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombre;
        private ImageView ivImagen;

        public CocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreCocktail);
            ivImagen = itemView.findViewById(R.id.fotoCocktail);
            ivImagen.setOnClickListener(v -> {
                if (cocktailClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Cocktail cocktail = cocktailList.get(position);
                        cocktailClickListener.onCocktailClick(cocktail);
                    }
                }
            });

        }

        public void bindCocktail(Cocktail cocktail) {
            tvNombre.setText(cocktail.getStrDrink());
            Glide.with(itemView.getContext())
                    .load(cocktail.getStrDrinkThumb())
                    .into(ivImagen);
        }
    }
    public void setOnCocktailClickListener(OnCocktailClickListener listener) {
        this.cocktailClickListener = listener;
    }

}
