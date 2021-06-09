package co.edu.unipiloto.appvacov;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class popup_vacuna extends AppCompatActivity implements Serializable {

    private TextView nombrepopup;
    private TextView cedulapopup;
    private Spinner spnpopup;
    private Cita cita;
    private String personal;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_cita);

        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        mDatabase = FirebaseDatabase.getInstance();
        mDataReference = mDatabase.getReference();

        int ancho = medidasVentana.widthPixels;
        int largo = medidasVentana.heightPixels;

        getWindow().setLayout((int) (ancho * 0.70), (int) (largo * 0.45));

        nombrepopup = (TextView) findViewById(R.id.nombrePacientePOPUP);
        cedulapopup = (TextView) findViewById(R.id.cedulaPacientePOPUP);
        spnpopup = (Spinner) findViewById(R.id.spnPOPUP);
        cita = (Cita) getIntent().getSerializableExtra("cita");
        nombrepopup.setText(cita.getNombrePaciente());
        cedulapopup.setText(cita.getDocumentoPaciente());
        personal = getIntent().getExtras().getString("personal");

        ImageButton btnmapa = (ImageButton) findViewById(R.id.buttonMaps);
        btnmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarMapa(v);
            }
        });

        Button btnafirmativo = (Button) findViewById(R.id.buttonVerificado);
        btnafirmativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarVacuna();
            }
        });

        Button btnnegativo = (Button) findViewById(R.id.buttonnoVerificado);
        btnnegativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_vacuna.this.finish();
            }
        });

        getSupportActionBar().hide();
    }

    private void confirmarVacuna() {
        String vacunaAplicada = String.valueOf(spnpopup.getSelectedItem());
        if (!vacunaAplicada.equals("Seleccione")) {
            cita.setNombreVacuna(vacunaAplicada);
            cita.setRealizada(true);
            mDataReference.child("Cita").child(cita.getFecha().substring(0, 4)).child(cita.getFecha().substring(5, 7)).
                    child(cita.getFecha().substring(8, 10)).child(personal).child(cita.getHora()).setValue(cita);
            mDataReference.child("Paciente").child(cita.getDocumentoPaciente()).child("citaVacuna").setValue(false);
            mDataReference.child("Vacuna").child(cita.getEps()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Long numero = task.getResult().child("numeroVacunas").getValue(Long.class);
                    mDataReference.child("Vacuna").child(cita.getEps()).child("numeroVacunas").setValue(numero - 1);
                }
            });
            AlertDialog.Builder alerta = new AlertDialog.Builder(popup_vacuna.this);
            alerta.setMessage("Proceso vacuna aplicada exitoso").setPositiveButton("Continuar", null).create().show();
            popup_vacuna.this.finish();
        } else {
            AlertDialog.Builder alerta = new AlertDialog.Builder(popup_vacuna.this);
            alerta.setMessage("Seleccione una vacuna").setNegativeButton("Reintentar", null).create().show();
        }
    }

    private void activarMapa(View view) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", cita.getLatitud() + "," + cita.getLongitud());
        String uri = builder.build().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}
