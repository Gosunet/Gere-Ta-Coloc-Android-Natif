package org.example.geretacoloc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class OuvrirLectureSeul extends ActionBarActivity {

    TextView titreNote;
    TextView texteNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouvrir_lecture_seul);

        titreNote = (TextView) findViewById(R.id.lectureTitre);
        texteNote = (TextView) findViewById(R.id.lectureTexte);

        //Récupérer les infos sur la note

        Intent i = getIntent();

        final String titreav = i.getStringExtra(BlocNotes.TITRE);
        final String texteav = i.getStringExtra(BlocNotes.TEXTE);

        titreNote.setText(titreav);
        texteNote.setText(texteav);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ouvrir_lecture_seul, menu);
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
