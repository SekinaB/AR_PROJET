package jus.aor.mobilagent.kernel;

import java.net.UnknownHostException;

/**
 * Description d'un agent du mod�le de bus � agents mobiles "BAM". Le constructeur d'un agent devra avoir la signature suivante : <bold>public XXXX(Object...)</bold>
 * @author  P.Morat
 */
public interface _Agent extends java.io.Serializable, Runnable {
	/**
	 * Initialise l'agent lors de son d�ploiement initial dans le bus � agents mobiles.
	 * @param agentServer le serveur h�bergeant initialement l'agent.
	 * @param serverName le nom logique du serveur d'agent
	 */
	public void init(AgentServer agentServer, String serverName);

	/**
	 * Initialise l'agent lors de son d�ploiement sur un des serveurs du bus.
	 * @param server le server actuel pour cet agent
	 * @param serverName le nom logique du serveur d'agent
	 * @throws UnknownHostException 
	 */
	public void reInit(AgentServer server, String serverName);

	/**
	 * ajoute une �tape en fin de la feuille de route de l'agent.
	 * @param etape l'�tape � ajouter
	 */
	public void addEtape(Etape etape);
}
