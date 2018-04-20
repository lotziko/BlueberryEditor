package blueberry.IO;

public class Asset {
	private Object content;
	
	public <T> T getContent(Class<T> type) {
		return type.cast(content);
	}
	
	public <T> Asset(Object content) {
		this.content = content;
	}
}
