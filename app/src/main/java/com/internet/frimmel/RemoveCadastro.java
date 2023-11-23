package com.internet.frimmel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RemoveCadastro extends AppCompatActivity {

    private EditText editTextEmail;
    private Button btnConfirmaRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removecad);

        editTextEmail = findViewById(R.id.DeleteCad);
        btnConfirmaRemove = findViewById(R.id.ConfirmaRemove);

        btnConfirmaRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RemoveCadastro", "Botão ConfirmaRemove clicado");
                desativarContaEExcluirUsuario();
            }
        });
    }

    private void desativarContaEExcluirUsuario() {
        String email = editTextEmail.getText().toString();

        // Verificar se o campo de e-mail está vazio
        if (email.isEmpty()) {
            Toast.makeText(this, "Por favor, digite o e-mail do cliente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obter uma referência à coleção "cliente"
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("cliente");

        // Procurar o documento com o e-mail fornecido
        usersCollection.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Documento encontrado, obtenha o ID do documento
                            String userId = task.getResult().getDocuments().get(0).getId();

                            // Desativar a conta do usuário
                            desativarContaNoFirestore(userId);
                        } else {
                            Toast.makeText(RemoveCadastro.this, "Conta não encontrada no Firestore.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Função para desativar uma conta no Firestore
    private void desativarContaNoFirestore(String userId) {
        // Substitua "cliente" pelo seu nome de coleção
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("cliente");

        usersCollection.document(userId).update("ativo", false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Conta desativada com sucesso no Firestore
                            Toast.makeText(RemoveCadastro.this, "Conta desativada com sucesso.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RemoveCadastro.this, "Erro ao desativar conta no Firestore: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}