package com.example.ecc.projetvesrion10;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class activity_recherche_restaurant extends AppCompatActivity {


    acssesbase bdd = new acssesbase();
    //Liste résultat de la requète:
    final ArrayList<String> list = new ArrayList<String>();
    static int listSize = 0;
    final ArrayList<Long> listId = new ArrayList<Long>();

    static long IDRESTAURANT = -1;//Id du restaurant séléctionné dans la liste.
    static String RESTO_SELECTION;//Restaurant selectionné dans la liste.
    static String uriRecherche=""; // garder le uri de la recherche, utilisation dans plusieurs methodes;;;
    TextView selection;

    View element_precedent = null;//utile pour mettre à jour la couleur du background des elements de la liste.

    boolean source_tout = true;//Vrai si on a fait une recherche globale, faux si on recherche un restaurant precis.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_restaurant);

        selection = (TextView)findViewById(R.id.RestoSelectionne);
    }

    //******************* Recherche:
    //Préparation de la recherche
    public void onClickRecherche(View v) {

        //Lancer la methode de recherche.
        //Tester si tout les champs sans vides:
        EditText editNom = (EditText) findViewById(R.id.idEditNom);
        EditText editType = (EditText) findViewById(R.id.idEditType);

        RatingBar editNote = (RatingBar) findViewById(R.id.idEditNote);

//        EditText editNote = (EditText) findViewById(R.id.idEditNote);
        EditText editCout = (EditText) findViewById(R.id.idEditCout);

        if(editNom.getText().toString().matches("") && editType.toString().matches("")  && editCout.getText().toString().matches("")){
            //Touts les champs sans vides, erreur !!
            Toast.makeText(this, "Touts les champs sont vides!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Récupération des champs:
        String nom = editNom.getText().toString();
        String type = editType.getText().toString();
        int note = (int)editNote.getRating();
        String cout = editCout.getText().toString();

        //Effectuer la recherche:
        //création de l'uri:
        String strUri = "content://com.example.ecc.projetvesrion10.rest/recherche/";
       /*
                Si champs vide, mettre la valeur du champs = _
                le char _ designe l'absence du champs.
        */
        if(nom.matches("")) strUri =strUri + base.NOM + "/_/";
        else  strUri =strUri + base.NOM+"/"+nom+"/" ;

        if(type.matches("")) strUri = strUri + base.TYPE_DE_CUISINE + "/_/";
        else strUri = strUri + base.TYPE_DE_CUISINE+"/"+type+"/";

        strUri = strUri  + base.NOTE_DAPPRECIATION+"/"+note+"/";

        if(cout.matches("")) strUri = strUri + base.COUT_MOYEN_DU_REPAS+"/_/";
        else strUri = strUri +base.COUT_MOYEN_DU_REPAS+"/"+cout+"/";

        this.uriRecherche = strUri;

        Cursor resultat = Recherche(strUri);
        System.out.println("Count: "+resultat.getCount());
        if(resultat.getCount() == 0){
            Toast.makeText(this, "Aucun resultat", Toast.LENGTH_LONG).show();
            return;
        }
        this.source_tout = false;
        //Afficher la recherche dans la listeview:
        AfficherResultat(resultat);
    }

    //Rechercher tout les element:
    public void onClickAfficherTout(View v){
        String strUri = "content://com.example.ecc.projetvesrion10.rest/tout";
        this.uriRecherche = strUri;
        Cursor resultat = Recherche(strUri);
        if (resultat.getCount() == 0) {
            Toast.makeText(this, "Base de données vide", Toast.LENGTH_LONG).show();
            return;
        }
        this.source_tout = true;
        //afficher les restaurants:
        AfficherResultat(resultat);
    }

    //LAncement de la recherche, on retourne le cursor des résultats trouvés
    public Cursor Recherche(String uristr){

        System.out.println("URI: " + uristr);
        // Toast.makeText(this, "Le uri: "+uristr, Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(uristr);
        ContentResolver cs= getContentResolver();
        Cursor c = bdd.chercherRestaurant(cs, uri);
        Toast.makeText(this, "Cursor: "+c.getCount(), Toast.LENGTH_SHORT).show();
        return c;
    }

    //On affiche la liste des résultat (on memorisant les ID de chaque restaurant, utile pour lancer la modification aprés)
    public void AfficherResultat(Cursor c){
        list.clear();
        listId.clear();
        restaurant courant;
        c.moveToFirst();
        //Liste des restaurant contenant les noms + notes:
        int i = 0;
        while(!c.isAfterLast()){
            courant = CursorToRestaurant(c);
            //Mise à jour de la liste
            list.add(courant.getNom()+", Note:  "+courant.getNote_dappreciation());
            //Ajouter l'id dans la liste des id:
            System.out.println("wiw: "+c.getColumnIndex("_id"));
            listId.add(c.getLong(c.getColumnIndex("_id")));
            c.moveToNext();
            i++;
        }
        //Initialisation de la liste view:
        final ListView listeView = (ListView)findViewById(R.id.listeResultat);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.element, list);
        listeView.setAdapter(adapter);
        System.out.println("list: "+list);
        //On item click listener:
        listeView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //************ 1. Modification couleur background:
                view.setBackgroundColor(Color.GRAY);//int value of the color: holo_orange_dark
                if(element_precedent != null && element_precedent != view){element_precedent.setBackgroundColor(Color.WHITE);}
                element_precedent = view;

                //********** Récupération de l'id du resto:
                IDRESTAURANT = listId.get(position);
                //Modification du msg du preview du restaurant selectionné:
                RESTO_SELECTION = list.get(position);
                selection.setText(RESTO_SELECTION);
            }
        });

        RelativeLayout layoutResultat = (RelativeLayout)findViewById(R.id.LayoutResultat);
        layoutResultat.setVisibility(View.VISIBLE);

        RelativeLayout layoutRecherche = (RelativeLayout)findViewById(R.id.LayoutRecherche);
        layoutRecherche.setVisibility(View.INVISIBLE);

    }

    //Methode Utile: Transformer un cursor vers un Restaurant
    public restaurant CursorToRestaurant(Cursor c){

        //le curseur n'est pas null,
        String nom = c.getString(c.getColumnIndex(base.NOM));
        String type = c.getString(c.getColumnIndex(base.TYPE_DE_CUISINE));
        String note = c.getString(c.getColumnIndex(base.NOTE_DAPPRECIATION));
        String cout = c.getString(c.getColumnIndex(base.COUT_MOYEN_DU_REPAS));
        String adresse = c.getString(c.getColumnIndex(base.ADRESSE));
        String siteWeb = c.getString(c.getColumnIndex(base.ADRESSE_DE_SITE_INTERNET));
        String geo = c.getString(c.getColumnIndex(base.LOCALISATION_GEOGRAPHIQUE));
        String phone = c.getString(c.getColumnIndex(base.NUMERO_DE_TELEPHONE));
        String periode = c.getString(c.getColumnIndex(base.PERIODE_OVERTURE));
        String  image = c.getString(c.getColumnIndex(base.DONNEES_MULTIMEDIA));
        restaurant rest  = new restaurant(nom, adresse, phone, siteWeb,Integer.valueOf(note),
                geo, Integer.valueOf(cout), periode, type,image);

        return rest;
    }


    public void onClickAnnuler(View v){
        finish();
    }

    //*************************** Modification:
    //Lancer l'activity du Update, avec les info necessaires
    public void onClickModifier(View v){
        if(IDRESTAURANT == -1){
            Toast.makeText(this, "Selectionner d'abord un restaurant", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent updateItn = new Intent(this, activity_modifier_restaurant.class);
        updateItn.putExtra("id", IDRESTAURANT);
        startActivity(updateItn);
    }

    public void onClickAnnulerRslt(View v){
        IDRESTAURANT = -1;
        RESTO_SELECTION = "";
        selection.setText("");
        list.clear();
        listId.clear();

        RelativeLayout layoutResultat = (RelativeLayout)findViewById(R.id.LayoutResultat);
        layoutResultat.setVisibility(View.INVISIBLE);

        RelativeLayout layoutRecherche = (RelativeLayout)findViewById(R.id.LayoutRecherche);
        layoutRecherche.setVisibility(View.VISIBLE);
    }

    //*************************** Suppression:
    public void onClickSupprimer(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmer la suppression");
        builder.setMessage("Voulez vous vraiment supprimer le restaurant " + RESTO_SELECTION + "?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity_recherche_restaurant.this, "Restaurant:\nid:" + IDRESTAURANT + " - " + RESTO_SELECTION + " va etre supprimer", Toast.LENGTH_LONG).show();
                //Requete suppression
                lancer_suppression();
                //mettre à jour la listeView.
                AfficherResultat(Recherche(uriRecherche));
                dialog.dismiss();
            }
        });

        //Si l'utilisateur click sur Non, nothing happens...
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Afficher le message de confirmation
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void lancer_suppression() {
        String str_Uri = "content://com.example.ecc.projetvesrion10.rest/delete_id/id/" + IDRESTAURANT;
        ContentResolver c = getContentResolver();
        boolean flag = bdd.supprimer_restaurant(c, str_Uri);

        if (flag) {
            Toast.makeText(getBaseContext(), "le restaurant", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "failed ", Toast.LENGTH_SHORT).show();

        }

    }

    //========================= On Resume
    @Override
    protected void onResume(){

        super.onResume();
    /*
        Quand on revient de la modification, on met à jour la listview;
     */
        if(uriRecherche.matches("")) Toast.makeText(this, "Nothing happens, uri vide...", Toast.LENGTH_LONG).show();
        else{
            if(source_tout == true)
                AfficherResultat(Recherche(uriRecherche));
            else{
                findViewById(R.id.LayoutResultat).setVisibility(View.GONE);
                findViewById(R.id.LayoutRecherche).setVisibility(View.VISIBLE);
            }
        }
    }

    //=========================== modifier le comportement du bouton Back:
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //Bouton Back is pressed:
            if(findViewById(R.id.LayoutResultat).getVisibility() == View.VISIBLE){
                //On est dans la partie Resultat, on revient vers la partie Recherche:
                findViewById(R.id.LayoutResultat).setVisibility(View.INVISIBLE);
                findViewById(R.id.LayoutRecherche).setVisibility(View.VISIBLE);
            }else finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //**********************************************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_recherche_restaurant, menu);
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