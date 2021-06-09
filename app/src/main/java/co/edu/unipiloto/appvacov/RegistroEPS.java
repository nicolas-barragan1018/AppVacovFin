package co.edu.unipiloto.appvacov;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroEPS extends AppCompatActivity{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_eps);
        getSupportActionBar().hide();

        EditText txtnit = (EditText) findViewById(R.id.nitEPS);
        EditText txtrazonSocial = (EditText) findViewById(R.id.razonsocialEPS);
        EditText txtnombreAbre = (EditText) findViewById(R.id.nombreabreviadoEPS);
        EditText txtdoc = (EditText) findViewById(R.id.docRepresentanteLEPS);
        EditText txtcorreo = (EditText) findViewById(R.id.correoEPS);
        EditText txtcontrasena = (EditText) findViewById(R.id.contrasenaEPS);
        EditText txttelefono = (EditText) findViewById(R.id.telefonoEPS);
        EditText txtdireccion = (EditText) findViewById(R.id.direccionEPS);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        TextView login = (TextView) findViewById(R.id.txtsigninEPS);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(RegistroEPS.this, login.class);
                RegistroEPS.this.startActivity(login);
                RegistroEPS.this.finish();
            }
        });

        Button btnRegistro = (Button) findViewById(R.id.btn_registerEPS);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String razonSoc = txtrazonSocial.getText().toString();
                String correo = txtcorreo.getText().toString();
                String clave = txtcontrasena.getText().toString();
                String documento = txtdoc.getText().toString();
                String nombreAbr = txtnombreAbre.getText().toString();
                String nit = txtnit.getText().toString();
                String telefono = txttelefono.getText().toString();
                String direccion = txtdireccion.getText().toString();

                if (razonSoc.equals("") || correo.equals("") || clave.equals("") || direccion.equals("") || documento.equals("") || nombreAbr.equals("") || nit.equals("") || telefono.equals("")) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroEPS.this);
                    alerta.setMessage("Debe llenar todos los campos").setNegativeButton("Reintentar", null).create().show();
                } else {
                    myRef.child("Entidad").child(documento).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.getResult().exists()){
                                mAuth.createUserWithEmailAndPassword(correo, clave)
                                        .addOnCompleteListener(RegistroEPS.this, new OnCompleteListener<AuthResult>() {

                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    txtrazonSocial.setText("");
                                                    txtcorreo.setText("");
                                                    txtcontrasena.setText("");
                                                    txtdoc.setText("");
                                                    txtnombreAbre.setText("");
                                                    txtnit.setText("");
                                                    txttelefono.setText("");
                                                    txtdireccion.setText("");
                                                    Entidad entidad = new Entidad(nit, razonSoc, nombreAbr, documento, correo, telefono, direccion);
                                                    myRef.child("Entidad").child(documento).setValue(entidad);
                                                    Intent inicio = new Intent(RegistroEPS.this, login.class);
                                                    RegistroEPS.this.startActivity(inicio);
                                                    RegistroEPS.this.finish();
                                                } else {
                                                    AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroEPS.this);
                                                    alerta.setMessage("Inscripción fallida").setNegativeButton("Reintentar", null).create().show();
                                                }
                                            }
                                        });
                            }else{
                                AlertDialog.Builder alerta = new AlertDialog.Builder(RegistroEPS.this);
                                alerta.setMessage("Inscripción fallida").setNegativeButton("Reintentar", null).create().show();
                            }
                        }
                    });

                }
            }
        });
    }
}

