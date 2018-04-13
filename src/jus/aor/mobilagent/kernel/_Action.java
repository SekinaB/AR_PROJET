/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;

/**
 * D�finit une action � �x�cuter par un agent.
 * 
 * @author Morat
 */
public interface _Action extends Serializable {
	/** l'action vide */
	public static final _Action NIHIL = new _Action() {
		
		private static final long serialVersionUID = 1L;

		public void execute(){
			System.out.println("NIHIL executed");
		}
		
		public String toString(){
			return "NIHIL";
		}
	};

	/**
	 * Ex�cute l'action
	 */
	public void execute();
}
