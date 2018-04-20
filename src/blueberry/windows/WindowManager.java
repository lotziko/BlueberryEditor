package blueberry.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisWindow;

import blueberry.engine.GameEngine;
import blueberry.resources.Sprite;
import blueberry.resources.Tilemap;
import blueberry.resources.Tileset;
import blueberry.windows.editors.SpriteEditorWindow;
import blueberry.windows.editors.TilemapEditorWindow;
import blueberry.windows.editors.TilesetEditorWindow;

public class WindowManager {

	public static WindowManager manager = new WindowManager();
	public VisDialog dialog;
	public Table table;
	
	public WindowManager() {
		table = new Table();
		table.setFillParent(true);
		GameEngine.stage.addActor(table);
	}
	
	public void openWelcomeWindow() {
		(dialog = new WelcomeWindow()).show(GameEngine.stage);
		dialog.setSize(300, 400);
		//GameEngine.stage.addActor((window = new WelcomeWindow()).fadeIn());
		//window.centerWindow();
	}
	
	public void openConfigWindow() {
		(dialog = new IdeConfigWindow()).show(GameEngine.stage);
		dialog.setSize(400, 300);
	}
	
	public void openCreateTilemapWindow() {
		(dialog = new CreateTilemapWindow()).show(GameEngine.stage);
		dialog.setSize(400, 300);
	}
	
	public void openCreateTilesetWindow() {
		(dialog = new CreateTilesetWindow()).show(GameEngine.stage);
		dialog.setSize(250, 150);
	}
	
	public void openCreateSpriteWindow() {
		(dialog = new CreateSpriteWindow()).show(GameEngine.stage);
		dialog.setSize(250, 150);
	}
	
	public <T> void openEditor(Class<?> type, T content) {
		
		/* TODO: switch editor types */
		
		switch (type.getSimpleName()) {
		case "Sprite":
			openSpriteEditor((Sprite) content);
			break;
		case "Tileset":
			openTilesetEditor((Tileset) content);
			break;
		case "Tilemap":
			openTilemapEditor((Tilemap) content);
			break;

		default:
			break;
		}
	}
	
	public void openSpriteEditor(Sprite sprite) {
		VisWindow editor = new SpriteEditorWindow(sprite);
		editor.setPosition(Gdx.graphics.getWidth()/2 - editor.getWidth()/2, Gdx.graphics.getHeight()/2 - editor.getHeight()/2);
		table.addActor(editor.fadeIn());
	}
	
	public void openTilesetEditor(Tileset tileset) {
		VisWindow editor = new TilesetEditorWindow(tileset);
		editor.setPosition(Gdx.graphics.getWidth()/2 - editor.getWidth()/2, Gdx.graphics.getHeight()/2 - editor.getHeight()/2);
		table.addActor(editor.fadeIn());
	}
	
	public void openTilemapEditor(Tilemap tilemap) {
		VisWindow editor = new TilemapEditorWindow(tilemap);
		editor.setPosition(Gdx.graphics.getWidth()/2 - editor.getWidth()/2, Gdx.graphics.getHeight()/2 - editor.getHeight()/2);
		table.addActor(editor.fadeIn());
	}
	
	public void openProjectWindow() {
		if (dialog != null) {
			dialog.fadeOut();
		}
		table.add(new ProjectWindow()).expand().fill();
	}
	
}
