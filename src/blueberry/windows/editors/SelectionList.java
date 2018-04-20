package blueberry.windows.editors;

import java.util.ArrayList;
import java.util.List;

public class SelectionList<Type> {

	private List<Container> objects = new ArrayList<>();
	private int x, y;
	
	public List<Container> getObjects() {
		return objects;
	}
	
	public void clear() {
		this.objects.clear();
	}
	
	public void copy(int x, int y, Type[][] objects) {
		this.x = x;
		this.y = y;
		for(int i = 0; i < objects.length; i++) {
			for(int j = 0; j < objects[i].length; j++) {
				this.objects.add(new Container(i, j, objects[i][j]));
			}
		}
	}
	
	public void paste(int x, int y, Type[][] objects) {
		for(Container container : this.objects) {
			int cellX = x + container.x, cellY = y + container.y;
			if (cellX >= 0 && cellX < objects.length && cellY >= 0 && cellY < objects[0].length) {
				objects[cellX][cellY] = container.object;
			}
		}
	}
	
	public class Container {
		
		private int x, y;
		public Type object;
		
		public Container(int x, int y, Type object) {
			this.x = x;
			this.y = y;
			this.object = object;
		}
		
	}
	
}
