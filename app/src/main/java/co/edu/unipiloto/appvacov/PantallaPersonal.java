package co.edu.unipiloto.appvacov;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;

public class PantallaPersonal extends AppCompatActivity implements Serializable {

    private UsersAdapter adapter;
    private ArrayList<Cita> citas;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataReference;
    private FirebaseAuth mAuth;
    private int mYear, mMonth, mDay;
    private EditText txtfechaSeleccionada;
    private ListView lsvPacientesFecha;
    private String documento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_personal);
        documento = getIntent().getExtras().getString("documento");

        mDatabase = FirebaseDatabase.getInstance();
        mDataReference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        txtfechaSeleccionada = (EditText) findViewById(R.id.fechaAsignada);
        txtfechaSeleccionada.setOnClickListener(this::clickFecha);

        citas = new ArrayList<Cita>();
        lsvPacientesFecha = (ListView) findViewById(R.id.listview_pacientes);
        lsvPacientesFecha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent popupActivity = new Intent(PantallaPersonal.this, popup_vacuna.class);
                popupActivity.putExtra("cita", (Cita) adapter.getItem(position));
                popupActivity.putExtra("personal", documento);
                PantallaPersonal.this.startActivity(popupActivity);
                actualizarCitas();
            }
        });

        Button btnactualizarDisp = (Button) findViewById(R.id.btn_actualizarDisponibilidad);
        btnactualizarDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDisponibilidad();
            }
        });

        Button btnactualizarCitas = (Button) findViewById(R.id.btn_actualizarCitas);
        btnactualizarCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarCitas();
            }
        });

        Button planearRuta = (Button) findViewById(R.id.btnruta);
        planearRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularRuta();
            }
        });

        TextView cerrar = (TextView) findViewById(R.id.txtcerrarSesionPERS);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        getSupportActionBar().hide();
    }

    private void calcularRuta() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1");

        String waypoints = "";

        for (int i = 0; i < citas.toArray().length - 1; i++) {
            waypoints += citas.get(i).getLatitud() + "," +citas.get(i).getLongitud() + "|";
        }

        waypoints += citas.get(citas.toArray().length - 1).getLatitud() + "," +citas.get(citas.toArray().length - 1).getLongitud();
        System.out.println(waypoints);
        builder.appendQueryParameter("waypoints", waypoints);
        String uri = builder.build().toString();
        System.out.println(uri);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Intent inicio = new Intent(PantallaPersonal.this, login.class);
        PantallaPersonal.this.startActivity(inicio);
        PantallaPersonal.this.finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clickFecha(View v) {
        if (v == txtfechaSeleccionada) {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            LocalDate fechaHoy = LocalDate.now();
                            LocalDate fechaCita = LocalDate.of(year, (monthOfYear + 1), dayOfMonth);
                            Period periodo = Period.between(fechaHoy, fechaCita);
                            if (periodo.getYears() > -1 && periodo.getMonths() > -1 && periodo.getDays() > -1) {
                                txtfechaSeleccionada.setText(fechaCita.toString());
                            } else {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPersonal.this);
                                alerta.setMessage("No puede ser fecha anterior").setNegativeButton("Reintentar", null).create().show();
                                txtfechaSeleccionada.setText("");
                            }
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        }
    }

    private void actualizarCitas() {
        String fechaSeleccionada = txtfechaSeleccionada.getText().toString();
        citas.clear();
        if (!fechaSeleccionada.equals("")) {
            mDataReference.child("Cita").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                    child(fechaSeleccionada.substring(8, 10)).child(documento).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (int i = 8; i < 18; i++) {
                            for (int j = 0; j < 4; j += 3) {
                                if (snapshot.child("0" + i + ":" + j + "0").exists()) {
                                    if (!snapshot.child("0" + i + ":" + j + "0").child("realizada").getValue(Boolean.class)) {
                                        citas.add(new Cita(snapshot.child("0" + i + ":" + j + "0").child("documentoPaciente").getValue(String.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("eps").getValue(String.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("nombrePaciente").getValue(String.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("realizada").getValue(Boolean.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("hora").getValue(String.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("nombreVacuna").getValue(String.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("fecha").getValue(String.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("latitud").getValue(String.class),
                                                snapshot.child("0" + i + ":" + j + "0").child("longitud").getValue(String.class)));
                                    }
                                } else if (snapshot.child(i + ":" + j + "0").exists()) {
                                    if (!snapshot.child(i + ":" + j + "0").child("realizada").getValue(Boolean.class)) {
                                        citas.add(new Cita(snapshot.child(i + ":" + j + "0").child("documentoPaciente").getValue(String.class),
                                                snapshot.child(i + ":" + j + "0").child("eps").getValue(String.class),
                                                snapshot.child(i + ":" + j + "0").child("nombrePaciente").getValue(String.class),
                                                snapshot.child(i + ":" + j + "0").child("realizada").getValue(Boolean.class),
                                                snapshot.child(i + ":" + j + "0").child("hora").getValue(String.class),
                                                snapshot.child(i + ":" + j + "0").child("nombreVacuna").getValue(String.class),
                                                snapshot.child(i + ":" + j + "0").child("fecha").getValue(String.class),
                                                snapshot.child(i + ":" + j + "0").child("latitud").getValue(String.class),
                                                snapshot.child(i + ":" + j + "0").child("longitud").getValue(String.class)));
                                    }
                                }
                            }
                            actualizarLista();
                        }
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPersonal.this);
                        alerta.setMessage("No hay citas disponibles").setNegativeButton("Reintentar", null).create().show();
                        txtfechaSeleccionada.setText("");
                        actualizarLista();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPersonal.this);
            alerta.setMessage("Seleccione una fecha").setNegativeButton("Reintentar", null).create().show();
        }
    }

    private void actualizarLista() {
        this.adapter = new UsersAdapter(this, R.layout.item_cita, this.citas);
        lsvPacientesFecha.setAdapter(adapter);
    }

    private void actualizarDisponibilidad() {
        String fechaSeleccionada = txtfechaSeleccionada.getText().toString();
        if (!fechaSeleccionada.equals("")) {
            mDataReference.child("Disponibilidad").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                    child(fechaSeleccionada.substring(8, 10)).child(documento).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        mDataReference.child("Disponibilidad").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                                child(fechaSeleccionada.substring(8, 10)).child(documento).child("disp").child("disponible").setValue(true);
                        txtfechaSeleccionada.setText("");
                        AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPersonal.this);
                        alerta.setMessage("Se actualiza su disponibilidad").setNegativeButton("Continuar", null).create().show();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPersonal.this);
                        alerta.setMessage("Ya tiene la disponibilidad actualizada").setNegativeButton("Continuar", null).create().show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPersonal.this);
            alerta.setMessage("Seleccione una fecha").setNegativeButton("Reintentar", null).create().show();
        }
    }
}