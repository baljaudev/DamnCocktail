package com.damncocktail.recyclerutil;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.damncocktail.R;
import com.damncocktail.apidata.Cocktail;
import com.damncocktail.fragments.IngredienteFragment;

import java.util.ArrayList;
import java.util.List;

public class NombreCocktailAdapter  extends RecyclerView.Adapter<NombreCocktailAdapter.NombreCocktailViewHolder> {

    private ArrayList<Cocktail> cocktailList;
    private FragmentActivity mActivity; // Variable para almacenar la actividad o el administrador de fragmentos

    private OnCocktailClickListener cocktailClickListener;

    public NombreCocktailAdapter(ArrayList<Cocktail> cocktailList, FragmentActivity activity) {
        this.cocktailList = cocktailList;
        this.mActivity = activity; // Almacena la referencia de la actividad o el administrador de fragmentos
    }

    @NonNull
    @Override
    public NombreCocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nombre_cocktail, parent, false);
        return new NombreCocktailViewHolder(view, mActivity); // Pasa la referencia de la actividad o el administrador de fragmentos
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

        private FragmentActivity mActivity; // Variable para almacenar la referencia de la actividad o el administrador de fragmentos

        public NombreCocktailViewHolder(@NonNull View itemView, FragmentActivity activity) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreCocktailNombre);
            ivImagen = itemView.findViewById(R.id.fotoCocktailNombre);
            ingredientesCocktail = itemView.findViewById(R.id.ingredientesCocktailNombre);
            medidasIngredientesCocktail = itemView.findViewById(R.id.medidasIngredientesCocktailNombre);
            parejasIngredientes1 = itemView.findViewById(R.id.parejasIngredientesCocktail1Nombre);
            parejasIngredientes2 = itemView.findViewById(R.id.parejasIngredientesCocktail2Nombre);
            parejasIngredientes3 = itemView.findViewById(R.id.parejasIngredientesCocktail3Nombre);
            medidasIngredientesCocktail1 = itemView.findViewById(R.id.medidasIngredientesCocktail1Nombre);
            medidasIngredientesCocktail2 = itemView.findViewById(R.id.medidasIngredientesCocktail2Nombre);
            medidasIngredientesCocktail3 = itemView.findViewById(R.id.medidasIngredientesCocktail3Nombre);
            instruccionesCocktail = itemView.findViewById(R.id.instruccionesCocktailNombre);
            ivImagen.setOnClickListener(v -> {
                if (cocktailClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Cocktail cocktail = cocktailList.get(position);
                        cocktailClickListener.onCocktailClick(cocktail);
                    }
                }
            });
            mActivity = activity; // Almacena la referencia de la actividad o el administrador de fragmentos

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
                    textView.setTextColor(Color.parseColor("#F45050"));
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                    mActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContenedor, fragment)
                            .addToBackStack(null)
                            .commit();
                    Log.d("Cocktail selected", cocktail.getStrDrink());
                });
            }
        }

    }

    public void setOnCocktailClickListener(OnCocktailClickListener listener) {
        this.cocktailClickListener = listener;
    }

}
