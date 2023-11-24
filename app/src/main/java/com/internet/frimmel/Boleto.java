package com.internet.frimmel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class Boleto extends AppCompatActivity {

    private Button viaBoleto;
    private FirebaseFirestore db;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boleto);

        viaBoleto = findViewById(R.id.ViaBoleto);

        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            email = currentUser.getEmail();


            db.collection("cliente").document(email)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nome = documentSnapshot.getString("Nome");
                            viaBoleto.setOnClickListener(view -> exibirPDFDoFirebase(email, nome));
                        } else {
                            Toast.makeText(this, "Documento não encontrado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro ao acessar Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }


        private void exibirPDFDoFirebase(String email, String nome) {
        String arquivo = nome + ".pdf";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pdfRef = storageRef.child("/" + arquivo);

        File localFile = new File(getExternalFilesDir(null), "/" + arquivo);

        pdfRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri pdfUri = Uri.fromFile(localFile);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "Erro ao abrir o PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao baixar o PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
