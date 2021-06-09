package co.edu.unipiloto.appvacov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PantallaEntidad extends AppCompatActivity {

    private String documento;
    private Entidad entidad;
    private Vacuna vacuna;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText txtvacunasRecibidas, txtvacunasDisponibles;
    private Spinner etapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_entidad);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        documento = getIntent().getExtras().getString("documento");

        vacuna = new Vacuna(0,"");
        entidad = new Entidad("","","","","","","");

        mDatabaseReference.child("Entidad").child(documento).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entidad.setNIT(snapshot.child("nit").getValue(String.class));
                entidad.setRazonSocial(snapshot.child("razonSocial").getValue(String.class));
                entidad.setNombreAbreviado(snapshot.child("nombreAbreviado").getValue(String.class));
                entidad.setDocumentoRepresentante(snapshot.child("documentoRepresentante").getValue(String.class));
                entidad.setCorreo(snapshot.child("correo").getValue(String.class));
                entidad.setCorreo(snapshot.child("telefono").getValue(String.class));
                entidad.setDireccion(snapshot.child("direccion").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        txtvacunasRecibidas = (EditText) findViewById(R.id.txtvacunasRecibidas);
        txtvacunasDisponibles = (EditText) findViewById(R.id.txtvacunasDisponibles);
        etapa = (Spinner) findViewById(R.id.spnEtapa);

        Button btnActualizar = (Button) findViewById(R.id.btn_actualizarVacunas);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { actualizarVacunas(); }
        });

        TextView txtCerrarSesion = (TextView) findViewById(R.id.txtcerrarSesionENT);
        txtCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { cerrarSesion(); }
        });
    }

    private void actualizarVacunas(){
        String etapaV = "";
        switch (etapa.getSelectedItemPosition()){
            case 0:
                etapaV = "Seleccione";
            break;
            case 1:
                etapaV = "1";
            break;
            case 2:
                etapaV = "2";
            break;
            case 3:
                etapaV = "3";
            break;
            case 4:
                etapaV = "4";
            break;
            case 5:
                etapaV = "5";
            break;
            default:
                etapaV = "Seleccione";
            break;
        }

        Long numerodeVacunas = txtvacunasRecibidas.getText().toString().equals("")?0:Long.parseLong(txtvacunasRecibidas.getText().toString());
        String finalEtapaV = etapaV;
        mDatabaseReference.child("Vacuna").child(entidad.getNombreAbreviado()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()){
                    vacuna.setNumeroVacunas(task.getResult().child("numeroVacunas").getValue(Long.class));
                    vacuna.setEtapa(task.getResult().child("etapa").getValue(String.class));
                    if (finalEtapaV.equals("Seleccione") && numerodeVacunas>0){
                        vacuna.setNumeroVacunas((vacuna.getNumeroVacunas()+numerodeVacunas));
                        txtvacunasRecibidas.setText("");
                        txtvacunasDisponibles.setText(vacuna.getNumeroVacunas()+"");
                        mDatabaseReference.child("Vacuna").child(entidad.getNombreAbreviado()).setValue(vacuna);
                    }else if((!finalEtapaV.equals("Seleccione")) && numerodeVacunas==0){
                        vacuna.setEtapa(finalEtapaV);
                        txtvacunasDisponibles.setText(vacuna.getNumeroVacunas()+"");
                        mDatabaseReference.child("Vacuna").child(entidad.getNombreAbreviado()).setValue(vacuna);
                    }else if ((!finalEtapaV.equals("Seleccione")) && numerodeVacunas>0){
                        vacuna.setEtapa(finalEtapaV);
                        vacuna.setNumeroVacunas(vacuna.getNumeroVacunas()+numerodeVacunas);
                        txtvacunasRecibidas.setText("");
                        txtvacunasDisponibles.setText(vacuna.getNumeroVacunas()+"");
                        mDatabaseReference.child("Vacuna").child(entidad.getNombreAbreviado()).setValue(vacuna);
                    }else{
                        txtvacunasDisponibles.setText(vacuna.getNumeroVacunas()+"");
                    }
                }else{
                    if (finalEtapaV.equals("Seleccione") || numerodeVacunas == 0){
                        AlertDialog.Builder alerta = new AlertDialog.Builder(PantallaEntidad.this);
                        alerta.setMessage("Digite número de vacuna y etapa").setNegativeButton("Reintentar", null).create().show();
                    }else{
                        vacuna.setEtapa(finalEtapaV);
                        vacuna.setNumeroVacunas(numerodeVacunas);
                        txtvacunasRecibidas.setText("");
                        txtvacunasDisponibles.setText(vacuna.getNumeroVacunas()+"");
                        mDatabaseReference.child("Vacuna").child(entidad.getNombreAbreviado()).setValue(vacuna);
                    }
                }
            }
        });
    }

    private void cerrarSesion (){
        mAuth.signOut();
        Intent inicio = new Intent(PantallaEntidad.this, login.class);
        PantallaEntidad.this.startActivity(inicio);
        PantallaEntidad.this.finish();
    }
}