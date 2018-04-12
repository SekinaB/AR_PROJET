package jus.aor.mobilagent.kernel;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

/**
<<<<<<< HEAD
 * Le server qui supporte le modèle du bus à agents mobiles "mobilagent".
 * Lorsqu'un agent se présente, le serveur charge son codebase et l'objet
 * représentant cet agent, puis il active cet objet qui exécute l'action que
 * l'agent a à réaliser sur ce serveur.
=======
 * Le server qui supporte le modï¿½le du bus ï¿½ agents mobiles "mobilagent".
 * Lorsqu'un agent se prï¿½sente, le serveur charge son codebase et l'objet
 * reprï¿½sentant cet agent, puis il active cet objet qui exï¿½cute l'action que
 * l'agent a ï¿½ rï¿½aliser sur ce serveur.
>>>>>>> branch 'Objectif3' of https://github.com/SekinaB/AR_PROJET
 * 
 * @author Morat
 */
final class AgentServer {
	/** le logger de ce serveur */
	private Logger logger;
	/** La table des services utilisables sur ce serveur */
	private Map<String, _Service<?>> services;
	/** Le port auquel est attaché le serveur */
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
<<<<<<< HEAD
	 *            the port où est attaché le srvice du bus à agents mobiles
=======
	 *            the port oï¿½ est attachï¿½ le srvice du bus ï¿½ agents mobiles
>>>>>>> branch 'Objectif3' of https://github.com/SekinaB/AR_PROJET
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
	void run() throws IOException, ClassNotFoundException {
		running = true;
		s = new ServerSocket(this.port);
		while (true) {
			// Accepter clients
			Socket clientSocket = s.accept();
			// Some stuff
		}
		// A COMPLETER
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
	 * restitue le service de nom name ou null si celui-ci n'est pas attaché au
	 * serveur.
	 * @param name
	 * @return le service souhaitï¿½ ou null
	 */
	_Service<?> getService(String name) {
		return services.get(name);
	}

	/**
	 * restitue l'URI de ce serveur qui est de la forme :
	 * "mobilagent://<host>:<port>" ou null si cette opération échoue.
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
		// TODO
		return null;
	}
}

/**
<<<<<<< HEAD
 * ObjectInputStream spécifique au bus à agents mobiles. Il permet d'utiliser le
=======
 * ObjectInputStream spï¿½cifique au bus ï¿½ agents mobiles. Il permet d'utiliser le
>>>>>>> branch 'Objectif3' of https://github.com/SekinaB/AR_PROJET
 * loader de l'agent.
 * 
 * @author Morat
 */
class AgentInputStream extends ObjectInputStream {
	/**
	 * le classLoader à utiliser
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
