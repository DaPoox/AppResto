package com.example.ecc.projetvesrion10;

/**
 * Created by ECC on 15/11/2015.
 */
public class restaurant {
    private String nom;
    private String adresse;
    private String numero_de_telephone;
    private String adresse_de_site_internet;
    private Integer note_dappreciation;
    private String localisation_geographique ;
    private Integer cout_moyen_du_repas;
    private String periode_ouverture; //(midi en semaine et samedi soir, par exemple) ;
    private String type_de_cuisine; //(végétarien, italien, japonais, . . .) ;
    private String donnees_multimedia;//(photos, commentaires vocaux etc).
    private String avis;


    public restaurant(){ }
    //Constructeur sans avis
    public restaurant(  String nom,  String adresse,  String numero_de_telephone, String adresse_de_site_internet,
                        int note_dappreciation, String localisation_geographique ,int cout_moyen_du_repas,
                        String periode_ouverture, String type_de_cuisine,String donnees_multimedia ){
        this.nom=nom;
        this.adresse=adresse;
        this.numero_de_telephone=numero_de_telephone;
        this.adresse_de_site_internet=adresse_de_site_internet;
        this.note_dappreciation=note_dappreciation;
        this.localisation_geographique=localisation_geographique;
        this.cout_moyen_du_repas=cout_moyen_du_repas;
        this.periode_ouverture=periode_ouverture;
        this.type_de_cuisine=type_de_cuisine;
        this.donnees_multimedia=donnees_multimedia;

    }

    //Constructeur avec avis:
    public restaurant(  String nom,  String adresse,  String numero_de_telephone, String adresse_de_site_internet,
                        int note_dappreciation, String localisation_geographique ,int cout_moyen_du_repas,
                        String periode_ouverture, String type_de_cuisine,String donnees_multimedia,
                        String avis){
        this.nom=nom;
        this.adresse=adresse;
        this.numero_de_telephone=numero_de_telephone;
        this.adresse_de_site_internet=adresse_de_site_internet;
        this.note_dappreciation=note_dappreciation;
        this.localisation_geographique=localisation_geographique;
        this.cout_moyen_du_repas=cout_moyen_du_repas;
        this.periode_ouverture=periode_ouverture;
        this.type_de_cuisine=type_de_cuisine;
        this.donnees_multimedia=donnees_multimedia;
        this.avis = avis;

    }
    public String getNom(){
        return nom;
    }
    public String getAdresse(){
        return adresse;
    }
    public String getNumero_de_telephone(){
        return numero_de_telephone;
    }

    public String getAdresse_de_site_internet(){
        return adresse_de_site_internet;
    }
    public int getNote_dappreciation(){
        return note_dappreciation;
    }
    public String getLocalisation_geographique(){
        return localisation_geographique;
    }

    public int getCout_moyen_du_repas(){
        return cout_moyen_du_repas;
    }
    public String getPeriode_ouverture(){
        return periode_ouverture;
    }
    public String getType_de_cuisine(){
        return type_de_cuisine;
    }

    public String getDonnees_multimedia(){
        return donnees_multimedia;
    }
    public void setDonnees_multimedia(String donnees_multimedia){
        this.donnees_multimedia=donnees_multimedia;
    }
    public void setNom(String nom){
        this.nom=nom;
    }
    public void setAdresse(String adresse){
        this.adresse=adresse;
    }
    public void setAdresse_de_site_internet(String Adresse_de_site_internet){
        this.adresse_de_site_internet=Adresse_de_site_internet;
    }

    public void setNumero_de_telephone(String numero_de_telephone) {
        this.numero_de_telephone = numero_de_telephone;
    }

    public void setCout_moyen_du_repas(int cout_moyen_du_repas) {
        this.cout_moyen_du_repas = cout_moyen_du_repas;
    }

    public void setLocalisation_geographique(String localisation_geographique) {
        this.localisation_geographique = localisation_geographique;
    }

    public void setNote_dappreciation(int note_dappreciation) {
        this.note_dappreciation = note_dappreciation;
    }

    public void setPeriode_ouverture(String periode_ouverture) {
        this.periode_ouverture = periode_ouverture;
    }

    public void setType_de_cuisine(String type_de_cuisine) {
        this.type_de_cuisine = type_de_cuisine;
    }

    public String getAvis(){
        return this.avis;
    }

    public void setAvis(String avis){
        this.avis = avis;
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("nom de restaurant= "+nom + "adresse = "+ adresse+"numéro de tel = "+ numero_de_telephone
                + "coût moyen des repas ="+ cout_moyen_du_repas+ "note d' appreciation ="+note_dappreciation+ "periode_ouverture ="
                +periode_ouverture+"type de cuisine ="+type_de_cuisine+"localisation_geographique ="+localisation_geographique
                + "adresse_de_site_internet" + adresse_de_site_internet);
        return sb.toString();
    }


    public static String codeURL(String url){
        String rslt= "";
        int i=0;
        while(i<url.length()){
            if(url.charAt(i) == '/'){
                rslt += '@';
            }else rslt += url.charAt(i);
            i ++;
        }
        return rslt;
    }

    public static String decodeURL(String url){
        String rslt= "";
        int i=0;
        while(i<url.length()){
            if(url.charAt(i) == '@'){
                rslt += '/';
            }else rslt += url.charAt(i);
            i ++;
        }
        return rslt;
    }
}