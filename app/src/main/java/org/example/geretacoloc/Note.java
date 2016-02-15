package org.example.geretacoloc;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Gosunet on 31/03/2015.
 *
 */
public class Note extends Module{

    @Expose private String titre;
    @Expose private String texte;

    public Note(String titre, String texte) {
        this.titre = titre;
        this.texte =texte;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String toString() {return titre + "\n" + texte;}
}
