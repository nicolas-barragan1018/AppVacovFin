package co.edu.unipiloto.appvacov;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Sintomas implements Serializable {
    public String nombre;
    public boolean variable;

    public Sintomas(String nombre, boolean variable){
        this.nombre = nombre;
        this.variable = variable;
    }

    public Sintomas(){

    }

    @Exclude
    public String getNombre() { return nombre; }

    @Exclude
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Exclude
    public boolean getVariable() { return variable;}

    @Exclude
    public void setVariable(boolean variable) { this.variable = variable; }
}