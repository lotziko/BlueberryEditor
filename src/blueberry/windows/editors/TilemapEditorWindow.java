package blueberry.windows.editors;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import blueberry.IO.AssetManager;
import blueberry.engine.GameEngine;
import blueberry.project.Project;
import blueberry.resources.Tile;
import blueberry.resources.Tilelayer;
import blueberry.resources.Tilemap;
import blueberry.resources.Tileset;
import blueberry.resources.UiAssetsLoader;
import blueberry.windows.PreviewScrollingWindow;
import blueberry.windows.PreviewWindow;
import blueberry.windows.editors.SelectionList.Container;

public class TilemapEditorWindow extends VisDialog {

	private Tilemap tilemap;
	private TileTable table;
	private VisList<Tilelayer> layers;
	// private boolean isPlacing = true;
	private int pencilCurrentSizeX = 1;
	private int pencilCurrentSizeY = 1;
	private boolean mouseJustReleased = false;
	private TilemapEditorInstrument currentInstrument = TilemapEditorInstrument.PENCIL;
	private SelectionList<Tile> selectionBuffer = new SelectionList<>();
	private SelectionList<Tile> penBuffer = new SelectionList<>();
	// private TilemapTable tilemapTable;

	@Override
	protected void close() {

		Project.project.saveAsset(Tilemap.class, Project.project.getAssetFilepath(Tilemap.class, tilemap));
		super.close();
	}

