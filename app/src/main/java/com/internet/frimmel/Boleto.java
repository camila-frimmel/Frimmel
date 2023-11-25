package com.internet.frimmel;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Boleto extends AppCompatActivity {

    private Button viaBoleto;
    private FirebaseFirestore db;
    private String email;
    private StorageReference storageRef;
    private String arquivoUrl;  // Declare arquivoUrl at the class level

    private static final String TAG = "Boleto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boleto);

        viaBoleto = findViewById(R.id.ViaBoleto);

        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if (currentUser != null) {
            email = currentUser.getEmail();

            viaBoleto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Supondo que "teste" é o nome da coleção
                    CollectionReference testeCollection = db.collection("teste");

                    testeCollection.whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        String arquivo = document.getString("arquivo");

                                        if (arquivo != null) {
                                            // Obtenha a URL do arquivo no Firebase Storage
                                            StorageReference arquivoRef = storageRef.child(arquivo);

                                            arquivoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                // Armazene a URL do arquivo para uso posterior
                                                arquivoUrl = uri.toString();
                                                // Agora, você pode chamar downloadFile(arquivoUrl) ou fazer o que quiser com a URL
                                                downloadFile();
                                            }).addOnFailureListener(exception -> {
                                                // O download falhou, trate o erro aqui.
                                                Log.e(TAG, "Erro ao obter a URL do arquivo", exception);
                                                Toast.makeText(Boleto.this, "Erro ao obter a URL do arquivo", Toast.LENGTH_SHORT).show();
                                            });
                                        } else {
                                            Log.e(TAG, "Nome do arquivo é nulo.");
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Erro ao obter documentos.", Objects.requireNonNull(task.getException()));
                                }
                            });
                }
            });
        }
    }

    // Método para baixar o arquivo usando a URL
    private void downloadFile() {
        // Obtenha a permissão para acessar o sistema de arquivos externo

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }


        // Abra o seletor de documento para escolher o diretório de salvamento
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri treeUri = data.getData();

                // Execute AsyncTask para baixar o arquivo em segundo plano
                new DownloadFileTask().execute(treeUri.toString());
            }
        }
    }

    private class DownloadFileTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String treeUri = params[0];

            try {
                // Implemente a lógica para baixar o arquivo aqui
                URL url = new URL(arquivoUrl);  // Agora usa a URL armazenada
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                // Obtenha o ContentResolver para interagir com o sistema de arquivos
                ContentResolver resolver = getContentResolver();

                // Crie um arquivo local para salvar o conteúdo
                String fileName = "downloaded_file.pdf";
                Uri destUri = DocumentsContract.buildDocumentUriUsingTree(Uri.parse(treeUri), DocumentsContract.getTreeDocumentId(Uri.parse(treeUri)));
                Uri fileUri = DocumentsContract.createDocument(resolver, destUri, "application/pdf", fileName);

                // Escreva os dados do InputStream no arquivo local
                OutputStream outputStream = resolver.openOutputStream(fileUri);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Feche os fluxos
                inputStream.close();
                outputStream.close();

                return true; // Indica sucesso
            } catch (Exception e) {
                Log.e(TAG, "Erro ao baixar o arquivo", e);
                return false; // Indica falha
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Toast.makeText(Boleto.this, "Arquivo baixado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Boleto.this, "Erro ao baixar o arquivo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
