package blueberry.windows.editors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;

import blueberry.engine.GameEngine;
import blueberry.project.Project;
import blueberry.resources.Sprite;
import blueberry.windows.FileChooserManager;
import blueberry.windows.PreviewWindow;

public class SpriteEditorWindow extends VisDialog {

	//private DragPane dragPane;
	private Table items;
	private Sprite sprite;
	private VisTextField xOrigin, yOrigin;
	private SpriteTable spriteTable;
	private Color color;
	private Texture curTexture;
	//private Draggable draggable;
	//private GridGroup group;
	
	@Override
	protected void close() {
		if (xOrigin.getText() != "" && yOrigin.getText() != "") {
			sprite.setxOrigin(Integer.parseInt(xOrigin.getText()));
			sprite.setyOrigin(Integer.parseInt(yOrigin.getText()));
		}
		Project.project.saveAsset(Sprite.class, Project.project.getAssetFilepath(Sprite.class, sprite));
		super.close();
	}
	
	public void updateFramesPane() {
		items.clear();
		FrameButton button;
		for (int i = 0; i < sprite.getFrameCount(); i++) {
			items.add(button = new FrameButton(sprite.getFrames()[i])).size(80, 80).pad(1f);
			button.getImageCell().size(80, 80);
		}
	}
	
	public SpriteEditorWindow(Sprite sprite) {
		super("Sprite Editor");
		this.sprite = sprite;
		setResizable(true);
		setSize(800, 600);
		addCloseButton();
		
		/*ColorPicker picker = new ColorPicker() {

			@Override
			public void setColor(Color newColor) {
				color = newColor;
				super.setColor(newColor);
			}
			
		};
		GameEngine.stage.addActor(picker.fadeIn());
		*/
		items = new Table();
		/*dragPane = new DragPane(false);
		dragPane.setGroup(group = new GridGroup());
		group.setSpacing(0f);
		group.setItemSize(50, 50);
		
		draggable = new Draggable();
		draggable.setKeepWithinParent(true);
		draggable.setBlockInput(true);
		draggable.setInvisibleWhenDragged(true);
		draggable.setListener(new DragPane.DefaultDragListener() {

			@Override
			public boolean onEnd(Draggable arg0, Actor arg1, float arg2, float arg3) {
				group.
				return super.onEnd(arg0, arg1, arg2, arg3);
			}
			
		});
		dragPane.setDraggable(draggable);
		dragPane.setListener(new DragPane.DragPaneListener.AcceptOwnChildren());*/
		
		VisScrollPane pane = new VisScrollPane(items);
		getContentTable().add(pane).expandX().fillX().height(100).row();
		if (sprite.getFrameCount() > 0) {
			spriteTable = new SpriteTable(sprite.getFrames()[0]);
			curTexture = sprite.getFrames()[0];
		} else {
			spriteTable = new SpriteTable(null);
		}
		spriteTable.setSprite(sprite);
		spriteTable.addListener(new ClickListener(Buttons.RIGHT) {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (sprite.getFrameCount() > 0) {
					sprite.setxOrigin((int) x);
					sprite.setyOrigin(sprite.getFrames()[0].getHeight() - (int) y);
					xOrigin.setText(sprite.getxOrigin() + "");
					yOrigin.setText(sprite.getyOrigin() + "");
				}
				
				super.clicked(event, x, y);
			}
			
		});
		
		PreviewWindow preview = new PreviewWindow(spriteTable);
		
		getContentTable().add(preview).expand().fill();
		
		Table originPane = new Table();
		getContentTable().add(originPane).row();
		
		originPane.add(new VisLabel("X origin"));
		originPane.add(xOrigin = new VisTextField(sprite.getxOrigin() + "")).pad(5f).row();
		
		originPane.add(new VisLabel("Y origin"));
		originPane.add(yOrigin = new VisTextField(sprite.getyOrigin() + "")).pad(5f).row();
		
		Button importButton = new VisTextButton("import");
		importButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileChooserManager.setFilter("Image file (*.png)", "png");
				FileChooserManager.setMode(Mode.OPEN);
				FileChooserManager.setMultiSelectionEnabled(true);
				FileChooserManager.setSelectionMode(SelectionMode.FILES);
				FileChooserManager.setListener(new FileChooserListener() {
					
					@Override
					public void selected(Array<FileHandle> arg0) {
						Texture[] frames = new Texture[arg0.size];
						for (int i = 0; i < arg0.size; i++) {
							frames[i] = new Texture(arg0.get(i));
						}
						SpriteEditorWindow.this.sprite.setFrames(frames);
						updateFramesPane();
						Project.project.saveAsset(Sprite.class, Project.project.getAssetFilepath(Sprite.class, SpriteEditorWindow.this.sprite));
					}
					
					@Override
					public void canceled() {
						
					}
				});
				FileChooserManager.open();
				super.clicked(event, x, y);
			}
			
		});
		getContentTable().add(importButton);
		updateFramesPane();
		
		Button editButton = new VisTextButton("edit");
		editButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				TextureEditorWindow editor = new TextureEditorWindow(curTexture) {

					@Override
					protected void close() {
						spriteTable.setTexture(curTexture);
						super.close();
					}
					
				};
				GameEngine.stage.addActor(editor);
				super.clicked(event, x, y);
			}
			
		});
		getContentTable().add(editButton);
	}

	private class FrameButton extends VisImageButton {

		public FrameButton(Texture imageUp) {
			super(new TextureRegionDrawable(new TextureRegion(imageUp)));
			addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					spriteTable.setTexture(imageUp);
					curTexture = imageUp;
					super.clicked(event, x, y);
				}
				
			});
		}
		
	}
	
}
