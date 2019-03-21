package com.example.tabatatimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tabatatimer.Datos.Entrenamiento;

public class CronometroEntrenamiento extends AppCompatActivity {
    Entrenamiento entrenamiento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro_entrenamiento);

        Bundle objetoRecibido = getIntent().getExtras();

        if(objetoRecibido!=null){
            entrenamiento=(Entrenamiento) objetoRecibido.getSerializable("entrenamiento");
            /*System.out.println("RECIBIDO ENTRENAMIENTO INICIAR: " + entrenamiento.getNombre());
            for(int i=0;i<entrenamiento.getNumeroSeries();i++){
                System.out.println("EJERCICIO: " + entrenamiento.getEjercicio(i));
            }*/
        }
    }
}
