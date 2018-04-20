package blueberry.IO.loaders;

import blueberry.IO.Loader;
import blueberry.json.JsonManager;
import blueberry.project.Project;
import blueberry.resources.Tilemap;

public class TilemapLoader extends Loader<Tilemap> {

	@Override
	public Tilemap load(String filename) {
		Tilemap tilemap = JsonManager.fromJsonFile(Project.project.getAssetPath() + filename + "/" + "config.json", Tilemap.class);
		return tilemap;
	}

}
