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
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

/*
activity pour charge totale
 */
public class ChargeTotale extends Module{

    public static ArrayList<Charge> charges = new ArrayList<>();
    public static int position=0;

    public final static int BUTTON_REQUEST = 0;
    public final static String NOM = "com.example.oeliniainaando_2.calcul_des_charges.NOM";
    public final static String MONTANT = "com.example.oeliniainaando_2.calcul_des_charges.MONTANT";

    //GET et SET

    public Charge getCharge (int i){return charges.get(i);}

    public static void setCharges(ArrayList<Charge> charges) {
        ChargeTotale.charges = charges;
    }

    private float montant_total=0;

    Button btnAjouterCharge;
    ListView listCharges;
    TextView Resultat;
    TextView textRes;
    TextView resultatParPersonne;
    TextView textResParPers;

    //-------------Fonction qui va servir à adapter et à rafraichir la listView-----------//
    public void majList()
    {
        ArrayAdapter<Charge> adapter = new ArrayAdapter<>(ChargeTotale.this,android.R.layout.simple_list_item_1, charges);
        listCharges.setAdapter(adapter);
        listCharges.invalidateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyer);

        btnAjouterCharge = (Button) findViewById(R.id.btnAjouterCharge);
        listCharges = (ListView) findViewById(R.id.listCharge);
        Resultat = (TextView) findViewById(R.id.Resultat);
        textRes = (TextView) findViewById(R.id.chargeTotale);
        resultatParPersonne = (TextView) findViewById(R.id.chargeTotaleParPersonne);
        textResParPers = (TextView) findViewById(R.id.montantParPersonne);
        registerForContextMenu(listCharges);

        File dir;
        dir = getDir("Test",MODE_PRIVATE);
        final SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
        setCharges(singletonColoc.getCharges());
        Log.d("regle",singletonColoc.objectToJson());

        //Aficher et recalculer les Charges quand on arrive sur le menu
        calculer_montant_total();
        Resultat.setText(String.valueOf(montant_total)+'€');
        textResParPers.setText(String.valueOf(calculer_montant_par_personne(montant_total,singletonColoc.getMails())));

        Log.d("bouton  retour", String.valueOf(BUTTON_REQUEST));

        //mis à jour de listes charges
        majList();

        //quand on appui sur le bouton ajouterCharge on arrive dans une nouvelle fenêtre
        btnAjouterCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAjouter = new Intent(ChargeTotale.this,AjouterChargeActivity.class);
                startActivityForResult(intentAjouter, BUTTON_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_loyer, menu);
        return true;
    }

    // Creer un menu contextuel
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextuel_modifier_charge, menu);
    }

    //Action du menu Contextuel
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.supprimer: // clique supprimer du menu contextuel
                Log.d("Supp", "supprimer");

                charges.remove(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);

                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setCharges(charges);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                Log.d("ajoutregle",jsonContent);
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);


                calculer_montant_total();
                Resultat.setText(String.valueOf(montant_total));
                textResParPers.setText(String.valueOf(calculer_montant_par_personne(montant_total,singletonColoc.getMails())));
                majList();

                return true;

            case R.id.modifier: // clique modfier

                Log.d("Mod","modifier");

                //Recuperer la charge à la position cliquée
                position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                String nouveau_nom = getCharge(position).getNom_charge();
                float un_nouveau_montant = getCharge(position).getMontant_charge();

                String nouveau_montant = String.valueOf(un_nouveau_montant);
                Intent intent = new Intent(ChargeTotale.this,ModifierChargeActivity.class);

                intent.putExtra(ChargeTotale.NOM,nouveau_nom);
                intent.putExtra(ChargeTotale.MONTANT, nouveau_montant);

                startActivityForResult(intent, BUTTON_REQUEST);
                majList();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        if(requestCode == BUTTON_REQUEST)
        {
            if (resultCode == 1)//si on est dans "ajouterChargeActivity
            {
                String le_nom_de_la_charge = data.getStringExtra(NOM);
                String le_montant_de_la_charge = data.getStringExtra(MONTANT);
                float montant_charge = Float.valueOf(le_montant_de_la_charge);

                Charge newCharge = new Charge(le_nom_de_la_charge, montant_charge);     //on cree la nouvelle charge
                charges.add(newCharge);
                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setCharges(charges);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                Log.d("ajoutregle",jsonContent);
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);                     // on crée la nouvelle charge dans la BDD
                textResParPers.setText(String.valueOf(calculer_montant_par_personne(montant_total,singletonColoc.getMails()))+'€');
            }

            if(resultCode==2) //si on est dans modifierChargeActivity
            {
                String le_nom_de_la_charge = data.getStringExtra(NOM);
                String le_montant_de_la_charge = data.getStringExtra(MONTANT);
                float montant_charge = Float.valueOf(le_montant_de_la_charge);

                getCharge(position).setNom_charge(le_nom_de_la_charge);            //on modifie le nom de la charge à la position de la charge
                getCharge(position).setMontant_charge(montant_charge);              //on modifie le montant de la charge à la position de la charge

                //maj singleton et BDD
                File dir;
                dir=getDir("Test",MODE_PRIVATE);
                SingletonColoc singletonColoc = SingletonColoc.getInstance().jsonToObject(lireFichierI(dir,MainActivity.nomColocation+".txt")); //récupérer le singleton qui doit contenir les infos
                singletonColoc.setCharges(charges);
                FtpUploadAsync ftpUploadAsync = new FtpUploadAsync();
                String jsonContent = singletonColoc.objectToJson();
                Log.d("ajoutregle",jsonContent);
                File file = writeJsonInFile(jsonContent,MainActivity.nomColocation);
                ftpUploadAsync.execute(file);           //on update dans la bdd
                textResParPers.setText(String.valueOf(calculer_montant_par_personne(montant_total,singletonColoc.getMails()))+'€');
            }

            calculer_montant_total();                               //on calcul le montant total
            Resultat.setText(String.valueOf(montant_total)+'€');        //on met le montant dans notre attribut montant total
            majList();
        }
    }

    public void calculer_montant_total(){
        montant_total= 0;
        for(Charge charge : charges)
        { montant_total+=charge.getMontant_charge();}
    }

    public float calculer_montant_par_personne(float montant, ArrayList<String> users){
        return montant / users.size();
    }

}

