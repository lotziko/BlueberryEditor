package blueberry.IO.loaders;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

import blueberry.IO.Loader;
import blueberry.project.Project;

public class TextureLoader extends Loader<Texture> {

	@Override
	public Texture load(String filename) {
		TextureData data = TextureData.Factory.loadFromFile(new FileHandle(new File(Project.project.getAssetPath() + filename)), false);
		return new Texture(data);
	}

}
