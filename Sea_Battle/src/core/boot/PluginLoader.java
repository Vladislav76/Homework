//package core.boot;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;

public class PluginLoader {
	public static HashMap<String, PluginInfo> plugins;

	public void loadPlugins() {
		File pluginDir = new File("src/plugins");
		File[] jars = pluginDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && file.getName().endsWith(".jar");
			}
		});

		plugins = new HashMap<>();

		for (File file : jars) {
			try {
				plugins.put(file.getName(), new PluginInfo(file));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Plugins are loaded: " + jars.length);
	}
}
