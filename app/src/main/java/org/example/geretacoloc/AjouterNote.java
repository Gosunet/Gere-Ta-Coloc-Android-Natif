package org.example.geretacoloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Thibault on 15/05/2015.
 * Activity pour ajouter une note
 */
public class AjouterNote extends ActionBarActivity {

    Button saveNote;
    EditText titreNote;
    EditText texteNote;
    String titre;
    String texte;
    public final static int CREATION_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouternote);

        saveNote = (Button) findViewById(R.id.saveNote);
        titreNote = (EditText) findViewById(R.id.titreNote);
        texteNote = (EditText) findViewById(R.id.texteNote);

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titre = titreNote.getText().toString();
                texte = texteNote.getText().toString();
                Intent result = new Intent();
                result.putExtra(BlocNotes.TITRE, titre);
                result.putExtra(BlocNotes.TEXTE, texte);

                setResult(CREATION_OK, result);
                finish();
            }
        });
    }

}
