package com.example.tabatatimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tabatatimer.Datos.BaseDatos;
import com.example.tabatatimer.Datos.Entrenamiento;

public class SiguienteNuevoEntrenamiento extends AppCompatActivity {
    int numero_series = 0;
    Entrenamiento entrenamiento = null;
    EditText caja_texto;
    BaseDatos bd = new BaseDatos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siguiente_nuevo_entrenamiento);

        Bundle objetoRecibido =getIntent().getExtras();

        if(objetoRecibido!=null){
            entrenamiento=(Entrenamiento) objetoRecibido.getSerializable("entrenamiento");
            numero_series = entrenamiento.getNumeroSeries();
            entrenamiento.inicializarEjercicios();
            generaScroll();
        }
    }

    public void finalizarGuardadoEntrenamiento(View view){
        bd.guardarEntrenamiento(entrenamiento);
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("entrenamiento", entrenamiento);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void crearBotonEjercicio(int indice) {
        final int copiaIndice = indice;
        AlertDialog.Builder dialogo = new AlertDialog.Builder(SiguienteNuevoEntrenamiento.this);
        caja_texto = new EditText(SiguienteNuevoEntrenamiento.this);

        caja_texto.setHint(entrenamiento.getEjercicio(indice));

        dialogo.setView(caja_texto);
        dialogo.setTitle("Nombre del ejercicio");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("BOTON: " + copiaIndice + "TEXTO: " + caja_texto.getText().toString());
                entrenamiento.setEjercicio(copiaIndice, caja_texto.getText().toString());
                setTextoBotones();
            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogo.show();
    }

    private void setTextoBotones(){
        for(int i = 0;i < numero_series;i++){
            ((Button)findViewById(entrenamiento.getIDEjercicio(i))).setText(entrenamiento.getEjercicio(i));
        }
    }

    private void generaScroll(){
        LinearLayout scroll_ejercicios = findViewById(R.id.scroll_ejercicios);

        for(int i = 0;i < numero_series;i++){
            final int indice = i;
            LinearLayout ejercicio = new LinearLayout(this);
            ejercicio.setOrientation(LinearLayout.HORIZONTAL);
            ejercicio.setBackgroundResource(R.drawable.cardview_bordes);

            LinearLayout.LayoutParams ejercicioParams = new LinearLayout.LayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
            ejercicioParams.setMargins(0, 0, 0, 10);
            ejercicio.setLayoutParams(ejercicioParams);

            //Genero el boton principal para cambiar el nombre del ejercicio
            LinearLayout.LayoutParams boton_nombre_ejercicioParams = new LinearLayout.LayoutParams(
                    0, // Width
                    LinearLayout.LayoutParams.WRAP_CONTENT, //Height
                    1 //Weight
            );
            Button boton_nombre_ejercicio = new Button(this);
            int id = View.generateViewId();
            entrenamiento.addIDEjercicio(id);
            boton_nombre_ejercicio.setId(id);
            boton_nombre_ejercicio.setText(entrenamiento.getEjercicio(indice));
            int[] attrs2 = new int[] { android.R.attr.selectableItemBackground};
            TypedArray ta2 = obtainStyledAttributes(attrs2);
            Drawable drawableFromTheme2 = ta2.getDrawable(0);
            ta2.recycle();
            boton_nombre_ejercicio.setLayoutParams(boton_nombre_ejercicioParams);
            boton_nombre_ejercicio.setTextColor(0xFFF05545);
            boton_nombre_ejercicio.setTextSize(15);
            boton_nombre_ejercicio.setTypeface(boton_nombre_ejercicio.getTypeface(), Typeface.BOLD);
            boton_nombre_ejercicio.setBackground(drawableFromTheme2);
            boton_nombre_ejercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    crearBotonEjercicio(indice);
                }
            });

            //Genero la imagen de la rueda dentada
            LinearLayout.LayoutParams imagenRuedaDentadaParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                    LinearLayout.LayoutParams.WRAP_CONTENT // Height
            );

            imagenRuedaDentadaParams.gravity = Gravity.CENTER_VERTICAL;

            ImageView rueda_dentada = new ImageView(this);
            rueda_dentada.setImageResource(R.drawable.icono_ajuste);
            id = View.generateViewId();
            rueda_dentada.setId(id);
            rueda_dentada.setLayoutParams(imagenRuedaDentadaParams);

            ejercicio.addView(boton_nombre_ejercicio);
            ejercicio.addView(rueda_dentada);
            scroll_ejercicios.addView(ejercicio);
        }
    }
}
