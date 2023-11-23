package com.internet.frimmel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        applyStyles(label);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        applyStyles(label);
        return label;
    }

    private void applyStyles(TextView textView) {
        textView.setTextColor(Color.parseColor("#1A8414")); // Defina a cor desejada aqui
        // Remova a linha abaixo para usar a fonte padrão do sistema
        // textView.setTypeface(customTypeface); // Defina a fonte desejada aqui
        textView.setTextSize(18); // Defina o tamanho do texto desejado aqui
        textView.setTypeface(null, Typeface.BOLD); // Defina o estilo do texto desejado aqui
    }
}
