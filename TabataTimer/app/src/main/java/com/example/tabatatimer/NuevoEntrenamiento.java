package com.example.tabatatimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tabatatimer.Datos.BaseDatos;
import com.example.tabatatimer.Datos.Entrenamiento;

public class NuevoEntrenamiento extends AppCompatActivity {
    Button boton_nombre_entrenamiento;
    Button boton_preparacion;
    Button boton_ejercicio;
    Button boton_descanso;
    Button boton_series;
    Button boton_tabatas;
    Button boton_duracion;
    Button boton_descanso_tabata;
    EditText caja_texto;
    Entrenamiento entrenamiento;
    BaseDatos bd = new BaseDatos(this);
    Boolean editar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_entrenamiento);

        Bundle objetoRecibido = getIntent().getExtras();

        if(objetoRecibido!=null){
            entrenamiento=(Entrenamiento) objetoRecibido.getSerializable("entrenamiento");
            editar = (Boolean) objetoRecibido.getSerializable("editar");
            setBotones();
            setTextoBotones();
        }else{
            editar=false;
            entrenamiento = new Entrenamiento();
            setBotones();
            setTextoBotones();
        }
    }

    public void abrirSiguienteNuevoEntrenamiento(View view){
        Intent intent = new Intent(this, SiguienteNuevoEntrenamiento.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("entrenamiento", entrenamiento);
        if(editar)
            bundle.putSerializable("editar", true);
        else
            bundle.putSerializable("editar", false);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void volverAtras(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //finish();
    }

    public void volverInicio(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //finish();
    }

    public void setTextoBotones(){
        boton_nombre_entrenamiento.setText("Entrenamiento" + '\n' + entrenamiento.getNombre());
        boton_preparacion.setText("Preparación" + '\n' + entrenamiento.getTiempoPreparacion());
        boton_ejercicio.setText("Ejercicio" + '\n' + entrenamiento.getTiempoEjercicio());
        boton_descanso.setText("Descanso" + '\n' + entrenamiento.getTiempoDescanso());
        boton_series.setText("Series" + '\n' + entrenamiento.getNumeroSeries());
        boton_tabatas.setText("Tabatas" + '\n' + entrenamiento.getNumeroTabatas());
        boton_duracion.setText("Duración" + '\n' + entrenamiento.getTiempoTotalString());
        boton_descanso_tabata.setText("Descanso" + '\n' + entrenamiento.getDescansoTabata());
    }

    public void setBotones(){
        boton_nombre_entrenamiento = findViewById(R.id.boton_nombre_entrenamiento);
        boton_preparacion = findViewById(R.id.boton_preparacion);
        boton_ejercicio = findViewById(R.id.boton_ejercicio);
        boton_descanso = findViewById(R.id.boton_descanso);
        boton_series = findViewById(R.id.boton_series);
        boton_tabatas = findViewById(R.id.boton_tabatas);
        boton_duracion = findViewById(R.id.boton_duracion);
        boton_descanso_tabata = findViewById(R.id.boton_descanso_tabata);
        caja_texto = new EditText(NuevoEntrenamiento.this);
    }

    public void setNombreEntrenamiento(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoEntrenamiento.this);
        caja_texto = new EditText(NuevoEntrenamiento.this);

        caja_texto.setHint(entrenamiento.getNombre());

        dialogo.setView(caja_texto);
        dialogo.setTitle("Nombre del entrenamiento");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("EDITAR NOMBRE ENTRENAMIENTO" + editar);

                entrenamiento.setNombre(caja_texto.getText().toString());
                if(editar)
                    bd.editarEntrenamiento(entrenamiento);
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

    public void setTiempoPreparacion(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoEntrenamiento.this);
        caja_texto = new EditText(NuevoEntrenamiento.this);
        caja_texto.setInputType(InputType.TYPE_CLASS_NUMBER);

        caja_texto.setHint(Integer.toString(entrenamiento.getTiempoPreparacion()));

        dialogo.setView(caja_texto);
        dialogo.setTitle("Tiempo de preparación");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                entrenamiento.setTiempoPreparacion(Integer.parseInt(caja_texto.getText().toString()));
                if(editar)
                    bd.editarEntrenamiento(entrenamiento);
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

    public void setTiempoEjercicio(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoEntrenamiento.this);
        caja_texto = new EditText(NuevoEntrenamiento.this);
        caja_texto.setInputType(InputType.TYPE_CLASS_NUMBER);

        caja_texto.setHint(Integer.toString(entrenamiento.getTiempoEjercicio()));

        dialogo.setView(caja_texto);
        dialogo.setTitle("Tiempo de ejercicio");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                entrenamiento.setTiempoEjercicio(Integer.parseInt(caja_texto.getText().toString()));
                if(editar)
                    bd.editarEntrenamiento(entrenamiento);
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

    public void setTiempoDescanso(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoEntrenamiento.this);
        caja_texto = new EditText(NuevoEntrenamiento.this);
        caja_texto.setInputType(InputType.TYPE_CLASS_NUMBER);

        caja_texto.setHint(Integer.toString(entrenamiento.getTiempoDescanso()));

        dialogo.setView(caja_texto);
        dialogo.setTitle("Tiempo de descanso");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                entrenamiento.setTiempoDescanso(Integer.parseInt(caja_texto.getText().toString()));
                if(editar)
                    bd.editarEntrenamiento(entrenamiento);
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

    public void setNumeroSeries(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoEntrenamiento.this);
        caja_texto = new EditText(NuevoEntrenamiento.this);
        caja_texto.setInputType(InputType.TYPE_CLASS_NUMBER);

        caja_texto.setHint(Integer.toString(entrenamiento.getNumeroSeries()));

        dialogo.setView(caja_texto);
        dialogo.setTitle("Numero de series");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                entrenamiento.setNumeroSeries(Integer.parseInt(caja_texto.getText().toString()));
                entrenamiento.inicializarEjercicios();

                if(editar){
                    bd.eliminarEjercicios(entrenamiento);
                    bd.editarEntrenamiento(entrenamiento);
                    bd.guardarEjercicios(entrenamiento);
                }

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

    public void setNumeroTabatas(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoEntrenamiento.this);
        caja_texto = new EditText(NuevoEntrenamiento.this);
        caja_texto.setInputType(InputType.TYPE_CLASS_NUMBER);

        caja_texto.setHint(Integer.toString(entrenamiento.getNumeroTabatas()));

        dialogo.setView(caja_texto);
        dialogo.setTitle("Numero de tabatas");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                entrenamiento.setNumeroTabatas(Integer.parseInt(caja_texto.getText().toString()));
                if(editar)
                    bd.editarEntrenamiento(entrenamiento);
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

    public void setDescansoTabata(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoEntrenamiento.this);
        caja_texto = new EditText(NuevoEntrenamiento.this);
        caja_texto.setInputType(InputType.TYPE_CLASS_NUMBER);

        caja_texto.setHint(Integer.toString(entrenamiento.getDescansoTabata()));

        dialogo.setView(caja_texto);
        dialogo.setTitle("Descanso entre tabatas");

        dialogo.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                entrenamiento.setDescansoTabata(Integer.parseInt(caja_texto.getText().toString()));
                if(editar)
                    bd.editarEntrenamiento(entrenamiento);
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
}
