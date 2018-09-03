//package core.boot;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;

public class Bootstrap {
	public void boot() throws Exception {
		File commonDir = new File("src/commons");
		File[] entries = commonDir.listFiles();
		URL[] urls = new URL[entries.length];

		for (int i = 0; i < entries.length; i++) {
			urls[i] = entries[i].toURI().toURL();
		}

		URLClassLoader commonsLoader = new URLClassLoader(urls);

		URL binDirURL = new File("bin").toURI().toURL();

		URLClassLoader appLoader = new URLClassLoader(new URL[] {binDirURL}, commonsLoader);

		Class pluginLoaderClass = commonsLoader.loadClass("PluginLoader");
		Object pluginLoaderInstance = pluginLoaderClass.newInstance();
		Method m = pluginLoaderClass.getMethod("loadPlugins");
		m.invoke(pluginLoaderInstance);

		Class appClass = appLoader.loadClass("Game");
		Object appInstance = appClass.newInstance();
		m = appClass.getMethod("start");
	 	m.invoke(appInstance);
	}

	public static void main(String[] args) {
		try {
			new Bootstrap().boot();
		}
		catch (Exception e) {
			System.out.println("Error of booting");
			e.printStackTrace();
		}
	}
}
