package blueberry.IO.savers;

import java.io.File;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

import blueberry.IO.Asset;
import blueberry.IO.Saver;
import blueberry.json.JsonManager;
import blueberry.project.Project;
import blueberry.resources.Tile;
import blueberry.resources.Tileset;

public class TilesetSaver extends Saver<Tileset> {

	@Override
	public void save(Asset asset, String filename) {
		Tileset tileset = asset.getContent(Tileset.class);
		List<Tile> tiles = tileset.getTiles();
		
		/* Remove do not needed images in directory */
		
		File[] files = new File(Project.project.getAssetPath() + filename + "/").listFiles();
		FileHandle temp;
		
		for (File file : files) {
			if ((temp = new FileHandle(file)).extension().equals("png")) {
				for(int i = 0; i < tiles.size(); i++) {
					if ((tiles.get(i).getID()+"").equals(temp.nameWithoutExtension())) {
						break;
					}
					if (i == tiles.size() - 1) {
						temp.delete();
					}
				}
			}
		}
		
		/* Save files */
		
		for (Tile tile : tiles) {
			Texture image = tile.getImage();
			if (!image.getTextureData().isPrepared()) {
				image.getTextureData().prepare();
			}
			Pixmap pixmap = image.getTextureData().consumePixmap();
			PixmapIO.writePNG(new FileHandle(new File(Project.project.getAssetPath() + filename + "/" + tile.getID() + ".png")), pixmap);
		}
		JsonManager.toJsonFile(Project.project.getAssetPath() + filename + "/config.json", tileset);
	}

}
