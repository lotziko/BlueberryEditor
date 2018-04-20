package blueberry.IO.creators;

import blueberry.IO.Creator;
import blueberry.project.Project;
import blueberry.resources.Tileset;
import blueberry.windows.ProjectWindow;

public class TilesetCreator extends Creator<Tileset> {

	@Override
	public Tileset create(String filename, Tileset data) {
		Project.project.createAsset(Tileset.class, filename, new Tileset(data), Project.project.getTileCounter());
		Project.project.incrementTileCounter();
		Project.project.saveAsset(Tileset.class, filename);
		Project.project.save();
		ProjectWindow.tree.loadAssetsToTree();
		return null;
	}

}
