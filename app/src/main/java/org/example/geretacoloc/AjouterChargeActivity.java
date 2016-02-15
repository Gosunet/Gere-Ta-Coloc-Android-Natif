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

/*
Activity pour ajouter une charge
 */
public class AjouterChargeActivity extends ActionBarActivity {

    EditText nomDeLaCharge;
    EditText montantDeLaCharge;
    Button valider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_charge);

        nomDeLaCharge = (EditText) findViewById(R.id.nomCharge);
        montantDeLaCharge = (EditText) findViewById(R.id.montantCharge);
        valider = (Button) findViewById(R.id.btnValider);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String le_nom_de_la_charge = nomDeLaCharge.getText().toString();
                final String le_montant_de_la_charge= montantDeLaCharge.getText().toString();

                if(le_montant_de_la_charge.trim().length()==0 | le_nom_de_la_charge.trim().length()==0){
                    Toast.makeText(AjouterChargeActivity.this,"Tu as laissé une case vide! ;)", Toast.LENGTH_LONG).show();
                }//si la charge n'a pas de montant alors on retourne sur la meme page en affichant le texte
                else {
                    Intent result = new Intent();
                    result.putExtra(ChargeTotale.NOM, le_nom_de_la_charge);     //on met dans nom ce que l'on vient de récuperer dans le editText
                    result.putExtra(ChargeTotale.MONTANT, le_montant_de_la_charge); //on met dans montant ce que l'on vient de récuperer dans le editText
                    setResult(1, result);       //on associe à l'activité ajouterChargeActivity int = 1
                    finish();                   //on sort de la boucle et de l'intent
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