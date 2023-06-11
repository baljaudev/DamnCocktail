package com.damncocktail.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.damncocktail.R;

public class FiltroFragment extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {

    TextView tvFiltro2;
    Spinner spnFiltro;
    Button btnFiltro;
    ImageView imageViewFiltro;

    FragmentManager fm;
    FragmentTransaction ft;

    public FiltroFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_filtro, container, false);

        tvFiltro2 = vista.findViewById(R.id.tvFiltro2);
        spnFiltro = vista.findViewById(R.id.spnFiltro);
        btnFiltro = vista.findViewById(R.id.btnFiltro);

        imageViewFiltro = vista.findViewById(R.id.imageViewFiltro);

        cargarTextoSpinnerSeleccionado();

        btnFiltro.setOnClickListener(this);
        spnFiltro.setOnItemSelectedListener(this);

        establecerFotoFiltro();

        return vista;
    }

    private void cargarFotoPrimeraLetra() {
        imageViewFiltro.setImageDrawable(getResources().getDrawable(R.drawable.cocktail1_first_letter));
    }

    private void cargarFotoIngrediente() {
        imageViewFiltro.setImageDrawable(getResources().getDrawable(R.drawable.cocktail1_ingredients));
    }

    private void cargarFotoSiNoAlcoholico() {
        imageViewFiltro.setImageDrawable(getResources().getDrawable(R.drawable.cocktail1_alcoholic));
    }

    private void cargarFotoNombre() {
        imageViewFiltro.setImageDrawable(getResources().getDrawable(R.drawable.cocktail1_name));
    }

    private void cargarFotoFavoritos() {
        imageViewFiltro.setImageDrawable(getResources().getDrawable(R.drawable.cocktail1_favorites));
    }

    @Override
    public void onClick(View v) {
        tvFiltro2.setText(spnFiltro.getSelectedItem().toString());

        if (v.getId() == R.id.btnFiltro && spnFiltro.getSelectedItemPosition() == 0) {
            cargarFragment(new NombreFragment());
        } else if (v.getId() == R.id.btnFiltro && spnFiltro.getSelectedItemPosition() == 1) {
            cargarFragment(new IngredienteFragment());
        } else if (v.getId() == R.id.btnFiltro && spnFiltro.getSelectedItemPosition() == 2) {
            cargarFragment(new SiNoAlcoholFragment());
        } else if (v.getId() == R.id.btnFiltro && spnFiltro.getSelectedItemPosition() == 3) {
            cargarFragment(new PrimeraLetraFragment());
        } else if (v.getId() == R.id.btnFiltro && spnFiltro.getSelectedItemPosition() == 4) {
            cargarFragment(new FavoritosFragment());
        }
    }

    private void cargarFragment(Fragment fragment) {
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.flContenedor, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void cargarTextoSpinnerSeleccionado() {
        spnFiltro.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String textoSeleccionado = parent.getItemAtPosition(position).toString();
        tvFiltro2.setText(textoSeleccionado);

        establecerFotoFiltro();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void establecerFotoFiltro() {
        if (spnFiltro.getSelectedItemPosition() == 0) {
            cargarFotoNombre() ;
        } else if (spnFiltro.getSelectedItemPosition() == 1) {
            cargarFotoIngrediente();
        } else if (spnFiltro.getSelectedItemPosition() == 2) {
            cargarFotoSiNoAlcoholico();
        } else if (spnFiltro.getSelectedItemPosition() == 3) {
            cargarFotoPrimeraLetra();
        } else if (spnFiltro.getSelectedItemPosition() == 4) {
            cargarFotoFavoritos();
        }
    }
}