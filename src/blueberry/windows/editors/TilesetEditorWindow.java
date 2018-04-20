package blueberry.windows.editors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;

import blueberry.project.Project;
import blueberry.resources.Tile;
import blueberry.resources.Tileset;
import blueberry.resources.UiAssetsLoader;
import blueberry.windows.FileChooserManager;

public class TilesetEditorWindow extends VisDialog {
	
	private Tileset tileset;
	private TileTable tileTable;
	
	public TilesetEditorWindow(Tileset tileset) {
		super("Tileset Editor");
		setSize(800, 600);
		addCloseButton();
		
		this.tileset = tileset;
		
		Table table = new Table();
		table.background(UiAssetsLoader.getTiledDrawable("opacityBackground"));
		
		tileTable = new TileTable(this.tileset);
		table.add(tileTable).expand().fill().pad(2f);
		
		VisScrollPane pane = new VisScrollPane(table);
		pane.setFadeScrollBars(false);
		getContentTable().add(pane).expand().fill().pad(5f);
		
		Table tableRight = new Table();
		
		VisTextButton newTile = new VisTextButton("New tile");
		newTile.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileChooserManager.setFilter("Image file (*.png)", "png");
				FileChooserManager.setMode(Mode.OPEN);
				FileChooserManager.setMultiSelectionEnabled(true);
				FileChooserManager.setSelectionMode(SelectionMode.FILES);
				FileChooserManager.setListener(new FileChooserListener() {
					
					@Override
					public void selected(Array<FileHandle> arg0) {
						for (FileHandle fileHandle : arg0) {
							TilesetEditorWindow.this.tileset.addTile(new Texture(fileHandle));
						}
						tileTable.update();
						Project.project.saveAsset(Tileset.class, Project.project.getAssetFilepath(Tileset.class, TilesetEditorWindow.this.tileset));
					}
					
					@Override
					public void canceled() {
						
					}
				});
				FileChooserManager.open();
				super.clicked(event, x, y);
			}
			
		});
		tableRight.add(newTile).width(90).pad(5f).row();
		
		VisTextButton removeTile = new VisTextButton("Remove tile");
		removeTile.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Tile tile = tileTable.getSelected();
				if (tile != null) {
					TilesetEditorWindow.this.tileset.removeTile(tile);
					Project.project.saveAsset(Tileset.class, Project.project.getAssetFilepath(Tileset.class, TilesetEditorWindow.this.tileset));
					tileTable.update();
				}
				super.clicked(event, x, y);
			}
			
		});
		tableRight.add(removeTile).width(90).pad(5f);
		
		getContentTable().add(tableRight).width(150).expandY().fillY();
	}
}
