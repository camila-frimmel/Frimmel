package com.internet.frimmel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroCliente extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText CadastroNome;
    private EditText CadastroCPF;
    private EditText CadastroEmail;
    private EditText CadastroTelefone;
    private EditText CadastroCEP;
    private EditText CadastroEndereço;
    private Spinner CadastroPlano;
    private Spinner CadastroMegas;
    private Spinner CadastroMensal;
    private EditText CadastroSenha;
    private Button Salvar;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrocliente);
        Salvar = findViewById(R.id.ConfirmarCadastro);
        Button Cancela = findViewById(R.id.CancelarCadastro);
        Button Remover = findViewById(R.id.RemoveCliente);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        CadastroNome = findViewById(R.id.CadastroNome);
        CadastroCPF = findViewById(R.id.CadastroCPF);
        CadastroEmail = findViewById(R.id.CadastroEmail);
        CadastroTelefone = findViewById(R.id.CadastroTelefone);
        CadastroCEP = findViewById(R.id.CadastroCEP);
        CadastroEndereço = findViewById(R.id.CadastroEndereço);
        CadastroPlano =  findViewById(R.id.CadastroPlano);
        CadastroMegas = findViewById(R.id.CadastroMegas);
        CadastroMensal = findViewById(R.id.CadastroMensal);
        CadastroSenha = findViewById(R.id.CadastroSenha);



        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = CadastroNome.getText().toString();
                String cpf = CadastroCPF.getText().toString();
                String email = CadastroEmail.getText().toString();
                String telefone = CadastroTelefone.getText().toString();
                String cep = CadastroCEP.getText().toString();
                String endereco = CadastroEndereço.getText().toString();
                String plano = CadastroPlano.getSelectedItem().toString();
                String megas = CadastroMegas.getSelectedItem().toString();
                String mensalidade = CadastroMensal.getSelectedItem().toString();
                String senha = CadastroSenha.getText().toString();

                if (validaCampos(nome, cpf, email, telefone, cep, endereco, plano, megas, mensalidade, senha)) {
                    CriacontaFirebase(email, senha);
                    salvarDadosNoFirestore(nome, cpf, email, telefone, cep, endereco, plano, megas, mensalidade);
                }
            }
        });

        Cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupCad popupDialog = new PopupCad();
                popupDialog.show(getSupportFragmentManager(), "popup_dialog");
            }
        });

        Remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rem = new Intent(getApplicationContext(), RemoveCadastro.class);
                startActivity(rem);
            }
        });
        // Configurar o Spinner de Planos
        configurarSpinnerPlanos();
    }

    private void configurarSpinnerPlanos() {
        List<String> planos = new ArrayList<>();
        planos.add("Plano 1");
        planos.add("Plano 2");
        planos.add("Plano 3");

        SpinnerAdapter planosAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, planos);
        planosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CadastroPlano.setAdapter(planosAdapter);

        // Configurar o Spinner de MM (valores)
        List<String> megasPlano1 = new ArrayList<>();
        megasPlano1.add("Megas: 50mg");

        List<String> megasPlano2 = new ArrayList<>();
        megasPlano2.add("Megas: 25mg");

        List<String> megasPlano3 = new ArrayList<>();
        megasPlano3.add("Megas: 15mg");

        SpinnerAdapter megasAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        megasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CadastroMegas.setAdapter(megasAdapter);

        List<String> mensalPlano1 = new ArrayList<>();
        mensalPlano1.add("Mensalidade: R$100");

        List<String> mensalPlano2 = new ArrayList<>();
        mensalPlano2.add("Mensalidade: R$80");

        List<String> mensalPlano3 = new ArrayList<>();
        mensalPlano3.add("Mensalidade: R$50");

        SpinnerAdapter mensalAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        mensalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CadastroMensal.setAdapter(mensalAdapter);

        // Defina um ouvinte para o Spinner de planos
        CadastroPlano.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Atualize o Spinner de valores com os valores correspondentes ao plano selecionado
                String planoSelecionado = planos.get(position);
                if ("Plano 1".equals(planoSelecionado)) {
                    atualizarMegas(megasPlano1);
                    atualizarMensal(mensalPlano1);
                } else if ("Plano 2".equals(planoSelecionado)) {
                    atualizarMegas(megasPlano2);
                    atualizarMensal(mensalPlano2);
                } else if ("Plano 3".equals(planoSelecionado)) {
                    atualizarMegas(megasPlano3);
                    atualizarMensal(mensalPlano3);
                }
                else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implemente conforme necessário
            }
        });
    }

    private void atualizarMegas(List<String> valores) {
        ArrayAdapter<String> valoresAdapter = (ArrayAdapter<String>) CadastroMegas.getAdapter();
        valoresAdapter.clear();
        valoresAdapter.addAll(valores);
        valoresAdapter.notifyDataSetChanged();
    }
    private void atualizarMensal(List<String> valores) {
        ArrayAdapter<String> mensalAdapter = (ArrayAdapter<String>) CadastroMensal.getAdapter();
        mensalAdapter.clear();
        mensalAdapter.addAll(valores);
        mensalAdapter.notifyDataSetChanged();
    }

    private boolean validaCampos(String nome, String cpf, String email, String telefone, String cep, String endereco, String plano, String megas, String mensalidade, String senha) {
        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || telefone.isEmpty() || cep.isEmpty() || endereco.isEmpty() || plano.isEmpty() || megas.isEmpty() || mensalidade.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.contains("@")) {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void CriacontaFirebase(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show();
                        // Redirecione o usuário ou realize ação após o sucesso
                    } else {
                        Toast.makeText(this, "Erro ao criar conta", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void salvarDadosNoFirestore(String nome, String cpf, String email, String telefone, String cep, String endereco, String plano, String megas, String mensalidade) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("Nome", nome);
        dados.put("CPF", cpf);
        dados.put("email", email);
        dados.put("Telefone", telefone);
        dados.put("CEP", cep);
        dados.put("Endereço", endereco);
        dados.put("Plano", plano);
        dados.put("Megas", megas);
        dados.put("Mensalidade", mensalidade);

        db.collection("cliente")
                .document(email)
                .set(dados)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CadastroCliente.this, "Salvo no banco", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CadastroCliente.this, MenuFuncionario.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Erro ao salvar dados no Firestore", e);
                        Toast.makeText(CadastroCliente.this, "Erro ao salvar dados no Firestore", Toast.LENGTH_SHORT).show();
                    }
                });

        try {
            String arquivo = nome + ".pdf";
            String filePath = getFilesDir() + "/" + arquivo;
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Nome: " + nome));
            document.add(new Paragraph("CPF: " + cpf));
            document.add(new Paragraph("Endereço: " + endereco));
            document.add(new Paragraph("Plano: " + plano));
            document.add(new Paragraph("Megas: " + megas));
            document.add(new Paragraph("Mensalidade: " + mensalidade));
            document.add(new Paragraph("Telefone: " + telefone));

            document.close();

            // Carregue o PDF no Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference pdfRef = storageRef.child("/" + arquivo); // Especifique o caminho no Storage

            File file = new File(filePath);

            pdfRef.putFile(Uri.fromFile(file))
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(this, "PDF enviado para o Firebase Storage com sucesso!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao enviar o PDF para o Firebase Storage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao criar o PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
