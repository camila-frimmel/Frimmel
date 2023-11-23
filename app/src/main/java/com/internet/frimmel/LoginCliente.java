package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.internet.frimmel.databinding.ActivityLogclienteBinding;

public class LoginCliente extends AppCompatActivity {

    private ActivityLogclienteBinding binding;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcliente);

        binding = ActivityLogclienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        binding.entrarCliente.setOnClickListener(view -> validaDados());

        binding.PrecisaAjuda.setOnClickListener(view -> Ajuda());

        binding.EsqueceuSenha.setOnClickListener(view -> Esqueceu());

    }

    private void Esqueceu(){
        Button Esquece = findViewById(R.id.EsqueceuSenha);

        Esquece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent esq = new Intent(getApplicationContext(), EsqueceuSenha.class);
                startActivity(esq);
            }
        });
    }

    private void Ajuda() {
        Button aju = findViewById(R.id.PrecisaAjuda);
        aju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ajuda = new Intent(getApplicationContext(), AjudaCliente.class);
                startActivity(ajuda);
            }
        });
    }

    private void validaDados() {
        String email = binding.EditCliente.getText().toString().trim();
        String senha = binding.passwordCliente.getText().toString().trim();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){

                loginFirebase(email, senha);

            }else {
                Toast.makeText(this, "Insira a senha",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Insira o email",Toast.LENGTH_SHORT).show();
        }
    }

    private void loginFirebase(String email, String senha){
        mAuth.signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                finish();
               startActivity(new Intent(this, MenuCliente.class));
            }else {
                Toast.makeText(this,"ERRO!, email ou senha incorreto", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
