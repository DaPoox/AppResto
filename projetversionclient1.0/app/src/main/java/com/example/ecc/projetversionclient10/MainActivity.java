package com.example.ecc.projetversionclient10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText EditNom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recupération des champs...
        EditNom = (EditText)findViewById(R.id.EditNomRestaurant);

    }

//********** click bouton recheche:
    public void onClickRecherche(View v){
        //Vérifier si le champs est vide...
        String nom = EditNom.getText().toString();
        if(nom.matches("")){
            Toast.makeText(this, "Nom vide...", Toast.LENGTH_SHORT).show();
            return;
        }
        //Le champs nom n'est pas vide:
        //Création de l'uri:
        String strUri = "content://com.example.ecc.projetvesrion10.rest/recherche_client/nom/"+nom;

        //Envoyer l'uri à l'activité pour afficher le resultat:
        Intent itn = new Intent(this, ResultatRecherche.class);
        itn.putExtra("uri", strUri);
        startActivity(itn);
    }
//*********** click bouton recherche avancée:
    public void onClickRechercheAvancee(View v){
        //Ouvrire l'activité de recherche avancée...
        //Avec intent
        Intent itn = new Intent(this, RechercheAvancee.class);
        startActivity(itn);
    }

    //============================= khatina =========================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
