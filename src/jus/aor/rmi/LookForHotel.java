package jus.aor.rmi;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jus.aor.rmi.Numero;
import jus.aor.rmi._Annuaire;
import jus.aor.rmi._Chaine;

/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.
 * @author  Morat
 */
public class LookForHotel{
	/** le critère de localisaton choisi */
	private String localisation;
	
	int nombre_chaines = 4; 
    int port = 1234;
    private List<Hotel> hotelsLocated;
	// ...
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String args[]){
		
		
		if (args.length>1){
		      System.out.println("Il faut rentrer une seule localisation");
		      System.exit(1);
		}
		
		else if (args.length==0){
		      System.out.println("Rentrer une localisation");
		      System.exit(1);
		}
		
		else {
			localisation = args[0];
			hotelsLocated = new ArrayList<Hotel>();
		}
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
	}
	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws NotBoundException 
	 * @throws RemoteException
	 */
	public long call() throws RemoteException, NotBoundException {
		Registry registry = null;
		Numero numero;
		for(int i=1;i<=nombre_chaines;i++){
			registry = LocateRegistry.getRegistry(port+i);
			_Chaine chaine = (_Chaine) registry.lookup("Chaine"+i);
			hotelsLocated.addAll(chaine.get(this.localisation));
		}
		
		
		registry = LocateRegistry.getRegistry(port);
		_Annuaire annuaire = (_Annuaire) registry.lookup("Annuaire");
	
		for (Hotel hotel : hotelsLocated) {
			numero = annuaire.get(hotel.name);
			System.out.println(hotel.name +", Numéro :" + numero.toString());
			
		}
		return nombre_chaines;
		
	}
	
	 public static void main(final String args[]) throws MalformedURLException, RemoteException, NotBoundException {
		 LookForHotel client = new LookForHotel(args);
		 client.call();
	 }

	
}
