package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManutençãoFuncionario extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> funcionariosList;
    private ArrayList<String> funcionariosIdsList; // Adicionada para armazenar os IDs
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufuncionario);

        // Inicializar componentes
        listView = findViewById(R.id.listView);
        funcionariosList = new ArrayList<>();
        funcionariosIdsList = new ArrayList<>(); // Inicializar a lista de IDs
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, funcionariosList);
        listView.setAdapter(adapter);

        // Configurar o acesso ao Firestore
        db = FirebaseFirestore.getInstance();

        db.collection("agenda")
                .orderBy("Data", Query.Direction.DESCENDING)
                .orderBy("Horário", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firestore", "Dados recuperados com sucesso");
                        funcionariosList.clear();
                        funcionariosIdsList.clear(); // Limpar a lista de IDs

                        // Iterar sobre os documentos do Firestore e adicionar à lista
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String horarioFuncionario = document.getString("Horário");
                            String dataFuncionario = document.getString("Data");
                            String horarioEData = dataFuncionario + " - " + horarioFuncionario;
                            funcionariosList.add(horarioEData);

                            // Adicionar o ID do documento à lista
                            funcionariosIdsList.add(document.getId());
                        }

                        // Notificar o adaptador sobre as mudanças
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Erro ao recuperar dados", task.getException());
                        Toast.makeText(ManutençãoFuncionario.this, "ERRO!", Toast.LENGTH_SHORT).show();
                    }
                });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Obter o item clicado da lista
            String itemSelecionado = funcionariosList.get(position);

            // Obter o ID correspondente ao item clicado
            String idSelecionado = funcionariosIdsList.get(position);

            // Criar uma Intent para iniciar a nova atividade
            Intent intent = new Intent(ManutençãoFuncionario.this, ManutencaoFunc2.class);

            // Passar o ID como extra para a nova atividade
            intent.putExtra("documentoId", idSelecionado);

            // Iniciar a nova atividade
            startActivity(intent);
        });
    }
}
