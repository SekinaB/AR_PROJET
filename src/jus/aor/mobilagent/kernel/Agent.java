package jus.aor.mobilagent.kernel;

public class Agent implements _Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Route route;

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEtape(Etape etape) {
		route.add(etape);
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		
	}

}
