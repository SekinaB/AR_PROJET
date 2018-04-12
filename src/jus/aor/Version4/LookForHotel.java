package jus.aor.mobilagent.kernel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.rmi.Hotel;


public class LookForHotel extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Hotel> hotelsLocated;
    private HashMap<Hotel, Numero> listeNumeros;
    protected Logger logger;
	
	
	/** le critère de localisaton choisi */
	private String localisation;
	// ...
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String args[]){
		
		localisation = args[0];
		hotelsLocated = new ArrayList<Hotel>();
		listeNumeros = new HashMap<Hotel,Numero>();
		
	}
	/**
	 * GetHotels search for all the hotels located in a given location and put the result inside the ArrayList 
	 */
	protected _Action GetHotels = new _Action (){

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			
			_Service<?> service = agentServer.getService("Hotels");
			hotelsLocated.add((Hotel) service.call(localisation));
			
			logger.log(Level.FINE, "Recherche des hotels présent dans " + localisation + " sur " + agentServer);
		}
		
	};
	
	/**
	 * For each hotel found in the location, GetNumbers fill the HashMap with the phone numbers of each Hotel
	 */
	protected _Action GetNumbers = new _Action (){

		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			
			_Service<?> service = agentServer.getService("Telephones");
			
			for (Hotel hotel : hotelsLocated) {
				Numero numero = (Numero) service.call(hotel.name);
				listeNumeros.put(hotel, numero);
			}
			
			logger.log(Level.FINE, "Récupération des numeros sur " + agentServer);
			
		}
		
	};
	
	protected void call() {
		
		for (Hotel hotel : hotelsLocated) {
			logger.log(Level.FINE, hotel.name +", Numéro :" + listeNumeros.get(hotel));
			
		}
	}
	
}



