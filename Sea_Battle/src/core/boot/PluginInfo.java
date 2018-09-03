//package core.boot;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginInfo {
    private Class pluginClass;

	public PluginInfo(File jarFile) throws Exception {
		try {
			Properties properties = getPluginProperties(jarFile);
			if (properties == null) {
				throw new IllegalArgumentException("No properties file found");
			}
			String pluginClassName = properties.getProperty("main.class");
			if (pluginClassName == null || pluginClassName.length() == 0) {
                throw new Exception("Missing property main.class");
            }

			URL jarURL = jarFile.toURI().toURL();
//			classLoader = new URLClassLoader(new URL[] {jarURL}, getClass().getClassLoader().getParent());
			URLClassLoader classLoader = new URLClassLoader(new URL[] {jarURL}, getClass().getClassLoader());
			pluginClass = classLoader.loadClass(pluginClassName);
		}
		catch (Exception e) {
			throw new Exception(e);
		}
	}

    public Player getNewPluginInstance() {
        try {
        	return (Player) pluginClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	private Properties getPluginProperties(File file) throws IOException {
		Properties result = null;
		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> entries = jar.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().equals("settings.properties")) {
				InputStream is = null;
				try {
					is = jar.getInputStream(entry);
					result = new Properties();
					result.load(is);
				}
				finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
		return result;
	}
}
