package blueberry.IO.creators;

import blueberry.IO.Creator;
import blueberry.project.Project;
import blueberry.resources.Tilemap;
import blueberry.windows.ProjectWindow;

public class TilemapCreator extends Creator<Tilemap> {

	@Override
	public Tilemap create(String filename, Tilemap data) {
		Project.project.createAsset(Tilemap.class, filename, new Tilemap(data), Project.project.getTilemapCounter());
		Project.project.incrementTilemapCounter();
		Project.project.saveAsset(Tilemap.class, filename);
		Project.project.save();
		ProjectWindow.tree.loadAssetsToTree();
		return null;
	}

}
