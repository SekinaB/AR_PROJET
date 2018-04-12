package jus.aor.mobilagent.kernel;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;

public class Agent implements _Agent {

	long id;

	public Agent(Object a) {
		// TODO : Arranger ce truc qui sert à rien
		this.id = System.currentTimeMillis();
		System.out.print(this.toString());
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
		// On bouge à la prochaine étape
		move(route.get().server);
	}

	protected void move(URI uri) {
		// Ouvrir une socket
		Socket socket;
		try {
			socket = new Socket(uri.getHost(), uri.getPort());

			System.out.println(
					this.toString() + " : Connected to " + socket.getInetAddress() + " URI path : " + uri.getPath());

			// Ouvrir un stream out et ObjOut
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			// TODO : enlever ces lignes
			// Completely useless, but I need them for psychological reasons
			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			// ClassLoader stuff : page 4 Figure 3
			BAMAgentClassLoader BAMAcl = (BAMAgentClassLoader) this.getClass().getClassLoader();
			Jar BAMAcljar = BAMAcl.extractCode();

			// Send BAMAcljar
			oos.writeObject(BAMAcljar);

			// Send Agent
			oos.writeObject(this);

			// Close sockets
			oos.close();
			os.close();
			
			// TODO : enlever
			ois.close();
			is.close();
			
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		return "Agent : " + this.id;
	}

	protected String route() {
		return this.route();
	}
}
