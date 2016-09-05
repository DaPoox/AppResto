package com.example.ecc.projetvesrion10;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ECC on 22/10/2015.
 */
public class base extends SQLiteOpenHelper {
    /// nom bdd
    public static final String BD_NAME="retaurant.db";
    /// nom table
    public static final String RESTAURANT="restaurant";
    // les attributs
    public static final String ID = "_id";
    public static final String NOM="nom";
    public static final String ADRESSE="adresse";
    public static final String NUMERO_DE_TELEPHONE="numero_de_telephone";
    public static final String ADRESSE_DE_SITE_INTERNET="site_internet";
    public static final String NOTE_DAPPRECIATION="note_dappreciation";
    public static final String LOCALISATION_GEOGRAPHIQUE ="localisation_geographique";
    public static final String COUT_MOYEN_DU_REPAS="cout_moyen_du_repas";
    public static final String PERIODE_OVERTURE="periode_ouverture";
    public static final String TYPE_DE_CUISINE="type_de_cuisine";
    public static final String DONNEES_MULTIMEDIA="donnees_multimedia";
    public static final String AVIS = "avis";


    private static int VERSION=1;
    //private static  final String CREATE_VOACAB =" create_table "+VOCAB+ "( "+ MOT_FRANCAIS+ " string_not_null" +
    //     MOT_ANGLAIS+ " string_not_null"+ THEME+ " string_not_null"+ NIVEAU + " INTEGER"+ ")";
    @Override
    public void onCreate(SQLiteDatabase db) {
        /// creation de la bdd
        String CREATE_RESTAURANT =
                "create table " + RESTAURANT + "( " +
                        ID + " INTEGER PRIMARY KEY, "+
                        NOM + " String not null, " +
                        ADRESSE + " String not null," +
                        NUMERO_DE_TELEPHONE  + " Integer, "+
                        ADRESSE_DE_SITE_INTERNET + " String ," +
                        NOTE_DAPPRECIATION + " Integer, "+
                        LOCALISATION_GEOGRAPHIQUE + " String ," +
                        COUT_MOYEN_DU_REPAS + " String ," +
                        PERIODE_OVERTURE + " String ," +
                        TYPE_DE_CUISINE + " String not null," +
                        DONNEES_MULTIMEDIA+ " String,"+
                        AVIS+" String );";




        db.execSQL(CREATE_RESTAURANT);
        db.execSQL("INSERT INTO " +RESTAURANT+ "( "+ NOM +", " + ADRESSE+", " + NUMERO_DE_TELEPHONE+", "+ ADRESSE_DE_SITE_INTERNET+", "+
                NOTE_DAPPRECIATION +", " +LOCALISATION_GEOGRAPHIQUE +", " +COUT_MOYEN_DU_REPAS+", " +PERIODE_OVERTURE+", "+
                TYPE_DE_CUISINE+", "+DONNEES_MULTIMEDIA+", "+ AVIS+" )"+
                "VALUES('l''age d''or', '26 Rue du Dr Magnan, 75013 Paris', ' 0145851058', 'http://www.lagedorparis.com'," +
                " '4', '48.826508, 2.360447', '20', 'ouvre à 9h', 'francaise'," +
                " 'http://www.lagedorparis.com/img/pagephotoslieu/bar01.gif'," +
                "'Excellent rapport qualité et prix du brunch avec des serveurs très souriants' );");

        db.execSQL("INSERT INTO " +RESTAURANT+ "( "+ NOM +", " + ADRESSE+", " + NUMERO_DE_TELEPHONE+", "+ ADRESSE_DE_SITE_INTERNET+", "+
                NOTE_DAPPRECIATION +", " +LOCALISATION_GEOGRAPHIQUE +", " +COUT_MOYEN_DU_REPAS+", " +PERIODE_OVERTURE+", "+
                TYPE_DE_CUISINE+", "+DONNEES_MULTIMEDIA+", "+ AVIS+" )"+
                "VALUES('mondol kiri', '59 Avenue de Choisy, 75013 Paris', ' 0153797596', 'http://www.mondolkiri.fr'," +
                " '4', '48.828429, 2.359889', '25', 'Ouverture : Mardi au dimanche" +
                "de 12h00 à 14h30 et 19h00 à 23h00', 'francaise'," +
                " 'http://mondolkiri.fr/image/logo.jpg'," +
                "'Je goûte et je découvre une farce, mélange de porc et de crevette.' );");

        db.execSQL("INSERT INTO " +RESTAURANT+ "( "+ NOM +", " + ADRESSE+", " + NUMERO_DE_TELEPHONE+", "+ ADRESSE_DE_SITE_INTERNET+", "+
                NOTE_DAPPRECIATION +", " +LOCALISATION_GEOGRAPHIQUE +", " +COUT_MOYEN_DU_REPAS+", " +PERIODE_OVERTURE+", "+
                TYPE_DE_CUISINE+", "+DONNEES_MULTIMEDIA+", "+ AVIS+" )"+
                "VALUES('chinatown olympiades', '44 Avenue Ivry, 75013 Paris', ' 0145847221', 'http://www.chinatownolympiades.com'," +
                " '3', '48.823598, 2.366284', '25', 'Ouverture : 11:45 – 14:45  18:45 – 01:00', 'chinoise'," +
                " 'http://mondolkiri.fr/image/logo.jpg'," +
                "'Un resto chinois qui ne sait même pas faire du riz, c''est ouf' );");


    }
    // constrecteur
    public base(Context context  ){
        super(context, BD_NAME, null, VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion> oldVersion){
            db.execSQL("drop_table_if exist"+RESTAURANT);
            onCreate(db);
        }
    }


}