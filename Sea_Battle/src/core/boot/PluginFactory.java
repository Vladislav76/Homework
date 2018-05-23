//package core.boot;

import java.util.HashMap;

public class PluginFactory {
	public static final String RANDOM_AI = "RandomAI.jar";

	private HashMap<String, PluginInfo> map;

	public PluginFactory(HashMap<String, PluginInfo> map) {
		this.map = map;
	}

	public Player instanceAI(String typeAI) {
		PluginInfo pluginInfo = map.get(typeAI);
		try {
			return pluginInfo.getNewPluginInstance();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}