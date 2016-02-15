package org.example.geretacoloc;

import android.support.v7.app.ActionBarActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Gosunet on 09/04/2015.
 *
 */
abstract class Module extends ActionBarActivity{

    public Module(){


    }


    public File writeJsonInFile(String jsonContent, String colocName) {
        BufferedWriter writer = null;
        File dir;
        dir = getDir("Test", MODE_PRIVATE);
        try {
            if (!dir.exists()) {
                dir.mkdir(); // On crée le répertoire (s'il n'existe
                // pas!!)
            }

            File newfile = new File(dir.getAbsolutePath() + File.separator
                    +colocName +".txt");
// Création du fichier
            newfile.createNewFile();
// Intégration du contenu dans un BufferedWriter
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(newfile)));
            writer.write(jsonContent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new File(dir.getAbsolutePath()+File.separator+colocName+".txt");
    }

    public static String lireFichierI(File dir, String nomFichier) {
        // Déclaration de l’objet fichier new file
        File newfile = new File(dir.getAbsolutePath() + File.separator
                + nomFichier);
        String monText = "";
        BufferedReader input = null;
        try {
            // Récupération du contenu du fichier dans un BufferdReader
            input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(newfile)
            ));
            String line;
            StringBuffer buffer = new StringBuffer();
            // Parcours du bufferReader et intégration dans un String
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            monText = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return monText;

    }



}
