package com.example.tabatatimer.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tabatatimer.Datos.EntrenamientoContract.ColumnasEntrenamiento;
import com.example.tabatatimer.Datos.EntrenamientoContract.ColumnasEjercicios;

import java.util.Vector;

public class BaseDatos extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RoutineTracker.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE Prueba(Texto TEXT)";


    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Comandos SQL
        System.out.println("TAMOOOOOOOOOOOOO");
        sqLiteDatabase.execSQL("CREATE TABLE " + ColumnasEntrenamiento.TABLE_NAME + " ("
                + ColumnasEntrenamiento._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ColumnasEntrenamiento.ID + " INTEGER UNIQUE NOT NULL,"
                + ColumnasEntrenamiento.NOMBRE_ENTRENAMIENTO + " TEXT NOT NULL,"
                + ColumnasEntrenamiento.TIEMPO_PREPARACION + " INTEGER NOT NULL,"
                + ColumnasEntrenamiento.TIEMPO_EJERCICIO + " INTEGER NOT NULL,"
                + ColumnasEntrenamiento.TIEMPO_DESCANSO + " INTEGER NOT NULL,"
                + ColumnasEntrenamiento.NUMERO_SERIES + " INTEGER NOT NULL,"
                + ColumnasEntrenamiento.NUMERO_TABATAS + " INTEGER NOT NULL,"
                + ColumnasEntrenamiento.DESCANSO_TABATA + " INTEGER NOT NULL,"
                + ColumnasEntrenamiento.TIEMPO_TOTAL + " INTEGER NOT NULL)"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + ColumnasEjercicios.TABLE_NAME + " ("
                + ColumnasEjercicios._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ColumnasEjercicios.ID + " INTEGER NOT NULL REFERENCES " + ColumnasEntrenamiento.TABLE_NAME + "(" + ColumnasEntrenamiento.ID + "),"
                + ColumnasEjercicios.NOMBRE_EJERCICIO + " TEXT NOT NULL)"
        );

        System.out.println("ENTRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public Cursor getTodosEntrenamientos() {
        return getReadableDatabase()
                .query(
                        ColumnasEntrenamiento.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getEntrenamientoPorID(String entrenamientoID) {
        Cursor c = getReadableDatabase().query(
                ColumnasEntrenamiento.TABLE_NAME,
                null,
                ColumnasEntrenamiento.ID + " LIKE ?",
                new String[]{entrenamientoID},
                null,
                null,
                null);
        return c;
    }

    //METODOS PROBADOS FUNCIONANDO
    public void guardarEntrenamiento(Entrenamiento entrenamiento) {
        //db.insert(ColumnasEjercicios.TABLE_NAME, null, entrenamiento.toContentValuesEjercicios());
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.insert(
                ColumnasEntrenamiento.TABLE_NAME,
                null,
                entrenamiento.toContentValuesEntrenamiento());

        ContentValues ejercicio = new ContentValues();
        for(int i=0;i<entrenamiento.getNumeroSeries();i++){
            ejercicio.put(ColumnasEjercicios.ID,entrenamiento.getIDentrenamiento());
            ejercicio.put(ColumnasEjercicios.NOMBRE_EJERCICIO,entrenamiento.getEjercicio(i));
            bd.insert(ColumnasEjercicios.TABLE_NAME,
                    null,
                    ejercicio);
        }
        bd.close();
    }

    public Vector getEntrenamientos(){
        Vector<Entrenamiento> entrenamientos = new Vector<>();
        Cursor cursorEntrenamiento = getReadableDatabase().query(ColumnasEntrenamiento.TABLE_NAME, null, null, null, null, null, null);

        if (cursorEntrenamiento.moveToFirst()){
            do{
                Cursor cursorEjercicios = getReadableDatabase().rawQuery("select nombre_ejercicio from " + ColumnasEjercicios.TABLE_NAME + " where id=?", new String[]{String.valueOf(cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("id")))});
                Vector<String> ejercicios = new Vector<>();
                if(cursorEjercicios.moveToFirst()){
                    do{
                        ejercicios.add(cursorEjercicios.getString(cursorEjercicios.getColumnIndex("nombre_ejercicio")));
                    }while(cursorEjercicios.moveToNext());
                }

                System.out.println("IDDDDDDDDD: " + cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("id")) + "\nEJERCICICOS: " + ejercicios);

                entrenamientos.add(new Entrenamiento(
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("id")),
                        cursorEntrenamiento.getString(cursorEntrenamiento.getColumnIndex("nombre")),
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("tiempoPreparacion")),
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("tiempoEjercicio")),
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("tiempoDescanso")),
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("numeroSeries")),
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("numeroTabatas")),
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("descansoTabata")),
                        cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("tiempoTotal")),
                        ejercicios)
                );
            }while(cursorEntrenamiento.moveToNext());
        }
        cursorEntrenamiento.close();

        return entrenamientos;
    }
}
