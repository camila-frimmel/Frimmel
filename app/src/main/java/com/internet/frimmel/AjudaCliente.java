package com.internet.frimmel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AjudaCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajudacliente);
        Button aju = findViewById(R.id.NumeroContato);

        findViewById(R.id.NumeroContato).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exibir o pop-up
                PopupAjuda popupDialog = new PopupAjuda();
                popupDialog.show(getSupportFragmentManager(), "popup_dialog");
            }
        });

    }
}
