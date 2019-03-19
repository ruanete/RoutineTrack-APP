package com.example.tabatatimer.Datos;

import android.content.ContentValues;
import android.view.View;

import com.example.tabatatimer.Datos.EntrenamientoContract.ColumnasEjercicios;
import com.example.tabatatimer.Datos.EntrenamientoContract.ColumnasEntrenamiento;

import java.io.Serializable;
import java.util.Vector;

public class Entrenamiento implements Serializable {
    private int IDentrenamiento;
    private String nombre;
    private int tiempoPreparacion;
    private int tiempoEjercicio;
    private int tiempoDescanso;
    private int numeroSeries;
    private int numeroTabatas;
    private int descansoTabata;
    private int tiempoTotal;
    private Vector<String> ejercicios;
    private Vector<Integer> IDEjercicios;

    public Entrenamiento(){
        IDentrenamiento= View.generateViewId();
        nombre = "Nombre del entrenamiento";
        tiempoPreparacion = 0;
        tiempoEjercicio = 0;
        tiempoDescanso = 0;
        numeroSeries = 1;
        numeroTabatas = 1;
        descansoTabata = 0;
        tiempoTotal = 0;
        ejercicios = new Vector<>();
        ejercicios.add("Nombre del ejercicio 1");
        IDEjercicios = new Vector<>();
    }

    public Entrenamiento(int IDentrenamiento, String nombre, int tiempoPreparacion, int tiempoEjercicio, int tiempoDescanso, int numeroSeries, int numeroTabatas, int descansoTabata, int tiempoTotal, Vector<String> ejercicios){
        this.IDentrenamiento = IDentrenamiento;
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
        this.tiempoEjercicio = tiempoEjercicio;
        this.tiempoDescanso = tiempoDescanso;
        this.numeroSeries = numeroSeries;
        this.numeroTabatas = numeroTabatas;
        this.descansoTabata = descansoTabata;
        this.tiempoTotal = tiempoTotal;
        this.ejercicios = ejercicios;
    }

    public int getIDentrenamiento(){
        return IDentrenamiento;
    }

    public void setIDentrenamiento(int IDentrenamiento){
        this.IDentrenamiento = IDentrenamiento;
    }

    public void calcularTiempoTotal(){
        tiempoTotal = tiempoPreparacion + tiempoEjercicio*numeroSeries*numeroTabatas + tiempoDescanso*numeroSeries*numeroTabatas + descansoTabata*numeroTabatas;
    }

    public void inicializarEjercicios(){
        for(int i=2;i<=numeroSeries;i++){
            ejercicios.add("Nombre del ejercicio " + i);
        }
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public int getTiempoPreparacion(){
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion){
        this.tiempoPreparacion = tiempoPreparacion;
        calcularTiempoTotal();
    }

    public int getTiempoEjercicio(){
        return tiempoEjercicio;
    }

    public void setTiempoEjercicio(int tiempoEjercicio){
        this.tiempoEjercicio = tiempoEjercicio;
        calcularTiempoTotal();
    }

    public int getTiempoDescanso(){
        return tiempoDescanso;
    }

    public void setTiempoDescanso(int tiempoDescanso){
        this.tiempoDescanso = tiempoDescanso;
        calcularTiempoTotal();
    }

    public int getNumeroSeries(){
        return numeroSeries;
    }

    public void setNumeroSeries(int numeroSeries){
        this.numeroSeries = numeroSeries;
        calcularTiempoTotal();
    }

    public int getNumeroTabatas(){
        return numeroTabatas;
    }

    public void setNumeroTabatas(int numeroTabatas){
        this.numeroTabatas = numeroTabatas;
        calcularTiempoTotal();
    }

    public int getDescansoTabata(){
        return descansoTabata;
    }

    public void setDescansoTabata(int descansoTabata){
        this.descansoTabata = descansoTabata;
        calcularTiempoTotal();
    }

    public int getTiempoTotal(){
        return tiempoTotal;
    }

    public void addEjercicio(String ejercicio){
        ejercicios.add(ejercicio);
    }

    public void setEjercicio(int i, String ejercicio){
        ejercicios.set(i, ejercicio);
    }

    public int getIDEjercicio(int pos){
        return IDEjercicios.get(pos);
    }

    public void setIDEjercicio(int i, int ID){
        IDEjercicios.set(i, ID);
    }

    public void addIDEjercicio(int ID){
        IDEjercicios.add(ID);
    }

    public String getEjercicio(int pos){
        return ejercicios.get(pos);
    }

    public ContentValues toContentValuesEjercicios() {
        ContentValues values = new ContentValues();
        values.put(ColumnasEjercicios.ID, IDentrenamiento);
        for (int i = 0; i < numeroSeries; i++)
            values.put(ColumnasEjercicios.NOMBRE_EJERCICIO, ejercicios.get(i));
        return values;
    }

    public ContentValues toContentValuesEntrenamiento() {
        ContentValues values = new ContentValues();
        values.put(ColumnasEntrenamiento.ID, IDentrenamiento);
        values.put(ColumnasEntrenamiento.NOMBRE_ENTRENAMIENTO, nombre);
        values.put(ColumnasEntrenamiento.TIEMPO_PREPARACION, tiempoPreparacion);
        values.put(ColumnasEntrenamiento.TIEMPO_EJERCICIO, tiempoEjercicio);
        values.put(ColumnasEntrenamiento.TIEMPO_DESCANSO, tiempoDescanso);
        values.put(ColumnasEntrenamiento.NUMERO_SERIES, numeroSeries);
        values.put(ColumnasEntrenamiento.NUMERO_TABATAS, numeroTabatas);
        values.put(ColumnasEntrenamiento.DESCANSO_TABATA, descansoTabata);
        values.put(ColumnasEntrenamiento.TIEMPO_TOTAL, tiempoTotal);
        return values;
    }

    public String getTiempoTotalString(){
        String tiempoFinal="";
        int horas = (tiempoTotal / 3600);
        int minutos = ((tiempoTotal-horas*3600)/60);
        int segundos = tiempoTotal-(horas*3600+minutos*60);

        if(horas==0 && minutos==0 && segundos>=0){
            tiempoFinal = Integer.toString(segundos);
        }else if(horas==0 && minutos>0 && segundos >=0){
            tiempoFinal = Integer.toString(minutos) + ":" + Integer.toString(segundos);
        }else{
            tiempoFinal = Integer.toString(horas) + ":" + Integer.toString(minutos) + ":" + Integer.toString(segundos);
        }

        return tiempoFinal;
    }
}
