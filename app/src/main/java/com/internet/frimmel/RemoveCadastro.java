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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
                excluirUsuario(v);
            }
        });

        // Restante do seu código...
    }

    public void excluirUsuario(View view) {
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
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Documento encontrado, obtenha o ID do documento
                                String userId = document.getId();

                                // Excluir o documento
                                usersCollection.document(userId).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> deleteTask) {
                                                if (deleteTask.isSuccessful()) {
                                                    // Documento excluído com sucesso, agora você pode desativar a conta do usuário se necessário
                                                    desativarContaUsuario(email);
                                                } else {
                                                    Toast.makeText(RemoveCadastro.this, "Erro ao excluir usuário.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RemoveCadastro.this, "Erro ao procurar usuário.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void desativarContaUsuario(String email) {
        // Obter uma referência à coleção "cliente"
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("cliente");

        // Procurar o documento com o e-mail fornecido
        usersCollection.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Documento encontrado, obtenha o ID do documento
                                String userId = document.getId();

                                // Atualizar o campo "ativo" para falso
                                usersCollection.document(userId).update("ativo", false)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> updateTask) {
                                                if (updateTask.isSuccessful()) {
                                                    Toast.makeText(RemoveCadastro.this, "Usuário excluído e conta desativada com sucesso.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(RemoveCadastro.this, "Erro ao desativar conta de usuário.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RemoveCadastro.this, "Erro ao desativar conta de usuário.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
