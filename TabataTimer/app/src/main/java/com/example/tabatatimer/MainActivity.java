package com.example.tabatatimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.tabatatimer.Datos.BaseDatos;
import com.example.tabatatimer.Datos.Entrenamiento;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    Entrenamiento entrenamiento = null;
    int numero_series = 0;
    BaseDatos bd = new BaseDatos(this);
    Vector<Entrenamiento> entrenamientosGuardados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*entrenamiento = new Entrenamiento();
        Entrenamiento entrenamiento1 = new Entrenamiento();
        Entrenamiento entrenamiento2 = new Entrenamiento();
        Entrenamiento entrenamiento3 = new Entrenamiento();

        System.out.println("TAMOOOOOOOOOOOOO22222222");
        bd.guardarEntrenamiento(entrenamiento);
        bd.guardarEntrenamiento(entrenamiento1);
        bd.guardarEntrenamiento(entrenamiento2);
        bd.guardarEntrenamiento(entrenamiento3);

        entrenamientosGuardados = bd.getEntrenamientos();
        System.out.println("TAMAAAAAÑOOOOOO: " + entrenamientosGuardados.size());*/

        entrenamientosGuardados = bd.getEntrenamientos();
        generaScroll();

        /*Bundle objetoRecibido =getIntent().getExtras();

        if(objetoRecibido!=null){
            entrenamiento=(Entrenamiento) objetoRecibido.getSerializable("entrenamiento");
            numero_series = entrenamiento.getNumeroSeries();
            entrenamiento.setIDentrenamiento(View.generateViewId());
            generaScroll();
        }*/
    }

    @SuppressLint("ResourceType")
    public void abrirNuevoEntrenamiento(View view){
        Intent intent = new Intent(this, NuevoEntrenamiento.class);
        startActivity(intent);
    }

    public void generaScroll(){
        for(int i=0;i<entrenamientosGuardados.size();i++) {
            LinearLayout entrenamientos = findViewById(R.id.layout_entrenamientos);

            RelativeLayout entrenamientoLayout = new RelativeLayout(this);
            entrenamientoLayout.setBackgroundResource(R.drawable.cardview_bordes);

            LinearLayout.LayoutParams entrenamientoParams = new LinearLayout.LayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            entrenamientoParams.setMargins(0, 0, 0, 10);
            entrenamientoLayout.setLayoutParams(entrenamientoParams);

            RelativeLayout.LayoutParams imagenPesaParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width
                    RelativeLayout.LayoutParams.WRAP_CONTENT // Height
            );

            RelativeLayout.LayoutParams botonCentralParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width
                    RelativeLayout.LayoutParams.MATCH_PARENT // Height
            );

            RelativeLayout.LayoutParams botonIniciarEntrenamientoParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width
                    RelativeLayout.LayoutParams.WRAP_CONTENT // Height
            );

            //Imagen de la pesa
            imagenPesaParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            imagenPesaParams.addRule(RelativeLayout.CENTER_VERTICAL);
            ImageView pesa = new ImageView(this);
            pesa.setImageResource(R.drawable.ic_fitness_center_24px);
            int id = View.generateViewId();
            pesa.setId(id);
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray ta = obtainStyledAttributes(attrs);
            Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
            ta.recycle();

            pesa.setLayoutParams(imagenPesaParams);

            //Boton iniciar entrenamiento
            botonIniciarEntrenamientoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ImageButton play = new ImageButton(this);
            id = View.generateViewId();
            play.setId(id);
            play.setImageResource(R.drawable.play);
            play.setBackgroundColor(Color.TRANSPARENT);
            play.setClickable(true);
            play.setBackground(drawableFromTheme);
            play.setLayoutParams(botonIniciarEntrenamientoParams);

            //Boton central para editar entrenamiento
            botonCentralParams.addRule(RelativeLayout.START_OF, play.getId());
            botonCentralParams.addRule(RelativeLayout.END_OF, pesa.getId());
            Button boton = new Button(this);
            id = View.generateViewId();
            boton.setId(id);
            boton.setText(entrenamientosGuardados.get(i).getNombre());

            int[] attrs2 = new int[]{android.R.attr.selectableItemBackground};
            TypedArray ta2 = obtainStyledAttributes(attrs2);
            Drawable drawableFromTheme2 = ta2.getDrawable(0);
            ta2.recycle();
            boton.setTextColor(0xFFF05545);
            boton.setTextSize(15);
            boton.setTypeface(boton.getTypeface(), Typeface.BOLD);
            boton.setBackground(drawableFromTheme2);

            boton.setLayoutParams(botonCentralParams);

            //Añado los dos botones y la imagen al relativeLayout
            entrenamientoLayout.addView(pesa);
            entrenamientoLayout.addView(boton);
            entrenamientoLayout.addView(play);

            //Añado el relative layout al scrolview
            entrenamientos.addView(entrenamientoLayout);
        }
    }
}
