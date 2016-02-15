package org.example.geretacoloc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ModifierChargeActivity extends ActionBarActivity {


    EditText nom_charge_modif;
    EditText montant_charge_modif;
    Button validerModif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_charge);

        nom_charge_modif = (EditText) findViewById(R.id.nomCharge_modif);
        montant_charge_modif = (EditText) findViewById(R.id.montantCharge_modif);
        validerModif = (Button) findViewById(R.id.btnValiderModif);

        //on récupere la charge qui était la précedemment
        Intent i = getIntent();
        final String nom_charge_a_modif = i.getStringExtra(ChargeTotale.NOM);
        final String montant_charge_a_modif = i.getStringExtra(ChargeTotale.MONTANT);

        nom_charge_modif.setText(nom_charge_a_modif);
        montant_charge_modif.setText(montant_charge_a_modif);


        validerModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nom_de_la_charge_modif = nom_charge_modif.getText().toString();
                final String montant_de_la_charge_modif= montant_charge_modif.getText().toString();

                if(montant_de_la_charge_modif.trim().length()==0 | nom_de_la_charge_modif.trim().length()==0){
                    Toast.makeText(ModifierChargeActivity.this, "Tu as laissé une case vide! ;)", Toast.LENGTH_LONG).show();
                }//si la charge n'a pas de montant alors on retourne sur la meme page en affichant le texte

                else {
                    Intent result = new Intent();
                    result.putExtra(ChargeTotale.NOM, nom_de_la_charge_modif);
                    result.putExtra(ChargeTotale.MONTANT, montant_de_la_charge_modif);
                    setResult(2, result);

                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modifier_charge, menu);
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