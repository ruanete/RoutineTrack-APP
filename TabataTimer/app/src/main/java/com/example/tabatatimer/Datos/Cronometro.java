package com.example.tabatatimer.Datos;

import android.os.CountDownTimer;

import com.example.tabatatimer.CronometroEntrenamiento;

public class Cronometro {
    private CountDownTimer cronometroTotal;
    private CountDownTimer cronometro;
    private Entrenamiento entrenamiento;
    private long tiempoRestante;

    private int serieActual;
    private int posEjActual;
    private int posEjSiguiente;
    private int contadorEjercicio=0;
    private int contadorDescanso=0;

    private Boolean preparacionRealizado=false;
    private Boolean ejercicioRealizado=false;
    private Boolean descansoSerieRealizado=false;
    private Boolean descansoTabataRealizado=false;

    public Cronometro(){
        tiempoRestante = 0;
        serieActual = 0;
        posEjActual=0;
        posEjSiguiente=1;
    }

    public Cronometro(Entrenamiento entrenamiento){
        this.entrenamiento = entrenamiento;
        tiempoRestante = entrenamiento.getTiempoTotal();
        serieActual = 1;
        posEjActual=0;
        posEjSiguiente=1;

        if(entrenamiento.getTiempoPreparacion()>0){
            CronometroEntrenamiento.setEjercicioActual("Preparación");
            CronometroEntrenamiento.setEjercicioSiguiente(entrenamiento.getEjercicio(0));
            CronometroEntrenamiento.setBackgroundTiempoCronometro(0);
            CronometroEntrenamiento.setTiempoCronometro(getTiempoTotalString(entrenamiento.getTiempoPreparacion()));
        }else{
            CronometroEntrenamiento.setEjercicioActual(entrenamiento.getEjercicio(0));
            CronometroEntrenamiento.setEjercicioSiguiente(entrenamiento.getEjercicio(1));
            CronometroEntrenamiento.setTiempoCronometro(getTiempoTotalString(entrenamiento.getTiempoEjercicio()));
        }

        CronometroEntrenamiento.setSerieActual("1");
        CronometroEntrenamiento.setTabataActual("1");
        CronometroEntrenamiento.setTiempoTotal(getTiempoTotalString(tiempoRestante));

        cronometroTotal = new CountDownTimer(entrenamiento.getTiempoTotal()*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished / 1000;
                CronometroEntrenamiento.setTiempoTotal(getTiempoTotalString(tiempoRestante));
                procedimientoCronometro();
            }

            public void onFinish() {
                CronometroEntrenamiento.setTiempoCronometro("FIN");
            }
        };
    }

    private void procedimientoCronometro(){
        if(entrenamiento.getTiempoPreparacion()>0 && !preparacionRealizado){
            CronometroEntrenamiento.setBackgroundTiempoCronometro(0);
            preparacionRealizado=true;
            ejercicioRealizado=true;
            descansoSerieRealizado=true;
            descansoTabataRealizado=true;
            cronometro = new CountDownTimer((entrenamiento.getTiempoPreparacion()+1)*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==1)
                        onFinish();
                }

                public void onFinish() {
                    preparacionRealizado=true;
                    ejercicioRealizado=false;
                    descansoSerieRealizado=true;
                    descansoTabataRealizado=true;
                    cronometro.cancel();
                }
            }.start();
        }else if(entrenamiento.getTiempoEjercicio()>0 && !ejercicioRealizado){
            CronometroEntrenamiento.setBackgroundTiempoCronometro(1);
            preparacionRealizado=true;
            ejercicioRealizado=true;
            descansoSerieRealizado=true;
            descansoTabataRealizado=true;
            cronometro = new CountDownTimer((entrenamiento.getTiempoEjercicio()+1)*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==1)
                        onFinish();
                }

                public void onFinish() {
                    if(entrenamiento.getTiempoDescanso()>0 && tiempoRestante!=0){
                        preparacionRealizado=true;
                        ejercicioRealizado=true;
                        descansoSerieRealizado=false;
                        descansoTabataRealizado=true;
                    }else if(entrenamiento.getTiempoDescanso()<0 && tiempoRestante!=0){
                        preparacionRealizado=true;
                        ejercicioRealizado=false;
                        descansoSerieRealizado=true;
                        descansoTabataRealizado=true;
                    }else{
                        preparacionRealizado=true;
                        ejercicioRealizado=true;
                        descansoSerieRealizado=true;
                        descansoTabataRealizado=true;
                    }

                    cronometro.cancel();
                }
            }.start();
        }else if(entrenamiento.getTiempoDescanso()>0 && !descansoSerieRealizado){
            CronometroEntrenamiento.setBackgroundTiempoCronometro(2);
            preparacionRealizado=true;
            ejercicioRealizado=true;
            descansoSerieRealizado=true;
            descansoTabataRealizado=true;
            cronometro = new CountDownTimer((entrenamiento.getTiempoDescanso()+1)*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==1)
                        onFinish();
                }

                public void onFinish() {
                    System.out.println("\nDESCANSO TABATA: " + entrenamiento.getDescansoTabata());
                    if(entrenamiento.getDescansoTabata()>0 && tiempoRestante!=0){
                        preparacionRealizado=true;
                        ejercicioRealizado=true;
                        descansoSerieRealizado=true;
                        descansoTabataRealizado=false;
                    }else if(entrenamiento.getDescansoTabata()<0 && tiempoRestante!=0){
                        preparacionRealizado=true;
                        ejercicioRealizado=false;
                        descansoSerieRealizado=true;
                        descansoTabataRealizado=true;
                    }else{
                        preparacionRealizado=true;
                        ejercicioRealizado=true;
                        descansoSerieRealizado=true;
                        descansoTabataRealizado=true;
                    }

                    cronometro.cancel();
                }
            }.start();
        }else if(entrenamiento.getDescansoTabata()>0 && !descansoTabataRealizado){
            CronometroEntrenamiento.setBackgroundTiempoCronometro(3);
            preparacionRealizado=true;
            ejercicioRealizado=true;
            descansoSerieRealizado=true;
            descansoTabataRealizado=true;
            cronometro = new CountDownTimer((entrenamiento.getDescansoTabata()+1)*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    CronometroEntrenamiento.setTiempoCronometro(Long.toString(millisUntilFinished / 1000));
                    if(millisUntilFinished / 1000==1)
                        onFinish();
                }

                public void onFinish() {
                    preparacionRealizado=true;
                    ejercicioRealizado=false;
                    descansoSerieRealizado=true;
                    descansoTabataRealizado=true;
                    cronometro.cancel();
                }
            }.start();
        }
    }

    public void iniciarCronometro(){
        cronometroTotal.start();
        //cronometro.start();
    }

    public void finalizarCronometro(){
        cronometroTotal.cancel();
        cronometro.cancel();
    }

    public void siguienteSerie(){
        serieActual++;
        posEjActual++;
        posEjActual%=entrenamiento.getNumeroSeries();
        posEjSiguiente++;
        posEjSiguiente%=entrenamiento.getNumeroSeries();
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
}

    /*private void procedimientoCronometro(){
        int inferior_ejercicio = entrenamiento.getTiempoDescanso()+(entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion()-serieActual*entrenamiento.getTiempoEjercicio())-serieActual*entrenamiento.getTiempoDescanso();
        long superior_ejercicio = entrenamiento.getTiempoEjercicio() + entrenamiento.getTiempoDescanso()+(entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion()-serieActual*entrenamiento.getTiempoEjercicio())-serieActual*entrenamiento.getTiempoDescanso();
        int inferior_descanso = (entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion()-serieActual*entrenamiento.getTiempoEjercicio())-serieActual*entrenamiento.getTiempoDescanso();
        long superior_descanso = entrenamiento.getTiempoDescanso()+(entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion()-serieActual*entrenamiento.getTiempoEjercicio())-serieActual*entrenamiento.getTiempoDescanso();



        if(tiempoRestante>(entrenamiento.getTiempoTotal()-entrenamiento.getTiempoPreparacion())){
            //Si tiene tiempo de descanso se cambia el background a amarillo del cronometro
            CronometroEntrenamiento.setBackgroundTiempoCronometro(0);
        }

        if(tiempoRestante>inferior_ejercicio && tiempoRestante<=superior_ejercicio){
            System.out.println("\nEJERCICIO: " + inferior_ejercicio + " - " + superior_ejercicio);
            //Si estamos en el tiempo de ejercicio cambiamos el background a rojo del cronometro
            contadorEjercicio++;
            CronometroEntrenamiento.setBackgroundTiempoCronometro(1);
            CronometroEntrenamiento.setEjercicioActual(entrenamiento.getEjercicio(posEjActual));
            CronometroEntrenamiento.setEjercicioSiguiente("Descanso");

            if(entrenamiento.getTiempoDescanso()<=0 && entrenamiento.getTiempoEjercicio()>0 && contadorDescanso==entrenamiento.getTiempoEjercicio()){
                siguienteSerie();
                if((serieActual%(entrenamiento.getNumeroSeries()+1))==0){
                    serieActual=1;
                    CronometroEntrenamiento.setSerieActual(Integer.toString(1));
                }else{
                    CronometroEntrenamiento.setSerieActual(Integer.toString(serieActual%=entrenamiento.getNumeroSeries()+1));
                }
                contadorEjercicio=0;
                contadorDescanso=0;
            }

        }

        if(tiempoRestante>=inferior_descanso && tiempoRestante<superior_descanso){
            System.out.println("\nDESCANSO: " + inferior_descanso + " - " + superior_descanso);
            //Si estamos en el tiempo de descanso cambiamos el background a verde del cronometro
            contadorDescanso++;
            CronometroEntrenamiento.setBackgroundTiempoCronometro(2);
            CronometroEntrenamiento.setEjercicioActual("Descanso");
            CronometroEntrenamiento.setEjercicioSiguiente(entrenamiento.getEjercicio(posEjActual));

            if(entrenamiento.getTiempoDescanso()>0 && contadorDescanso==entrenamiento.getTiempoDescanso()){
                siguienteSerie();
                if((serieActual%(entrenamiento.getNumeroSeries()+1))==0){
                    serieActual=1;
                    CronometroEntrenamiento.setSerieActual(Integer.toString(1));
                }else{
                    CronometroEntrenamiento.setSerieActual(Integer.toString(serieActual%=entrenamiento.getNumeroSeries()+1));
                }
                contadorEjercicio=0;
                contadorDescanso=0;
            }

        }

        CronometroEntrenamiento.setTiempoCronometro(getTiempoTotalString(tiempoRestante));
    }*/
