package blueberry.IO.savers;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

import blueberry.IO.Asset;
import blueberry.IO.Saver;
import blueberry.json.JsonManager;
import blueberry.project.Project;
import blueberry.resources.Sprite;

public class SpriteSaver extends Saver<Sprite> {

	@Override
	public void save(Asset asset, String filename) {
		Sprite sprite = asset.getContent(Sprite.class);
		Texture[] frames = sprite.getFrames();
		
		/* Remove do not needed images in directory */
		
		File[] files = new File(Project.project.getAssetPath() + filename + "/").listFiles();
		FileHandle temp;
		
		for (File file : files) {
			if ((temp = new FileHandle(file)).extension().equals("png")) {
				for(int i = 0; i < frames.length; i++) {
					if (temp.nameWithoutExtension().equals("_"+i)) {
						break;
					}
					if (i == frames.length - 1) {
						temp.delete();
					}
				}
			}
		}
		
		/* Save files */
		
		if (frames != null)
		for(int i = 0; i < frames.length; i++) {
			Texture image = frames[i];
			if (!image.getTextureData().isPrepared()) {
				image.getTextureData().prepare();
			}
			Pixmap pixmap = image.getTextureData().consumePixmap();
			PixmapIO.writePNG(new FileHandle(new File(Project.project.getAssetPath() + filename + "/" + "_" + i + ".png")), pixmap);
		}
		JsonManager.toJsonFile(Project.project.getAssetPath() + filename + "/config.json", sprite);
	}

}
