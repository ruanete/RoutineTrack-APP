package com.example.tabatatimer.Datos;

import android.content.ContentValues;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Evento {
    private Date fecha;
    private String descripcion;

    public Evento(){
        fecha = new Date();
        descripcion = "";
    }

    public Evento(String descripcion){
        fecha = new Date();
        this.descripcion = descripcion;
    }

    public Date getFecha(){
        return fecha;
    }

    public void setFecha(Date fecha){
        this.fecha = fecha;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(){
        this.descripcion = descripcion;
    }

    public String getYear(){
        SimpleDateFormat year = new SimpleDateFormat("y");
        return year.format(fecha);
    }

    public String getMonth(){
        SimpleDateFormat month = new SimpleDateFormat("MM");
        return month.format(fecha);
    }

    public String getDay(){
        SimpleDateFormat day = new SimpleDateFormat("dd");
        return day.format(fecha);
    }

    public String getHora(){
        SimpleDateFormat hour = new SimpleDateFormat("HH:mm");
        return hour.format(fecha);
    }

    public ContentValues toContentValuesEvento() {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues=new ContentValues();

        values.put(EventoContract.ColumnasEventos.FECHA, dateFormat.format(fecha));
        values.put(EventoContract.ColumnasEventos.DESCRIPCION, descripcion);
        return values;
    }
}
