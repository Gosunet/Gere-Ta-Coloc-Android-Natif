package org.example.geretacoloc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

/*
Activity Module
 */
public class BlocNotes extends Module {

    //Attribut

    public ArrayList<Note> notes = new ArrayList<>();

    //Varible pour interface et communication entre intent

    int ETAT_SUPPRIMER = 0;

    Button btnAjouterNote;
    Button btnSupprimerNote;
    ListView listNotes;
    EditText chercherNotes;

    // Pour l'échange de données entre activités
    public final static int BUTTON_REQUEST = 0;
    public final static int CREATION_OK = 1;
    public final static int MODIF_OK = 2;
    public final static String TITRE = "org.example.geretacoloc.TITRE";
    public final static String TEXTE = "org.example.geretacoloc.TEXTE";
    public final static String POSITION = "org.example.geretacoloc.POSITION";

    // Pour récupérer les données saisie lors d'un ajout/modification de note
    String titre;
    String texte;
    int position;
    boolean connected = true;

    //Get et Set

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }


    public Note getNote(int i) {
        return notes.get(i);
    }

    //Méthode

    public void deleteNote(ArrayList<Note> notes, int i){
        notes.remove(getNote(i));
    }

    public void filter(){
        String titre = chercherNotes.getText().toString();

        ArrayList<Note> listNotesNew = new ArrayList<>();

        for (Note note : notes){
            if(note.getTitre().toLowerCase().toString().startsWith(titre)){
                listNotesNew.add(note);
            }
        }
        ArrayAdapter<Note> adapter = new ArrayAdapter<>(BlocNotes.this, android.R.layout.simple_list_item_1, listNotesNew);
        listNotes.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_note);

        btnAjouterNote = (Button) findViewById(R.id.btnAjouterNote);
        btnSupprimerNote = (Button) findViewById(R.id.btnSupprimerNote);
        listNotes = (ListView) findViewById(R.id.listNote);
        chercherNotes = (EditText) findViewById(R.id.chercherNotes);

        // Recupérer les infos du Singleton

        File dir;
        dir = getDir("Test",MODE_PRIVATE);
        final SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
        setNotes(singletonColoc.getNotes());
        Log.d("regle",singletonColoc.objectToJson());

        //Aficher les notes quand on arrive sur le menu
        ArrayAdapter<Note> adapter = new ArrayAdapter<>(BlocNotes.this, android.R.layout.simple_list_item_1, notes);
        listNotes.setAdapter(adapter);

        btnAjouterNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // sur appuie de ajouter Note

                //ouvre nouvelle activitée de saisie de la note
                Intent intent = new Intent(BlocNotes.this, AjouterNote.class);
                startActivityForResult(intent, BUTTON_REQUEST);
            }
        });


        chercherNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter();
            }
        });

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(connected && ETAT_SUPPRIMER==0){
                    //Recuperer la note à la position cliquée
                    Note laNote = getNote(position);

                    Intent intent = new Intent(BlocNotes.this,ModifierNote.class);
                    //envoyer la regle à l'intent

                    intent.putExtra(TITRE,laNote.getTitre());
                    intent.putExtra(TEXTE,laNote.getTexte());
                    intent.putExtra(POSITION,position);
                    startActivityForResult(intent, BUTTON_REQUEST);


                }
                else if(!connected && ETAT_SUPPRIMER==0){
                    Note lanote = getNote(position);

                    Intent intent = new Intent(BlocNotes.this, OuvrirLectureSeul.class);

                    intent.putExtra(TITRE,lanote.getTitre());
                    intent.putExtra(TEXTE,lanote.getTexte());
                    startActivity(intent);
                }

            }
        });

        listNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch(ETAT_SUPPRIMER) {
                    case 0:
                        listNotes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        //mise à jour affichage liste
                        ArrayAdapter<Note> adapter = new ArrayAdapter<>(BlocNotes.this, android.R.layout.simple_list_item_multiple_choice, notes);
                        listNotes.setAdapter(adapter);
                        //rend le bouton supprimer visible
                        btnSupprimerNote.setVisibility(View.VISIBLE);
                        btnAjouterNote.setVisibility(View.GONE);
                        ETAT_SUPPRIMER=1;
                        break;
                    case 1:
                        listNotes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        //mise à jour affichage liste
                        ArrayAdapter<Note> adapter2 = new ArrayAdapter<>(BlocNotes.this, android.R.layout.simple_list_item_1, notes);
                        listNotes.setAdapter(adapter2);
                        //rend le bouton supprimer invisible
                        btnSupprimerNote.setVisibility(View.GONE);
                        btnAjouterNote.setVisibility(View.VISIBLE);
                        ETAT_SUPPRIMER=0;
                }
                return false;
            }
        });

        btnSupprimerNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("test1", "clic entendu");
                SparseBooleanArray suppList = listNotes.getCheckedItemPositions();
                int nb_note_initial = notes.size();

                for(int i = nb_note_initial-1; i >= 0; i--){
                    Log.d("test2","décompte lancé");
                    Log.d("test3","" + i + "");
                    Log.d("test4", "" + suppList.get(i) + "");
                    Log.d("test5", getNote(i).getTitre());
                    if(suppList.get(i)){
                        Log.d("test3","note coché trouvée");
                        //singletonColoc.supprimerNoteBDD(getNote(i)); //suprimerNoteBDD supprime toujours la note qu'il y a après c'est pourquoi on ne peut pas supprimer la première et que supprimer la dernière fait planter
                        deleteNote(notes,i);
                    }
                }

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setNotes(notes);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);


                ETAT_SUPPRIMER = 0;
                ArrayAdapter<Note> adapter = new ArrayAdapter<>(BlocNotes.this, android.R.layout.simple_list_item_1, notes);
                listNotes.setAdapter(adapter);
                btnSupprimerNote.setVisibility(View.GONE);
                btnAjouterNote.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Sur résultat on le traite après vérification qu'il y a bien eu appuie sur le bouton et pas juste un retour
        if(requestCode == BUTTON_REQUEST){
            if (resultCode == CREATION_OK){

                // Récuperation des données de l'autre activité
                titre = data.getStringExtra(TITRE);
                texte = data.getStringExtra(TEXTE);

                //On ajoute à la liste de notes (ArrayList)

                Note nouvelleNote = new Note(titre,texte);
                notes.add(nouvelleNote);

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setNotes(notes);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);

                // Ajout à la base de données

                //singletonColoc.ajouterNoteBDD(nouvelleNote);

                //Aficher les notes quand on arrive sur le menu
                ArrayAdapter<Note> adapter = new ArrayAdapter<>(BlocNotes.this, android.R.layout.simple_list_item_1, notes);
                listNotes.setAdapter(adapter);
            }

            if (resultCode == MODIF_OK){

                //On récupère la note que l'on vient de modifier
                position = data.getIntExtra(POSITION, 0);
                titre = data.getStringExtra(TITRE);
                texte = data.getStringExtra(TEXTE);
                Note laNote = notes.get(position);

                //Et on met à jour les valeurs
                laNote.setTitre(titre);
                laNote.setTexte(texte);
                //singletonColoc.updateNoteBDD(laNote);

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setNotes(notes);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);

                //On met l'affichage à jour
                ArrayAdapter<Note> adapter = new ArrayAdapter<>(BlocNotes.this, android.R.layout.simple_list_item_1, notes);
                listNotes.setAdapter(adapter);

            }
        }

    }

}
