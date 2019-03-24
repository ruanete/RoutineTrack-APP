package com.example.tabatatimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tabatatimer.Datos.Cronometro;
import com.example.tabatatimer.Datos.Entrenamiento;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro_entrenamiento);

        Bundle objetoRecibido = getIntent().getExtras();

        if(objetoRecibido!=null){
            temporizadorIniciado = false;
            inicializarElementos();
            entrenamiento=(Entrenamiento) objetoRecibido.getSerializable("entrenamiento");
            cronometro = new Cronometro(entrenamiento);
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
    }

}

/*
temporizador = new CountDownTimer(entrenamiento.getTiempoTotal()*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if((millisUntilFinished / 1000)>=(entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion())){
                    tiempoCronometro.setBackgroundResource(R.drawable.text_view_circular_amarillo);
                    //ejercicio_actual.setText("Preparación");
                }else if((millisUntilFinished / 1000)>=(entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion())-entrenamiento.getTiempoEjercicio() && enTiempoEjercicio<entrenamiento.getTiempoEjercicio()){
                    tiempoCronometro.setBackgroundResource(R.drawable.text_view_circular_rojo);
                    ejercicio_actual.setText(cronometro.getEjercicioActual());
                    ejercicio_siguiente.setText(cronometro.getEjercicioSiguiente());
                    //serie_actual.setText(Integer.toString(cronometro.getSerieActual()));
                    cronometro.siguienteSerie();
                    enTiempoEjercicio++;
                    enTiempoDescanso=0;
                }else if((millisUntilFinished / 1000)>=(entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion())-entrenamiento.getTiempoEjercicio() - entrenamiento.getTiempoDescanso() && enTiempoDescanso<entrenamiento.getTiempoDescanso()){
                    tiempoCronometro.setBackgroundResource(R.drawable.text_view_circular_verde);
                    ejercicio_actual.setText("Descanso serie");
                    enTiempoDescanso++;
                    enTiempoEjercicio=0;
                    serie_actual.setText(Integer.toString(cronometro.getSerieActual()));
                    //enTiempoEjercicio=false;
                }
                tiempoCronometro.setText(getTiempoTotalString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                tiempoCronometro.setText("FIN");
                temporizadorIniciado=false;
            }
        };
 */
