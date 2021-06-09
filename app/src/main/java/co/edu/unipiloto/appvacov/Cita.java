package co.edu.unipiloto.appvacov;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Cita implements Serializable {

    public String documentoPaciente;
    public String eps;
    public String nombrePaciente;
    public boolean realizada;
    public String hora;
    public String nombreVacuna;
    public String fecha;
    public String latitud;
    public String longitud;

    public Cita(String documentoPaciente, String eps, String nombrePaciente, boolean realizada, String hora, String nombreVacuna, String fecha, String latitud, String longitud) {
        this.documentoPaciente = documentoPaciente;
        this.eps = eps;
        this.nombrePaciente = nombrePaciente;
        this.realizada = realizada;
        this.hora = hora;
        this.nombrePaciente = nombrePaciente;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Exclude
    public String getLatitud() { return latitud; }

    @Exclude
    public void setLatitud(String latitud) { this.latitud = latitud; }

    @Exclude
    public String getLongitud() { return longitud; }

    @Exclude
    public void setLongitud(String longitud) { this.longitud = longitud; }

    @Exclude
    public String getFecha() { return fecha; }

    @Exclude
    public void setFecha(String fecha) { this.fecha = fecha; }

    @Exclude
    public String getDocumentoPaciente() { return documentoPaciente; }

    @Exclude
    public void setDocumentoPaciente(String documentoPaciente) { this.documentoPaciente = documentoPaciente; }

    @Exclude
    public String getEps() { return eps; }

    @Exclude
    public void setEps(String eps) { this.eps = eps; }

    @Exclude
    public String getNombrePaciente() { return nombrePaciente; }

    @Exclude
    public boolean isRealizada() { return realizada; }

    @Exclude
    public void setRealizada(boolean realizada) { this.realizada = realizada; }

    @Exclude
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    @Exclude
    public String getHora() { return hora; }

    @Exclude
    public void setHora(String hora) { this.hora = hora; }

    @Exclude
    public String getNombreVacuna() { return nombreVacuna; }

    @Exclude
    public void setNombreVacuna(String nombreVacuna) { this.nombreVacuna = nombreVacuna; }
}
