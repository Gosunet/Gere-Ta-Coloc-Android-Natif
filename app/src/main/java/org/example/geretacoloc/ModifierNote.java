package org.example.geretacoloc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ModifierNote extends ActionBarActivity {

    Button modifyNote;
    EditText titreNote;
    EditText texteNote;
    public final static int MODIF_OK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_note);

        modifyNote = (Button) findViewById(R.id.modifyNote);
        titreNote = (EditText) findViewById(R.id.titreModNote);
        texteNote = (EditText) findViewById(R.id.texteModNote);

        //Récupérer la note

        Intent i = getIntent();

        final String titreav = i.getStringExtra(BlocNotes.TITRE);
        final String texteav = i.getStringExtra(BlocNotes.TEXTE);
        final int position = i.getIntExtra(BlocNotes.POSITION, -1);

        //Mettre à jour les editText

        titreNote.setText(titreav, TextView.BufferType.EDITABLE);
        texteNote.setText(texteav,  TextView.BufferType.EDITABLE);

        modifyNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent result = new Intent();
                result.putExtra(BlocNotes.TITRE, titreNote.getText().toString());
                result.putExtra(BlocNotes.TEXTE, texteNote.getText().toString());
                result.putExtra(BlocNotes.POSITION,position);

                setResult(MODIF_OK, result);
                finish();
            }
        });
    }
}
