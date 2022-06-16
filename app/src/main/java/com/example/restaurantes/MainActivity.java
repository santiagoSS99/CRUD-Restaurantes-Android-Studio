package com.example.restaurantes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void restauActv(View view) {

        Intent sitios = new Intent(MainActivity.this, restaurantes.class);
        startActivity(sitios);
    }

    public void restauCata(View view) {
        Intent a_la_carta = new Intent(MainActivity.this, catalogo.class);
        startActivity(a_la_carta);
    }

}