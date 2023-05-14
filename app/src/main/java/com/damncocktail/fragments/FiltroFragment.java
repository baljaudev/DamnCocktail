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
import android.widget.Spinner;
import android.widget.TextView;

import com.damncocktail.R;


public class FiltroFragment extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {

    TextView tvFiltro2;
    Spinner spnFiltro;
    Button btnFiltro;

    FragmentManager fm;
    FragmentTransaction ft;

    public FiltroFragment() {
        // Required empty public constructor
    }

    public static FiltroFragment newInstance(String param1, String param2) {
        FiltroFragment fragment = new FiltroFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_filtro, container, false);

        tvFiltro2 = vista.findViewById(R.id.tvFiltro2);
        spnFiltro = vista.findViewById(R.id.spnFiltro);
        btnFiltro = vista.findViewById(R.id.btnFiltro);

        cargarTextoSpinnerSeleccionado();

        btnFiltro.setOnClickListener(this);
        spnFiltro.setOnItemSelectedListener(this);

        if (spnFiltro.getSelectedItemPosition() == 0) {

            cargarFotoNombre() ;
        } else if (spnFiltro.getSelectedItemPosition() == 1) {
            cargarFotoIngrediente();
        } else if (spnFiltro.getSelectedItemPosition() == 2) {
            cargarFotoSiNoAlcoholico();
        } else if (spnFiltro.getSelectedItemPosition() == 3) {
            cargarFotoPrimeraLetra();
        }

        return vista;
    }

    private void cargarFotoPrimeraLetra() {

    }

    private void cargarFotoSiNoAlcoholico() {
    }

    private void cargarFotoIngrediente() {
    }

    private void cargarFotoNombre() {
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
        }


    }

    private void cargarFragment(Fragment fragment) {
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.flContenedor, fragment);
        ft.commit();
    }



    private void cargarTextoSpinnerSeleccionado() {
        spnFiltro.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String textoSeleccionado = parent.getItemAtPosition(position).toString();
        tvFiltro2.setText(textoSeleccionado);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}