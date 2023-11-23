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

        view.findViewById(R.id.CancelaRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("RestrictedApi") Intent remo = new Intent(getApplicationContext(), CadastroCliente.class);
                startActivity(remo);
            }
        });

        return view;
    }
}
