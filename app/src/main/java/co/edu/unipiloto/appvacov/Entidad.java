package co.edu.unipiloto.appvacov;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Entidad implements Serializable {
    public String NIT;
    public String razonSocial;
    public String nombreAbreviado;
    public String documentoRepresentante;
    public String correo;
    public String telefono;
    public String direccion;

    public Entidad(String NIT, String razonSocial, String nombreAbreviado, String documentoRepresentante, String correo, String telefono, String direccion) {
        this.NIT = NIT;
        this.razonSocial = razonSocial;
        this.nombreAbreviado = nombreAbreviado;
        this.documentoRepresentante = documentoRepresentante;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    @Exclude
    public String getNIT() {
        return NIT;
    }

    @Exclude
    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    @Exclude
    public String getRazonSocial() {
        return razonSocial;
    }

    @Exclude
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Exclude
    public String getNombreAbreviado() {
        return nombreAbreviado;
    }

    @Exclude
    public void setNombreAbreviado(String nombreAbreviado) {
        this.nombreAbreviado = nombreAbreviado;
    }

    @Exclude
    public String getDocumentoRepresentante() {
        return documentoRepresentante;
    }

    @Exclude
    public void setDocumentoRepresentante(String documentoRepresentante) {
        this.documentoRepresentante = documentoRepresentante;
    }

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
    public String getDireccion() {
        return direccion;
    }

    @Exclude
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}

