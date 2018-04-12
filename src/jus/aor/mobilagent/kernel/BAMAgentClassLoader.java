package jus.aor.mobilagent.kernel;

import java.util.Iterator;
import java.util.Map.Entry;

public class BAMAgentClassLoader extends ClassLoader {

	BAMAgentClassLoader(String jarName, ClassLoader parent) {
		try {
			Jar jar = new Jar(jarName);
			Iterator<Entry<String, byte[]>> it = jar.classIterator().iterator();
			while (it.hasNext()) {
				Entry<String, byte[]> next = it.next();
				super.defineClass(next.getKey(), next.getValue(), 0, next.getValue().length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void integrateCode(Jar jar) {
		// TODO
	}

	Jar extractCode() {
		// TODO
		return null;
	}

	public String toString() {
		// TODO
		return null;
	}

	private String className(String arg1) {
		// TODO
		return null;
	}
}
