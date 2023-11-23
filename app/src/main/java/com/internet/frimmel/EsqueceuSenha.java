package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.internet.frimmel.databinding.ActivityEsqueceusenhaBinding;
import com.internet.frimmel.databinding.ActivityLogfuncionarioBinding;

public class EsqueceuSenha extends AppCompatActivity {

    private ActivityEsqueceusenhaBinding binding;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceusenha);

        binding = ActivityEsqueceusenhaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.entrarSenhaEsquecida.setOnClickListener(view -> validaDados());
    }

    private void validaDados() {
        String email = binding.editEmail.getText().toString().trim();

        if(!email.isEmpty()){

            recuperacontaFirebase(email);

        } else {
            Toast.makeText(this, "Insira o email",Toast.LENGTH_SHORT).show();
        }
    }

    private void recuperacontaFirebase(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                Toast.makeText(this,"Verifique seu email", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this,"ERRO!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}