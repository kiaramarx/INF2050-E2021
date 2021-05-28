/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atelierjson;

import java.io.IOException;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/* Pour Exemple 5*/
import java.text.DecimalFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author yan
 */
public class AtelierJSON {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
      String jsonString = FileReader.loadFileIntoString
        ("catalogue.json","UTF-8");
      JSONArray catalogue = JSONArray.fromObject(jsonString);
      
      /*Exemple 1*/
      afficherNbrLivres(catalogue);
      
     
      /*Exemple 2*/
      listerTitreSelonDate(catalogue);
      
      
      /*Exemple 3*/
      listerPrixSelonDisponibilite(catalogue);
      
      
      /*Exemple 4*/
      ConvertirObjectToJSON();
      
      
      
       /*Exemple 5 */
      convertirCommandeToJSON(catalogue);
      

    }
    
    
    /**
     * Cette méthode affiche la taille du tableau JSON
     * @param JSONArray le tableau(catalogue) à trouver la taille
     */
    private static void afficherNbrLivres(JSONArray catalogue){
      int taille = catalogue.size();
      System.out.println("Exemple 1:");
      System.out.println("Il y a "+taille+" livres dans le catalogue.");
    }
    
    
    /**
     * Cette méthode affiche les livres parus après 2009
     * @param JSONArray le tableau(catalogue) à afficher les livres trouvés
     */
    private static void listerTitreSelonDate(JSONArray catalogue){
        int taille = catalogue.size();
        int livreTrouvé = 0; 
        JSONObject livre;
        System.out.println("\n"+"Exemple 2:");
        System.out.println("Livres parus après 2009:");
        
        
        for (int i = 0; i < taille; i++){
            
            livre = catalogue.getJSONObject(i);
            
            /*livre.getString("annee").equals("2009")*/
            if(livre.getInt("annee") > 2009) {
                System.out.println(" * "+livre.getString("titre"));
                livreTrouvé ++;
            }
        }
        System.out.println("Il y a "+taille+" livre(s) dans le catalogue, dont "
        +livreTrouvé+" paru(s) en 2009");
         
    }
    
     /**
     * Cette méthode affiche les livres selon leur disponibilité. Si le livre
     * est disponible, le titre et le prix seront affichés.
     * @param JSONArray le tableau(catalogue) à trouver les livres
     */
    private static void listerPrixSelonDisponibilite(JSONArray catalogue) {
        int taille = catalogue.size();
        int nbrLivreDispo = 0;
        JSONObject livre;
        System.out.println("\n"+"Exemple 3:");
        System.out.println("Prix des livres disponibles:");
        
         for (int i = 0; i < taille; i++){
             
             livre = catalogue.getJSONObject(i);
             
             if(livre.getBoolean("disponible")){
             System.out.println(" * " + livre.getString("titre") + ": " +
                     livre.getDouble("prix") + "$");
             nbrLivreDispo++;
             
             }
        }
        System.out.println("Il y a "+nbrLivreDispo+" livre(s) disponible(s)");
    }
    
    /**
     * Cette méthode permet la création d'un objet JSON. Dans ce cas-ci, une
     * facture pour les livres.
     */
    private static void ConvertirObjectToJSON() {
        
        //Premier niveau --> Commande
        JSONObject facture = new JSONObject();
        facture.accumulate("id", "1321033823");
        facture.accumulate("total", 49.9);
        facture.accumulate("date", "11/11/2011");
        facture.accumulate("validation", true);

        //Troisième niveau --> Livre Individuel
        JSONObject livre = new JSONObject();
        livre.accumulate("id", "1");
        livre.accumulate("titre", "Database System Concepts");

        //Deuxième niveau --> Tableau "Livres"
        JSONArray livres = new JSONArray();
        livres.add(livre);

        
        facture.accumulate("livres", livres);

        System.out.println("\n"+"Exemple 4:");
        System.out.println(facture);
        
        
        /*
        try{
        FileWriter.saveStringIntoFile
        ("objet.json", commande.toString());
        }catch (IOException e){
            System.out.println("Erreur lors de la conversion,"
                    + " opération annulée!");
        }
        */
    }
    
    /**
     * Cette méthode permet la création d'une commande JSON. Par la suite,
     * l'utilitaire FileWriter permets la création du fichier JSON contenant
     * l'objet commande.
     */
    private static void convertirCommandeToJSON(JSONArray catalogue){
       int taille = catalogue.size();
       double total = 0.0;
       JSONObject livre;
       //Liste locale des livres
       JSONArray livres = new JSONArray();
       
        for (int i = 0; i < taille; i++) {
            
            livre = catalogue.getJSONObject(i);
            
            if (livre.getDouble("prix") < 100.0) {
                //Prix total
                total += livre.getDouble("prix");
                //On ajoute au tableau livres
                livres.add(livre);
            }
        }
        String montantTotal = formaterPrixTotal(total);   
        String dateDuJour = formaterDateDuJour();
       
        JSONObject order = new JSONObject();
        order.accumulate("id", "1321033823");
        order.accumulate("total", montantTotal);
        order.accumulate("date", dateDuJour);
        order.accumulate("validation", true);
        order.accumulate("livres", livres);
              
        System.out.println("\n"+"Exemple 5:");
        System.out.println(order);
                
        try{
        FileWriter.saveStringIntoFile("commande.json", order.toString());
        }catch (IOException e){
            System.out.println("Erreur lors de la conversion,"
                    + " opération annulée!");
        }           
    }
    /**
     * Cette méthode permet de formater le prix total avec deux nombres décimaux.
     *  @return stringTotal, le montant total, format String.
     */
    private static String formaterPrixTotal(double prix){   
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        String stringTotal = format.format(prix);
        
        return stringTotal;
    }
    
    /**
     * Cette méthode permet de formater la date du jour et la retourner.
     * @return stringDate, la date du jour, format String
     */
    private static String formaterDateDuJour(){
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String stringDate = dateFormat.format(date);;
         
       return stringDate;
    }
}

