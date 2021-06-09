package co.edu.unipiloto.appvacov;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener; 

public class login extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    private int entra = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        EditText txtDoc = (EditText) findViewById(R.id.doc_login);
        EditText txtEmail = (EditText) findViewById(R.id.email_login);
        EditText txtContrasena = (EditText) findViewById(R.id.contrasena_login);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        TextView txtRegistro = (TextView) findViewById(R.id.txtsignup);
        TextView txtRecuperar = (TextView) findViewById(R.id.txtrecuperar);

        txtRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
            }
        });

        txtRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                if (!email.equals("")){
                    recuperarContrasena(email);
                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                    alerta.setMessage("Email Vacio").setNegativeButton("Reintentar", null).create().show();
                }

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doc = txtDoc.getText().toString();
                String email = txtEmail.getText().toString();
                String contrasena = txtContrasena.getText().toString();

                inicioSesion(doc,email,contrasena);
            }
        });
        getSupportActionBar().hide();
    }

    private void registro(){
        Intent tipoUsuario = new Intent(login.this, TipoUsuario.class);
        login.this.startActivity(tipoUsuario);
        login.this.finish();
    }

    private void inicioSesion(String doc, String email, String contrasena){
        mAuth.signInWithEmailAndPassword(email, contrasena)
                .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDatabaseReference.child("Paciente").child(doc).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                       entra = 1;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                                    alerta.setMessage("Intente de nuevo").setNegativeButton("Reintentar", null).create().show();
                                }
                            });
                            mDatabaseReference.child("Personal").child(doc).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        entra = 2;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                                    alerta.setMessage("Intente de nuevo.").setNegativeButton("Reintentar", null).create().show();
                                }
                            });
                            mDatabaseReference.child("Entidad").child(doc).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        entra = 3;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                                    alerta.setMessage("Intente de nuevo.").setNegativeButton("Reintentar", null).create().show();
                                }
                            });
                            switch (entra) {
                                case 1:
                                    Intent paciente = new Intent(login.this, PantallaPaciente.class);
                                    paciente.putExtra("documento", doc);
                                    login.this.startActivity(paciente);
                                    login.this.finish();
                                break;
                                case 2:
                                    Intent personal = new Intent(login.this, PantallaPersonal.class);
                                    personal.putExtra("documento", doc);
                                    login.this.startActivity(personal);
                                    login.this.finish();
                                break;
                                case 3:
                                    Intent entidad = new Intent(login.this, PantallaEntidad.class);
                                    entidad.putExtra("documento", doc);
                                    login.this.startActivity(entidad);
                                    login.this.finish();
                                break;
                                case 0:
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                                    alerta.setMessage("Fallo el inicio de sesión, intente de nuevo.").setNegativeButton("Reintentar", null).create().show();
                                break;
                            }
                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                            alerta.setMessage("Fallo el inicio de sesión, intente de nuevo.").setNegativeButton("Reintentar", null).create().show();
                        }
                    }
                });
    }

    private void recuperarContrasena(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                    alerta.setMessage("Se envio correctamente el correo para la recuperacion").setPositiveButton("Continuar", null).create().show();
                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(login.this);
                    alerta.setMessage("Error en la recuperación de la contraseña").setNegativeButton("Continuar", null).create().show();
                }
            }
        });
    }

}