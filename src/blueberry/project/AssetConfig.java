package blueberry.project;

import com.google.gson.annotations.Expose;

public class AssetConfig {
	@Expose
	private String path;
	@Expose
	private String type;
	
	public <T> Class<?> getType() {
		try {
			return Class.forName(type);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getPath() {
		return path;
	}
	
	public AssetConfig(String path, Class type) {
		this.path = path;
		this.type = type.getName();
	}
}
