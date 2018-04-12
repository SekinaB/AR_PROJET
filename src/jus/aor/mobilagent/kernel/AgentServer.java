package jus.aor.mobilagent.kernel;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

/**
 * <<<<<<< HEAD Le server qui supporte le mod�le du bus � agents mobiles
 * "mobilagent". Lorsqu'un agent se pr�sente, le serveur charge son codebase et
 * l'objet repr�sentant cet agent, puis il active cet objet qui ex�cute l'action
 * que l'agent a � r�aliser sur ce serveur. ======= Le server qui supporte le
 * mod�le du bus � agents mobiles "mobilagent". Lorsqu'un agent se pr�sente, le
 * serveur charge son codebase et l'objet repr�sentant cet agent, puis il active
 * cet objet qui ex�cute l'action que l'agent a � r�aliser sur ce serveur.
 * >>>>>>> branch 'Objectif3' of https://github.com/SekinaB/AR_PROJET
 * 
 * @author Morat
 */
final class AgentServer {
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
	 *            <<<<<<< HEAD the port o� est attach� le srvice du bus � agents
	 *            mobiles ======= the port o� est attach� le srvice du bus �
	 *            agents mobiles >>>>>>> branch 'Objectif3' of
	 *            https://github.com/SekinaB/AR_PROJET
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

		// On doit toujours être à l'écoute
		// TODO : correct this stuff I'm sure it's wrong
		while (true) {
			// Accepter clients
			Socket clientSocket = s.accept();

			// Je choppe l'agent
			Agent agent = (Agent) this.getAgent(clientSocket);

			// Je démarre l'agent
			agent.run();

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
	 * restitue le service de nom name ou null si celui-ci n'est pas attach� au
	 * serveur.
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
		BAMAgentClassLoader BAMAcl = new BAMAgentClassLoader(getClass().getClassLoader());
		// TODO
		// J'ouvre le stream d'entrée
		InputStream is;
		try {
			is = aSocket.getInputStream();
			AgentInputStream ois = new AgentInputStream(is, null);

			// Je choppe le Jar
			Object obj = ois.readObject();
			if (obj.getClass().getSimpleName() == "Jar") {
				// Give it the right type
				Jar BAMAcljar = (Jar) obj;
				// Integrate the code in the new class loader
				BAMAcl.integrateCode(BAMAcljar);
				// Read what's next
				obj = ois.readObject();
			}

			// Je choppe l'agent
			if (obj.getClass().getSimpleName() == "_Agent") {
				// Give it the right type
				_Agent agent = (_Agent) obj;

				// Initialisation de l'agent
				agent.reInit(this, name);

				return agent;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

/**
 * <<<<<<< HEAD ObjectInputStream sp�cifique au bus � agents mobiles. Il permet
 * d'utiliser le ======= ObjectInputStream sp�cifique au bus � agents mobiles.
 * Il permet d'utiliser le >>>>>>> branch 'Objectif3' of
 * https://github.com/SekinaB/AR_PROJET loader de l'agent.
 * 
 * @author Morat
 */
class AgentInputStream extends ObjectInputStream {
	/**
	 * le classLoader � utiliser
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
