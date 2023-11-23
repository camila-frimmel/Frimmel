package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuCliente extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menucliente);
        Button Boletos = findViewById(R.id.Boletos);
        Button AgendaC = findViewById(R.id.ManutençãoCliente);
        Button DadosC = findViewById(R.id.DadosCliente);

        Boletos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Via = new Intent(getApplicationContext(), Boleto.class);
                startActivity(Via);
            }
        });

        AgendaC.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent ManuC = new Intent(getApplicationContext(), ManutençãoCliente.class);
               startActivity(ManuC);
           }
       });

        DadosC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Dados = new Intent(getApplicationContext(), DadosCliente.class);
                startActivity(Dados);
            }
        });

    }
}