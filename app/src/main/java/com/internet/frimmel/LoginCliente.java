package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    private void Esqueceu() {
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

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                loginFirebase(email, senha);

            } else {
                Toast.makeText(this, "Insira a senha", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Insira o email", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginFirebase(String email, String senha) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login bem-sucedido
                            checkAccountStatus(email);
                        } else {
                            // Se falhar, exiba uma mensagem para o usuário.
                            Toast.makeText(LoginCliente.this, "Falha no login: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkAccountStatus(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cliente")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean ativo = document.getBoolean("ativo");
                                if (ativo) {
                                    // A conta está ativa, permitir acesso ao aplicativo
                                    startActivity(new Intent(LoginCliente.this, MenuCliente.class));
                                    finish();
                                } else {
                                    // A conta está desativada, impedir acesso ao aplicativo
                                    FirebaseAuth.getInstance().signOut(); // Deslogar usuário
                                    Toast.makeText(LoginCliente.this, "Esta conta está desativada.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            // Tratar erro
                            Toast.makeText(LoginCliente.this, "Erro ao verificar conta no Firestore.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}