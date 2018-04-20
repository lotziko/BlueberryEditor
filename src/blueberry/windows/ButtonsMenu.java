package blueberry.windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageButton;

import blueberry.project.Project;
import blueberry.resources.UiAssetsLoader;

public class ButtonsMenu extends BlueberryNoTitleStandardWindow {

	public ButtonsMenu() {
		background(UiAssetsLoader.getNinepatchDrawable("grayTopStandardBackground"));
		
		HorizontalGroup pane = new HorizontalGroup();
		pane.padLeft(10f);
		pane.space(3f);
		
		VisImageButton save = new VisImageButton(UiAssetsLoader.getDrawable("saveIcon"));
		save.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Project.project.save();
				Project.project.saveAllAssets();
				super.clicked(event, x, y);
			}
			
		});
		pane.addActor(save);
		
		VisImageButton sprite = new VisImageButton(UiAssetsLoader.getDrawable("spriteIcon"));
		sprite.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				WindowManager.manager.openCreateSpriteWindow();
				super.clicked(event, x, y);
			}
			
		});
		pane.addActor(sprite);
		
		VisImageButton tileset = new VisImageButton(UiAssetsLoader.getDrawable("tileIcon"));
		tileset.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				WindowManager.manager.openCreateTilesetWindow();
				super.clicked(event, x, y);
			}
			
		});
		pane.addActor(tileset);
		
		VisImageButton tilemap = new VisImageButton(UiAssetsLoader.getDrawable("tilemapIcon"));
		tilemap.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				WindowManager.manager.openCreateTilemapWindow();
				super.clicked(event, x, y);
			}
			
		});
		pane.addActor(tilemap);
		
		VisImageButton run = new VisImageButton(UiAssetsLoader.getDrawable("runIcon"));
		run.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Project.project.compile();
				super.clicked(event, x, y);
			}
			
		});
		pane.addActor(run);
		
		VisImageButton export = new VisImageButton(UiAssetsLoader.getDrawable("exportIcon"));
		export.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Project.project.export();
				super.clicked(event, x, y);
			}
			
		});
		pane.addActor(export);
		
		add(pane).expand().fill();
	}
	
}
