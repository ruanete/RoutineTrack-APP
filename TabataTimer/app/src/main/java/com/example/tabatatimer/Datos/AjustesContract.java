package com.example.tabatatimer.Datos;

import android.provider.BaseColumns;

public class AjustesContract {
    public static abstract class ColumnasAjustes implements BaseColumns {
        public static final String TABLE_NAME = "ajustes";

        public static final String NOMBRE = "nombre";
        public static final String SEXO = "sexo";
        public static final String FECHA_NACIMIENTO = "fecha_nacimiento";
        public static final String ALTURA = "altura";
        public static final String PESO = "peso";
    }
}
