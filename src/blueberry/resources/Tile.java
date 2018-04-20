package blueberry.resources;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.annotations.Expose;

public class Tile {
	@Expose
	private int ID;
	private Texture image;
	
	public Texture getImage() {
		return image;
	}
	public void setImage(Texture image) {
		this.image = image;
	}
	
	public int getID() {
		return ID;
	}
	
	public static Tile create(int ID, Texture image) {
		return new Tile(ID, image);
	}
	
	private Tile(int ID, Texture image) {
		this.ID = ID;
		this.image = image;
	}
}
