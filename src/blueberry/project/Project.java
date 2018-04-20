package blueberry.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.google.gson.annotations.Expose;

import blueberry.IO.AssetManager;
import blueberry.IO.InputOutputManager;
import blueberry.engine.IdeConfig;
import blueberry.json.JsonManager;
import blueberry.resources.Tilemap;
import utils.ConsoleListener;
import utils.StringUtils;

public class Project {

	public static Project project;
	@Expose
	private int spriteCounter = 0, tilemapCounter = 500, tilesetCounter = 10000;
	@Expose
	private HashMap<Integer, AssetConfig> resourceLog = new HashMap<>();

	private String path;
	private String name;
	
	public ConsoleListener listener;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getAssetPath() {
		return path + "assets/";
	}

	public String getName() {
		return name;
	}
	
	public int getSpriteCounter() {
		return spriteCounter;
	}
	
	public int getTileCounter() {
		return tilesetCounter;
	}
	
	public int getTilemapCounter() {
		return tilemapCounter;
	}
	
	public int incrementSpriteCounter() {
		return ++spriteCounter;
	}
	
	public int incrementTileCounter() {
		return tilesetCounter += 400;
	}
	
	public int incrementTilemapCounter() {
		return ++tilemapCounter;
	}

	public HashMap<Integer, AssetConfig> getResourceLog() {
		return resourceLog;
	}
	
	public boolean resourceLogContainsPath(String path) {
		for (AssetConfig config : resourceLog.values()) {
			if (config.getPath().equals(path)) {
				return true;
			}
		}
		return false;
	}

