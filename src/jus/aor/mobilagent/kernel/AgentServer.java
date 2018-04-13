package jus.aor.mobilagent.kernel;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

/**
 * Le server qui supporte le mod�le du bus � agents mobiles "mobilagent".
 * Lorsqu'un agent se pr�sente, le serveur charge son codebase et l'objet
 * repr�sentant cet agent, puis il active cet objet qui ex�cute l'action que
 * l'agent a � r�aliser sur ce serveur.
 * 
 * @author Morat
 */
final class AgentServer implements Runnable {
	/** le logger de ce serveur */
	private Logger logger;
	/** La table des services utilisables sur ce serveur */
	private Map<String, _Service<?>> services;
	/** Le port auquel est attach� le serveur */
	private int port;
	/** l'�tat du serveur */
	private boolean running;
	/** la socket de communication du bus */
	private ServerSocket s;
	/** le nom logique de ce serveur */
	private String name;

	/**
	 * L'initialisation du server
	 * 
	 * @param port
	 *            the port o� est attach� le srvice du bus � agents
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

			// On doit toujours �tre � l'�coute tant que agent en vie
			while (running) {
				// Accepter clients
				Socket clientSocket = s.accept();
				logger.log(Level.INFO, "Connection accepted");

				// R�cup�rtion de l'agent
				Agent agent = (Agent) this.getAgent(clientSocket);

				// On ferme la connexion
				clientSocket.close();

				// Je d�marre l'agent si il existe
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
	 * restitue le service de nom name ou null si celui-ci n'est pas attach�
	 * au serveur.
	 * 
	 * @param name
	 * @return le service souhait� ou null
	 */
	_Service<?> getService(String name) {
		return services.get(name);
	}

	/**
	 * restitue l'URI de ce serveur qui est de la forme :
	 * "mobilagent://<host>:<port>" ou null si cette op�ration �choue.
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
			// J'ouvre le stream d'entr�e
			InputStream is = aSocket.getInputStream();
			AgentInputStream ais = new AgentInputStream(is, bamAcl);

			// Recup�ration du Jar
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
 * ObjectInputStream sp�cifique au bus � agents mobiles. Il permet
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
