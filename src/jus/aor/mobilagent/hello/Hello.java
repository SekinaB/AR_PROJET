package jus.aor.mobilagent.hello;

import java.net.URI;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Server;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent{

	private List<URI> feuilleDeRoute;
	private Logger logger;
	
	private static final long serialVersionUID = 1L;
	 /**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public Hello(Object... args) {
		 feuilleDeRoute = new ArrayList<>();
	 }
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	protected _Action doIt = new _Action(){

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			feuilleDeRoute.add(agentServer.site());
		}
		
	};
	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	protected _Action retour(){
		return new _Action(){

			@Override
			public void execute() {
				for (URI server : feuilleDeRoute) {
					
					logger.log(Level.FINE, "The list visited servers " + server.toString());
				}
			}
			
		};
	}
}