	public TilemapEditorWindow(Tilemap tilemap) {
		super("");
		String[] path = AssetManager.manager.getAssetFilepath(Tilemap.class, tilemap).split("/");
		this.getTitleLabel().setText("Tilemap Editor - " + path[path.length - 1]);
		setResizable(true);
		this.tilemap = tilemap;
		if (!tilemap.isConverted())
			tilemap.convertIDsToTiles();
		tilemap.setCurrentLayer(0);

		setSize(950, 600);
		addCloseButton();
		TilemapTable tileTable;
		PreviewScrollingWindow previewWindow = new PreviewScrollingWindow(tileTable = new TilemapTable(this.tilemap) {

			private Vector2 mouse;

			@Override
			public void act(float arg0) {
				mouse = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
				int mouseX = Math.floorDiv((int) mouse.x, tilemap.getCellWidth()),
						mouseY = Math.floorDiv((int) (tilemap.getHeight() * tilemap.getCellHeight() - mouse.y), tilemap.getCellHeight());

				int offsetX = (int) pencilCurrentSizeX / 2, offsetY = (int) pencilCurrentSizeY / 2, cellX, cellY;
				switch (currentInstrument) {
				case ERASER:
					if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
						for (int i = -offsetX; i <= offsetX; i++) {
							for (int j = -offsetY; j <= offsetY; j++) {
								cellX = mouseX + i;
								cellY = mouseY + j;
								tilemap.removeTile(cellX, cellY);
							}
						}
					break;
				case MOVE_AREA:
					if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
						selectionBuffer.setPosition(mouseX, tilemap.getHeight() - mouseY);
					} else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
						if (Gdx.input.justTouched()) {
							selectionBuffer.paste(selectionBuffer.getX(), selectionBuffer.getY(), tilemap.getCurrentLayer().getTiles());
							selectionBuffer.clear();
						}
					} else {
						// selectionBuffer.firstChange = true;
					}
					break;
				case PENCIL:
					if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
						if (penBuffer.size() == 0) {
							tilemap.addTile(mouseX, mouseY, table.getSelected());
						} else {
							penBuffer.paste(mouseX, mouseY - 1, tilemap.getCurrentLayer().getTiles());
						}
					}
					else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
						if (Gdx.input.justTouched()) {
							if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
								penBuffer.clear();
							penBuffer.setSelectionPosition(mouseX, mouseY);
							penBuffer.setActive(true);
						}
					} else if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
						if (Gdx.input.isKeyPressed(Keys.C)) {
							GameEngine.copyBuffer = penBuffer;
						} else if (Gdx.input.isKeyPressed(Keys.V)) {
							if (GameEngine.copyBuffer != null && GameEngine.copyBuffer instanceof SelectionList<?>) {
								penBuffer = (SelectionList<Tile>)GameEngine.copyBuffer;
							}
						}
						
					} else {
						if (penBuffer.isActive()) {
							penBuffer.setActive(false);
							penBuffer.copy(penBuffer.getSelectionX(), penBuffer.getSelectionY(), mouseX, mouseY + 1, tilemap.getCurrentLayer().getTiles());
							if (penBuffer.size() == 1) {
								table.setSelected((Tile) penBuffer.getObjects().get(0).object);
								penBuffer.clear();
							}
						}
					}
					if (penBuffer.size() > 0) penBuffer.setPosition(mouseX, mouseY);
					break;
				case PICKER: 
					if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
						Tile tile = tilemap.getCurrentLayer().getTiles()[mouseX][mouseY];
						table.setSelected(tile);
					}
					break;
				case SELECTOR:
					if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
						if (Gdx.input.justTouched()) {
							if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
								selectionBuffer.clear();
							selectionBuffer.setSelectionPosition(mouseX, mouseY);
						}

						mouseJustReleased = true;
					} else if (Gdx.input.isKeyJustPressed(Keys.FORWARD_DEL)) {
						selectionBuffer.delete(tilemap.getCurrentLayer().getTiles());
						selectionBuffer.clear();
					} else {
						if (mouseJustReleased) {
							// selection end
							selectionBuffer.copy(selectionBuffer.getSelectionX(), selectionBuffer.getSelectionY(), mouseX, mouseY + 1, tilemap.getCurrentLayer().getTiles());
							mouseJustReleased = false;
						}
					}
					break;
				default:
					break;

				}

				super.act(arg0);
			}

			@Override
			public void draw(Batch arg0, float arg1) {
				super.draw(arg0, arg1);
				mouse = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

				int offsetX = (int) pencilCurrentSizeX / 2, offsetY = (int) pencilCurrentSizeY / 2;
				switch (currentInstrument) {
				case ERASER:
					for (int i = -offsetX; i <= offsetX; i++) {
						for (int j = -offsetY; j <= offsetY; j++) {
							drawRemoveTile(arg0, Math.floorDiv((int) mouse.x, tilemap.getCellWidth()) + i, Math.floorDiv((int) mouse.y, tilemap.getCellHeight()) + j);
						}
					}
					break;
				case PENCIL:

					if (penBuffer.size() > 0) {
						for (Container container : penBuffer.getObjects()) {
							Tile tile = (Tile) container.object;
							drawTileShadow(arg0, tile, /*Math.floorDiv((int) mouse.x, tilemap.getCellWidth()) + */container.getX(),/* Math.floorDiv((int) mouse.y, tilemap.getCellHeight()) + */tilemap.getHeight() - container.getY());
						}
					} else if (table.getSelected() != null) {
						drawTileShadow(arg0, table.getSelected(), Math.floorDiv((int) mouse.x, tilemap.getCellWidth()), Math.floorDiv((int) mouse.y, tilemap.getCellHeight()));
					}

					if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
						drawSelectionRectangle(arg0, penBuffer.getSelectionX(), tilemap.getHeight() - penBuffer.getSelectionY(), Math.floorDiv((int) mouse.x, tilemap.getCellWidth()), Math.floorDiv((int) mouse.y, tilemap.getCellHeight()));
					}
					break;
				case PICKER:
					if (table.getSelected() != null) {
						drawTileShadow(arg0, table.getSelected(), Math.floorDiv((int) mouse.x, tilemap.getCellWidth()), Math.floorDiv((int) mouse.y, tilemap.getCellHeight()));
					}
					break;
				case SELECTOR:
					if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
						drawSelectionRectangle(arg0, selectionBuffer.getSelectionX(), tilemap.getHeight() - selectionBuffer.getSelectionY(), Math.floorDiv((int) mouse.x, tilemap.getCellWidth()), Math.floorDiv((int) mouse.y, tilemap.getCellHeight()));
					}
					break;
				default:
					break;

				}
				if (selectionBuffer.size() > 0)
					for (Container container : selectionBuffer.getObjects()) {
						// Tile tile = (Tile) container.object;
						drawRemoveTile(arg0, container.getX(), tilemap.getHeight() - container.getY() - 1);
					}
			}

		});
		previewWindow.setDraggable(false);

		getContentTable().add(previewWindow).top().expand().fill();
		Collection<Tileset> tilesets = Project.project.getAssetsOfType(Tileset.class);

		Table rightPane = new Table();

		rightPane.align(Align.top);

		Table buttons = new Table();

		VisLabel pencilSizeNumberX = new VisLabel(pencilCurrentSizeX + "");
		buttons.add(pencilSizeNumberX).width(20f);

		VisSlider pencilSizeX = new VisSlider(pencilCurrentSizeX, 15, 1, false);
		pencilSizeX.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				pencilCurrentSizeX = (int) pencilSizeX.getValue();
				pencilSizeNumberX.setText(pencilCurrentSizeX + "");
			}

		});
		buttons.add(pencilSizeX).width(50f).pad(5f);

		VisLabel pencilSizeNumberY = new VisLabel(pencilCurrentSizeY + "");
		buttons.add(pencilSizeNumberY).width(20f);

		VisSlider pencilSizeY = new VisSlider(pencilCurrentSizeY, 15, 1, false);
		pencilSizeY.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				pencilCurrentSizeY = (int) pencilSizeY.getValue();
				pencilSizeNumberY.setText(pencilCurrentSizeY + "");
			}

		});
		buttons.add(pencilSizeY).width(50f).pad(5f);

		for (TilemapEditorInstrument enumItem : TilemapEditorInstrument.values()) {
			VisImageButton button = new VisImageButton(UiAssetsLoader.getDrawable(enumItem.getImage()));
			button.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					currentInstrument = enumItem;
					super.clicked(event, x, y);
				}

			});
			buttons.add(button).pad(1f);
		}

		rightPane.add(buttons).right().row();

		table = new TileTable(tilesets.toArray(new Tileset[tilesets.size()]));
		VisScrollPane tileTableScrollPane = new VisScrollPane(table);
		tileTableScrollPane.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				getStage().setScrollFocus(tileTableScrollPane);
				super.enter(event, x, y, pointer, fromActor);
			}

		});

		rightPane.add(new VisLabel("Tiles")).row();

		Table rightTopTable = new Table();
		rightTopTable.background(UiAssetsLoader.getNinepatchDrawable("layerListBackground"));
		rightTopTable.add(tileTableScrollPane).expandY().fillY().width(250).row();
		rightPane.add(rightTopTable).expandY().fillY().row();

		Table layerButtons = new Table();

		VisImageButton newLayerButton = new VisImageButton(UiAssetsLoader.getDrawable("newLayer"));
		newLayerButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				VisDialog dialog = new VisDialog("New layer");
				dialog.addCloseButton();
				Table table = new Table();
				VisTextField nameField = new VisTextField();
				table.add(new VisLabel("name"));
				table.add(nameField).pad(5f).row();
				dialog.getContentTable().add(table).expand().fill().row();
				VisTextButton confirm = new VisTextButton("OK");
				confirm.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (nameField.getText() != "") {
							tilemap.addLayer(nameField.getText());
							dialog.fadeOut();
							refreshLayersList();
						}
					}

				});
				dialog.getContentTable().add(confirm).width(90);
				dialog.show(GameEngine.stage).fadeIn();
				dialog.setSize(250, 150);

				super.clicked(event, x, y);
			}

		});
		layerButtons.add(newLayerButton).pad(5f);

		VisImageButton renameLayerButton = new VisImageButton(UiAssetsLoader.getDrawable("paintIcon"));
		renameLayerButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				VisDialog dialog = new VisDialog("Rename layer");
				dialog.addCloseButton();
				Table table = new Table();
				VisTextField nameField = new VisTextField(layers.getSelected().getName());
				table.add(new VisLabel("name"));
				table.add(nameField).pad(5f).row();
				dialog.getContentTable().add(table).expand().fill().row();
				VisTextButton confirm = new VisTextButton("OK");
				confirm.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (nameField.getText() != "") {
							layers.getSelected().setName(nameField.getText());
							dialog.fadeOut();
							refreshLayersList();
						}
					}

				});
				dialog.getContentTable().add(confirm).width(90);
				dialog.show(GameEngine.stage).fadeIn();
				dialog.setSize(250, 150);

				super.clicked(event, x, y);
			}

		});
		layerButtons.add(renameLayerButton);

		VisImageButton removeLayerButton = new VisImageButton(UiAssetsLoader.getDrawable("removeLayer"));
		removeLayerButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				tilemap.removeLayer(layers.getSelected());
				refreshLayersList();
			}
		});
		layerButtons.add(removeLayerButton).pad(5f);

		VisImageButton moveLayerUp = new VisImageButton(UiAssetsLoader.getDrawable("arrowUp"));
		moveLayerUp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Tilelayer layer;
				int index;
				if ((layer = layers.getSelected()) != null) {
					if ((index = tilemap.getLayerIndex(layer)) > 0) {
						tilemap.swapLayers(index, index - 1);
					}
				}
				refreshLayersList();
			}
		});
		layerButtons.add(moveLayerUp);

		VisImageButton moveLayerDown = new VisImageButton(UiAssetsLoader.getDrawable("arrowDown"));
		moveLayerDown.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Tilelayer layer;
				int index;
				if ((layer = layers.getSelected()) != null) {
					if ((index = tilemap.getLayerIndex(layer)) < tilemap.getTilelayers().size() - 1) {
						tilemap.swapLayers(index, index + 1);
					}
				}
				refreshLayersList();
			}

		});
		layerButtons.add(moveLayerDown).pad(5f);

		VisImageButton resizeButton = new VisImageButton(UiAssetsLoader.getDrawable("resizeIcon"));
		resizeButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				VisDialog dialog = new VisDialog("Resize");
				dialog.addCloseButton();
				Table table = new Table();
				VisTextField width, height, cellWidth, cellHeight;

				table.add(new VisLabel("width"));
				table.add(width = new VisTextField(tilemap.getWidth() + "")).pad(5f).row();

				table.add(new VisLabel("height"));
				table.add(height = new VisTextField(tilemap.getHeight() + "")).pad(5f).row();

				table.add(new VisLabel("cell width"));
				table.add(cellWidth = new VisTextField(tilemap.getCellWidth() + "")).pad(5f).row();

				table.add(new VisLabel("cell height"));
				table.add(cellHeight = new VisTextField(tilemap.getCellHeight() + "")).pad(5f).row();

				dialog.getContentTable().add(table).expand().fill().row();

				VisTextButton confirm = new VisTextButton("OK");
				confirm.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						tilemap.resize(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), Integer.parseInt(cellWidth.getText()), Integer.parseInt(cellHeight.getText()));
						tileTable.resize();
						dialog.fadeOut();
					}

				});
				dialog.getContentTable().add(confirm).width(90);
				dialog.show(GameEngine.stage).fadeIn();
				dialog.setSize(400, 300);

				super.clicked(event, x, y);
			}

		});
		layerButtons.add(resizeButton);

		rightPane.add(layerButtons).right().row();

		rightPane.add(new VisLabel("Layers")).row();
		Table rightBottomTable = new Table();
		rightBottomTable.background(UiAssetsLoader.getNinepatchDrawable("layerListBackground"));
		rightBottomTable.add(layers = new VisList<>()).width(250).height(200);
		rightPane.add(rightBottomTable);

		layers.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (layers.getSelected() != null)
					tilemap.setCurrentLayer(layers.getSelected());
				super.clicked(event, x, y);
			}

		});
		refreshLayersList();

		getContentTable().add(rightPane).expandY().fillY();
	}

	public void refreshLayersList() {
		layers.getItems().clear();
		for (Tilelayer layer : tilemap.getTilelayers()) {
			layers.getItems().add(layer);
		}
	}

}
