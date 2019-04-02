package com.example.tabatatimer;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.tabatatimer.Datos.BaseDatos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuAjustes extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    EditText edittext_fecha, edittext_nombre, edittext_altura, edittext_peso;
    RadioButton opcion_sexo, opcion_masculino, opcion_femenina;
    RadioGroup grupo_opcion_sexo;
    CircleImageView imagen_perfil;
    public static final int STORAGE_PERMISSION_CODE=23;
    Activity activity = this;
    String nombre_usuario="", sexo="", fecha_nacimiento="", altura="", peso="", direccion_imagen="";
    BaseDatos bd = new BaseDatos(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ajustes);

        imagen_perfil = findViewById(R.id.imagen_perfil);
        edittext_nombre = findViewById(R.id.edittext_nombre);
        edittext_altura = findViewById(R.id.edittext_altura);
        edittext_peso = findViewById(R.id.edittext_peso);
        edittext_fecha = findViewById(R.id.edittext_fecha);
        grupo_opcion_sexo = findViewById(R.id.grupo_sexo);
        opcion_masculino = findViewById(R.id.opcion_masculino);
        opcion_femenina = findViewById(R.id.opcion_femenino);

        setDatosUsuario();

        imagen_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    }
                }else{
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });

        edittext_fecha = findViewById(R.id.edittext_fecha);
        edittext_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.edittext_fecha:
                        showDatePickerDialog();
                        break;
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            direccion_imagen = picturePath;
            cursor.close();

            imagen_perfil.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                edittext_fecha.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDatosUsuario(){
        Vector<String> ajustes = bd.getAjustes();

        if(!ajustes.get(0).equals(""))
            imagen_perfil.setImageBitmap(BitmapFactory.decodeFile(ajustes.get(0)));

        if(!ajustes.get(1).equals(""))
            edittext_nombre.setText(ajustes.get(1));

        if(!ajustes.get(2).equals("")){
            if(ajustes.get(2).equals("Masculino")){
                opcion_masculino.setChecked(true);
            }else if(ajustes.get(2).equals("Femenino")){
                opcion_femenina.setChecked(true);
            }
        }

        if(!ajustes.get(3).equals(""))
            edittext_fecha.setText(ajustes.get(3));

        if(!ajustes.get(5).equals(""))
            edittext_peso.setText(ajustes.get(5));

        if(!ajustes.get(4).equals(""))
            edittext_altura.setText(ajustes.get(4));
    }

    public void volverAtras(View view){
        finish();
    }

    public void volverInicio(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void finalizarGuardadoEntrenamiento(View view){
        leerDatos();
        bd.guardarAjustes(direccion_imagen, nombre_usuario, sexo, fecha_nacimiento, altura, peso);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void leerDatos(){
        Vector<String> ajustes = bd.getAjustes();
        if(direccion_imagen.equals("") && !ajustes.get(0).equals(""))
            direccion_imagen = ajustes.get(0);

        nombre_usuario = edittext_nombre.getText().toString();
        opcion_sexo = findViewById(grupo_opcion_sexo.getCheckedRadioButtonId());
        sexo = opcion_sexo.getText().toString();
        fecha_nacimiento = edittext_fecha.getText().toString();
        altura = edittext_altura.getText().toString();
        peso = edittext_peso.getText().toString();

        System.out.println("\n1: " + nombre_usuario + "\n2:" + sexo + "\n3: " + fecha_nacimiento + "\n4:" + altura + "\n5:" + peso);
    }
}
