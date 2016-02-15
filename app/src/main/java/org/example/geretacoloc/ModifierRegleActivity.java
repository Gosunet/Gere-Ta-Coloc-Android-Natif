package org.example.geretacoloc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ModifierRegleActivity extends ActionBarActivity {

    EditText regleModifiable;
    Button validerModifierRegle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_regle);
        regleModifiable = (EditText) findViewById(R.id.Regle);
        validerModifierRegle = (Button) findViewById(R.id.btnValiderModificationRegle);


        Intent i = getIntent();

        //Recupére la regle envoyer depuis regledevie
        final String laRegle = i.getStringExtra("org.example.geretacoloc.regleDeVie");
        Log.d("laregle", laRegle);
        regleModifiable.setText(laRegle);


        validerModifierRegle.setOnClickListener(new View.OnClickListener() { // Sur clique du bouton on retourne aux regle de vie et on envoie la valeur modifier
            @Override
            public void onClick(View v) {
                final String laRegle = regleModifiable.getText().toString();
                Log.d("messageFin", laRegle);
                Intent result = new Intent();
                result.putExtra("org.example.geretacoloc.ModifierRegleDeVie",laRegle);
                setResult(RESULT_OK, result);
                finish();
            }
        });

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
