package com.example.ecc.projetvesrion10;

/**
 * Created by ECC on 15/11/2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by ECC on 22/10/2015.
 */
public class acssesbase  {
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.ecc.projetvesrion10.rest/tout");
    String[] mProjection = {""};
    String mSelectionClause = null;
    String[] mSelectionArgs = {""};
    String mSortOrder = null;


    public  Boolean  ajouter_restaurant (ContentResolver cs, restaurant rest ){

        ContentValues ligne= new ContentValues();


        ligne.put("nom", rest.getNom());
        ligne.put("adresse",rest.getAdresse());
        ligne.put("numero_de_telephone", rest.getNumero_de_telephone());
        ligne.put("site_internet", rest.getAdresse_de_site_internet());
        ligne.put("note_dappreciation",rest.getNote_dappreciation());
        ligne.put("localisation_geographique", rest.getLocalisation_geographique());
        ligne.put("cout_moyen_du_repas", rest.getCout_moyen_du_repas());
        ligne.put("periode_ouverture",rest.getPeriode_ouverture());
        ligne.put("type_de_cuisine", rest.getType_de_cuisine());
        ligne.put("donnees_multimedia", rest.getDonnees_multimedia());
        ligne.put("avis", rest.getAvis());

        System.out.println("++++++++++++++");
        if(cs.insert(Uri.parse("content://com.example.ecc.projetvesrion10.rest/tout"),ligne)!=null)
        {  System.out.println("insertion baravoo");

            return true;
        }
        else
        {
            System.out.println("nonnnnnne");

            return false;
        }

    }
    public boolean modifier_restaurant(ContentResolver cs,restaurant rest , String uri){

        ContentValues ligne= new ContentValues();


        ligne.put("nom", rest.getNom());
        ligne.put("adresse",rest.getAdresse());
        ligne.put("numero_de_telephone", rest.getNumero_de_telephone());
        ligne.put("site_internet", rest.getAdresse_de_site_internet());
        ligne.put("note_dappreciation",rest.getNote_dappreciation());
        ligne.put("localisation_geographique", rest.getLocalisation_geographique());
        ligne.put("cout_moyen_du_repas", rest.getCout_moyen_du_repas());
        ligne.put("periode_ouverture",rest.getPeriode_ouverture());
        ligne.put("type_de_cuisine", rest.getType_de_cuisine());
        ligne.put("donnees_multimedia", rest.getDonnees_multimedia());

        System.out.println("++++URi:" + uri);
        System.out.println("++++type:"+ligne.get("type_de_cuisine"));
        int kk=  cs.update(Uri.parse(uri), ligne, null, null);
        if(kk!=-1)
        {  System.out.println("modifiaction baravoo  le nombre de ligne modifieé est " + kk);

            return true;
        }
        else
        {
            System.out.println("failed failed");

            return false;
        }


    }

    public Cursor chercherRestaurant(ContentResolver cs, Uri uri){

        //  Uri uri = Uri.parse("content://com.example.ecc.projetvesrion10.rest/recherche_un_seul_critere/type_de_cuisine/kader");

        //Uri uri = Uri.parse("content://com.example.ecc.projetvesrion10.rest/tout");
        Cursor c = cs.query(uri, mProjection, mSelectionClause, mSelectionArgs, mSortOrder);
        if(c!=null) // si c contient bien resultat
        {
            return c;
            // if(c.moveToFirst()) //si c va bien au premier
            //    return c.getString(c.getColumnIndex("nom")); // on retourne l'élément grace a sa column
        } else
        {
            System.out.println("does not exist ");

            return null;
        }

    }
    public Cursor chercher_id_Restaurant(ContentResolver cs, Uri uri){

        System.out.println("avant");

        Cursor c = cs.query(uri, mProjection, mSelectionClause, mSelectionArgs, mSortOrder);

        System.out.println("apres");

        if(c!=null) // si c contient bien resultat
        {

            System.out.println("ID TROUVVé");

            return c;
            // if(c.moveToFirst()) //si c va bien au premier
            //    return c.getString(c.getColumnIndex("nom")); // on retourne l'élément grace a sa column
        } else
        {
            System.out.println("does not exist ");

            return null;
        }

    }

    public boolean supprimer_restaurant(ContentResolver cs, String uri){

        System.out.println("++++++++++++++");
        int kk=  cs.delete(Uri.parse(uri), null, null);
        if(kk!=-1)
        {  System.out.println("delete baravoo  le nombre de ligne supprimée est = " + kk);

            return true;
        }
        else
        {
            System.out.println("delete failed failed");

            return false;
        }


    }

}
