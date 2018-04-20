package blueberry.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.gson.annotations.Expose;

import blueberry.project.Project;

public class Tilemap {

	@Expose
	private int width, height, cellWidth, cellHeight;

	@Expose
	private List<Tilelayer> layers;
	private Tilelayer currentLayer;
	
	public int getCellWidth() {
		return cellWidth;
	}

	public int getWidth() {
		return width;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public int getHeight() {
		return height;
	}
	
	public void resize(int width, int height, int cellWidth, int cellHeight) {
		for (Tilelayer tilelayer : layers) {
			tilelayer.resize(width, height, cellWidth, cellHeight);
		}
		this.width = width;
		this.height = height;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}
	
	public Tilelayer getCurrentLayer() {
		return currentLayer;
	}
	
	public List<Tilelayer> getTilelayers() {
		return layers;
	}

	public void convertTilesToIDs() {
		for (Tilelayer tileLayer : layers) {
			tileLayer.convertTilesToIDs();
			tileLayer.setConverted(false);
		}
	}
	
	public void convertIDsToTiles() {
		
		List<Tileset> dictionary = Project.project.getAssetsOfType(Tileset.class);
		HashMap<Integer, Tile> tiles = new HashMap<>();
		tiles.put(0, null);
		if (dictionary != null)
		for (Tileset tileset : dictionary) {
			for (Tile tile : tileset.getTiles()) {
				tiles.put(tile.getID(), tile);
			}
		}
		for (Tilelayer tileLayer : layers) {
			tileLayer.convertIDsToTiles(tiles);
		}
	}
	
	public boolean isConverted() {
		if (layers.size() == 0) {
			return true;
		}
		if (layers.get(0).isConverted()) {
			return true;
		}
		return false;
	}
	
	public void setCurrentLayer(Tilelayer currentLayer) {
		this.currentLayer = currentLayer;
	}
	
	public void setCurrentLayer(int index) {
		currentLayer = layers.get(index);
	}
	
	public Tilelayer addLayer(String name) {
		Tilelayer layer = new Tilelayer(name, width, height, cellWidth, cellHeight);
		layers.add(layer);
		return layer;
	}
	
	public void removeLayer(String name) {
		for (Tilelayer tileLayer : layers) {
			if (tileLayer.getName().equals(name)) {
				layers.remove(tileLayer);
				break;
			}
		}
	}
	
	public void removeLayer(Tilelayer layer) {
		if (layer != null) {
			layers.remove(layer);
		}
	}
	
	public void swapLayers(int index1, int index2) {
		Collections.swap(layers, index1, index2);
	}
	
	public int getLayerIndex(Tilelayer layer) {
		if (layer != null) {
			return layers.indexOf(layer);
		}
		return 0;
	}
	
	public void addTile(int x, int y, Tile tile) {
		if (currentLayer != null) {
			currentLayer.add(x, y, tile);
		}
	}
	
	public void removeTile(int x, int y) {
		if (currentLayer != null) {
			currentLayer.remove(x, y);
		}
	}

	public void draw(Batch batch, float x, float y, float scale) {
		for (Tilelayer tileLayer : layers) {
			tileLayer.draw(batch, x, y, scale);
		}
	}

	public Tilemap(int width, int height, int cellWidth, int cellHeight) {
		this.width = width;
		this.height = height;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		layers = new ArrayList<>();
		currentLayer = addLayer("test");
	}

	public Tilemap(Tilemap data) {
		this(data.width, data.height, data.cellWidth, data.cellHeight);
		layers = data.layers;
		currentLayer = data.currentLayer;
	}
	
}
