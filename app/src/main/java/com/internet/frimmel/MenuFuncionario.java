package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuFuncionario extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menufuncionario);
        Button Cadastro = findViewById(R.id.CadastroCliente);
        Button AgendaF = findViewById(R.id.ManutençãoFuncionario);

        Cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cad = new Intent(getApplicationContext(), CadastroCliente.class);
                startActivity(Cad);
            }
        });
        AgendaF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ManuF = new Intent(getApplicationContext(), ManutençãoFuncionario.class);
                startActivity(ManuF);
            }
        });
    }

}