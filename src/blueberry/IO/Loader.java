package blueberry.IO;

public abstract class Loader<Type> {
	
	public abstract Type load(String filename);
	
}
