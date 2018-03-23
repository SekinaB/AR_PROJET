package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;

// Class finished
public class BAMServerClassLoader extends URLClassLoader {

	public BAMServerClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public void addURL(URL url){
		super.addURL(url);
	}
}
