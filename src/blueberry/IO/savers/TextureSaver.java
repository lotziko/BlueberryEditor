package blueberry.IO.savers;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

import blueberry.IO.Asset;
import blueberry.IO.Saver;
import blueberry.project.Project;

public class TextureSaver extends Saver<Texture> {

	@Override
	public void save(Asset asset, String filename) {
		Texture image = asset.getContent(Texture.class);
		if (!image.getTextureData().isPrepared()) {
			image.getTextureData().prepare();
		}
		Pixmap pixmap = image.getTextureData().consumePixmap();
		PixmapIO.writePNG(new FileHandle(new File(Project.project.getAssetPath() + filename + "/")), pixmap);
	}

}
