package blueberry.IO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Texture;

import blueberry.IO.creators.SpriteCreator;
import blueberry.IO.creators.TilemapCreator;
import blueberry.IO.creators.TilesetCreator;
import blueberry.IO.loaders.SpriteLoader;
import blueberry.IO.loaders.TextureLoader;
import blueberry.IO.loaders.TilemapLoader;
import blueberry.IO.loaders.TilesetLoader;
import blueberry.IO.savers.SpriteSaver;
import blueberry.IO.savers.TextureSaver;
import blueberry.IO.savers.TilemapSaver;
import blueberry.IO.savers.TilesetSaver;
import blueberry.resources.Sprite;
import blueberry.resources.Tilemap;
import blueberry.resources.Tileset;

public class AssetManager {

	public static AssetManager manager = new AssetManager();

	private HashMap<Class, HashMap<String, Asset>> assets = new HashMap<>();
	private HashMap<Class, HashMap<String, Loader>> loaders = new HashMap<>();
	private HashMap<Class, HashMap<String, Creator>> creators = new HashMap<>();
	private HashMap<Class, HashMap<String, Saver>> savers = new HashMap<>();

	public AssetManager() {
		setLoader(Texture.class, new TextureLoader());
		setSaver(Texture.class, new TextureSaver());
		setLoader(Sprite.class, new SpriteLoader());
		setSaver(Sprite.class, new SpriteSaver());
		setCreator(Sprite.class, new SpriteCreator());
		setLoader(Tileset.class, new TilesetLoader());
		setSaver(Tileset.class, new TilesetSaver());
		setCreator(Tileset.class, new TilesetCreator());
		setLoader(Tilemap.class, new TilemapLoader());
		setSaver(Tilemap.class, new TilemapSaver());
		setCreator(Tilemap.class, new TilemapCreator());
	}

	public <T> T getAsset(Class<T> type, String filename) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			return null;
		}
		Asset asset = category.get(filename);
		if (asset == null) {
			return null;
		}
		if (asset.getContent(type) == null) {
			return null;
		}
		return asset.getContent(type);
	}

	public <T> List<T> getAssetsOfType(Class<T> type) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			return null;
		}
		Collection<Asset> assetsFound = category.values();
		if (assetsFound == null) {
			return null;
		}
		List<T> result = new ArrayList<>();
		for (Asset asset : assetsFound) {
			result.add(asset.getContent(type));
		}
		return result;
	}

	public <T> String getAssetFilepath(Class<T> type, T content) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			return "";
		}
		for (Entry entry : category.entrySet()) {
			if (((Asset) entry.getValue()).getContent(type).equals(content)) {
				return (String) entry.getKey();
			}
		}
		return "";
	}

	public <T> void replaceAsset(Class<T> type, String filename, T content) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			assets.put(type, category = new HashMap<>());
		}
		Asset asset = category.get(filename);
		if (asset != null) {
			category.remove(asset);
		}
		category.put(filename, new Asset(content));
	}

	public <T> void renameAsset(Class<T> type, String filename, String newFilename) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			return;
		}
		load(type, newFilename);
		Asset asset = new Asset(getAsset(type, newFilename));
		category.remove(filename);
		category.put(newFilename, asset);
	}

	public <T> void removeAsset(Class<T> type, String filename) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			return;
		}
		Asset asset = category.get(filename);
		if (asset != null) {
			category.remove(asset);
		}
	}

	public <T> void addAsset(Class<T> type, String filename, T content) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			assets.put(type, category = new HashMap<>());
		}
		category.put(filename, new Asset(content));
	}

	public <T> boolean isAssetLoaded(Class<T> type, String filename) {
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			return false;
		}
		Asset asset = category.get(filename);
		if (asset == null) {
			return false;
		}
		return true;
	}

	public <T> void load(Class<T> type, String filename) {

		HashMap<String, Loader> getLoaders = loaders.get(type);
		if (loaders == null) {
			return;
		}
		Loader loader = getLoaders.get("");
		if (loader == null) {
			return;
		}
		HashMap<String, Asset> category = assets.get(type);
		if (category == null) {
			assets.put(type, category = new HashMap<>());
		}
		category.put(filename, new Asset(loader.load(filename)));

		// listener.handle();

	}

	public <T> void create(Class<T> type, String filename, Object data) {
		
		HashMap<String, Creator> getCreators = creators.get(type);
		if (creators == null) {
			return;
		}
		Creator creator = getCreators.get("");
		if (creator == null) {
			return;
		}
		creator.create(filename, data);
	}

	/* TODO: load using config */

	public <T> void save(Class<T> type, String filename) {
		new Thread() {

			@Override
			public void run() {
				HashMap<String, Saver> getSavers = savers.get(type);
				HashMap<String, Asset> category = assets.get(type);

				Asset asset = category.get(filename);
				Saver saver = getSavers.get("");
				if (asset != null)
					saver.save(asset, filename);
			}

		}.start();
	}

	public <T> void setLoader(Class<T> type, Loader<T> loader) {
		HashMap<String, Loader> loaders = this.loaders.get(type);
		if (loaders == null) {
			this.loaders.put(type, loaders = new HashMap<>());
		}
		loaders.put("", loader);
	}

	public <T> Loader getLoader(Class<T> type, String filename) {
		HashMap<String, Loader> loaders = this.loaders.get(type);
		if (filename == null) {
			return loaders.get("");
		} else {
			return loaders.get(filename);
		}
	}

	public <T> void setSaver(Class<T> type, Saver<T> saver) {
		HashMap<String, Saver> savers = this.savers.get(type);
		if (savers == null) {
			this.savers.put(type, savers = new HashMap<>());
		}
		savers.put("", saver);
	}
	
	public <T> void setCreator(Class<T> type, Creator<T> creator) {
		HashMap<String, Creator> creators = this.creators.get(type);
		if (creators == null) {
			this.creators.put(type, creators = new HashMap<>());
		}
		creators.put("", creator);
	}
}