	public static Project create(String name, String path) {
		Project project = new Project(name, path);
		try {
			String dir = System.getProperty("user.dir");
			String gdxDir = dir + "\\lib\\gdx-setup.jar";
			String disk = path.charAt(0) + "";
			//System.out.println(gdxDir);
			Runtime.getRuntime().exec("cmd /c " + disk + ": & java -jar " + gdxDir + " --dir "+project.getPath()+"\\project"+" --name "+name+" --package com."+name.toLowerCase()+".game --mainClass Game --excludeModules android;ios;iosmoe;html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return project;
	}

	public static Project load(String path) {
		Project project = JsonManager.fromJsonFile(path, Project.class);
		String[] words = path.split("/");
		project.path = path.replaceAll(words[words.length - 1], "");
		project.name = words[words.length - 1].replaceAll(".bbep", "");
		Gdx.graphics.setTitle("BlueberryEngine - " + project.name);
		return project;
	}
	
	public void packSprites() {
		Settings settings = new Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.combineSubdirectories = true;
		settings.duplicatePadding = true;
		settings.square = true;
		TexturePacker.process(settings, getAssetPath() + "sprites/", getPath() + "project/core/assets/", "sprites");
		
		File spritesDirectory = new File(getAssetPath() + "sprites");
		File classDirectory = new File(getPath() + "project/core/src/assets/");
		if (!classDirectory.exists()) {
			classDirectory.mkdirs();
		}
		
		String output = "package assets;\n"
				+"\n"
				+"import com.badlogic.gdx.Gdx;\n"
				+"import com.badlogic.gdx.graphics.g2d.TextureAtlas;\n"
				+ "\n"
				+"import blueberry.engine.sprites.Sprite;\n"
				+"\n"
				+"public class Sprites {\n"
				+ "\tprivate static TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(\"sprites.atlas\"));\n";

		File[] files = spritesDirectory.listFiles();
		for (File file : files) {
			String name = file.getName();
			blueberry.resources.Sprite sprite = Project.project.getAsset(blueberry.resources.Sprite.class, "sprites/" + file.getName());
			if (sprite != null)
			output += "\tpublic static Sprite " + name +" = new Sprite(" + sprite.getxOrigin() + ", " + sprite.getyOrigin() + ", \"" + name + "\", atlas.findRegions(\"" + name + "/\"));\n";
		}
		output += "}\n";
		InputOutputManager.write(classDirectory.getAbsolutePath() + "/Sprites.java", output);
		System.out.println("Sprites packed");
	}
	
	public void packTilemaps() {
		File tilemapsDirectory = new File(getAssetPath() + "tilemaps");
		File classDirectory = new File(getPath() + "project/core/assets/tilemaps/");
		if (!classDirectory.exists()) {
			classDirectory.mkdirs();
		}
		for (File file : classDirectory.listFiles()) {
			file.delete();
		}
		
		File[] files = tilemapsDirectory.listFiles();
		
		for (File file : files) {
			JsonManager.toJsonFile(classDirectory + "/" + file.getName() + ".json", getAsset(Tilemap.class, "tilemaps/" + file.getName()));
		}
		System.out.println("Tilemaps packed");
	}
	
	//TODO: tilemap resize (interpolation)
	
	public void packTiles() {
		Settings settings = new Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.combineSubdirectories = true;
		settings.duplicatePadding = true;
		settings.square = true;
		TexturePacker.process(settings, getAssetPath() + "tilesets/", getPath() + "project/core/assets/", "tiles");
		/*File tilesetsDirectory = new File(getAssetPath() + "tilesets");
		File classDirectory = new File(getPath() + "project/core/src/assets/");
		if (!classDirectory.exists()) {
			classDirectory.mkdirs();
		}
		
		String output = "package assets;\n"
				+ "\n"
				+ "import com.badlogic.gdx.Gdx;\n"
				+ "import com.badlogic.gdx.graphics.g2d.TextureAtlas;\n"
				+ "\n"
				+ "public class Tiles {\n"
				+ "\n"
				+ "\tprivate static TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(\"tiles.atlas\"));\n"
				+ "\n";
		
		File[] files = tilesetsDirectory.listFiles();
		for (File file : files) {
			output += "\t//" + file.getName() + "\n";
			File[] tileNames = file.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return pathname.getPath().toLowerCase().endsWith(".png");
				}
			});
			output += "\n";
			for (File tileFile : tileNames) {
				output += "\t//" + tileFile.getName() + "\n";
			}
			output += "\n";
		}
		output += "}";
		
		InputOutputManager.write(classDirectory.getAbsolutePath() + "/Tiles.java", output);*/
		System.out.println("Tiles packed");
	}
	
	public void compile() {
		String path = Project.project.getPath().replaceAll("/", "\\\\") + "project";
		String disk = path.charAt(0) + "";
		new Thread() {

			@Override
			public void run() {
				packSprites();
				packTiles();
				packTilemaps();
				
				try {
					String line;
					Process p = Runtime.getRuntime().exec("cmd /c " + disk + ": & cd " + path + " & set \"JAVA_HOME="+ IdeConfig.config.getJdkLocation().replaceAll("/", "\\\\") +"\" & gradlew & gradlew desktop:run");
					BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					if (listener != null) {
						listener.clear();
					}
					while ((line = input.readLine()) != null) {
						if (listener != null) {
							System.out.println(line);
							//listener.print(line);
						}
					}
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				super.run();
			}
			
		}.start();
	}
	
	public void export() {
		String path = Project.project.getPath().replaceAll("/", "\\\\") + "project";
		String disk = path.charAt(0) + "";
		new Thread() {

			@Override
			public void run() {
				packSprites();
				packTiles();
				packTilemaps();
				
				try {
					String line;
					Process p = Runtime.getRuntime().exec("cmd /c " + disk + ": & cd " + path + " & set \"JAVA_HOME="+ IdeConfig.config.getJdkLocation().replaceAll("/", "\\\\") +"\" & gradlew & gradlew desktop:dist");
					BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					if (listener != null) {
						listener.clear();
					}
					while ((line = input.readLine()) != null) {
						if (listener != null) {
							System.out.println(line);
							//listener.print(line);
						}
					}
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				super.run();
			}
			
		}.start();
	}

	public void save() {
		JsonManager.toJsonFile(path + name + ".bbep", this);
	}

	public <T> T getAsset(Class<T> type, String filename) {
		return AssetManager.manager.getAsset(type, filename);
	}
	
	public <T> List<T> getAssetsOfType(Class<T> type) {
		return AssetManager.manager.getAssetsOfType(type);
	}

	public <T> String getAssetFilepath(Class<T> type, T content) {
		return AssetManager.manager.getAssetFilepath(type, content);
	}
	
	public <T> void createAsset(Class<T> type, String filename, int ID) {
		AssetManager.manager.addAsset(type, filename, null);
		resourceLog.put(ID, new AssetConfig(filename, type));
		File file = new File(getAssetPath() + filename + "/");
		file.mkdirs();
	}

	public <T> void createAsset(Class<T> type, String filename, T content, int ID) {
		AssetManager.manager.addAsset(type, filename, content);
		resourceLog.put(ID, new AssetConfig(filename, type));
		File file = new File(getAssetPath() + filename + "/");
		file.mkdirs();
	}

	public <T> void replaceAsset(Class<T> type, String filename, T content) {
		AssetManager.manager.replaceAsset(type, filename, content);
	}

	public <T> void saveAsset(Class<T> type, String filename) {
		AssetManager.manager.save(type, filename);
	}

	public <T> void renameAsset(Class<T> type, String filename, String newFilename) {
		for (Entry<Integer, AssetConfig> entry : resourceLog.entrySet()) {
			if (entry.getValue().getPath().equals(filename)) {
				resourceLog.replace(entry.getKey(), new AssetConfig(newFilename, type));
				break;
			}
		}
		File oldDirectory = new File(getAssetPath() + filename);
		File newDirectory = new File(getAssetPath() + newFilename);
		newDirectory.mkdirs();
		File[] files = oldDirectory.listFiles();
		if (files != null)
		for (File file : files) {
			//String[] words = file.getAbsolutePath().split("/");
			//words[words.length - 2] = newFilename;
			String[] strings = filename.split("/");
			String oldName = strings[strings.length - 1];
			strings = newFilename.split("/");
			String newName = strings[strings.length - 1];
			String newPath = file.getAbsolutePath().replaceAll(oldName, newName);//StringUtils.join("/", words);
			try {
				Files.move(file.toPath(), new File(newPath).toPath(), StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		oldDirectory.delete();
		AssetManager.manager.renameAsset(type, filename, newFilename);
	}

	public <T> void removeAsset(Class<T> type, String filename) {
		AssetManager.manager.removeAsset(type, filename);
		for (Entry<Integer, AssetConfig> entry : resourceLog.entrySet()) {
			if (entry.getValue().getPath().equals(filename)) {
				resourceLog.remove(entry.getKey());
			}
		}
	}
	
	public void loadAllAssets() {
		for (AssetConfig config : project.getResourceLog().values()) {
			AssetManager.manager.load(config.getType(), config.getPath());
		}
	}
	
	public void saveAllAssets() {
		for (AssetConfig config : project.getResourceLog().values()) {
			AssetManager.manager.save(config.getType(), config.getPath());
		}
	}

	public void createStandardDirectories() {
		new File(project.getAssetPath() + "sprites").mkdirs();
		new File(project.getAssetPath() + "tilesets").mkdirs();
		new File(project.getAssetPath() + "tilemaps").mkdirs();
	}
	
	private Project(String name, String path) {
		this.name = name;
		this.path = path;
	}

}
