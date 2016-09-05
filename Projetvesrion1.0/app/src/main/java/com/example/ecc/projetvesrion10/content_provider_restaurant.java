package com.example.ecc.projetvesrion10;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;

import java.util.List;

public class content_provider_restaurant extends ContentProvider {

    private base   bdd;
    private static final int codecherchertout = 1;
    private static final int codechercher = 2;




    ////  cod uri pour la recherhce
    private static final int coderech= 10; // admin
    ////  cod uri pour la recherhce par id
    private static final int coderech_id= 12; // admin
    ////  cod uri pour la mise a jour
    private static final int codeupdatetous= 11; // admin
    ////  cod uri pour la recherhce par id
    private static final int codedelete_id= 13; // admin
    /****************************uri client*******************/
    private static final int recherche_client_par_nom= 14; // client
    private static final int recherche_client_par_critetre= 15; // client
    private static final int recherche_client_id= 16; // client
    private static final int code_update_client= 17; // client



    public static final String AUTHORITY = "com.example.ecc.projetvesrion10.rest";
    private final static UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
    // static UriMatcher
    static {
        uriMatcher.addURI(AUTHORITY,"tout", codecherchertout);
        uriMatcher.addURI(AUTHORITY,"mot_theme/*/*",codechercher);
        // uti matcher de recherche par critère
        uriMatcher.addURI(AUTHORITY,"recherche/*/*/*/*/*/*/*/*", coderech);
        /// recherhce par id utilisé dans la mise a jour
        uriMatcher.addURI(AUTHORITY,"recherche_id/id/*", coderech_id);
        // rechrehce par nom utilisé par le client
        uriMatcher.addURI(AUTHORITY,"recherche_client/nom/*", recherche_client_par_nom);
        // rechrehce par plusieurs criteres utilisé par le client
        uriMatcher.addURI(AUTHORITY,"recherche_client_critere/*/*/*/*/*/*/*/*", recherche_client_par_critetre);
        // rechrehce id pour utiliser dans le boutton consulter  utilisé par le client
        uriMatcher.addURI(AUTHORITY,"recherche_client_id/*/*", recherche_client_id);

        // uti matcher de recherche
        uriMatcher.addURI(AUTHORITY,"update/*/*/*/*/*/*/*/*/*/*/*", codeupdatetous); // admin
        uriMatcher.addURI(AUTHORITY,"modifier_client_id/*/*/*/*/*/*", code_update_client); // client

        // uri match pour delete
        uriMatcher.addURI(AUTHORITY,"delete_id/id/*", codedelete_id);



    }
    public content_provider_restaurant() {
    }


    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        //int uriType= uriMatcher.match(uri) ;
        SQLiteDatabase db=  bdd.getWritableDatabase();
        try {
            long id = db.insertOrThrow(bdd.RESTAURANT, null, values);
            if (id!=-1)
                // / db.insert(db.)
                return ContentUris.withAppendedId(uri,id);
            else
                return null;

        } catch(SQLException e)
        {

            System.out.println("element existe");
            return null;

        }


