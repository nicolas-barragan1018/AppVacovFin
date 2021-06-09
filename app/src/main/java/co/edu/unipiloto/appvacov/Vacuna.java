package co.edu.unipiloto.appvacov;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Vacuna implements Serializable {

    public long numeroVacunas;
    public String etapa;

    public Vacuna(long numeroVacunas, String etapa) {
        this.numeroVacunas = numeroVacunas;
        this.etapa = etapa;
    }

    @Exclude
    public long getNumeroVacunas() { return numeroVacunas; }

    @Exclude
    public void setNumeroVacunas(long numeroVacunas) { this.numeroVacunas = numeroVacunas; }

    @Exclude
    public String getEtapa() { return etapa; }

    @Exclude
    public void setEtapa(String etapa) { this.etapa = etapa; }
}
