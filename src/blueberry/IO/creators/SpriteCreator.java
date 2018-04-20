package blueberry.IO.creators;

import blueberry.IO.Creator;
import blueberry.project.Project;
import blueberry.resources.Sprite;
import blueberry.windows.ProjectWindow;

public class SpriteCreator extends Creator<Sprite> {

	@Override
	public Sprite create(String filename, Sprite data) {
		Project.project.createAsset(Sprite.class, filename, data == null ? new Sprite(): new Sprite(data), Project.project.getSpriteCounter());
		Project.project.incrementSpriteCounter();
		Project.project.saveAsset(Sprite.class, filename);
		Project.project.save();
		ProjectWindow.tree.loadAssetsToTree();
		return null;
	}

}
