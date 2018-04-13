package jus.aor.mobilagent.kernel;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

/**
 * Le server qui supporte le modï¿½le du bus ï¿½ agents mobiles "mobilagent".
 * Lorsqu'un agent se prï¿½sente, le serveur charge son codebase et l'objet
 * reprï¿½sentant cet agent, puis il active cet objet qui exï¿½cute l'action que
 * l'agent a ï¿½ rï¿½aliser sur ce serveur.
 * 
 * @author Morat
 */
final class AgentServer implements Runnable {
	/** le logger de ce serveur */
	private Logger logger;
	/** La table des services utilisables sur ce serveur */
	private Map<String, _Service<?>> services;
	/** Le port auquel est attachï¿½ le serveur */
	private int port;
	/** l'ï¿½tat du serveur */
	private boolean running;
	/** la socket de communication du bus */
	private ServerSocket s;
	/** le nom logique de ce serveur */
	private String name;

	/**
	 * L'initialisation du server
	 * 
	 * @param port
	 *            the port oï¿½ est attachï¿½ le srvice du bus ï¿½ agents
	 *            mobiles
	 * @param name
	 *            le nom du serveur
	 * @throws Exception
	 *             any exception
	 */
	AgentServer(int port, String name) throws Exception {
		this.name = name;
		logger = Logger.getLogger("jus.aor.mobilagent." + InetAddress.getLocalHost().getHostName() + "." + name);
		this.port = port;
		services = new HashMap<String, _Service<?>>();
		s = new ServerSocket(port);
	}

	/**
	 * le lancement du serveur
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void run() {
		running = true;
		try {

			// On doit toujours être à l'écoute tant que agent en vie
			while (running) {
				// Accepter clients
				Socket clientSocket = s.accept();
				logger.log(Level.INFO, "Connection accepted");

				// Récupértion de l'agent
				Agent agent = (Agent) this.getAgent(clientSocket);

				// On ferme la connexion
				clientSocket.close();

				// Je démarre l'agent si il existe
				if (agent != null) {
					logger.log(Level.INFO, "Agent launched");
					new Thread(agent).start();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/**
	 * Arrete l'execution de l'agent
	 */
	public void stop() {
		this.running = false;
	}

	/**
	 * ajoute un service au serveur
	 * 
	 * @param name
	 *            le nom du service
	 * @param service
	 *            le service
	 */
	void addService(String name, _Service<?> service) {
		services.put(name, service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return "mobilagent://" + InetAddress.getLocalHost().getHostName() + ":" + port;
		} catch (UnknownHostException e) {
			return "mobilagent://";
		}
	}

	/**
	 * restitue le service de nom name ou null si celui-ci n'est pas attachï¿½
	 * au serveur.
	 * 
	 * @param name
	 * @return le service souhaitï¿½ ou null
	 */
	_Service<?> getService(String name) {
		return services.get(name);
	}

	/**
	 * restitue l'URI de ce serveur qui est de la forme :
	 * "mobilagent://<host>:<port>" ou null si cette opï¿½ration ï¿½choue.
	 * 
	 * @return l'URI du serveur
	 */
	URI site() {
		try {
			return new URI("mobilagent://" + InetAddress.getLocalHost().getHostName() + ":" + port);
		} catch (Exception e) {
			return null;
		}
	}

	private _Agent getAgent(Socket aSocket) {
		_Agent agent = null;
		BAMAgentClassLoader bamAcl = new BAMAgentClassLoader(getClass().getClassLoader());

		try {
			// J'ouvre le stream d'entrée
			InputStream is = aSocket.getInputStream();
			AgentInputStream ais = new AgentInputStream(is, bamAcl);

			// Recupération du Jar
			Object obj = ais.readObject();

			// On test si c'est bien le Jar
			if (obj.getClass().getSimpleName() == "Jar") {
				// Integrate the code in the class loader
				bamAcl.integrateCode((Jar) obj);
				logger.log(Level.INFO, "Jar ok !");
				// Read what's next : l'agent
				obj = ais.readObject();
			}

			// On test si c'est bien l'agent
			if (obj.getClass().getSimpleName() == "_Agent") {
				agent = (_Agent) obj;
				// Initialisation de l'agent
				agent.reInit(this, name);
				logger.log(Level.INFO, "Agent ok !");
			}
			ais.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return agent;

	}
}

/**
 * ObjectInputStream spï¿½cifique au bus ï¿½ agents mobiles. Il permet
 * d'utiliser le loader de l'agent.
 * 
 * @author Morat
 */
class AgentInputStream extends ObjectInputStream {
	/**
	 * le classLoader a utiliser
	 */
	BAMAgentClassLoader loader;

	AgentInputStream(InputStream is, BAMAgentClassLoader cl) throws IOException {
		super(is);
		loader = cl;
	}

	protected Class<?> resolveClass(ObjectStreamClass cl) throws IOException, ClassNotFoundException {
		return loader.loadClass(cl.getName());
	}
}
