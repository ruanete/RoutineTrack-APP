package com.example.tabatatimer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.tabatatimer.Datos.BaseDatos;
import com.example.tabatatimer.Datos.Evento;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class CalendarioEntrenamientos extends AppCompatActivity {
    CalendarView calendario;
    TextView anio, fecha;
    LinearLayout eventos_calendario;
    BaseDatos bd;
    ImageButton boton_calendario, boton_ajustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_entrenamientos);

        calendario = (CalendarView) findViewById(R.id.calendarView);
        anio = (TextView) findViewById(R.id.anio);
        fecha = (TextView) findViewById(R.id.fecha);
        eventos_calendario = (LinearLayout) findViewById(R.id.eventos_calendario);
        boton_calendario = findViewById(R.id.imageButton6);
        boton_calendario.setVisibility(View.INVISIBLE);
        boton_ajustes = findViewById(R.id.imageButton);
        boton_ajustes.setVisibility(View.INVISIBLE);

        bd = new BaseDatos(this);

        Date fecha_actual = new Date();
        generalScroll(fecha_actual);

        SimpleDateFormat y = new SimpleDateFormat("y");
        String year = y.format(fecha_actual);

        SimpleDateFormat m = new SimpleDateFormat("MM");
        String month = m.format(fecha_actual);

        SimpleDateFormat d = new SimpleDateFormat("dd");
        String day = d.format(fecha_actual);

        anio.setText(year);

        String resultantDate=getDateFormattedString("MM/dd/yyyy", month+1 + "/" + day + "/" + year, "EEEE, MMMM d");
        fecha.setText(resultantDate);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendario, int year, int month, int dayOfMonth) {
                String fecha_buscar=getDateFormattedString("MM/dd/yyyy", month+1 + "/" + dayOfMonth + "/" + year, "yyyy-MM-dd");

                try {
                    Date f=new SimpleDateFormat("yyyy-MM-dd").parse(fecha_buscar);
                    generalScroll(f);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                anio.setText(Integer.toString(year));
                String resultantDate=getDateFormattedString("MM/dd/yyyy", month+1 + "/" + dayOfMonth + "/" + year, "EEEE, MMMM d");
                fecha.setText(resultantDate);
            }
        });
    }

    public void abrirAjustes(View view){
        Intent intent = new Intent(this, MenuAjustes.class);
        startActivity(intent);
    }

    public static String getDateFormattedString(String sourceFormat,String dateString,String targetFormat) {
        SimpleDateFormat mOriginalFormat = new SimpleDateFormat(sourceFormat);//3/1/2016
        SimpleDateFormat mTargetFormat = new SimpleDateFormat(targetFormat);//Mar/1/2016

        String reqstring = null;
        try {
            reqstring = mTargetFormat.format(mOriginalFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reqstring;
    }

    public void volverInicio(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(new Intent(getBaseContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

    public void generalScroll(Date f){
        Vector<Evento> eventos = bd.getEventosPorFecha(f);
        if(eventos.size()!=0){
            eventos_calendario.removeAllViews();
            for(int i=0;i<eventos.size();i++){
                LinearLayout evento = new LinearLayout(this);
                TextView hora = new TextView(this);
                TextView titulo_evento = new TextView(this);
                View linea_separadora = new View(this);

                evento.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams eventoParams = new LinearLayout.LayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                eventoParams.setMargins(0, 0, 0, 10);
                evento.setLayoutParams(eventoParams);

                linea_separadora.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        5
                ));
                linea_separadora.setBackgroundColor(Color.parseColor("#C50E29"));

                hora.setText(eventos.get(i).getHora());
                hora.setTextColor(0xFFC50E29);
                hora.setTypeface(null, Typeface.BOLD);

                titulo_evento.setText(eventos.get(i).getDescripcion());
                titulo_evento.setTextColor(0xFFC50E29);

                linea_separadora.setBackgroundColor(0xFFC50E29);
                linea_separadora.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));


                evento.addView(hora);
                evento.addView(titulo_evento);
                evento.addView(linea_separadora);

                eventos_calendario.addView(evento);
            }
        }else{
            eventos_calendario.removeAllViews();

            LinearLayout evento = new LinearLayout(this);
            TextView hora = new TextView(this);
            TextView titulo_evento = new TextView(this);
            View linea_separadora = new View(this);

            evento.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams eventoParams = new LinearLayout.LayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            eventoParams.setMargins(0, 0, 0, 10);
            evento.setLayoutParams(eventoParams);

            linea_separadora.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            linea_separadora.setBackgroundColor(Color.parseColor("#C50E29"));

            hora.setText("Hora del entrenamiento");
            hora.setTextColor(0xFFC50E29);
            hora.setTypeface(null, Typeface.BOLD);

            titulo_evento.setText("No hay registros de entrenamiento este dia");
            titulo_evento.setTextColor(0xFFC50E29);

            linea_separadora.setBackgroundColor(0xFFC50E29);
            linea_separadora.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));


            evento.addView(hora);
            evento.addView(titulo_evento);
            evento.addView(linea_separadora);

            eventos_calendario.addView(evento);
        }
    }
}
