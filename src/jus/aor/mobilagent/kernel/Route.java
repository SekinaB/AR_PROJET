/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * D�finit la feuille de route que l'agent va suivre
 * 
 * @author Morat
 */
class Route implements Iterable<Etape>, Serializable {
	private static final long serialVersionUID = 9081294824590167316L;

	/** la liste des �tapes � parcourir autres que la derni�re */
	protected List<Etape> route;

	/**
	 * la derni�re �tape de la feuille de route de l'agent qui d�signe le
	 * serveur de d�part.
	 */
	protected Etape retour;

	/** Indique si la feuille de route est epuisee ou non. */
	protected boolean hasNext;

	/**
	 * Construction d'une route.
	 * 
	 * @param retour
	 *            le server initial et de retour.
	 */
	public Route(Etape retour) {
		route = new LinkedList<Etape>();
		this.retour = retour;
		hasNext = true;
	}

	/**
	 * Ajoute une �tape en fin de route.
	 * 
	 * @param e
	 *            l'�tape � ajouter
	 */
	public void add(Etape e) {
		route.add(route.size(), e);
	}

	/**
	 * Restitue la prochaine �tape ou la derni�re qui est la base de d�part.
	 * 
	 * @return la prochaine �tape.
	 */
	Etape get() throws NoSuchElementException {
		if (route.size() > 0) {
			// Si il y a une etape suivante alors on la recupere
			return route.get(0);
		} else {
			// Sinon on met hasNext a jour et on revoie le depart
			hasNext = false;
			return retour;
		}
	}

	/**
	 * Restitue la prochaine �tape et �limine de la route ou la derni�re qui est
	 * la base de d"part.
	 * 
	 * @return la prochaine �tape.
	 */
	Etape next() throws NoSuchElementException {
		if (route.size() > 0) {
			// Si il y a une etape suivante alors on la recupere en l'elevant de
			// la liste
			return route.remove(0);
		} else {
			// Sinon on met hasNext a jour et on revoie le depart
			hasNext = false;
			return retour;
		}
	}

	/**
	 * Il y a-t-il encore une étape à parcourir.
	 * 
	 * @return vrai si une étape est possible.
	 */
	public boolean hasNext() {
		return hasNext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Etape> iterator() {
		return route.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return route.toString().replaceAll(", ", "->");
	}
}
