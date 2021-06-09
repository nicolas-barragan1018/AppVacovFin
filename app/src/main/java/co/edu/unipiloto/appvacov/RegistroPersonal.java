package co.edu.unipiloto.appvacov;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.EventListener;

public class RegistroPersonal extends AppCompatActivity implements View.OnClickListener {
    EditText txtFN;
    private int mYear, mMonth, mDay;
    private int checkGenero = 3;
    private String genero = "";
    private FirebaseAuth mAuth;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_personal);
        getSupportActionBar().hide();

        EditText txtnombre = (EditText) findViewById(R.id.nombrePERS);
        EditText txtcorreo = (EditText) findViewById(R.id.correoPERS);
        EditText txtclave = (EditText) findViewById(R.id.contrasenaPERS);
        EditText txtnumero = (EditText) findViewById(R.id.telefonoPERS);
        EditText txtdocumento = (EditText) findViewById(R.id.documentoPERS);
        Spinner spnocupacion = (Spinner) findViewById(R.id.spnocupacionPERS);
        Spinner spneps = (Spinner) findViewById(R.id.spnEpsPERS);

        txtFN = (EditText) findViewById(R.id.etFechaNacimientoPERS);
        txtFN.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        TextView login = (TextView) findViewById(R.id.txtsigninPERS);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(RegistroPersonal.this, login.class);
                RegistroPersonal.this.startActivity(login);
                RegistroPersonal.this.finish();
            }
        });

        Button btnRegistro = (Button) findViewById(R.id.btn_registerPERS);
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

                if (nombre.equals("") || correo.equals("") || clave.equals("") || numero.equals("") || documento.equals("") || fechaNacimiento.equals("") || ocupacion == "Seleccione" || eps == "Seleccione") {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPersonal.this);
                    alerta.setMessage("Debe llenar todos los campos").setNegativeButton("Reintentar", null).create().show();
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    myRef.child("Personal").child(documento).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.getResult().exists()) {
                                mAuth.createUserWithEmailAndPassword(correo, clave)
                                        .addOnCompleteListener(RegistroPersonal.this, new OnCompleteListener<AuthResult>() {

                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    txtcorreo.setText("");
                                                    txtnombre.setText("");
                                                    txtclave.setText("");
                                                    txtnumero.setText("");
                                                    txtdocumento.setText("");
                                                    txtFN.setText("");
                                                    Personal personal = new Personal(nombre, fechaNacimiento, genero, correo, numero, ocupacion, documento, eps);
                                                    myRef.child("Personal").child(documento).setValue(personal);
                                                    Intent inicio = new Intent(RegistroPersonal.this, login.class);
                                                    RegistroPersonal.this.startActivity(inicio);
                                                    RegistroPersonal.this.finish();
                                                } else {
                                                    AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPersonal.this);
                                                    alerta.setMessage("Inscripción fallida").setNegativeButton("Reintentar", null).create().show();
                                                }
                                            }
                                        });
                            } else {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPersonal.this);
                                alerta.setMessage("Inscripción fallida").setNegativeButton("Reintentar", null).create().show();
                            }
                        }
                    });

                }
            }
        });
        getSupportActionBar().hide();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rbtgeneroHPERS:
                if (checked) {
                    checkGenero = 1;
                    genero = "M";
                }
                break;
            case R.id.rbtgeneroMPERS:
                if (checked) {
                    checkGenero = 0;
                    genero = "F";
                }
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
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
                            LocalDate fechaNacimiento = LocalDate.of(year,(monthOfYear+1),dayOfMonth);
                            Period periodo = Period.between(fechaNacimiento,fechaHoy);
                            if (periodo.getYears() < 16){
                                AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroPersonal.this);
                                alerta.setMessage("Tiene que ser mayor de 16 años.").setNegativeButton("Reintentar", null).create().show();
                                txtFN.setText("");
                            }else{
                                txtFN.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        }
    }
}
