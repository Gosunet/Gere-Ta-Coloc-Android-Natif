package org.example.geretacoloc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
Activity Agenda
 */
public class Agenda extends Module {

    // Les attributs de l'interface graphique
        CalendarView calendrier; // Le calendrier
        Button btnAjout;  //  bouton d'ajout
        ListView listeTaches; // La liste des tachesListView listeTaches; // La liste des taches
        TextView dateAffichage;

    // Pour l'échange de données entre activités
        public final static int BUTTON_REQUEST = 0;
        public final static String NAME = "org.example.geretacoloc.NAME";
        public final static String URGENCE = "org.example.geretacoloc.URGENCE";

    // Attributs 'indépendants' de l'interface graphique
        public ArrayList<String> taches = new ArrayList<>();
        public ArrayList<String> nomTaches = new ArrayList<>(); // Contient les nom des taches
        public ArrayList<String> dateTaches = new ArrayList<>(); // Contient les dates des taches
        public ArrayList<Boolean> urgenceTaches = new ArrayList<>(); // Contient l'urgence des taches


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Pour permettre un format de date plus simple
        String dateActuelle; // La date sélectionnée
        String n;
        Boolean u;

    /*
    Actualise l'array adapter pour l'agenda
     */
    public void refreshlist()
    {
        // Ceci est l'adapter des taches qui change selon la date sélectionnée
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(Agenda.this,android.R.layout.simple_list_item_1, taches);
        listeTaches.setAdapter(adapter);
        registerForContextMenu(listeTaches);

        // taches est vidé
        taches.clear();
        // l'adaptateur est vidé
        adapter.clear();
        for (int i = 0 ; i < nomTaches.size() ; i++)
        {

            if(dateActuelle.equals(dateTaches.get(i)))
            {

                if (urgenceTaches.get(i))
                {

                    taches.add("*URGENT* " + nomTaches.get(i));
                }
                else
                {
                    taches.add(nomTaches.get(i));
                }
            }
        }
        // On remet à jour visuellement notre liste de taches
        listeTaches.invalidateViews();
    }

    // Les méthodes pour gérer l'interface graphique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);


        calendrier =(CalendarView) findViewById(R.id.calendrier); // Je récupère mon calendrier
        btnAjout = (Button) findViewById(R.id.boutonAjoutAT); // Je récupère le bouton d'ajout
        listeTaches = (ListView) findViewById(R.id.listeTaches); // Je récupère ma liste


        dateAffichage = (TextView) findViewById(R.id.dateSelection); // Le texte qui affiche la date actuellle
        dateActuelle = sdf.format(new Date(calendrier.getDate())); // On récupère la date du calendrier
        dateAffichage.setText(dateActuelle); // Et on l'affiche dans l'espace destiné

        //Récupération de l'instance singleton et des infos cotenus dans celle-ci

        File dir;
        dir = getDir("Test",MODE_PRIVATE);
        final SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les info
        Log.d("regle", singletonColoc.objectToJson());
        nomTaches = singletonColoc.getNomTaches();
        dateTaches = singletonColoc.getDateTaches();
        urgenceTaches = singletonColoc.getUrgenceTaches();

        refreshlist();


       // ---------- GESTION DU CALENDRIER ----------------------

           calendrier.setFirstDayOfWeek(2); // Lundi est le premier jour de la semaine
           calendrier.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
               public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    // On modifie la date actuelle à chaque sélection différente
                    // On récupère la date à chaque fois que l'on change
                    dateActuelle = sdf.format(new Date(calendrier.getDate()));
                    dateAffichage.setText(dateActuelle); // Et on la remet dans l'affichage de la date

                   // Nom de la fonction assez explicite ^^
                    refreshlist();
                }
            });

        // Si je clique sur le bouton pour ajouter une tache
                btnAjout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Agenda.this, Ajout_Tache.class);
                        startActivityForResult(intent, BUTTON_REQUEST);
                    }
                });
     }

    // Création du menu contexuel
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextuel_agenda, menu);
    }

    //Action du menu contextuel
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.supprimerTache:

                nomTaches.remove(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                dateTaches.remove(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                urgenceTaches.remove(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt"));
                singletonColoc.setNomTaches(nomTaches);
                singletonColoc.setDateTaches(dateTaches);
                singletonColoc.setUrgenceTaches(urgenceTaches);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);

                refreshlist();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agenda, menu);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Sur résultat on le traite après vérification qu'il y a bien eu appuie sur le bouton et pas juste un retour
        if(requestCode == BUTTON_REQUEST){
            if (resultCode == RESULT_OK){

            // Récuperation des données de l'autre activité
            n = data.getStringExtra(NAME);
            u = data.getBooleanExtra(URGENCE, false);

                //On ajoute à la liste de taches (ArrayList)

                nomTaches.add(n);
                dateTaches.add(dateActuelle);
                urgenceTaches.add(u);


                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setNomTaches(nomTaches);
                singletonColoc.setDateTaches(dateTaches);
                singletonColoc.setUrgenceTaches(urgenceTaches);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);
                refreshlist();
            }
        }

    }
}
