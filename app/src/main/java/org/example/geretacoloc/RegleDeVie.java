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

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;


public class RegleDeVie extends Module {

    //Attribut

    public ArrayList<Regle> regles = new ArrayList<>();

    //Varible pour interface et communication entre intent

    private static int compteurRegles = 1;
    public final static int BUTTON_REQUEST = 0;
    public static int position = 0;
    final static String texteDebut = "Appuie long pour Modifier/Supprimer";

    Button btnAjouterRegle;
    ListView listRegles;
    EditText ajouterRegle;

    //Get et Set

    public void setRegles(ArrayList<Regle> regles) {
        this.regles = regles;
    }

    public Regle getRegle(int i) {

        return regles.get(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regle_de_vie);

        btnAjouterRegle = (Button) findViewById(R.id.btnAjouterRegle);
        listRegles = (ListView) findViewById(R.id.listRegle);
        ajouterRegle = (EditText) findViewById(R.id.ajouterUneRegle);

        // Menu contextuel

        registerForContextMenu(listRegles); // save le listView pour le menuContext

        // Consigne

        Toast.makeText(this,texteDebut,Toast.LENGTH_LONG).show();

        // Recupérer les infos du Singleton
        SingletonColoc singletonColoc = SingletonColoc.getInstance();
        File dir;
        dir=getDir("Test",MODE_PRIVATE);
        FtpDownloadAsync ftpDownloadAsync = new FtpDownloadAsync();
        ftpDownloadAsync.execute(MainActivity.nomColocation,dir.getAbsolutePath());
        singletonColoc = singletonColoc.jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt"));
        setRegles(singletonColoc.getRegles());

        //Récupérer le dernier numéro de Regle

        recupCompteurRegle();


        //Aficher les règles quand on arrive sur le menu
        ArrayAdapter<Regle> adapter = new ArrayAdapter<>(RegleDeVie.this,android.R.layout.simple_list_item_1, regles);
        listRegles.setAdapter(adapter);

        btnAjouterRegle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // sur appuie de ajouter Règle

                recupCompteurRegle();
                Regle nouvelleRegle = new Regle(compteurRegles, ajouterRegle.getText().toString());
                regles.add(nouvelleRegle); // ajouter une regles par défault
                 // augmenter le compterur de numéro de règle

                //+ au singleton

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setRegles(regles);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                Log.d("ajoutregle",jsonContent);
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);


                // Mettre aux formats pour list view
                ArrayAdapter<Regle> adapter = new ArrayAdapter<>(RegleDeVie.this, android.R.layout.simple_list_item_1, regles);
                listRegles.setAdapter(adapter);



            }
        });

        // Appuie sur un item de la List de Regle

        listRegles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Mes","Click"); // message dans le LogCat pour vérfier le clique
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_regle_de_vie, menu);
        return true;
    }

    // Creer un menu contextuel
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextuel_regle_de_vie, menu);

    }

    //Action du menu Contextuel
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            //------------------clique supprimer----------------------//
            case R.id.supprimer:
                Log.d("Supp","supprimer");

                regles.remove(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                recupCompteurRegle();

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setRegles(regles);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                Log.d("ajoutregle",jsonContent);
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);

                //réafficher

                ArrayAdapter<Regle> adapter = new ArrayAdapter<>(RegleDeVie.this,android.R.layout.simple_list_item_1, regles);
                listRegles.setAdapter(adapter);
                return true;

            //-------------- clique modfier--------------------------//
            case R.id.modifier:

                Log.d("Mod","modifier");

                //Recuperer la regle à la position cliquée
                position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                String laRegle = getRegle(position).getRegle();

                Intent intent = new Intent(RegleDeVie.this,ModifierRegleActivity.class);
                //envoyer la regle à l'intent

                intent.putExtra("org.example.geretacoloc.regleDeVie",laRegle);
                startActivityForResult(intent, BUTTON_REQUEST);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

                String laNouvelleRegle = data.getStringExtra("org.example.geretacoloc.ModifierRegleDeVie");
                Log.d("nouvelle regle",laNouvelleRegle);

                getRegle(position).setRegle(laNouvelleRegle);

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setRegles(regles);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                Log.d("ajoutregle",jsonContent);
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);

                ArrayAdapter<Regle> adapter = new ArrayAdapter<>(RegleDeVie.this, android.R.layout.simple_list_item_1, regles);
                listRegles.setAdapter(adapter);

            }
        }

    }

    public void recupCompteurRegle(){
        for (Regle regle:regles)
            if (compteurRegles<=regle.getNumero()) {
                compteurRegles ++;
            }
    }
}
