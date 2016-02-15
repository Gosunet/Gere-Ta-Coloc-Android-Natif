package org.example.geretacoloc;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    // Initialisation des boutons

    ImageButton btnAgenda;
    ImageButton btnBlocNotes;
    ImageButton btnRegleDeVie;
    ImageButton btnLoyer;
    static String nomColocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Quand l'écran est créée liée les boutons

        btnAgenda = (ImageButton) findViewById(R.id.btnAgenda);
        btnBlocNotes = (ImageButton) findViewById(R.id.btnBlocNote);
        btnRegleDeVie = (ImageButton) findViewById(R.id.btnRegleDeVie);
        btnLoyer = (ImageButton) findViewById(R.id.btnLoyer);

        SingletonColoc singletonColoc = SingletonColoc.getInstance();
        Intent intent = getIntent();
        nomColocation = intent.getStringExtra("nomColoc");
        File dir;
        dir=getDir("Test",MODE_PRIVATE);
        FtpDownloadAsync ftpDownloadAsync = new FtpDownloadAsync();
        ftpDownloadAsync.execute(nomColocation,dir.getAbsolutePath());
        Log.d("texte4", lireFichierI(dir, nomColocation + ".txt"));
        singletonColoc = singletonColoc.jsonToObject(lireFichierI(dir,nomColocation+".txt"));
        Log.d("singleton",singletonColoc.objectToJson());

        //attente d'un event

        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Appeler l'écran charge sur clique du btnAgenda.

                Intent intent = new Intent(MainActivity.this,Agenda.class);
                startActivity(intent);
            }
        });

        btnBlocNotes.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Appeler l'écran charge sur clique du btnBlocNotes.

                Intent intent = new Intent(MainActivity.this,BlocNotes.class);
                startActivity(intent);
            }
        });

        btnRegleDeVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Appeler l'écran charge sur clique du btnRegleDeVie.

                Intent intent = new Intent(MainActivity.this,RegleDeVie.class);
                startActivity(intent);
            }
        });

        btnLoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Appeler l'écran charge sur clique du btnLoyer.


                Intent intent = new Intent(MainActivity.this,ChargeTotale.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     *
     * @param dir
     * @param nomFichier
     * @return le contenu d'un fichier .txt
     */
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
