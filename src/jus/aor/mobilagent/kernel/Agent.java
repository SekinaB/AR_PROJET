package jus.aor.mobilagent.kernel;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;

public class Agent implements _Agent {

	// long id;
	//
	// public Agent(Object a) {
	// this.id = System.currentTimeMillis();
	// System.out.print(this.toString());
	// }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// La route � parcourir de l'agent
	private Route route;

	// Origine de l'agent
	public AgentServer agentServer;

	// Server courant
	private transient AgentServer server;
	private transient String serverName;

	public Agent(Object... args) {
	}

	@Override
	public void run() {
		// Recuperation de l'etape actuelle
		Etape etape = route.next();

		// Execution l'action associee
		etape.action.execute();

		// Deplacement vers le prochaine server si il y en a un
		if (route.hasNext()) {
			try {
				move();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		} else {
			// Sinon l'agent a termine son travail
			agentServer.stop();
		}
	}

	@Override
	public void addEtape(Etape etape) {
		route.add(etape);
	}

	/**
	 * Initialise l'agent lors de son deploiement initial dans le bus � agents
	 * mobiles.
	 * 
	 * @param agentServer
	 *            le serveur hebergeant initialement l'agent.
	 * @param serverName
	 *            le nom logique du serveur d'agent
	 */
	@Override
	/*
	 * Deploiement initial => L'agent doit executer une action vide NIHIL pour
	 * uniformisation
	 */
	public void init(AgentServer agentServer, String serverName) {
		this.agentServer = agentServer;
		this.serverName = serverName;
		route = new Route(new Etape(agentServer.site(), _Action.NIHIL));
	}

	/**
	 * Initialise l'agent lors de son deploiement sur un des serveurs du bus.
	 * 
	 * @param server
	 *            le server actuel pour cet agent
	 * @param serverName
	 *            le nom logique du serveur d'agent
	 * @throws UnknownHostException
	 */
	@Override
	public void reInit(AgentServer server, String serverName) {
		this.server = server;
		this.serverName = serverName;
	}

	private void move() throws Exception {
		// Deplacement vers la prochaine etape
		move(route.get().server);
	}

	protected void move(URI uri) throws Exception {
		// Ouvrir une socket
		Socket socket;
		socket = new Socket(uri.getHost(), uri.getPort());

		System.out.println(
				this.toString() + " : Connected to " + socket.getInetAddress() + " URI path : " + uri.getPath());

		// Ouvrir un stream out et ObjOut
		OutputStream os = socket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		// Creation du jar
		BAMAgentClassLoader bamAcl = (BAMAgentClassLoader) this.getClass().getClassLoader();
		Jar jar = bamAcl.extractCode();

		// Send le jar de l'agent
		oos.writeObject(jar);

		// Send Agent
		oos.writeObject(this);

		// Close output streams and sockets
		oos.close();
		os.close();
		socket.close();
	}

	public String toString() {
		return "Agent in " + serverName;
	}

	protected String route() {
		return this.route().toString();
	}
}
