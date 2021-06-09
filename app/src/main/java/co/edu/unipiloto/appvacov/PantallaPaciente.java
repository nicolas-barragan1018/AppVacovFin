package co.edu.unipiloto.appvacov;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PantallaPaciente extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText txtFechaInscripcion, txtHoraInscripcion, txtetapa;
    private int mYear, mMonth, mDay, hora, minuto;
    private String documento, etapa, documentoPers;
    private Paciente paciente;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_paciente);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        documento = getIntent().getExtras().getString("documento");
        etapa = "";
        documentoPers = "";
        paciente = new Paciente("", "", "", "", "", "", "", "", "", false, "", "", "");

        mDatabaseReference.child("Paciente").child(documento).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                paciente.setNombres(snapshot.child("nombres").getValue(String.class));
                paciente.setDocumento(snapshot.child("documento").getValue(String.class));
                paciente.setEnfermedad(snapshot.child("enfermedad").getValue(String.class));
                paciente.setEps(snapshot.child("eps").getValue(String.class));
                paciente.setCorreo(snapshot.child("correo").getValue(String.class));
                paciente.setFechaNacimiento(snapshot.child("fechaNacimiento").getValue(String.class));
                paciente.setGenero(snapshot.child("genero").getValue(String.class));
                paciente.setOcupacion(snapshot.child("ocupacion").getValue(String.class));
                paciente.setTelefono(snapshot.child("telefono").getValue(String.class));
                paciente.setCitaVacuna(snapshot.child("citaVacuna").getValue(boolean.class));
                paciente.setDireccion(snapshot.child("direccion").getValue(String.class));
                paciente.setLatitud(snapshot.child("latitud").getValue(String.class));
                paciente.setLongitud(snapshot.child("longitud").getValue(String.class));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("0").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("0").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("1").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("1").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("2").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("2").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("3").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("3").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("4").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("4").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("5").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("5").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("6").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("6").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("7").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("7").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("8").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("8").child("variable").getValue(Boolean.class)));
                paciente.setSintomas(new Sintomas(snapshot.child("sintomas").child("9").child("nombre").getValue(String.class),
                        snapshot.child("sintomas").child("9").child("variable").getValue(Boolean.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button btnSintomas = (Button) findViewById(R.id.btn_sintomas);
        btnSintomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarSintomas();
            }
        });

        txtetapa = (EditText) findViewById(R.id.txt_etapaPAC);

        txtFechaInscripcion = (EditText) findViewById(R.id.txt_fechaRegistroPAC);
        txtFechaInscripcion.setOnClickListener(this::clickFecha);

        txtHoraInscripcion = (EditText) findViewById(R.id.txt_horaRegistroPAC);
        txtHoraInscripcion.setOnClickListener(this::clickHora);

        Button btnCita = (Button) findViewById(R.id.btn_cita);
        btnCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirCita();
            }
        });

        TextView txtcerrarSesion = (TextView) findViewById(R.id.txtcerrarSesionPAC);
        txtcerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        getSupportActionBar().hide();
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.cb_fiebre:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Fiebre", true));
                } else {
                    paciente.setSintomas(new Sintomas("Fiebre", false));
                }
                break;
            case R.id.cb_garganta:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Dolor Garganta", true));
                } else {
                    paciente.setSintomas(new Sintomas("Dolor Garganta", false));
                }
                break;
            case R.id.cb_nasal:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Congestion Nasal", true));
                } else {
                    paciente.setSintomas(new Sintomas("Congestion Nasal", false));
                }
                break;
            case R.id.cb_tos:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Tos", true));
                } else {
                    paciente.setSintomas(new Sintomas("Tos", false));
                }
                break;
            case R.id.cb_respirar:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Dificultad Respirar", true));
                } else {
                    paciente.setSintomas(new Sintomas("Dificultad Respirar", false));
                }
                break;
            case R.id.cb_fatiga:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Fatiga", true));
                } else {
                    paciente.setSintomas(new Sintomas("Fatiga", false));
                }
                break;
            case R.id.cb_escalofrio:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Escalofrio", true));
                } else {
                    paciente.setSintomas(new Sintomas("Escalofrio", false));
                }
                break;
            case R.id.cb_musculos:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Musculos", true));
                } else {
                    paciente.setSintomas(new Sintomas("Musculos", false));
                }
                break;
            case R.id.cb_cabeza:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Cabeza", true));
                } else {
                    paciente.setSintomas(new Sintomas("Cabeza", false));
                }
                break;
            case R.id.cb_ninguno:
                if (checked) {
                    paciente.setSintomas(new Sintomas("Ninguno", true));
                    paciente.setSintomas(new Sintomas("Fiebre", false));
                    paciente.setSintomas(new Sintomas("Dolor Garganta", false));
                    paciente.setSintomas(new Sintomas("Congestion Nasal", false));
                    paciente.setSintomas(new Sintomas("Tos", false));
                    paciente.setSintomas(new Sintomas("Dificultad Respirar", false));
                    paciente.setSintomas(new Sintomas("Fatiga", false));
                    paciente.setSintomas(new Sintomas("Escalofrio", false));
                    paciente.setSintomas(new Sintomas("Musculos", false));
                    paciente.setSintomas(new Sintomas("Cabeza", false));
                } else {
                    paciente.setSintomas(new Sintomas("Ninguno", false));
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clickFecha(View v) {
        etapaVacunacion();
        traerEtapa();
        if (v == txtFechaInscripcion) {
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
                            if (periodo.getYears() > 0 || periodo.getMonths() > 0 || periodo.getDays() > 0) {
                                txtFechaInscripcion.setText(fechaCita.toString());
                            } else {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                                alerta.setMessage("No puede ser fecha anterior").setNegativeButton("Reintentar", null).create().show();
                                txtFechaInscripcion.setText("");
                            }
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clickHora(View v) {
        if (v == txtHoraInscripcion) {
            final Calendar tiempo = Calendar.getInstance();
            hora = tiempo.get(Calendar.HOUR_OF_DAY);
            minuto = tiempo.get(Calendar.MINUTE);

            TimePickerDialog recogerHora = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    LocalTime tiempo = LocalTime.of(hourOfDay, minute);
                    if (hourOfDay > 7 && hourOfDay < 18) {
                        if (minute == 0 || minute == 30) {
                            txtHoraInscripcion.setText(tiempo.toString());
                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                            alerta.setMessage("Las citas se permiten en franjas 00 o 30").setNegativeButton("Reintentar", null).create().show();
                            txtFechaInscripcion.setText("");
                        }
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                        alerta.setMessage("Citas en horario de oficina").setNegativeButton("Reintentar", null).create().show();
                        txtFechaInscripcion.setText("");
                    }
                }
            }, hora, minuto, false);
            recogerHora.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            recogerHora.show();
        }
    }

    private void cerrarSesion() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent inicio = new Intent(PantallaPaciente.this, login.class);
        PantallaPaciente.this.startActivity(inicio);
        PantallaPaciente.this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void etapaVacunacion() {
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaNacimiento = LocalDate.parse(paciente.getFechaNacimiento());
        Period periodo = Period.between(fechaNacimiento, fechaHoy);
        if (periodo.getYears() > 79) {
            txtetapa.setText("1");
        } else if (periodo.getYears() > 59) {
            txtetapa.setText("2");
        } else {
            switch (paciente.getOcupacion()) {
                case "Salud primera linea":
                    txtetapa.setText("1");
                    return;
                case "Salud":
                    txtetapa.setText("2");
                    return;
                case "Educador":
                case "Fuerza publica":
                    txtetapa.setText("3");
                    return;
                case "Socorrista":
                    txtetapa.setText("4");
                    return;
            }
            if (!paciente.getEnfermedad().equals("Ninguna de las anteriores")) {
                txtetapa.setText("3");
                return;
            }
            txtetapa.setText("5");
        }
    }

    private void actualizarSintomas() {
        mDatabaseReference.child("Paciente").child(documento).setValue(paciente);
    }

    private void traerEtapa() {
        mDatabaseReference.child("Vacuna").child(paciente.getEps()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("asdasd");
                System.out.println("1 " + snapshot.child("etapa").getValue(String.class));
                etapa = snapshot.child("etapa").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pedirCita() {
        String fechaSeleccionada = txtFechaInscripcion.getText().toString();
        String horaSeleccionada = txtHoraInscripcion.getText().toString();
        final boolean[] complete = {true};
        if (fechaSeleccionada.equals("") || horaSeleccionada.equals("")) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
            alerta.setMessage("Llene datos Hora y Fecha").setNegativeButton("Continuar", null).create().show();
        } else {
            if (!paciente.isCitaVacuna()) {
                Cita cita = new Cita(paciente.getDocumento(), paciente.getEps(), paciente.getNombres(), false, horaSeleccionada, "No Aplicada", fechaSeleccionada, paciente.getLatitud(), paciente.getLongitud());
                if (etapa.equals(txtetapa.getText().toString())) {
                    mDatabaseReference.child("Cita").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                            child(fechaSeleccionada.substring(8, 10)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                documentoPers = snap.getKey();
                                if (!snap.child(horaSeleccionada).exists()) {
                                    paciente.setCitaVacuna(true);
                                    mDatabaseReference.child("Cita").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                                            child(fechaSeleccionada.substring(8, 10)).child(documentoPers).child(horaSeleccionada).setValue(cita);
                                    mDatabaseReference.child("Paciente").child(documento).setValue(paciente);
                                    complete[0] = false;
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                                    alerta.setMessage("Inscripción exitosa cita fecha: " + fechaSeleccionada + "hora: " + horaSeleccionada).setPositiveButton("Continuar", null).create().show();
                                    sendMail();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if (complete[0]){
                        mDatabaseReference.child("Disponibilidad").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                                child(fechaSeleccionada.substring(8, 10)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    documentoPers = snap.getKey();
                                    paciente.setCitaVacuna(true);
                                    mDatabaseReference.child("Cita").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                                            child(fechaSeleccionada.substring(8, 10)).child(documentoPers).child(horaSeleccionada).setValue(cita);
                                    mDatabaseReference.child("Paciente").child(documento).setValue(paciente);
                                    mDatabaseReference.child("Disponibilidad").child(fechaSeleccionada.substring(0, 4)).child(fechaSeleccionada.substring(5, 7)).
                                            child(fechaSeleccionada.substring(8, 10)).child(documentoPers).removeValue();
                                    complete[0] = false;
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                                    alerta.setMessage("Inscripción exitosa cita fecha: " + fechaSeleccionada + "hora: " + horaSeleccionada).setPositiveButton("Continuar", null).create().show();
                                    sendMail();
                                    break;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if (complete[0]){
                            AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                            alerta.setMessage("No hay citas diponibles para la hora y fecha").setPositiveButton("Continuar", null).create().show();
                        }
                    }
                } else {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                    alerta.setMessage("Etapa de vacunación no habilitada por EPS").setNegativeButton("Continuar", null).create().show();
                }
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaPaciente.this);
                alerta.setMessage("Ya tiene una cita inscrita").setNegativeButton("Continuar", null).create().show();
            }
        }
    }

    private void sendMail() {
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, paciente.getCorreo(), "Asignación de cita AppVacov",
                "Se asigno su cita para la fecha " + txtFechaInscripcion.getText() + " En la hora " + txtHoraInscripcion.getText());
        javaMailAPI.execute();
    }
}