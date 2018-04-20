package blueberry.IO.savers;

import blueberry.IO.Asset;
import blueberry.IO.Saver;
import blueberry.json.JsonManager;
import blueberry.project.Project;
import blueberry.resources.Tilemap;

public class TilemapSaver extends Saver<Tilemap> {

	@Override
	public void save(Asset asset, String filename) {
		Tilemap tilemap = asset.getContent(Tilemap.class);
		tilemap.convertTilesToIDs();
		JsonManager.toJsonFile(Project.project.getAssetPath() + filename + "/config.json", tilemap);
	}

}
