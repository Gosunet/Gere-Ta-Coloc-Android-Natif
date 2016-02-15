package org.example.geretacoloc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Gosunet on 10/05/2015.
 * SingetonColoc est une classe de mdèle Singleton, cad qu'il ne peut être instancier qu'une seule fois.
 * Une méthode get instance est fourni qui crée ou récupére l'instance de la Singleton
 */
public class SingletonColoc {

        // D'autres attributs, classiques et non "static".

        @Expose private ArrayList<Regle> regles = new ArrayList<>();
        @Expose private ArrayList<String> nomTaches = new ArrayList<>(); // Contient les nom des taches
        @Expose private ArrayList<String> dateTaches = new ArrayList<>(); // Contient les dates des taches
        @Expose private ArrayList<Boolean> urgenceTaches = new ArrayList<>();// Contient l'urgence des taches
        @Expose private ArrayList<Charge> charges = new ArrayList<>();
        @Expose private ArrayList<Note> notes = new ArrayList<>();
        @Expose private ArrayList<String> mails = new ArrayList<>();
        @Expose private ArrayList<String> mdps = new ArrayList<>();
        @Expose private String nomColoc;

        /**
         * Constructeur de l'objet.
         */
        private SingletonColoc() {
            // La présence d'un constructeur privé supprime le constructeur public par défaut.
            // De plus, seul le singleton peut s'instancier lui-même.
            super();
        }

        private static class SigngletonHolder
        {
            private final static SingletonColoc instance = new SingletonColoc();
        }


        /**
         * Méthode permettant de renvoyer une instance de la classe Singleton
         * @return Retourne l'instance du singleton.
         */
        public static SingletonColoc getInstance() {
            return SigngletonHolder.instance;
        }

        // D'autres méthodes classiques et non "static".


        public ArrayList<Regle> getRegles() {
        return regles;
    }

        public void setRegles(ArrayList<Regle> regles) {
        this.regles = regles;
    }


        public ArrayList<String> getDateTaches() {
        return dateTaches;
    }

        public void setDateTaches(ArrayList<String> dateTaches) {
        this.dateTaches = dateTaches;
    }

        public ArrayList<String> getNomTaches() {
        return nomTaches;
    }

        public void setNomTaches(ArrayList<String> nomTaches) {
        this.nomTaches = nomTaches;
    }

        public ArrayList<Boolean> getUrgenceTaches() {
        return urgenceTaches;
    }

        public void setUrgenceTaches(ArrayList<Boolean> urgenceTaches) {
            this.urgenceTaches = urgenceTaches;
        }

        public ArrayList<String> getMails() {
            return mails;
        }

        public ArrayList<String> getMdps() {
            return mdps;
        }

        public void setNomColoc(String nomColoc) {
                this.nomColoc = nomColoc;
            }

        public ArrayList<Charge> getCharges() {
                return charges;
            }

        public void setCharges(ArrayList<Charge> charges) {
                this.charges = charges;
            }

        public ArrayList<Note> getNotes() {
            return notes;
        }

        public void setNotes(ArrayList<Note> notes) {
            this.notes = notes;
        }


        /*
        Convert Java object to JSON format and returned as JSON foratted String
         */
        public String objectToJson(){

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            return  gson.toJson(this);
        }
        /*
        convert JSON format to java Object
        and returned as SingletonColoc
         */
        public SingletonColoc jsonToObject(String JsonContent){

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            return gson.fromJson(JsonContent,SingletonColoc.class);
        }
}

