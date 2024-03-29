package com.example.tabatatimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tabatatimer.Datos.BaseDatos;
import com.example.tabatatimer.Datos.Cronometro;
import com.example.tabatatimer.Datos.Entrenamiento;
import com.example.tabatatimer.Datos.Evento;

public class CronometroEntrenamiento extends AppCompatActivity {
    Entrenamiento entrenamiento;
    Cronometro cronometro;
    Boolean temporizadorIniciado;
    static TextView tiempoCronometro;
    static TextView serieActual;
    static TextView ejercicioActual;
    static TextView ejercicioSiguiente;
    static TextView tiempoTotal;
    static TextView tabataActual;
    BaseDatos bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro_entrenamiento);
        bd = new BaseDatos(this);

        Bundle objetoRecibido = getIntent().getExtras();

        if(objetoRecibido!=null){
            temporizadorIniciado = false;
            inicializarElementos();
            entrenamiento=(Entrenamiento) objetoRecibido.getSerializable("entrenamiento");
            cronometro = new Cronometro(entrenamiento);
            cronometro.setContext(this);
        }
    }

    public static void setBackgroundTiempoCronometro(int opcion){
        if(opcion==0)
            tiempoCronometro.setBackgroundResource(R.drawable.text_view_circular_amarillo);
        else if(opcion==1)
            tiempoCronometro.setBackgroundResource(R.drawable.text_view_circular_rojo);
        else if(opcion==2)
            tiempoCronometro.setBackgroundResource(R.drawable.text_view_circular_verde);
        else if(opcion==3)
            tiempoCronometro.setBackgroundResource(R.drawable.text_view_circular_azul);
    }

    public static void setTiempoCronometro(String tiempo_cronometro){
        tiempoCronometro.setText(tiempo_cronometro);
    }

    public static void setTiempoTotal(String tiempo_total){
        tiempoTotal.setText(tiempo_total);
    }

    public static void setSerieActual(String serie_actual){
        serieActual.setText(serie_actual);
    }

    public static void setTabataActual(String tabata_actual){
        tabataActual.setText(tabata_actual);
    }

    public static void setEjercicioActual(String ejercicio_actual){
        ejercicioActual.setText(ejercicio_actual);
    }

    public static void setEjercicioSiguiente(String ejercicio_siguiente){
        ejercicioSiguiente.setText(ejercicio_siguiente);
    }

    public void empezarEntrenamiento(View v){
        if(!temporizadorIniciado){
            temporizadorIniciado=true;
            cronometro.iniciarCronometro();
        }
    }

    public void finalizarEntrenamiento(View v){
        cronometro.finalizarCronometro();
        finish();
    }

    private void inicializarElementos(){
        tiempoCronometro = findViewById(R.id.circulo_cronometro);
        tiempoTotal = findViewById(R.id.tiempo_total);
        serieActual = findViewById(R.id.serie_actual);
        ejercicioActual = findViewById(R.id.ejercicio_actual);
        ejercicioSiguiente = findViewById(R.id.ejercicio_siguiente);
        tabataActual = findViewById(R.id.tabata_actual);

        tiempoTotal.setVisibility(View.INVISIBLE);
    }

}
