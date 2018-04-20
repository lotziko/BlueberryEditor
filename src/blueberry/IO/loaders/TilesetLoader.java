package blueberry.IO.loaders;

import java.io.File;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

import blueberry.IO.Loader;
import blueberry.json.JsonManager;
import blueberry.project.Project;
import blueberry.resources.Tile;
import blueberry.resources.Tileset;

public class TilesetLoader extends Loader<Tileset> {

	@Override
	public Tileset load(String filename) {
		Tileset tileset = JsonManager.fromJsonFile(Project.project.getAssetPath() + filename + "/" + "config.json", Tileset.class);
		List<Tile> tiles = tileset.getTiles();
		
		for(int i = 0; i < tileset.getSize(); i++) {
			TextureData data = TextureData.Factory.loadFromFile(new FileHandle(new File(Project.project.getAssetPath() + filename + "/" + tiles.get(i).getID() +".png")), false);
			tiles.get(i).setImage(new Texture(data));
		}
		
		return tileset;
	}

}