       /* long id=db.insertOrThrow().insert(bdd.RESTAURANT,null,values);
        if (id!=1)
            // / db.insert(db.)
            return ContentUris.withAppendedId(uri,id);
        else
            return null;*/
    }

    @Override
    public boolean onCreate() {
        try{

            bdd= new base(getContext());
        }catch (Exception e){
            return false;
        }
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db=  bdd.getReadableDatabase();
        Cursor c=null;

        int uriType= uriMatcher.match(uri) ;

        System.out.println("hhhhhhhhhhh---------------");
        switch (uriType){
            case (codecherchertout):
                c=db.rawQuery("SELECT * FROM " + bdd.RESTAURANT, null);
                System.out.println("Cursor: "+c.getCount());
                break;

            case(coderech):
                List<String> path = uri.getPathSegments(); //recupère "mot_theme/*/*"
                String champ1 = path.get(1);
                String champ2 = path.get(2);
                String champ3 = path.get(3);
                String champ4 = path.get(4);
                String champ5 = path.get(5);
                String champ6 = path.get(6);
                String champ7 = path.get(7);
                String champ8 = path.get(8);

                /// preparation de la requete
                Boolean premier = true;
                String requete = "SELECT * FROM " + bdd.RESTAURANT  + " WHERE ";
                if (!champ2.matches("_")){
                    if(premier) {
                        requete = requete + " " + champ1 + " = '" + champ2 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ1 +" = '"+ champ2+"'";
                    }
                }
                ///------------------
                if (!champ4.matches("_")) {

                    if(premier) {
                        requete = requete + " " + champ3 + " = '" + champ4 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ3 +" = '"+ champ4+"'";
                    }

                }

                //--------------------------------
                if (!champ6.matches("_")) {

                    if(premier) {
                        requete = requete + " " + champ5 + " >= '" + champ6 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ5 +" >= '"+ champ6+"'";
                    }
                }
                ///-----------------------------
                if (!champ8.matches("_")) {
                    if(premier) {
                        requete = requete + " " + champ7 + " = '" + champ8 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ7 +" = '"+ champ8+"'";
                    }
                }

                System.out.println(requete);
                c=db.rawQuery(requete,null);

                break;
            case(coderech_id):

                String valeur_id = uri.getLastPathSegment();

                System.out.println(" requette SELECT * FROM " + bdd.RESTAURANT + " WHERE "+ " " + bdd.ID + " = '" + valeur_id + "'");

                c=db.rawQuery("SELECT * FROM " + bdd.RESTAURANT + " WHERE " + " " + bdd.ID + " = '" + valeur_id + "'", null);
                System.out.println("Cursor de  requette id: "+c.getCount());
                break;

            case(recherche_client_par_nom):

                String nom = uri.getLastPathSegment();

                System.out.println(" requette SELECT * FROM " + bdd.RESTAURANT + " WHERE "+ " " + bdd.NOM + " = '" + nom + "'");

                c=db.rawQuery("SELECT * FROM " + bdd.RESTAURANT + " WHERE "+ " " + bdd.NOM + " = '" + nom + "'", null);
                System.out.println("Cursor de  requette id client: "+c.getCount());
                break;
            /// recherche multicritere
            case(recherche_client_par_critetre):
                path = uri.getPathSegments(); //recupère "mot_theme/*/*"
                champ1 = path.get(1);
                champ2 = path.get(2);
                champ3 = path.get(3);
                champ4 = path.get(4);
                champ5 = path.get(5);
                champ6 = path.get(6);
                champ7 = path.get(7);
                champ8 = path.get(8);

                /// preparation de la requete
                premier = true;
                requete = "SELECT * FROM " + bdd.RESTAURANT  + " WHERE ";
                if (!champ2.matches("_")){
                    if(premier) {
                        requete = requete + " " + champ1 + " = '" + champ2 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ1 +" = '"+ champ2+"'";
                    }
                }
                ///------------------
                if (!champ4.matches("_")) {

                    if(premier) {
                        requete = requete + " " + champ3 + " = '" + champ4 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ3 +" = '"+ champ4+"'";
                    }

                }

                //--------------------------------
                if (!champ6.matches("_")) {

                    if(premier) {
                        requete = requete + " " + champ5 + " >= '" + champ6 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ5 +" >= '"+ champ6+"'";
                    }
                }
                ///-----------------------------
                if (!champ8.matches("_")) {
                    if(premier) {
                        requete = requete + " " + champ7 + " = '" + champ8 + "'";
                        premier= false;
                    }
                    else{
                        requete = requete+ " AND " + champ7 +" = '"+ champ8+"'";
                    }
                }

                System.out.println(requete);
                c=db.rawQuery(requete,null);

                break;
            case(recherche_client_id):

                valeur_id = uri.getLastPathSegment();
                System.out.println(valeur_id);

                System.out.println(" requette SELECT * FROM " + bdd.RESTAURANT + " WHERE "+ " " + bdd.ID + " = '" + valeur_id + "'");

                c=db.rawQuery("SELECT * FROM " + bdd.RESTAURANT + " WHERE " + " " + bdd.ID + " = '" + valeur_id + "'", null);
                System.out.println(" requette client id: "+c.getCount());
                break;///recherhc
        }
        // TODO: Implement this to handle query requests from clients.
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = bdd.getWritableDatabase();
        int nombre_de_ligne_modifiee = 0;

        switch (uriType) {

            case codeupdatetous:
                List<String> path = uri.getPathSegments();
                String id = path.get(1);
                String segment = uri.getLastPathSegment();
                System.out.println(segment+"      update admin");
                //  nombre_de_ligne_modifiee = db.update(bdd.RESTAURANT, values, selection, selectionArgs);
                nombre_de_ligne_modifiee = db.update(bdd.RESTAURANT,values, bdd.ID + " = " + id,  null);
            break;
            case code_update_client:
                path = uri.getPathSegments();
                id = path.get(2);
                //  nombre_de_ligne_modifiee = db.update(bdd.RESTAURANT, values, selection, selectionArgs);
                System.out.print("provider, avis: " + values.get("avis") + "!");

                nombre_de_ligne_modifiee = db.update(bdd.RESTAURANT,values, bdd.ID + " = " + id,  null);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return nombre_de_ligne_modifiee;
    }




    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = bdd.getWritableDatabase();
        int nombre_de_ligne_supprime = 0;

        switch (uriType) {
            case codedelete_id:

                String id = uri.getLastPathSegment();
                nombre_de_ligne_supprime = db.delete(bdd.RESTAURANT, bdd.ID + " = " + id, null);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return nombre_de_ligne_supprime;
    }
}