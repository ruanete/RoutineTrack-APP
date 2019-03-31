package com.example.tabatatimer.Datos;

import android.provider.BaseColumns;

public class EventoContract {
    public static abstract class ColumnasEventos implements BaseColumns{
        public static final String TABLE_NAME = "eventos";

        public static final String FECHA = "fecha";
        public static final String DESCRIPCION = "descripcion";
    }
}
