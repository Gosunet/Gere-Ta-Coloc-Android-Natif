package org.example.geretacoloc;

import com.google.gson.annotations.Expose;

/**
 * Created by Gosunet on 31/03/2015.
 *
 */
public class Regle {
    @Expose private int numero;
    @Expose private String regle;


    public Regle(int numero, String regle) {
        this.numero = numero;
        this.regle = regle;
    }

    public int getNumero() {
        return numero;
    }


    public String getRegle() {
        return regle;
    }

    public void setRegle(String regle) {
        this.regle = regle;
    }

    public String toString(){
        return " n° " + this.numero + " " + this.regle;
    }
}
