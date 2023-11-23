package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.internet.frimmel.databinding.ActivityLogclienteBinding;
import com.internet.frimmel.databinding.ActivityLogfuncionarioBinding;

public class LoginFuncionario extends AppCompatActivity {

    private ActivityLogfuncionarioBinding binding;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logfuncionario);
        Button entrarF = findViewById(R.id.entrarFuncionario);

        binding = ActivityLogfuncionarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.entrarFuncionario.setOnClickListener(view -> validaDados());

        entrarF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Funciona = new  Intent(getApplicationContext(), MenuFuncionario.class);
                startActivity(Funciona);
            }
        });
    }

    private void validaDados() {
        String email = binding.EditFuncionario.getText().toString().trim();
        String senha = binding.passwordFuncionario.getText().toString().trim();

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
                startActivity(new Intent(this, MenuFuncionario.class));
            }else {
                Toast.makeText(this,"ERRO!, email ou senha incorreto", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


