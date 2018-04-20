package blueberry.IO.loaders;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

import blueberry.IO.Loader;
import blueberry.json.JsonManager;
import blueberry.project.Project;
import blueberry.resources.Sprite;

public class SpriteLoader extends Loader<Sprite> {

	@Override
	public Sprite load(String filename) {
		Sprite sprite = JsonManager.fromJsonFile(Project.project.getAssetPath() + filename + "/" + "config.json", Sprite.class);
		Texture[] frames = new Texture[sprite.getFrameCount()];
		for(int i = 0; i < frames.length; i++) {
			TextureData data = TextureData.Factory.loadFromFile(new FileHandle(new File(Project.project.getAssetPath() + filename + "/" + "_" + i +".png")), false);
			frames[i] = new Texture(data);
		}
		sprite.setFrames(frames);
		return sprite;
	}
	
}
