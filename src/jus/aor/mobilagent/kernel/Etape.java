/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.net.URI;

/**
 * D�finit une �tape de la feuille de route du parcours d'un agent.
 * 
 * @author Morat
 */
public class Etape implements Serializable {
	private static final long serialVersionUID = 4102055378099993883L;
	/** l'adresse du serveur de l'�tape */
	protected URI server;
	/** l'action � r�aliser � cette �tape */
	protected _Action action;

	/**
	 * Cr�ation d'une �tape � partir d'une adresse de serveur et d'une
	 * action.
	 * 
	 * @param server
	 *            le serveur de l'�tape
	 * @param action
	 *            l'action � ex�cuter
	 */
	public Etape(URI server, _Action action) {
		this.server = server;
		this.action = action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return server + "(" + action + ")";
	}
}
