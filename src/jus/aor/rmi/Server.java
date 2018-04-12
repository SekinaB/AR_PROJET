/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server{
	/**
	   * @param args
	   */
	  public static void main(final String args[]) {
	    
	    int nombre_chaines = 4; 
	    int port = 1234;
	    Registry registry = null;
	  	
	    if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
	    
	    try {
	      for(int i=1;i<=nombre_chaines;i++){
	    	  
	    	  registry = LocateRegistry.createRegistry(port+i);
	    	  	
	    	  	_Chaine newChaine = new Chaine("/Users/amina/Documents/TP/Repository/DataStore/Hotels"+i+".xml");
	    	  	registry.bind("Chaine"+i, newChaine);
	    	  	
	      }
	    } catch (Exception e) {
	      System.out.println("Chaine error: " + e);
	    }
	    
	    try {
	  		
	  		registry = LocateRegistry.createRegistry(port);
	  		
	  		_Annuaire newAnnuaire = new Annuaire("/Users/amina/Documents/TP/Repository/DataStore/Annuaire.xml");
			registry.bind("Annuaire", newAnnuaire);		
			
		} catch (Exception e1) {
			System.out.println("Annuaire error : " + e1);
		}
	    
	  }
}
