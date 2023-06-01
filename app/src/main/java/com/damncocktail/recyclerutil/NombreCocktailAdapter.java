package com.damncocktail.recyclerutil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;

import java.util.ArrayList;
import java.util.List;

public class NombreCocktailAdapter  extends RecyclerView.Adapter<NombreCocktailAdapter.NombreCocktailViewHolder> {

    private ArrayList<Cocktail> cocktailList;


    public NombreCocktailAdapter(ArrayList<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
    }

    @NonNull
    @Override
    public NombreCocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nombre_cocktail, parent, false);
        return new NombreCocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NombreCocktailAdapter.NombreCocktailViewHolder holder, int position) {
        holder.bindCocktail(cocktailList.get(position));
    }

    @Override
    public int getItemCount() {
        return cocktailList.size();
    }

    public class NombreCocktailViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombre;
        private ImageView ivImagen;

        private TextView ingredientesCocktail;

        private TextView medidasIngredientesCocktail;

        LinearLayout parejasIngredientes1;
        LinearLayout parejasIngredientes2;
        LinearLayout parejasIngredientes3;

        LinearLayout medidasIngredientesCocktail1;
        LinearLayout medidasIngredientesCocktail2;
        LinearLayout medidasIngredientesCocktail3;

        private TextView instruccionesCocktail;


        public NombreCocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreCocktail);
            ivImagen = itemView.findViewById(R.id.fotoCocktail);
            ingredientesCocktail = itemView.findViewById(R.id.ingredientesCocktail);
            medidasIngredientesCocktail = itemView.findViewById(R.id.medidasIngredientesCocktail);
            parejasIngredientes1 = itemView.findViewById(R.id.parejasIngredientesCocktail1);
            parejasIngredientes2 = itemView.findViewById(R.id.parejasIngredientesCocktail2);
            parejasIngredientes3 = itemView.findViewById(R.id.parejasIngredientesCocktail3);
            medidasIngredientesCocktail1 = itemView.findViewById(R.id.medidasIngredientesCocktail1);
            medidasIngredientesCocktail2 = itemView.findViewById(R.id.medidasIngredientesCocktail2);
            medidasIngredientesCocktail3 = itemView.findViewById(R.id.medidasIngredientesCocktail3);
            instruccionesCocktail = itemView.findViewById(R.id.instruccionesCocktail);



        }



        public void bindCocktail(Cocktail cocktail) {
            tvNombre.setText(cocktail.getStrDrink());
            Glide.with(itemView.getContext())
                    .load(cocktail.getStrDrinkThumb())
                    .into(ivImagen);
            cargarIngredientes(cocktail);
            cargarMedidas(cocktail);
            instruccionesCocktail.setText(cocktail.getStrInstructions());

        }

        private void cargarMedidas(Cocktail cocktail) {
            List<TextView> textViewsMedidas = new ArrayList<>();

            for (int i = 1; i <= cocktail.getNumIngredientes(); i++) {
                String medida = cocktail.getMedida(i);
                if (medida != null && !medida.isEmpty()) {
                    TextView textView = new TextView(itemView.getContext());
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
                    TextView textView = new TextView(itemView.getContext());
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
                    Toast.makeText(itemView.getContext(), "Clic en el ingrediente: " + ingredient, Toast.LENGTH_SHORT).show();
                });
            }
        }


    }



}
