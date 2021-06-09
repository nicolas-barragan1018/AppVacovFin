package co.edu.unipiloto.appvacov;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Personal implements Serializable {

    public String nombres;
    public String fechaNacimiento;
    public String documento;
    public String genero;
    public String correo;
    public String telefono;
    public String ocupacion;
    public String eps;


    public Personal(String nombres, String fechaNacimiento, String genero, String correo, String telefono, String ocupacion, String documento, String eps) {
        this.nombres = nombres;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.telefono = telefono;
        this.ocupacion = ocupacion;
        this.documento = documento;
        this.eps = eps;
    }

    public Personal(){

    }

    @Exclude
    public String getNombres() {
        return nombres;
    }

    @Exclude
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @Exclude
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    @Exclude
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    @Exclude
    public String getCorreo() {
        return correo;
    }

    @Exclude
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Exclude
    public String getTelefono() {
        return telefono;
    }

    @Exclude
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Exclude
    public String getOcupacion() { return ocupacion; }

    @Exclude
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    @Exclude
    public String getGenero() {
        return genero;
    }

    @Exclude
    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Exclude
    public String getDocumento() { return documento; }

    @Exclude
    public void setDocumento(String documento) { this.documento = documento; }

    @Exclude
    public String getEps() { return eps; }

    @Exclude
    public void setEps(String eps) { this.eps = eps; }
}
