package blueberry.engine;

import com.google.gson.annotations.Expose;

import blueberry.json.JsonManager;

public class IdeConfig {

	public static IdeConfig config = new IdeConfig();
	
	@Expose
	private String JdkLocation;

	public String getJdkLocation() {
		return JdkLocation;
	}

	public void setJdkLocation(String jdkLocation) {
		JdkLocation = jdkLocation;
	}
	
	public void save() {
		JsonManager.toJsonFile(System.getProperty("user.dir") + "/config.json", this);
	}
	
	public void load() {
		config = JsonManager.fromJsonFile(System.getProperty("user.dir") + "/config.json", IdeConfig.class);
	}
	
	private IdeConfig() {
		
	}
	
}
