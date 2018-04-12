package jus.aor.mobilagent.kernel;

import java.net.UnknownHostException;

public class Agent implements _Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Route route;

	public Agent(Object... args){
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addEtape(Etape etape) {
		route.add(etape);
	}

	/**
	 * Initialise l'agent lors de son déploiement initial dans le bus à agents mobiles.
	 * @param agentServer le serveur hébergeant initialement l'agent.
	 * @param serverName le nom logique du serveur d'agent
	 */
	@Override
	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		route = new Route(new Etape(agentServer.site(), _Action.NIHIL));
	}

	/**
	 * Initialise l'agent lors de son déploiement sur un des serveurs du bus.
	 * @param server le server actuel pour cet agent
	 * @param serverName le nom logique du serveur d'agent
	 * @throws UnknownHostException 
	 */
	@Override
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		Etape next = route.next();
		
	}

}
