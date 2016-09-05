package com.example.ecc.projetvesrion10;

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
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.*;
import java.lang.String;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class activity_ajout_restaurant extends AppCompatActivity {

    Bitmap bmpimage = null;

    final acssesbase bdd= new acssesbase();

    EditText nom_edittext;
    EditText adresse_edittext;
    EditText numero_de_telephone_edittext;
    EditText adresse_de_site_internet_edittext;
    // EditText note_dappreciation_edittext;
    EditText localisation_geographique_edittext ;
    EditText cout_moyen_du_repas_edittext;
    EditText periode_ouverture_edittext;
    EditText type_de_cuisine_edittext;
    EditText url_image_edittext;

    RatingBar note_dappreciation_ratingbar;

    static int choix = -1;


    Button valider;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_ajout_restaurant);
        Intent aa= getIntent();
        valider =(Button)findViewById(R.id.valide);
        nom_edittext=(EditText) findViewById(R.id.nom);
        adresse_edittext=(EditText) findViewById(R.id.adresse);
        numero_de_telephone_edittext=(EditText) findViewById(R.id.tel);
        adresse_de_site_internet_edittext=(EditText) findViewById(R.id.site);
        //  note_dappreciation_edittext=(EditText) findViewById(R.id.note);

        note_dappreciation_ratingbar=(RatingBar) findViewById(R.id.ratingBar);
        localisation_geographique_edittext=(EditText) findViewById(R.id.gps) ;
        cout_moyen_du_repas_edittext=(EditText) findViewById(R.id.cout);
        periode_ouverture_edittext=(EditText) findViewById(R.id.periode);
        type_de_cuisine_edittext=(EditText) findViewById(R.id.type_cuisine);
        url_image_edittext =(EditText)findViewById(R.id.urlImage);

    }

    public  void ajout(View view){
        String regexWeb = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$";
        String regexImg =  "^https?://(?:[a-z0-9\\-]+\\.)+[a-z]{2,6}(?:/[^/#?]+)+\\.(?:jpg|gif|png|jpeg)$";

        if(!adresse_de_site_internet_edittext.getText().toString().toLowerCase().startsWith("http")){
            adresse_de_site_internet_edittext.setText("http://"+adresse_de_site_internet_edittext.getText().toString());
        }
        if(!url_image_edittext.getText().toString().toLowerCase().startsWith("http")) {
            url_image_edittext.setText("http://"+url_image_edittext.getText().toString());
        }

        if(!adresse_de_site_internet_edittext.getText().toString().matches(regexWeb)){
            Toast.makeText(this, "Format site web incorrect", Toast.LENGTH_LONG).show();
            return;
        }
        if(!url_image_edittext.getText().toString().matches(regexImg)){
            Toast.makeText(this, "Format URL image incorrect", Toast.LENGTH_LONG).show();
            return;
        }

        restaurant rest= new restaurant(nom_edittext.getText().toString(),
                adresse_edittext.getText().toString(),
                numero_de_telephone_edittext.getText().toString(),
                restaurant.codeURL(adresse_de_site_internet_edittext.getText().toString()),
                (int) note_dappreciation_ratingbar.getRating(),
                //Integer.parseInt(note_dappreciation_edittext.getText().toString()),
                localisation_geographique_edittext.getText().toString(),
                Integer.parseInt(cout_moyen_du_repas_edittext.getText().toString()),
                periode_ouverture_edittext.getText().toString(),
                type_de_cuisine_edittext.getText().toString(),
                restaurant.codeURL(url_image_edittext.getText().toString()), "");


        ContentResolver c= getContentResolver();
        boolean flag= bdd.ajouter_restaurant(c, rest);

        if (flag) {
            Toast.makeText(this,"restaurant ajouté dans la base de donneés",Toast.LENGTH_SHORT).show();
            //   Toast.makeText(this,"rating bar " +String.valueOf((int) note_dappreciation_ratingbar.getRating()), Toast.LENGTH_SHORT).show();
            // note_dappreciation_ratingbar.setProgress(4);
        }else
        {
            Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show();

        }
    }

    public void onClickLocaliser(View v){
        //Générer les coordonnées GPS depuis l'adresse données
        if(this.adresse_edittext.getText().toString().matches("")){
            Toast.makeText(this, "Adresse vide!", Toast.LENGTH_LONG).show();
            return;
        }
        trouverGPS(adresse_edittext.getText().toString());
    }

    public String trouverGPS(String strAddress){

        Geocoder coder = new Geocoder(this, Locale.FRANCE);
        List<Address> address;
        String p1 = "";

        try {
            address = coder.getFromLocationName(strAddress+" France ",10);
            if (address==null || address.size() < 1) {
                Toast.makeText(context, "Aucun resultat, verifier l'adresse", Toast.LENGTH_LONG).show();
                return null;
            }

            AlertDialogView(address);

            return p1;
        }catch(IOException e){
            Toast.makeText(this, "Aucun resultat, verifier votre connexion et l'adresse entrée", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void AlertDialogView(final List <Address> listeAdrs) {
        //final CharSequence[] items = { "15 secs", "30 secs", "1 min", "2 mins" };
        final String[] items = new String[listeAdrs.size()];
        int index = 0;


        for (Address value : listeAdrs) {
            /*
                Adresse - Ville - Etat - Pays -
             */
            items[index] = ""+value.getAddressLine(0);
            if(value.getLocality() != null){
                items[index]+= ", "+value.getLocality();
            }
            if(value.getAdminArea() != null){
                items[index]+= " - "+value.getAdminArea();
            }
            if(value.getCountryName() != null){
                items[index] += "\n"+value.getCountryName();
            }
            index++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//ERROR ShowDialog cannot be resolved to a type
        builder.setTitle("Alert Dialog with ListView and Radio button");
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        choix = item;
                    }
                });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                        .show();
                EditText edit = (EditText)findViewById(R.id.gps);
                if(choix == -1){
                    Toast.makeText(context, "Aucun choix selectionné", Toast.LENGTH_SHORT).show();
                }else
                    edit.setText(listeAdrs.get(choix).getLatitude()+", "+listeAdrs.get(choix).getLongitude());
                    ((EditText) findViewById(R.id.adresse)).setText(items[choix]);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT)
                        .show();
                choix = -1;
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onClickApercu(View v){
        String url = url_image_edittext.getText().toString();
        String regexImg =  "^https?://(?:[a-z0-9\\-]+\\.)+[a-z]{2,6}(?:/[^/#?]+)+\\.(?:jpg|gif|png|jpeg)$";

        if(url.matches("")){
            Toast.makeText(context, "URL image vide!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!url_image_edittext.getText().toString().matches(regexImg)){
            Toast.makeText(this, "Format URL image incorrect", Toast.LENGTH_LONG).show();
            return;
        }

        //Créer l'alert dialog:
        AlertDialogImage(url);
    }

    private void AlertDialogImage(String url){
        //Création de la boite du dialogue:
        //Charger l'image:
        new Chargeur().execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_ajout_restaurant, menu);
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
            ImageView image = new ImageView(context);
            progressDialog.dismiss();

            bmpimage =result;

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context).
                            setMessage("Aperçu de l'image").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

            if(bmpimage == null){
                Toast.makeText(context, "Image introuvble, vérifier connexion et URL", Toast.LENGTH_SHORT).show();
                return;
            }
            image.setImageBitmap(bmpimage);
            builder.setView(image);
            builder.create().show();

        }


    }
}