package com.internet.frimmel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RemoveCadastro extends AppCompatActivity {

    private EditText Deletecad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removecad);

        Button removeButton = findViewById(R.id.ConfirmaRemove);
        Deletecad = findViewById(R.id.DeleteCad);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailParaExcluir = Deletecad.getText().toString();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.fetchSignInMethodsForEmail(emailParaExcluir)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                        // O e-mail está associado a uma conta, obtenha o usuário atual
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        // Remova o usuário
                                        if (user != null) {
                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> deleteTask) {
                                                            if (deleteTask.isSuccessful()) {
                                                                // Usuário excluído com sucesso
                                                                Toast.makeText(RemoveCadastro.this, "Usuário removido com sucesso", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Houve um erro ao excluir o usuário
                                                                Toast.makeText(RemoveCadastro.this, "Erro ao remover usuário: " + deleteTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // O e-mail não está associado a uma conta
                                        Toast.makeText(RemoveCadastro.this, "E-mail não associado a uma conta", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Houve um erro ao verificar o e-mail
                                    Toast.makeText(RemoveCadastro.this, "Erro ao verificar e-mail: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
