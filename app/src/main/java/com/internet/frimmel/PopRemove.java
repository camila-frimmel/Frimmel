package com.internet.frimmel;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PopRemove extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_popremove, container, false);
        // Configurar elementos e comportamentos do pop-up aqui

        view.findViewById(R.id.VoltarRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Fechar o pop-up
            }
        });

        view.findViewById(R.id.ConfirmaRemover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Agora, chamamos o método desativarContaEExcluirUsuario da atividade RemoveCadastro
                ((RemoveCadastro) requireActivity()).desativarContaEExcluirUsuario();
                dismiss(); // Fechar o pop-up após a ação ser concluída
            }
        });

        return view;
    }
}
