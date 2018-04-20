package blueberry.resources;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.annotations.Expose;

public class Tileset {

	@Expose
	private int size;
	@Expose
	private List<Tile> tiles = new ArrayList<>();
	@Expose
	private int ID, counter = 0;
	
	public Texture[] getFrames() {
		Texture[] result = new Texture[size];
		for(int i = 0; i < size; i++) {
			result[i] = tiles.get(i).getImage();
		}
		return result;
	}
	
	public List<Tile> getTiles() {
		return tiles;
	}
	
	public int getSize() {
		return size;
	}
	
	public void addTile(Texture image) {
		tiles.add(Tile.create(ID + ++counter, image));
		size++;
	}
	
	public void removeTile(Tile tile) {
		tiles.remove(tile);
		size--;
	}
	
	public Tileset(int ID, Texture... frames) {
		this.ID = ID;
		for (Texture texture : frames) {
			addTile(texture);
		}
	}
	
	public Tileset(Tileset tileset) {
		this(tileset.ID, tileset.getFrames());
	}
}
