//package core.boot;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;

public class Bootstrap {
	public static void main(String[] args) throws Exception {
		File commonDir = new File("src/commons");
		File[] entries = commonDir.listFiles();
		URL[] urls = new URL[entries.length];

		for (int i = 0; i < entries.length; i++) {
			urls[i] = entries[i].toURI().toURL();
		}

		URLClassLoader commonsLoader = new URLClassLoader(urls, null);

		URL binDirURL = new File("bin").toURI().toURL();
		URLClassLoader appLoader = new URLClassLoader(new URL[] {binDirURL}, commonsLoader);

		Class appClass = appLoader.loadClass("Game");
		Object appInstance = appClass.newInstance();
		Method m = appClass.getMethod("start");

	 	m.invoke(appInstance);
	}
}
