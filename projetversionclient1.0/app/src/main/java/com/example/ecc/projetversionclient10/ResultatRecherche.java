package com.example.ecc.projetversionclient10;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultatRecherche extends AppCompatActivity {

    //Liste résultat de la requète:
    final ArrayList<String> list = new ArrayList<String>();
    static int listSize = 0;
    final ArrayList<Long> listId = new ArrayList<Long>();

    static long IDRESTAURANT = -1;//Id du restaurant séléctionné dans la liste.
    static String RESTO_SELECTION;//Restaurant selectionné dans la liste.
    static String uriRecherche=""; // garder le uri de la recherche, utilisation dans plusieurs methodes;;;

    View element_precedent = null;//utile pour mettre à jour la couleur du background des elements de la liste.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_recherche);

        //Récupération de l'uri :
        Intent itn = getIntent();
        String strUri = itn.getStringExtra("uri");
        Toast.makeText(this, "URI: "+strUri, Toast.LENGTH_LONG).show();
        Cursor c = Recherche(strUri);
     //Si le resultat est vide...
        if(c == null){
            Toast.makeText(this, "Aucun résultat", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if(c.getCount() <= 0){
            Toast.makeText(this, "Aucun résultat", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
     //Sinon, afficher la liste des résultats:
        AfficherResultat(c);
   }

//===>LAncement de la recherche, on retourne le cursor des résultats trouvés
    public Cursor Recherche(String strUri){

        acssesbaseclient bdd = new acssesbaseclient();
        //
        ContentResolver cs= getContentResolver();
        Uri uri = Uri.parse(strUri);
        Cursor c= bdd.chercherRestaurant(cs, uri);
        Toast.makeText(this, "Cursor: "+c.getCount(), Toast.LENGTH_SHORT).show();
        return c;
    }

//=====> On affiche la liste des résultat (on memorisant les ID de chaque restaurant, utile pour lancer la modification aprés)
    public void AfficherResultat(Cursor c){
        list.clear();
        listId.clear();
        restaurant courant;
        c.moveToFirst();
        //Liste des restaurant contenant les noms + notes:
        Intent itn  = getIntent();
        String distance = itn.getStringExtra("distance");
        if(distance == null || distance.matches("")){
            distance = "999999999";
        }
        System.out.println("dis: "+distance);
        int i = 0;
        while(!c.isAfterLast()){
            courant = CursorToRestaurant(c);
            //Mise à jour de la liste
            //Tester si c'est < 1km, si c'est le cas, ajouter, sinon ne pas ajouter
            if(proche(courant, distance)){
                list.add(courant.getNom()+", Note:  "+courant.getNote_dappreciation());
            }
            //Ajouter l'id dans la liste des id:
            System.out.println("wiw: " +c.getColumnIndex("_id"));
            listId.add(c.getLong(c.getColumnIndex("_id")));
            c.moveToNext();
            i++;
        }
        if(list.isEmpty()) {
            Toast.makeText(this, "Aucun Resultat==)", Toast.LENGTH_SHORT).show();
            return;
        }
        //Initialisation de la liste view:
        final ListView listeView = (ListView)findViewById(R.id.listeResultat);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.element, list);
        listeView.setAdapter(adapter);
        System.out.println("list: "+list);
        //On item click listener:
        listeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //************ 1. Modification couleur background:
                view.setBackgroundColor(Color.GRAY);//int value of the color: holo_orange_dark
                if (element_precedent != null && element_precedent != view) {
                    element_precedent.setBackgroundColor(Color.WHITE);
                }
                element_precedent = view;

                //********** Récupération de l'id du resto:
                IDRESTAURANT = listId.get(position);
                //Modification du msg du preview du restaurant selectionné:
                RESTO_SELECTION = list.get(position);
            }
        });
    }

//====> Methode Utile: Verifier si un restaurant est proche de la position courante:
    public boolean proche(restaurant rest, String distance){
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locationRestaurant = new Location("");
        locationRestaurant.setLatitude(this.getLat(rest.getLocalisation_geographique()));
        locationRestaurant.setLongitude(this.getLng(rest.getLocalisation_geographique()));
        if(location.distanceTo(locationRestaurant) < Integer.parseInt(distance)){
            return true;
        }

        return false;
    }
//===>Methode Utile: Transformer un cursor vers un Restaurant
    public restaurant CursorToRestaurant(Cursor c){

        //le curseur n'est pas null,
        String nom = c.getString(c.getColumnIndex("nom"));
        String type = c.getString(c.getColumnIndex("type_de_cuisine"));
        String note = c.getString(c.getColumnIndex("note_dappreciation"));
        String cout = c.getString(c.getColumnIndex("cout_moyen_du_repas"));
        String adresse = c.getString(c.getColumnIndex("adresse"));
        String siteWeb = c.getString(c.getColumnIndex("site_internet"));
        String geo = c.getString(c.getColumnIndex("localisation_geographique"));
        String phone = c.getString(c.getColumnIndex("numero_de_telephone"));
        String periode = c.getString(c.getColumnIndex("periode_ouverture"));
        String  image = c.getString(c.getColumnIndex("donnees_multimedia"));
        String avis = c.getString(c.getColumnIndex("avis"));

        restaurant rest  = new restaurant(nom, adresse, phone, siteWeb,Integer.valueOf(note),
                geo, Integer.valueOf(cout), periode, type,image);

        return rest;
    }


    //************* bouton onclick:
    public void onClickConsulter(View v){
        //Consuler
        if(IDRESTAURANT == -1){
            Toast.makeText(this, "Selectionner d'abord un restaurant", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent itn = new Intent(this, Consulter.class);
        itn.putExtra("id", IDRESTAURANT);
        startActivity(itn);
    }

    public void onClickAnnuler(View v){
        finish();
    }
//====> Methodes utiles, get lat et getlng
    public double getLat(String gps){
    double lat=0;
    String chaine = "";
    int i=0;
    while(gps.charAt(i) != ',' && gps.charAt(i) != '.'){
        chaine += gps.charAt(i);
        i++;
    }
    lat = Double.parseDouble(chaine);
    return lat;
}

    public double getLng(String gps){
        double lng = 0;
        String chaine = "";
        int i=0;
        while(gps.charAt(i) != ' '){
            i++;
        }
        i ++;
        while(i != gps.length()){
            chaine += gps.charAt(i);
            i++;
        }
        lng = Double.parseDouble(chaine);
        return lng;
    }
    public void onClickAnnulerRslt(View v){
        finish();
    }
}
