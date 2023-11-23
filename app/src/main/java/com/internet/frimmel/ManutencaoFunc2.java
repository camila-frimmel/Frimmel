package com.internet.frimmel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ManutencaoFunc2 extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textHorario;
    private TextView textData;
    private TextView textObs;
    private TextView textNome;
    private TextView textEndereco;
    private TextView textFhone;
    private Button AgendaConcluida;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufunc2);

        db = FirebaseFirestore.getInstance();

        // Referências para os elementos de exibição em seu layout
        textHorario = findViewById(R.id.textHorario);
        textData = findViewById(R.id.textData);
        textObs = findViewById(R.id.textObs);
        textNome = findViewById(R.id.textNome);
        textEndereco = findViewById(R.id.textEndereco);
        textFhone = findViewById(R.id.textFhone);
        AgendaConcluida = findViewById(R.id.AgendaConcluida);

        String documentoId = getIntent().getStringExtra("documentoId");

        readDataFromCollection("agenda",documentoId);
        readDataFromAnotherCollection("cliente", documentoId);

        AgendaConcluida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirAgendamento("agenda", documentoId);
            }
        });
    }
    private void readDataFromCollection(String agenda, String documentoId) {
        db.collection(agenda)
                .document(documentoId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String horario = documentSnapshot.getString("Horário");
                            String data = documentSnapshot.getString("Data");
                            String obs = documentSnapshot.getString("Obs");

                            // Exibir os dados nos TextViews
                            textHorario.setText("Horário: " + horario);
                            textData.setText("Data: " + data);
                            textObs.setText("Observação: " + obs);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManutencaoFunc2.this, "ERRO!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void readDataFromAnotherCollection(String cliente, String agendaId) {
        db.collection(cliente)
                .whereEqualTo("Endereço", agendaId) // Assumindo que há um campo chamado "agendaId" na coleção "cliente" que armazena o ID da coleção "agenda"
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Assumindo que você deseja exibir todos os documentos encontrados
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String nome = document.getString("Nome");
                            String endereco = document.getString("Endereço");
                            String telefone = document.getString("Telefone");

                            // Exibir os dados nos TextViews
                            textNome.setText("Nome: " + nome);
                            textEndereco.setText("Endereço: " + endereco);
                            textFhone.setText("Telefone: " + telefone);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManutencaoFunc2.this, "ERRO!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void excluirAgendamento(String agenda, String documentoId) {
        db.collection(agenda)
                .document(documentoId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ManutencaoFunc2.this, "Agendamento excluído com sucesso", Toast.LENGTH_SHORT).show();
                        // Adicione qualquer lógica adicional após a exclusão bem-sucedida, se necessário
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManutencaoFunc2.this, "Erro ao excluir agendamento", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
