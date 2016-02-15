package org.example.geretacoloc;

import com.google.gson.annotations.Expose;

/**
 * Created by Oeliniaina on 31/03/2015.
 */
public class Charge {
    @Expose private String nom_charge;
    @Expose private float montant_charge;

    public Charge(String nom, float montant){
        this.nom_charge =  nom;
        this.montant_charge = montant;
    }
    //accesseurs
    public String getNom_charge () {return nom_charge;}
    public float getMontant_charge () {return montant_charge;}

    //mutateurs
    public void setNom_charge(String nom) {this.nom_charge = nom;}
    public void setMontant_charge(float montant) {this.montant_charge = montant;}

    public String toString() {
        String enString = nom_charge + "\n" + montant_charge + " €";
        return enString;
    }

}