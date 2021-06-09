package co.edu.unipiloto.appvacov;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Paciente implements Serializable {

    public String nombres;
    public String fechaNacimiento;
    public String documento;
    public String genero;
    public String correo;
    public String telefono;
    public String ocupacion;
    public String eps;
    public String enfermedad;
    public boolean citaVacuna;
    public ArrayList<Sintomas> sintomas;
    public String direccion;
    public String latitud;
    public String longitud;

    public Paciente(String nombres, String fechaNacimiento, String genero, String correo, String telefono, String ocupacion, String documento, String eps, String enfermedad, boolean citaVacuna, String dirrecion, String latitud, String longitud) {
        this.nombres = nombres;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.correo = correo;
        this.telefono = telefono;
        this.ocupacion = ocupacion;
        this.documento = documento;
        this.eps = eps;
        this.enfermedad = enfermedad;
        this.citaVacuna = citaVacuna;
        this.latitud = latitud;
        this.longitud = longitud;
        this.sintomas = new ArrayList<Sintomas>();
        sintomas.add(new Sintomas("Fiebre", false));
        sintomas.add(new Sintomas("Dolor Garganta", false));
        sintomas.add(new Sintomas("Congestion Nasal", false));
        sintomas.add(new Sintomas("Tos", false));
        sintomas.add(new Sintomas("Dificultad Respirar", false));
        sintomas.add(new Sintomas("Fatiga", false));
        sintomas.add(new Sintomas("Escalofrio", false));
        sintomas.add(new Sintomas("Dolor Musculos", false));
        sintomas.add(new Sintomas("Cabeza", false));
        sintomas.add(new Sintomas("Ninguno", false));
    }

    public Paciente (){

    }

    @Exclude
    public void setSintomas(ArrayList<Sintomas> sintomas) { this.sintomas = sintomas; }

    @Exclude
    public String getDireccion() { return direccion; }

    @Exclude
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Exclude
    public String getLatitud() { return latitud; }

    @Exclude
    public void setLatitud(String latitud) { this.latitud = latitud; }

    @Exclude
    public String getLongitud() { return longitud; }

    @Exclude
    public void setLongitud(String longitud) { this.longitud = longitud; }

    @Exclude
    public ArrayList<Sintomas> getSintomas(){
        return sintomas;
    }

    @Exclude
    public void setSintomasArray(ArrayList<Sintomas> sintomas){this.sintomas = sintomas; }

    @Exclude
    public void setSintomas(Sintomas sintomas) {
        switch (sintomas.getNombre()){
            case "Fiebre":
                this.sintomas.set(0,sintomas);
            break;
            case "Dolor Garganta":
                this.sintomas.set(1,sintomas);
                break;
            case "Congestion Nasal":
                this.sintomas.set(2,sintomas);
                break;
            case "Tos":
                this.sintomas.set(3,sintomas);
                break;
            case "Dificultad Respirar":
                this.sintomas.set(4,sintomas);
                break;
            case "Fatiga":
                this.sintomas.set(5,sintomas);
                break;
            case "Escalofrio":
                this.sintomas.set(6,sintomas);
                break;
            case "Dolor Musculos":
                this.sintomas.set(7,sintomas);
                break;
            case "Cabeza":
                this.sintomas.set(8,sintomas);
                break;
            case "Ninguno":
                this.sintomas.set(9,sintomas);
                break;
        }
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

    @Exclude
    public String getEnfermedad(){return enfermedad;}

    @Exclude
    public void setEnfermedad (String enfermedad){this.enfermedad = enfermedad;}

    @Exclude
    public boolean isCitaVacuna() { return citaVacuna; }

    @Exclude
    public void setCitaVacuna(boolean citaVacuna) { this.citaVacuna = citaVacuna; }
}