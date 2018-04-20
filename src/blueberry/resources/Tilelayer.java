package blueberry.resources;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.gson.annotations.Expose;

public class Tilelayer implements Cloneable {

	@Expose
	private int[][] tileIDs;
	@Expose
	String name;
	private Tile[][] tileObjects;
	@Expose
	private int width, height, cellWidth, cellHeight;
	
	/* Defines that tilelayer was converted from IDs to tiles */
	
	private boolean isConverted;
	
	public void setConverted(boolean isConverted) {
		this.isConverted = isConverted;
	}
	
	public boolean isConverted() {
		return isConverted;
	}

	public Tile[][] getTiles() {
		return tileObjects;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void add(int x, int y, Tile tile) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			tileObjects[x][y] = tile;
		}
	}
	
	public void remove(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			tileObjects[x][y] = null;
		}
	}

	public void draw(Batch batch, float x, float y, float scale) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tileObjects[i][height-j-1] != null) {
					Texture image = tileObjects[i][height-j-1].getImage();
					batch.draw(image, x + i * cellWidth * scale, y + j * cellHeight * scale, image.getWidth()*scale, image.getHeight()*scale);
				}
			}
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public void convertTilesToIDs() {
		if (tileObjects != null)
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if (tileObjects[i][j] != null) {
					tileIDs[i][j] = tileObjects[i][j].getID();
				} else {
					tileIDs[i][j] = 0;
				}
				
			}
		}
	}
	
	public void convertIDsToTiles(HashMap<Integer, Tile> dictionary) {
		
		tileObjects = new Tile[width][height];
		
		for (int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				tileObjects[i][j] = dictionary.get(tileIDs[i][j]);
			}
		}
		
		isConverted = true;
	}

	public void resize(int width, int height, int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		int[][] newTileIDs = new int[width][height];
		Tile[][] newTileObjects = new Tile[width][height];
		for(int i = 0; i < Math.min(this.width, width); i++) {
			for(int j = 0; j < Math.min(this.height, height); j++) {
				if (i < this.width && j < this.height) {
					newTileIDs[i][j] = tileIDs[i][j];
					newTileObjects[i][j] = tileObjects[i][j];
				}
			}
		}
		this.width = width;
		this.height = height;
		this.tileIDs = newTileIDs;
		this.tileObjects = newTileObjects;
	}
	
	public Tilelayer(String name, int width, int height, int cellWidth, int cellHeight) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;

		tileIDs = new int[width][height];
		tileObjects = new Tile[width][height];
	}

	@Override
	public String toString() {
		return name;
	}
	
}
