package com.example.ecc.projetversionclient10;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Consulter extends AppCompatActivity {


    Bitmap bmpimage = null;
    static Context context;
    ImageView image;
    int id;
    String GPS;
    String phone;
    String siteweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter);

        context = this;
        image = (ImageView)findViewById(R.id.imageView);

        Intent itn = getIntent();
        id = (int)itn.getLongExtra("id", -1);
        if(id == -1){
            Toast.makeText(this, "Erreur, id = -1", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        acssesbaseclient bdd = new acssesbaseclient();
        //
        ContentResolver cs= getContentResolver();

        String strUri = "content://com.example.ecc.projetvesrion10.rest/recherche_client_id/id/"+id;
        System.out.println("URI: "+strUri);
        Uri uri = Uri.parse(strUri);
        Cursor c= bdd.chercher_id_Restaurant(cs, uri);

     //Vérifier si c'est tout est normal...
        if (c == null) {
            Toast.makeText(this, "Erreur cursor = null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (c != null && c.getCount() <= 0) {
            Toast.makeText(this, "Erreur cursor =0 ou -1", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Tout est normal ici...
        Toast.makeText(this, "-------------------------" + c.getCount(), Toast.LENGTH_SHORT).show();
        c.moveToFirst();
        System.out.print("Cursor: " + c.getString(c.getColumnIndex("avis")));
        AfficherRestaurant(CursorToRestaurant(c));
    }
    public void AfficherRestaurant(restaurant r){

        System.out.println("Lien Image :"+restaurant.decodeURL(r.getDonnees_multimedia()));
        new Chargeur().execute(restaurant.decodeURL(r.getDonnees_multimedia()));

        //Récupération des champs:
        TextView editNom = (TextView)findViewById(R.id.textNom);
        TextView editAdresse = (TextView)findViewById(R.id.textAdresse);
        TextView editCout = (TextView)findViewById(R.id.TextCout);
        TextView editType = (TextView)findViewById(R.id.textType);
        RatingBar Note = (RatingBar)findViewById(R.id.ratingBar);
        EditText editAvis = (EditText)findViewById(R.id.editAvis);

        //Mettre les données dans les champs:
        editNom.setText(r.getNom());
        editAdresse.setText(r.getAdresse());
        editCout.setText(""+editCout.getText()+r.getCout_moyen_du_repas()+"€ par repas");
        editType.setText(""+editType.getText()+r.getType_de_cuisine());
        Note.setProgress(r.getNote_dappreciation());
        editAvis.setText(r.getAvis());

        //Sauvgarder les coordonnées GPS:
        this.GPS = r.getLocalisation_geographique();
        this.phone = r.getNumero_de_telephone();
        this.siteweb = r.getAdresse_de_site_internet();
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
                geo, Integer.valueOf(cout), periode, type,image, avis);

        return rest;
    }
    //=========================================================================================
    class Chargeur extends AsyncTask<String, Integer, Bitmap> {

        int progress;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", "Please wait");
        }
        @Override
        protected Bitmap doInBackground(String... urlArray) {
            try {
                URL url = new URL(urlArray[0]);
                HttpURLConnection co =  (HttpURLConnection) url.openConnection();
                co.setDoInput(true);
                co.connect();
                System.out.println("1");

                InputStream in =co.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(in);
                System.out.println("imageeee");

                return bitmap;
            }catch (Exception E){
                E.printStackTrace();
                System.out.println("nooooooo  ooooo  imageeee");

                return null;
            }
        }

        protected void onProgressUpdate(Integer... progressArray) {
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //imageView.setImageBitmap(result);
             progressDialog.dismiss();

            bmpimage =result;

            if(bmpimage == null){
                Toast.makeText(context, "Image introuvble, vérifier connexion et URL", Toast.LENGTH_SHORT).show();
                return;
            }
            image.setImageBitmap(bmpimage);

        }


    }

    // Validation:
    public void onClickValider(View v){
        //Récupération des champs:
        //Note et Avis
        RatingBar rating = (RatingBar)findViewById(R.id.ratingBar);
        EditText editAvis = (EditText)findViewById(R.id.editAvis);

        acssesbaseclient bdd = new acssesbaseclient();
        //
        ContentResolver cs= getContentResolver();

        String strUri = "content://com.example.ecc.projetvesrion10.rest/modifier_client_id/id/"+id
                    +"/note_dappreciation/"+(int)rating.getRating()+"/avis/"+editAvis.getText().toString();
        Uri uri = Uri.parse(strUri);
        restaurant rest = new restaurant((int)rating.getRating(), editAvis.getText().toString());
        Boolean N= bdd.modifier_restaurant(cs,rest, strUri);
        if(N){
            Toast.makeText(this, "Modification avec succées", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Erreur dans la modification", Toast.LENGTH_SHORT).show();
    }

    public void onClickGMaps(View v){
        //Récupérer les coordonnées GPS de l'adresse:
        //GPS: LAT, LNG
        Uri uri = Uri.parse("geo:"+getLat(GPS)+", "+getLng(GPS)+"?q="+getLat(GPS)+", "+getLng(GPS));
        Intent itn = new Intent(Intent.ACTION_VIEW);
        itn.setData(uri);
        itn.setPackage("com.google.android.apps.maps");
        startActivity(itn);
    }

    public double getLat(String gps){
        double lat=0;
        String chaine = "";
        int i=0;
        while(gps.charAt(i) != ','){
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

    public void onClickCall(View v){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.phone));
        startActivity(intent);
    }

    public void onClickSiteWeb(View v){
        //Decode URL:
        String site = restaurant.decodeURL(this.siteweb);
        Uri uri = Uri.parse(site);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onClickRetour(View v){
        finish();
    }
}
