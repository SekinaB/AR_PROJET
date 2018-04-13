/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.kernel.BAMAgentClassLoader;
import jus.aor.mobilagent.kernel._Agent;
import jus.aor.mobilagent.obj6.InSigner;
import jus.aor.mobilagent.obj6._InSigner;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et
 * les fonctions permettant de d�ployer des services et des agents.
 * 
 * @author Morat
 */
public final class Server implements _Server {
	/** le nom logique du serveur */
	protected String name;
	/**
	 * le port où sera atach� le service du bus � agents mobiles. Pafr d�faut on
	 * prendra le port 10140
	 */
	protected int port = 10140;
	/** le server d'agent d�marr� sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger = null;

	/**
	 * D�marre un serveur de type mobilagent
	 * 
	 * @param port
	 *            le port d'�cuote du serveur d'agent
	 * @param name
	 *            le nom du serveur
	 */
	public Server(final int port, final String name) {
		this.name = name;
		try {
			this.port = port;
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/" + InetAddress.getLocalHost().getHostName() + "/" + this.name;
			logger = Logger.getLogger(loggerName);
			/*
			 * d�marrage du server d'agents mobiles attach� � cette machine
			 */
			// A COMPLETER
			agentServer = new AgentServer(port, name);
			new Thread(agentServer).start();
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
		} catch (Exception ex) {
			logger.log(Level.FINE, " erreur durant le lancement du serveur" + this, ex);
			return;
		}
	}

	/**
	 * Ajoute le service caract�ris� par les arguments
	 * 
	 * @param name
	 *            nom du service
	 * @param classeName
	 *            classe du service
	 * @param codeBase
	 *            codebase du service
	 * @param args
	 *            arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			// On va chercher la classe puis le constructeur puis on instancie
			// le service
			agentServer.addService(name,
					(_Service<?>) Class.forName(classeName).getConstructor(String.class).newInstance(args[0]));
		} catch (Exception ex) {
			logger.log(Level.FINE, " erreur durant le lancement du serveur" + this, ex);
			return;
		}
	}

	/**
	 * deploie l'agent caract�ris� par les arguments sur le serveur
	 * 
	 * @param classeName
	 *            classe du service
	 * @param args
	 *            arguments de construction de l'agent
	 * @param codeBase
	 *            codebase du service
	 * @param etapeAddress
	 *            la liste des adresse des �tapes
	 * @param etapeAction
	 *            la liste des actions des �tapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress,
			List<String> etapeAction) {
		try {
			_Agent agent = (_Agent) Class.forName(classeName).getConstructor(String.class).newInstance(args[0]);
			agent.init(agentServer, this.name);

			for (int i = 0; i < etapeAction.size(); i++) {
				Field field = agent.getClass().getDeclaredField(etapeAction.get(i));
				field.setAccessible(true);
				Object value = field.get(agent);
				agent.addEtape(new Etape(new URI(etapeAddress.get(i)), (_Action) value));
			}

			// A COMPLETER en terme de startAgent
		} catch (Exception ex) {
			logger.log(Level.FINE, " erreur durant le lancement du serveur" + this, ex);
			return;
		}
	}

	/**
	 * Primitive permettant de "mover" un agent sur ce serveur en vue de son
	 * ex�cution imm�diate.
	 * 
	 * @param agent
	 *            l'agent devant être ex�cut�
	 * @param loader
	 *            le loader � utiliser pour charger les classes.
	 * @throws Exception
	 */
	protected void startAgent(_Agent agent, BAMAgentClassLoader loader) throws Exception {
		// Ouvrir une socket
		Socket socket;
		try {
			socket = new Socket(agentServer.site().getHost(), agentServer.site().getPort());

			System.out.println(
					this.toString() + " : Connected to " + socket.getInetAddress());

			// Ouvrir un stream out et ObjOut
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			// TODO : enlever ces lignes
			// Completely useless, but I need them for psychological reasons
			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);

			// ClassLoader stuff : page 4 Figure 3
			//BAMAgentClassLoader BAMAcl = (BAMAgentClassLoader) this.getClass().getClassLoader();
			Jar BAMAcljar = loader.extractCode();

			// Send BAMAcljar
			oos.writeObject(BAMAcljar);

			// Send Agent
			oos.writeObject(agent);

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
	public void signIn(String service) {
		try {
			_InSigner InSignerCenter = (InSigner) java.rmi.Naming.lookup("//localhost:2001/InSignerCenter");
			InSignerCenter.signIn(service, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkout(String service) {
		try {
			_InSigner InSignerCenter = (InSigner) java.rmi.Naming.lookup("//localhost:2001/InSignerCenter");
			return InSignerCenter.checkout(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
