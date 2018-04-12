package jus.aor.mobilagent.kernel;

import java.net.URI;
import java.net.URL;

public class Agent implements _Agent {

	public Agent(Object a) {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Route route;

	// Server d'origine
	private AgentServer agentServer;

	// Server courant
	private AgentServer server;
	private String serverName;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (route.hasNext()) {
			// TODO Agent stuff
		}

	}

	@Override
	public void addEtape(Etape etape) {
		route.add(etape);
	}

	@Override
	/*
	 * Déploiement initial => L'agent doit executer une action vide NIHIL pour
	 * uniformisation
	 */
	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		this.agentServer = agentServer;
		this.serverName = serverName;
		route = new Route(new Etape(agentServer.site(), _Action.NIHIL));
	}

	@Override
	public void reInit(AgentServer server, String serverName) {
		this.server = server;
		this.serverName = serverName;
	}

	private void move() {
		// TODO
	}

	protected void move(URL url) {
		// TODO
	}

	public String toString() {
		// TODO
		return null;
	}

	protected String route() {
		return this.route();
	}
}
