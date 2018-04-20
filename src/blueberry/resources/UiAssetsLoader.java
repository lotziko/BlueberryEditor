package blueberry.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class UiAssetsLoader {
	
	private static TextureAtlas tAtlas = new TextureAtlas(Gdx.files.internal("editorIcons/data.atlas"));

	public static Drawable getDrawable(String name) {
		return new TextureRegionDrawable(tAtlas.findRegion(name));
	}
	
	public static Drawable getTiledDrawable(String name) {
		return new TiledDrawable(tAtlas.findRegion(name));
	}
	
	public static NinePatch getNinepatch(String name) {
		return tAtlas.createPatch(name);
	}
	
	public static Drawable getNinepatchDrawable(String name) {
		return new NinePatchDrawable(tAtlas.createPatch(name));
	}
	
	public static TextureRegion getTexture(String name) {
		return tAtlas.findRegion(name);
	}

}
