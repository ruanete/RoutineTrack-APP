package com.example.tabatatimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.example.tabatatimer.Datos.BaseDatos;
import com.example.tabatatimer.Datos.Entrenamiento;

import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    BaseDatos bd = new BaseDatos(this);
    Vector<Entrenamiento> entrenamientosGuardados;
    Entrenamiento entrenamientoEditar;
    Entrenamiento entrenamientoIniciar;
    boolean eliminar;

    LinearLayout layout_entrenamientos;
    Button boton_eliminar;
    TextView texto_peso, texto_altura, texto_nombre;
    CircleImageView foto_perfil;
    ImageButton boton_casa;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout_entrenamientos = findViewById(R.id.layout_entrenamientos);
        boton_eliminar = findViewById(R.id.boton_eliminar);
        texto_peso = findViewById(R.id.texto_peso);
        texto_altura= findViewById(R.id.texto_altura);
        texto_nombre= findViewById(R.id.texto_nombre);
        foto_perfil = findViewById(R.id.foto_perfil);
        boton_casa = findViewById(R.id.imageButton2);

        boton_casa.setVisibility(View.INVISIBLE);

        setDatosUsuario();

        entrenamientoEditar = null;
        entrenamientoIniciar = null;
        eliminar = false;

        entrenamientosGuardados = bd.getEntrenamientos();
        generaScroll(false);
    }

    public void setDatosUsuario(){
        Vector<String> ajustes = bd.getAjustes();
        if(!ajustes.isEmpty()) {
            if (!ajustes.get(5).equals(""))
                texto_peso.setText(ajustes.get(5) + "kg");

            if (!ajustes.get(4).equals(""))
                texto_altura.setText(ajustes.get(4) + "cm");

            if (!ajustes.get(1).equals(""))
                texto_nombre.setText(ajustes.get(1));

            if (BitmapFactory.decodeFile(ajustes.get(0)) != null)
                foto_perfil.setImageBitmap(BitmapFactory.decodeFile(ajustes.get(0)));
        }
    }

    public void abrirAjustes(View view){
        Intent intent = new Intent(this, MenuAjustes.class);
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    public void abrirNuevoEntrenamiento(View view){
        Intent intent = new Intent(this, NuevoEntrenamiento.class);
        startActivity(intent);
    }

    public void abrirEditarEntrenamiento(View view){
        Intent intent = new Intent(this, NuevoEntrenamiento.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("entrenamiento", entrenamientoEditar);
        bundle.putSerializable("editar", true);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void abrirIniciarEntrenamiento(View view){
        Intent intent = new Intent(this, CronometroEntrenamiento.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("entrenamiento", entrenamientoIniciar);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void abrirCalendarioEntrenamientos(View view){
        Intent intent = new Intent(this, CalendarioEntrenamientos.class);
        startActivity(intent);
    }

    public void abrirBorrarEntrenamientos(View view){
        generaScroll(true);
        boton_eliminar.setText("TERMINAR");

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boton_eliminar.setText("BORRAR");
                eliminar=false;
                generaScroll(false);

                boton_eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirBorrarEntrenamientos(v);
                    }
                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void generaScroll(final boolean eliminar){
        layout_entrenamientos.removeAllViews();
        for(int i=0;i<entrenamientosGuardados.size();i++) {
            final int indice = i;
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
            pesa.setImageResource(R.drawable.icono_pesa);
            int id = View.generateViewId();
            pesa.setId(id);
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray ta = obtainStyledAttributes(attrs);
            final Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
            ta.recycle();

            pesa.setLayoutParams(imagenPesaParams);

            //Boton iniciar entrenamiento
            botonIniciarEntrenamientoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ImageButton play = new ImageButton(this);
            id = View.generateViewId();
            play.setId(id);
            if(eliminar)
                play.setImageResource(R.drawable.icono_eliminar);
            else
                play.setImageResource(R.drawable.play);
            play.setBackgroundColor(Color.TRANSPARENT);
            play.setClickable(true);
            play.setBackground(drawableFromTheme);
            play.setLayoutParams(botonIniciarEntrenamientoParams);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    entrenamientoIniciar = entrenamientosGuardados.get(indice);
                    if(eliminar){
                        bd.borrarEntrenamiento(entrenamientoIniciar);
                        entrenamientosGuardados = bd.getEntrenamientos();
                        generaScroll(true);
                    }else
                        abrirIniciarEntrenamiento(v);
                }
            });

            //Boton central para editar entrenamiento
            botonCentralParams.addRule(RelativeLayout.START_OF, play.getId());
            botonCentralParams.addRule(RelativeLayout.END_OF, pesa.getId());
            botonCentralParams.addRule(RelativeLayout.CENTER_VERTICAL);
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

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    entrenamientoEditar = entrenamientosGuardados.get(indice);
                    abrirEditarEntrenamiento(v);
                }
            });

            //Añado los dos botones y la imagen al relativeLayout
            entrenamientoLayout.addView(pesa);
            entrenamientoLayout.addView(boton);
            entrenamientoLayout.addView(play);

            //Añado el relative layout al scrolview
            entrenamientos.addView(entrenamientoLayout);
        }
    }
}
