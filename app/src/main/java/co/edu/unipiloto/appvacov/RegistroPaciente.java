package co.edu.unipiloto.appvacov;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegistroPaciente extends AppCompatActivity implements View.OnClickListener {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private EditText txtFN;
    private int mYear, mMonth, mDay;
    private int checkGenero = 3;
    private String genero = "";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private EditText txtnombre;
    private EditText txtcorreo;
    private EditText txtclave;
    private EditText txtnumero;
    private EditText txtdocumento;
    private EditText txtdireccion;
    private Spinner spnocupacion;
    private Spinner spneps;
    private Spinner spnenfermedad;
    private String latitud;
    private String longitud;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_paciente);

        txtnombre = (EditText) findViewById(R.id.etenombre);
        txtcorreo = (EditText) findViewById(R.id.etecorreo);
        txtclave = (EditText) findViewById(R.id.etecontraseña);
        txtnumero = (EditText) findViewById(R.id.etenumero);
        txtdocumento = (EditText) findViewById(R.id.etedocumento);
        spnocupacion = (Spinner) findViewById(R.id.spnocupacion);
        spneps = (Spinner) findViewById(R.id.spnEps);
        spnenfermedad = (Spinner) findViewById(R.id.spnenfermedad);

        txtdireccion = (EditText) findViewById(R.id.etedireccion);
        txtFN = (EditText) findViewById(R.id.etFechaNacimiento);
        txtFN.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        TextView login = (TextView) findViewById(R.id.txtsignin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(RegistroPaciente.this, login.class);
                RegistroPaciente.this.startActivity(login);
                RegistroPaciente.this.finish();
            }
        });

        Button btnRegistro = (Button) findViewById(R.id.btn_register);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtnombre.getText().toString();
                String correo = txtcorreo.getText().toString();
                String clave = txtclave.getText().toString();
                String numero = txtnumero.getText().toString();
                String documento = txtdocumento.getText().toString();
                String fechaNacimiento = txtFN.getText().toString();
                String ocupacion = String.valueOf(spnocupacion.getSelectedItem());
                String eps = String.valueOf(spneps.getSelectedItem());
                String enfermedad = String.valueOf(spnenfermedad.getSelectedItem());
                String direccion = txtdireccion.getText().toString();

                if (nombre.equals("") || correo.equals("") || clave.equals("") || numero.equals("") || documento.equals("") || fechaNacimiento.equals("") || ocupacion == "Seleccione" || eps == "Seleccione" || enfermedad == "Seleccione" || direccion.equals("")) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPaciente.this);
                    alerta.setMessage("Debe llenar todos los campos").setNegativeButton("Reintentar", null).create().show();
                } else {
                    registeruser(nombre, correo, clave, clave, numero, documento, fechaNacimiento, ocupacion, eps, enfermedad, direccion);
                }

            }
        });
        getSupportActionBar().hide();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rbtgeneroH:
                if (checked) {
                    checkGenero = 1;
                    genero = "M";
                }
                break;
            case R.id.rbtgeneroM:
                if (checked) {
                    checkGenero = 0;
                    genero = "F";
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        if (v == txtFN) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            LocalDate fechaHoy = LocalDate.now();
                            LocalDate fechaNacimiento = LocalDate.of(year, (monthOfYear + 1), dayOfMonth);
                            Period periodo = Period.between(fechaNacimiento, fechaHoy);
                            if (periodo.getYears() < 16) {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPaciente.this);
                                alerta.setMessage("Tiene que ser mayor de 16 años.").setNegativeButton("Reintentar", null).create().show();
                                txtFN.setText("");
                            } else {
                                txtFN.setText(fechaNacimiento.toString());
                            }
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        }
    }

    private void registeruser(String nombre, String correo, String clave, String s, String numero, String documento, String fechaNacimiento, String ocupacion, String eps, String enfermedad, String direccion) {
        myRef.child("Paciente").child(documento).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.getResult().exists()) {
                    mAuth.createUserWithEmailAndPassword(correo, clave)
                            .addOnCompleteListener(RegistroPaciente.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        txtcorreo.setText("");
                                        txtnombre.setText("");
                                        txtclave.setText("");
                                        txtnumero.setText("");
                                        txtdocumento.setText("");
                                        txtFN.setText("");
                                        txtdireccion.setText("");
                                        Paciente paciente = new Paciente(nombre, fechaNacimiento, genero, correo, numero, ocupacion, documento, eps, enfermedad, false, direccion, latitud, longitud);
                                        myRef.child("Paciente").child(documento).setValue(paciente);
                                        Intent inicio = new Intent(RegistroPaciente.this, login.class);
                                        RegistroPaciente.this.startActivity(inicio);
                                        RegistroPaciente.this.finish();
                                    } else {
                                        AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPaciente.this);
                                        alerta.setMessage("Inscripción fallida").setNegativeButton("Reintentar", null).create().show();
                                    }
                                }
                            });
                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPaciente.this);
                    alerta.setMessage("Inscripción fallida").setNegativeButton("Reintentar", null).create().show();
                }
            }
        });
    }

    public void obtenerDireccion(View view) {
        if (ActivityCompat.checkSelfPermission(RegistroPaciente.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(RegistroPaciente.this, Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            txtdireccion.setText(addressList.get(0).getAddressLine(0));
                            latitud = "" + addressList.get(0).getLatitude();
                            longitud = "" + addressList.get(0).getLongitude();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(RegistroPaciente.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
}
