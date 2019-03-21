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

import static com.example.tabatatimer.Datos.EntrenamientoContract.ColumnasEjercicios.NOMBRE_EJERCICIO;

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
                + ColumnasEjercicios.ID_ENTRENAMIENTO + " INTEGER NOT NULL REFERENCES " + ColumnasEntrenamiento.TABLE_NAME + "(" + ColumnasEntrenamiento.ID + "),"
                + ColumnasEjercicios.ID_EJERCICIO + " INTEGER NOT NULL,"
                + NOMBRE_EJERCICIO + " TEXT NOT NULL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    //METODOS PROBADOS FUNCIONANDO
    public void guardarEjercicios(Entrenamiento entrenamiento) {
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues ejercicio = new ContentValues();
        for(int i=0;i<entrenamiento.getNumeroSeries();i++){
            ejercicio.put(ColumnasEjercicios.ID_ENTRENAMIENTO,entrenamiento.getIDentrenamiento());
            ejercicio.put(ColumnasEjercicios.ID_EJERCICIO,entrenamiento.getIDEjercicio(i));
            ejercicio.put(ColumnasEjercicios.NOMBRE_EJERCICIO,entrenamiento.getEjercicio(i));
            bd.insert(ColumnasEjercicios.TABLE_NAME,
                    null,
                    ejercicio);
        }
        bd.close();
    }

    public void guardarEntrenamiento(Entrenamiento entrenamiento) {
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.insert(
                ColumnasEntrenamiento.TABLE_NAME,
                null,
                entrenamiento.toContentValuesEntrenamiento());

        ContentValues ejercicio = new ContentValues();
        for(int i=0;i<entrenamiento.getNumeroSeries();i++){
            ejercicio.put(ColumnasEjercicios.ID_ENTRENAMIENTO,entrenamiento.getIDentrenamiento());
            ejercicio.put(ColumnasEjercicios.ID_EJERCICIO,entrenamiento.getIDEjercicio(i));
            ejercicio.put(NOMBRE_EJERCICIO,entrenamiento.getEjercicio(i));
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
                Cursor cursorEjercicios = getReadableDatabase().rawQuery("select nombre_ejercicio, id_ejercicio from " + ColumnasEjercicios.TABLE_NAME + " where id_entrenamiento=?", new String[]{String.valueOf(cursorEntrenamiento.getInt(cursorEntrenamiento.getColumnIndex("id")))});
                Vector<String> ejercicios = new Vector<>();
                Vector<Integer> IDEjercicios = new Vector<>();
                if(cursorEjercicios.moveToFirst()){
                    do{
                        ejercicios.add(cursorEjercicios.getString(cursorEjercicios.getColumnIndex("nombre_ejercicio")));
                        IDEjercicios.add(cursorEjercicios.getInt(cursorEjercicios.getColumnIndex("id_ejercicio")));
                    }while(cursorEjercicios.moveToNext());
                }
                cursorEjercicios.close();

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
                        ejercicios,
                        IDEjercicios)
                );
            }while(cursorEntrenamiento.moveToNext());
        }
        cursorEntrenamiento.close();

        return entrenamientos;
    }

    public void editarEntrenamiento(Entrenamiento entrenamiento){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues c = entrenamiento.toContentValuesEntrenamiento();

        bd.update(ColumnasEntrenamiento.TABLE_NAME, c, "id = ?", new String[]{String.valueOf(entrenamiento.getIDentrenamiento())});

        bd.close();
    }

    public void editarEjerciciosEntrenamiento(Entrenamiento entrenamiento){
        SQLiteDatabase bd = this.getWritableDatabase();
        Vector<ContentValues> values;
        values = entrenamiento.toContentValuesEjercicios();

        for (int i = 0; i < values.size(); i++) {
            System.out.println("\nCONTENT VALUE ID:" + values.get(i).getAsString("id_entrenamiento") + "\nCONTENT VALUE IDEJERICICO:" + values.get(i).getAsString("id_ejercicio"));
            bd.update(ColumnasEjercicios.TABLE_NAME, values.get(i), "id_entrenamiento = ? and id_ejercicio=?", new String[]{String.valueOf(values.get(i).getAsString("id_entrenamiento")),
                                                                                                                                        String.valueOf(values.get(i).getAsString("id_ejercicio"))});
        }

        bd.close();
    }

    public void eliminarEjercicios(Entrenamiento entrenamiento){
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.delete(ColumnasEjercicios.TABLE_NAME, "id_entrenamiento=" + entrenamiento.getIDentrenamiento(), null);
    }
}
