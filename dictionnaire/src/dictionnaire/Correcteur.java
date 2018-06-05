package dictionnaire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Correcteur {

	ArrayList<String> mots = new ArrayList<String>();
	public Correcteur () throws IOException  
	{	  	
		BufferedReader lecteurAvecBuffer = null;
	    String ligne;
	    System.out.println(System.getProperty("file.encoding"));
	
	    try
	      {
	    	File fileDir = new File("mots.txt");
	    	lecteurAvecBuffer =new BufferedReader(
				   new InputStreamReader(
		           new FileInputStream(fileDir), "UTF-8"));
	      }
	    catch(FileNotFoundException exc)
	      {
		System.out.println("Erreur d'ouverture");
	      }
	    while ((ligne = lecteurAvecBuffer.readLine()) != null)
	    {
	    	mots.add(ligne);
	    }
	    lecteurAvecBuffer.close();
	    mots.sort(null);
    } 
	
	public ArrayList<String> testerMot(String motTest) throws IOException
	{
		int i = 0 ;
		ArrayList<String> motProbable = new ArrayList<String> () ;
	    while (i < mots.size())
	    {
	    		if (mots.get(i).equals(motTest)) {motProbable.clear();motProbable.add("Existe");break;}
	    		if (LevenshteinDistance(motTest,mots.get(i)) <= 2)	   		
	    		if (motTest.substring(0,2).equals(mots.get(i).substring(0,2)))
	    			motProbable.add(mots.get(i)) ; 
	    		i++ ;
	    }
	    i=0 ;
	    return motProbable ;
	}
	    
	  
	
	
	public  int LevenshteinDistance(CharSequence lhs, CharSequence rhs)
	{                          
		    int len0 = lhs.length() + 1;                                                     
		    int len1 = rhs.length() + 1;                                                     
		                                                                                    
		    // Tableau qui contiennent les distances                                                       
		    int[] cost = new int[len0];                                                     
		    int[] newcost = new int[len0];                                                  
		                                                                                    
		    for (int i = 0; i < len0; i++) cost[i] = i;                                     
		                                                                                                                                                                    
		    // Parcours des caractéres de la premiere chaine                                   
		    for (int j = 1; j < len1; j++) {                                                
		        newcost[0] = j;                                                             
		                                                                                    
		        // transformation cost for each letter in s0                                
		        for(int i = 1; i < len0; i++) {                                             
		            // Comparaison ds caracteres des deux mots                             
		            int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;             
		                                                                                    
		                                         
		            int cost_replace = cost[i - 1] + match;                                 
		            int cost_insert  = cost[i] + 1;                                         
		            int cost_delete  = newcost[i - 1] + 1;                                  
		                                                                                    
		            // On garde le cout minimum entre les deux mots                                                    
		            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
		        }                                                                           
		                                                                                    
		        // Reinitialiser les variables pour le prochain tour                                                 
		        int[] swap = cost; cost = newcost; newcost = swap;                          
		    }                                                                               
		                                                                                    
		    return cost[len0 - 1];                                                          
		}
	

}
