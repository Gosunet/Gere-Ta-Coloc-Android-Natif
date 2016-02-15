package org.example.geretacoloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
Activity pour ajouter une nouvelle tache à l'agenda
 */
public class Ajout_Tache extends ActionBarActivity {

    // -------------- GESTION DE L'ACTION DES BOUTONS ------------------------
    Button btnAnnuler;
    Button btnValid;
    TextView name;
    CheckBox uIG;
    String n;
    Boolean u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout__tache);


        // ~~~~~~~~~~~POUR ANNULER L'AJOUT D'UNE TACHE ~~~~~~~~~~
        btnAnnuler = (Button) findViewById(R.id.btnAnnulerAT);
        name = (EditText) findViewById(R.id.EditNomTacheAT);
        uIG = (CheckBox) findViewById(R.id.urgentOUpas);

        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Ajout_Tache.this, "Tâche annulée...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        // ~~~~~~~~~~~POUR AJOUTER UNE TACHE ~~~~~~~~~~
        btnValid = (Button) findViewById(R.id.btnValiderAT);
        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                n = name.getText().toString();
                u = uIG.isChecked();
                Intent result = new Intent();
                result.putExtra(Agenda.NAME, n);
                result.putExtra(Agenda.URGENCE, u);

                setResult(RESULT_OK, result);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajout__tache, menu);
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
}
