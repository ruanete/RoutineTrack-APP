package com.example.tabatatimer.Datos;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Pair;

import com.example.tabatatimer.CronometroEntrenamiento;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Cronometro {
    private CountDownTimer cronometroTotal;
    private CountDownTimer cronometro;
    private Entrenamiento entrenamiento;
    private long tiempoRestante;
    private Vector<Vector<String>> pasosEntrenamiento;
    private static boolean enProceso;
    int i = 0;
    Context context;
    BaseDatos bd;

    public Cronometro(){
        tiempoRestante = 0;
        pasosEntrenamiento = new Vector<Vector<String>>();
        entrenamiento = null;
        cronometro = null;
        cronometroTotal = null;
        enProceso=false;
    }

    public void setContext(Context context){
        this.context = context;
        bd = new BaseDatos(context);
    }

    public Cronometro(final Entrenamiento entrenamiento){
        this.entrenamiento = entrenamiento;
        long tiempoQueRestar = 1 + entrenamiento.getNumeroSeries()*entrenamiento.getNumeroTabatas();
        tiempoRestante = entrenamiento.getTiempoTotal() - tiempoQueRestar;
        pasosEntrenamiento = new Vector<Vector<String>>();
        enProceso=false;
        generarPasosEntrenamiento();

        if(entrenamiento.getTiempoPreparacion()>0){
            CronometroEntrenamiento.setBackgroundTiempoCronometro(0);
            CronometroEntrenamiento.setTiempoCronometro(getTiempoTotalString(entrenamiento.getTiempoPreparacion()));
        }

        CronometroEntrenamiento.setEjercicioActual(pasosEntrenamiento.get(i).get(2));
        CronometroEntrenamiento.setEjercicioSiguiente(pasosEntrenamiento.get(i).get(3));
        CronometroEntrenamiento.setTiempoCronometro(getTiempoTotalString(entrenamiento.getTiempoEjercicio()));
        CronometroEntrenamiento.setSerieActual(pasosEntrenamiento.get(i).get(0));
        CronometroEntrenamiento.setTabataActual(pasosEntrenamiento.get(i).get(1));
        CronometroEntrenamiento.setTiempoTotal(getTiempoString(tiempoRestante));

        cronometroTotal = new CountDownTimer(tiempoRestante*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished / 1000;
                CronometroEntrenamiento.setTiempoTotal(getTiempoString(tiempoRestante));
            }

            public void onFinish() {
                CronometroEntrenamiento.setTiempoTotal("FIN");
                bd.guardarEvento(new Evento(entrenamiento.getNombre()));
            }
        };
    }

    public void iniciarCronometro(){
        cronometroTotal.start();
        procedimientoCronometro();
    }

    public void finalizarCronometro() {
        if (cronometroTotal != null && cronometro != null){
            cronometroTotal.cancel();
            cronometro.cancel();
        }
    }

    private Vector<String> crearPaso(int serie, int tabata, String actual, String siguiente){
        Vector<String> paso = new Vector<String>();
        paso.add(Integer.toString(serie));
        paso.add(Integer.toString(tabata));
        paso.add(actual);
        paso.add(siguiente);
        return paso;
    }

    private void generarPasosEntrenamiento(){
        //Si tiene un tiempo de preparación lo metemos primero
        if(entrenamiento.getTiempoPreparacion()>0){
            pasosEntrenamiento.add(crearPaso(1, 1, "Preparación", entrenamiento.getEjercicio(0)));
        }

        //1) Si el entrenamiento no tiene ningun tipo de descanso
        //2) Si el entrenamiento tiene solo descanso entre series
        //3) Si el entrenamiento tiene solo descanso entre tabatas
        //4) Si el entrenamiento tiene descanso entre series y entre tabatas
        if(entrenamiento.getTiempoDescanso()<=0 && entrenamiento.getDescansoTabata()<=0) {
            for (int i = 0; i < entrenamiento.getNumeroTabatas(); i++) {
                for (int j = 0; j < entrenamiento.getNumeroSeries(); j++) {
                    if(j==entrenamiento.getNumeroSeries()-1 && i==entrenamiento.getNumeroTabatas()-1){
                        pasosEntrenamiento.add(crearPaso(entrenamiento.getNumeroSeries(), entrenamiento.getNumeroTabatas(), entrenamiento.getEjercicio(j), "FIN"));
                    }else{
                        pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, entrenamiento.getEjercicio(j), entrenamiento.getEjercicio((j+1)%entrenamiento.getNumeroSeries())));
                    }
                }
            }
        }else if(entrenamiento.getTiempoDescanso()>0 && entrenamiento.getDescansoTabata()<=0){
            for (int i = 0; i < entrenamiento.getNumeroTabatas(); i++) {
                for (int j = 0; j < entrenamiento.getNumeroSeries(); j++) {
                    if(j==entrenamiento.getNumeroSeries()-1 && i==entrenamiento.getNumeroTabatas()-1){
                        pasosEntrenamiento.add(crearPaso(entrenamiento.getNumeroSeries(), entrenamiento.getNumeroTabatas(), entrenamiento.getEjercicio(j), "FIN"));
                    }else{
                        pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, entrenamiento.getEjercicio(j), "Descanso"));
                        pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, "Descanso", entrenamiento.getEjercicio((j+1)%entrenamiento.getNumeroSeries())));
                    }
                }
            }
        }else if(entrenamiento.getTiempoDescanso()<=0 && entrenamiento.getDescansoTabata()>0){
            for (int i = 0; i < entrenamiento.getNumeroTabatas(); i++) {
                for (int j = 0; j < entrenamiento.getNumeroSeries(); j++) {
                    if(j==entrenamiento.getNumeroSeries()-1 && i==entrenamiento.getNumeroTabatas()-1){
                        pasosEntrenamiento.add(crearPaso(entrenamiento.getNumeroSeries(), entrenamiento.getNumeroTabatas(), entrenamiento.getEjercicio(j), "FIN"));
                    }else{
                        if(j==entrenamiento.getNumeroSeries()-1)
                            pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, entrenamiento.getEjercicio(j), "Descanso tabata"));
                        else
                            pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, entrenamiento.getEjercicio(j), entrenamiento.getEjercicio((j+1)%entrenamiento.getNumeroSeries())));
                    }
                }
                if(i!=entrenamiento.getNumeroTabatas()-1)
                    pasosEntrenamiento.add(crearPaso(1, i+1, "Descanso tabata", entrenamiento.getEjercicio(0)));
            }
        }else if(entrenamiento.getTiempoDescanso()>0 && entrenamiento.getDescansoTabata()>0){
            for (int i = 0; i < entrenamiento.getNumeroTabatas(); i++) {
                for (int j = 0; j < entrenamiento.getNumeroSeries(); j++) {
                    if(j==entrenamiento.getNumeroSeries()-1 && i==entrenamiento.getNumeroTabatas()-1){
                        pasosEntrenamiento.add(crearPaso(entrenamiento.getNumeroSeries(), entrenamiento.getNumeroTabatas(), entrenamiento.getEjercicio(j), "FIN"));
                    }else{
                        if(j==entrenamiento.getNumeroSeries()-1)
                            pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, entrenamiento.getEjercicio(j), "Descanso tabata"));
                        else{
                            pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, entrenamiento.getEjercicio(j), "Descanso"));
                            pasosEntrenamiento.add(crearPaso((j+1)%(entrenamiento.getNumeroSeries()+1), i+1, "Descanso", entrenamiento.getEjercicio((j+1)%entrenamiento.getNumeroSeries())));
                        }
                    }
                }
                if(i!=entrenamiento.getNumeroTabatas()-1)
                    pasosEntrenamiento.add(crearPaso(1, i+1, "Descanso tabata", entrenamiento.getEjercicio(0)));
            }
        }

        for(int i=0;i<pasosEntrenamiento.size();i++){
            System.out.println("\nVECTOR: " + pasosEntrenamiento.get(i));
        }
    }

    public String getTiempoTotalString(long tiempo){
        String tiempoFinal="";
        long horas = (tiempo / 3600);
        long minutos = ((tiempo-horas*3600)/60);
        long segundos = tiempo-(horas*3600+minutos*60);

        if(horas==0 && minutos==0 && segundos>=0){
            tiempoFinal = Long.toString(segundos);
        }else if(horas==0 && minutos>0 && segundos >=0){
            if(segundos/10==0 && minutos/10==0)
                tiempoFinal = "0" + Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
            else if(segundos/10==0 && minutos/10!=0)
                tiempoFinal = Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
            else if(segundos/10!=0 && minutos/10==0)
                tiempoFinal = "0" + Long.toString(minutos) + ":" + Long.toString(segundos);
            else
                tiempoFinal = Long.toString(minutos) + ":" + Long.toString(segundos);
        }else{
            if(horas/10==0 && segundos/10==0 && minutos/10==0)
                tiempoFinal = "0" + Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
            else if(horas/10!=0 && segundos/10==0 && minutos/10==0)
                tiempoFinal = Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
            else if(horas/10==0 && segundos/10!=0 && minutos/10==0)
                tiempoFinal = "0" + Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + Long.toString(segundos);
            else if(horas/10==0 && segundos/10==0 && minutos/10!=0)
                tiempoFinal = "0" + Long.toString(horas) + ":" + Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
            else if(horas/10!=0 && segundos/10!=0 && minutos/10==0)
                tiempoFinal = Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + Long.toString(segundos);
            else if(horas/10==0 && segundos/10!=0 && minutos/10!=0)
                tiempoFinal = "0" + Long.toString(horas) + ":" + Long.toString(minutos) + ":" + Long.toString(segundos);
            else if(horas/10!=0 && segundos/10==0 && minutos/10!=0)
                tiempoFinal = Long.toString(horas) + ":" + Long.toString(minutos) + ":" + "0" +  Long.toString(segundos);
            else
                tiempoFinal = Long.toString(horas) + ":" + Long.toString(minutos) + ":" + Long.toString(segundos);
        }

        return tiempoFinal;
    }

    private String getTiempoString(long tsegundos){
        String tiempoFinal="";
        long horas = (tsegundos / 3600);
        long minutos = ((tsegundos-horas*3600)/60);
        long segundos = tsegundos-(horas*3600+minutos*60);

        if(horas/10==0 && segundos/10==0 && minutos/10==0)
            tiempoFinal = "0" + Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
        else if(horas/10!=0 && segundos/10==0 && minutos/10==0)
            tiempoFinal = Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
        else if(horas/10==0 && segundos/10!=0 && minutos/10==0)
            tiempoFinal = "0" + Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + Long.toString(segundos);
        else if(horas/10==0 && segundos/10==0 && minutos/10!=0)
            tiempoFinal = "0" + Long.toString(horas) + ":" + Long.toString(minutos) + ":" + "0" + Long.toString(segundos);
        else if(horas/10!=0 && segundos/10!=0 && minutos/10==0)
            tiempoFinal = Long.toString(horas) + ":" + "0" + Long.toString(minutos) + ":" + Long.toString(segundos);
        else if(horas/10==0 && segundos/10!=0 && minutos/10!=0)
            tiempoFinal = "0" + Long.toString(horas) + ":" + Long.toString(minutos) + ":" + Long.toString(segundos);
        else if(horas/10!=0 && segundos/10==0 && minutos/10!=0)
            tiempoFinal = Long.toString(horas) + ":" + Long.toString(minutos) + ":" + "0" +  Long.toString(segundos);
        else
            tiempoFinal = Long.toString(horas) + ":" + Long.toString(minutos) + ":" + Long.toString(segundos);

        return tiempoFinal;
    }

    public void actualizarViewCronometro(int i){
        CronometroEntrenamiento.setSerieActual(pasosEntrenamiento.get(i).get(0));
        CronometroEntrenamiento.setTabataActual(pasosEntrenamiento.get(i).get(1));
        CronometroEntrenamiento.setEjercicioActual(pasosEntrenamiento.get(i).get(2));
        CronometroEntrenamiento.setEjercicioSiguiente(pasosEntrenamiento.get(i).get(3));
    }

    public static void setEnProceso(boolean opcion){
        enProceso = opcion;
    }

    private void procedimientoCronometro(){
        if(pasosEntrenamiento.get(i).get(2)=="Preparación"){
            actualizarViewCronometro(i);
            CronometroEntrenamiento.setBackgroundTiempoCronometro(0);
            cronometro = new CountDownTimer((entrenamiento.getTiempoPreparacion()+1)*1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==0)
                        onFinish();
                }

                public void onFinish() {
                    cronometro.cancel();
                    if(i<pasosEntrenamiento.size())
                        procedimientoCronometro();
                    else
                        CronometroEntrenamiento.setTiempoCronometro("FIN");
                }
            }.start();
        }else if(pasosEntrenamiento.get(i).get(2)=="Descanso"){
            actualizarViewCronometro(i);
            CronometroEntrenamiento.setBackgroundTiempoCronometro(2);
            cronometro = new CountDownTimer((entrenamiento.getTiempoDescanso()+1)*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==0)
                        onFinish();
                }

                public void onFinish() {
                    cronometro.cancel();
                    if(i<pasosEntrenamiento.size())
                        procedimientoCronometro();
                    else
                        CronometroEntrenamiento.setTiempoCronometro("FIN");
                }
            }.start();
        }else if(pasosEntrenamiento.get(i).get(2)=="Descanso tabata"){
            actualizarViewCronometro(i);
            CronometroEntrenamiento.setBackgroundTiempoCronometro(3);
            cronometro = new CountDownTimer((entrenamiento.getDescansoTabata()+1)*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==0)
                        onFinish();
                }

                public void onFinish() {
                    cronometro.cancel();
                    if(i<pasosEntrenamiento.size())
                        procedimientoCronometro();
                    else
                        CronometroEntrenamiento.setTiempoCronometro("FIN");
                }
            }.start();
        }else{
            actualizarViewCronometro(i);
            CronometroEntrenamiento.setBackgroundTiempoCronometro(1);
            cronometro = new CountDownTimer((entrenamiento.getTiempoEjercicio()+1)*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==0)
                        onFinish();
                }

                public void onFinish() {
                    cronometro.cancel();
                    if(i<pasosEntrenamiento.size())
                        procedimientoCronometro();
                    else
                        CronometroEntrenamiento.setTiempoCronometro("FIN");
                }
            }.start();
        }
        i++;
    }
}