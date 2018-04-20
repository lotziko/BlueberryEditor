package blueberry.windows.editors;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;

import blueberry.project.Project;
import blueberry.resources.Tile;
import blueberry.resources.Tileset;
import blueberry.resources.UiAssetsLoader;

public class TileTable extends Table {

	private Tileset[] tileset;
	private ButtonGroup<TileButton> buttonGroup;
	private TileButton lastClicked;
	private Tile lastTile;

	public TileTable(Tileset... tileset) {
		this.tileset = tileset;
		align(Align.top);
		
		
		buttonGroup = new ButtonGroup<>();
		buttonGroup.setMaxCheckCount(1);

		update();

		addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				lastClicked = buttonGroup.getChecked();
				if (lastClicked != null)
					lastTile = lastClicked.getTile();
				super.clicked(event, x, y);
			}
			
		});
		
	}

	/* updates table content */

	public void update() {
		buttonGroup.clear();
		clearChildren();

		if (tileset.length == 1) {
			HorizontalFlowGroup group = new HorizontalFlowGroup();
			group.setSpacing(2f);
			add(group).expand().fill();

			List<Tile> tiles = tileset[0].getTiles();
			for (Tile tile : tiles) {
				TileButton button = new TileButton(tile);
				button.getStyle().up = UiAssetsLoader.getDrawable("imageBackgroundUp");
				button.getStyle().over = UiAssetsLoader.getDrawable("imageBackgroundOver");
				button.getStyle().down = null;
				button.getImageCell().width(35).height(35).pad(5f);
				group.addActor(button);
				buttonGroup.add(button);
			}

		} else {
			for (int i = 0; i < tileset.length; i++) {
				String[] words = Project.project.getAssetFilepath(Tileset.class, tileset[i]).split("/");
				add(new VisLabel(words[words.length - 1])).left().row();

				HorizontalFlowGroup group = new HorizontalFlowGroup();
				group.setSpacing(2f);
				add(group).expandX().fillX().row();
				add(new Separator()).expandX().fillX().height(2f).padTop(5f).padBottom(5f).row();

				List<Tile> tiles = tileset[i].getTiles();
				for (Tile tile : tiles) {
					TileButton button = new TileButton(tile);
					button.getImageCell().size(25, 25).padTop(3f).padBottom(3f);
					group.addActor(button);
					buttonGroup.add(button);
				}
			}
		}
	}

	/* returns selected tile */

	public Tile getSelected() {
		if (lastClicked != null)
		return lastClicked.getTile();
		return lastTile;
	}
	
	public void setSelected(Tile tile) {
		lastTile = tile;
	}
	
	public void unselect() {
		lastClicked = null;
	}

	/* holds tile in a button */

	private class TileButton extends VisImageButton {

		private Tile tile;

		public Tile getTile() {
			return tile;
		}

		public TileButton(Tile tile) {
			super(new TextureRegionDrawable(new TextureRegion(tile.getImage())));
			this.tile = tile;
		}

	}

}
