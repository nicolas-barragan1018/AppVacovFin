package co.edu.unipiloto.appvacov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TipoUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_usuario);
        Button btnPaciente = (Button) findViewById(R.id.btn_rp);
        btnPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registroPaciente = new Intent(TipoUsuario.this, RegistroPaciente.class);
                TipoUsuario.this.startActivity(registroPaciente);
                TipoUsuario.this.finish();
            }
        });
        Button btnPersonal = (Button) findViewById(R.id.btn_rps);
        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registroPersonal = new Intent(TipoUsuario.this, RegistroPersonal.class);
                TipoUsuario.this.startActivity(registroPersonal);
                TipoUsuario.this.finish();
            }
        });
        Button btnEPS = (Button) findViewById(R.id.btn_eps);
        btnEPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registroEPS = new Intent(TipoUsuario.this, RegistroEPS.class);
                TipoUsuario.this.startActivity(registroEPS);
                TipoUsuario.this.finish();
            }
        });
    }
}