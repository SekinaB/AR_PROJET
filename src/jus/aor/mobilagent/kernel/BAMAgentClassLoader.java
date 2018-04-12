package jus.aor.mobilagent.kernel;

import java.util.Iterator;
import java.util.Map.Entry;

public class BAMAgentClassLoader extends ClassLoader {

	/**
	 * Fichier Jar de l'agent.
	 */
	Jar jar;

	/**
	 * Création du Loader pour l'agent. L'ensemble des classes est contenu dans le jar
	 * 
	 * @param jarName, le nom du fichier jar
	 */
	BAMAgentClassLoader(String jarName, ClassLoader parent) {
		super(parent);
		try {
			integrateCode(new Jar(jarName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Création du Loader pour l'agent sans fichier jar.
	 */
	public BAMAgentClassLoader(ClassLoader parent) {
		super(parent);
	}

	/** 
	 * Définition des classes présentes dans le jar.
	 * @param jar
	 */
	void integrateCode(Jar jar) {
		this.jar = jar;
		Iterator<Entry<String, byte[]>> it = jar.classIterator().iterator();
		while (it.hasNext()) {
			Entry<String, byte[]> next = it.next();
			super.defineClass(className(next.getKey()), next.getValue(), 0, next.getValue().length);
		}
	}


	/**
	 * renvois le jar.
	 */
	Jar extractCode() {
		return jar;
	}
	
	/**
	 * renvois le nom du jar et les noms des classes qu'il contient.
	 */
	public String toString() {
		String string;
		string = jar.toString() + "contains : \n";
		Iterator<Entry<String, byte[]>> it = jar.classIterator().iterator();
		while (it.hasNext()) {
			Entry<String, byte[]> next = it.next();
			string = string + className(next.getKey()) + "\n";
		}
		return string + "END \n";
	}

	/**
	 * remplace le séparateur de niveau par le séparateur de package.
	 * 
	 * @param name le nom physique de la classe
	 * @return le nom logique associé au nom physique de la classe.
	 */
	private String className(String name) {
		// On affiche le nom sans '.class' et en remplassant '/' par '.'
		return name.substring(0, name.length()-6).replace('/', '.');
	}
}
