package com.damncocktail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.damncocktail.fragments.AleatorioFragment;
import com.damncocktail.fragments.FiltroFragment;
import com.damncocktail.fragments.InicioFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnAleatorio;
    ImageButton btnFiltro;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        ft.add(R.id.flContenedor, new InicioFragment());
        ft.commit();

        btnAleatorio = findViewById(R.id.btnAleatorio);
        btnFiltro = findViewById(R.id.btnFiltro);

        btnAleatorio.setOnClickListener(this);
        btnFiltro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAleatorio) cargarAleatorio();
        else if (v.getId() == R.id.btnFiltro) cargarFiltro();
    }

    private void cargarAleatorio() {
        ft = fm.beginTransaction();
        ft.replace(R.id.flContenedor, new AleatorioFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void cargarFiltro() {
        ft = fm.beginTransaction();
        ft.replace(R.id.flContenedor, new FiltroFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}