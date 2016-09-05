package com.example.ecc.projetversionclient10;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.Rating;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class RechercheAvancee extends AppCompatActivity {

    EditText editNom;
    EditText editType;
    RatingBar editNote;
    EditText editCout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_avancee);
//Récupération des champs de saisie:

        editNom = (EditText) findViewById(R.id.idEditNom);
        editCout = (EditText) findViewById(R.id.idEditCout);
        editType = (EditText) findViewById(R.id.idEditType);
        editNote = (RatingBar) findViewById(R.id.idEditNote);

    }



    //****** Annuler:
    public void onClickAnnuler(View v){
        //Fermer l'activité...
        finish();
    }

    //***** on Click recherche:
    public void onClickLancerRecherche(View v){
        //Vérifier les champs:

        if(editNom.getText().toString().matches("") && editType.toString().matches("")  && editCout.getText().toString().matches("")){
            //Touts les champs sans vides, erreur !!
            Toast.makeText(this, "Touts les champs sont vide!", Toast.LENGTH_SHORT).show();
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
        if(nom.matches("")) strUri =strUri + "nom/_/";
        else  strUri =strUri +"nom/"+nom+"/" ;

        if(type.matches("")) strUri = strUri +"type_de_cuisine/_/";
        else strUri = strUri + "type_de_cuisine/"+type+"/";

        strUri = strUri  + "note_dappreciation/"+note+"/";

        if(cout.matches("")) strUri = strUri +"cout_moyen_du_repas/_/";
        else strUri = strUri +"cout_moyen_du_repas/"+cout+"/";


        // GPS HERE...
        EditText editGPS = (EditText)findViewById(R.id.editDistance);

        Toast.makeText(this, "URI: "+strUri, Toast.LENGTH_LONG).show();

        //Uri ready, open new activity :
        Intent itn = new Intent(this, ResultatRecherche.class);
        itn.putExtra("uri", strUri);
        itn.putExtra("distance", editGPS.getText().toString());
        startActivity(itn);
    }


}
