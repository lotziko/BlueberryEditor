package blueberry.windows.editors;

import java.util.ArrayList;
import java.util.List;

public class SelectionList<Type> {

	private List<Container> containers = new ArrayList<>();
	private int x, y, selectionX, selectionY, width, height;
	private boolean active = false;

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getSelectionX() {
		return selectionX;
	}

	public int getSelectionY() {
		return selectionY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int size() {
		return containers.size();
	}
	
	public void setSelectionPosition(int selectionX, int selectionY) {
		this.selectionX = selectionX;
		this.selectionY = selectionY;
	}
	
	public void setPosition(int x, int y) {
		if (containers.size() == 0) {
			return;
		}
		Container left = containers.get(0), top = left;
		width = 0;
		height = 0;
		for(Container container : containers) {
			if (container.x < left.x) {
				left = container;
			}
			if (container.y < top.y) {
				top = container;
			}
			if (container.x > width) {
				width = container.x;
			}
			if (container.y > height) {
				height = container.y;
			}
		}
		int xOffset = left.x, yOffset = top.y;
		width -= xOffset;
		height -= yOffset;
		
		for(Container container : containers) {
			container.x += -xOffset + x - width / 2;
			container.y += -yOffset + y - height / 2;
		}
		this.x = x;
		this.y = y;
		/*int xOffset = x - this.x, yOffset = y - this.y;
		for(Container container : containers) {
			container.x += xOffset;
			container.y -= yOffset;
		}
		this.x = x;
		this.y = y;*/
	}
	
	public List<Container> getObjects() {
		return containers;
	}
	
	public void clear() {
		this.containers.clear();
	}
	
	public void copy(int x1, int y1, int x2, int y2, Type[][] objects) {
		int firstX, firstY, secondX, secondY;
		if (x1 < x2) {
			firstX = x1;
			secondX = x2;
		} else {
			firstX = x2;
			secondX = x1;
		}
		if (y1 < y2) {
			firstY = y1;
			secondY = y2;
		} else {
			firstY = y2;
			secondY = y1;
		}
		boolean exists = false;
		for(int i = firstX > 0 ? firstX : 0; i < objects.length && i < secondX; i++) {
			for(int j = firstY > 0 ? firstY : 0; j < objects[i].length && j < secondY; j++) {
				exists = false;
				for(Container container : this.containers) {
					if (container.x == i && container.y == j) {
						exists = true;
					}
				}
				if (!exists && objects[i][j] != null)
				this.containers.add(new Container(i, j, objects[i][j]));
			}
		}
	}
	
	public void paste(int x, int y, Type[][] objects) {
		//List<Container> copy = new ArrayList<>();
		if (this.containers.size() == 0) {
			return;
		}
		/*Container leftContainer = this.containers.get(0), topContainer = leftContainer;
		for(Container container : this.containers) {
			if (container.x < leftContainer.x) {
				leftContainer = container;
			}
			if (container.y < topContainer.y) {
				topContainer = container;
			}
		}
		int xOffset = leftContainer.x, yOffset = topContainer.y;
		for(Container container : this.containers) {
			copy.add(new Container(container.x - xOffset, container.y - yOffset, container.object));
		}
		*/
		setPosition(x, y);
		for(Container container : containers) {
			int objX = /*x +*/ container.x, objY = /* y + */ container.y;
			if (objX >= 0 && objY >= 0 && objX < objects.length && objY < objects[objX].length) {
				objects[objX][objY] = container.object;
			}
		}
	}
	
	public void delete(Type[][] objects) {
		for(Container container : containers) {
			int objX = /*x +*/ container.x, objY = /* y + */ container.y;
			if (objX >= 0 && objY >= 0 && objX < objects.length && objY < objects[objX].length) {
				objects[objX][objY] = null;
			}
		}
	}
	
	public class Container {
		
		private int x, y;
		public Type object;
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public Container(int x, int y, Type object) {
			this.x = x;
			this.y = y;
			this.object = object;
		}
		
	}
	
}
