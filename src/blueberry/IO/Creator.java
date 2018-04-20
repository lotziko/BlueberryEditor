package blueberry.IO;

public abstract class Creator<Type> {

	public abstract Type create(String filename, Type data);
	
}
