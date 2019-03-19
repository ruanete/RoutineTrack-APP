package com.example.tabatatimer.Datos;

import android.provider.BaseColumns;

public class EntrenamientoContract {
    public static abstract class ColumnasEntrenamiento implements BaseColumns{
        public static final String TABLE_NAME = "entrenamientos";

        public static final String ID = "id";
        public static final String NOMBRE_ENTRENAMIENTO = "nombre";
        public static final String TIEMPO_PREPARACION = "tiempoPreparacion";
        public static final String TIEMPO_EJERCICIO = "tiempoEjercicio";
        public static final String TIEMPO_DESCANSO = "tiempoDescanso";
        public static final String NUMERO_SERIES = "numeroSeries";
        public static final String NUMERO_TABATAS = "numeroTabatas";
        public static final String DESCANSO_TABATA = "descansoTabata";
        public static final String TIEMPO_TOTAL = "tiempoTotal";
    }

    public static abstract class ColumnasEjercicios implements BaseColumns{
        public static final String TABLE_NAME = "ejercicios";

        public static final String ID = "id";
        public static final String NOMBRE_EJERCICIO = "nombre_ejercicio";
    }
}
