package blueberry.windows.editors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;

import blueberry.engine.GameEngine;
import blueberry.resources.UiAssetsLoader;
import blueberry.windows.PreviewWindow;

public class TextureEditorWindow extends VisDialog {

	private TextureEditorInstrument currentInstrument = TextureEditorInstrument.PENCIL;
	private List<Pixmap> pixmaps = new ArrayList<>();
	private Pixmap currentPixmap, tempPixmap;
	private PreviewWindow editorPane;
	// first, second, ...
	private List<Color> colors = new ArrayList<>();
	private Texture tex;
	private boolean mouseDown, touchable;

	@Override
	protected void close() {
		tex = new Texture(currentPixmap);
		super.close();
	}

	public TextureEditorWindow(Texture texture) {
		super("");
		tex = texture;
		addCloseButton();
		setResizable(true);
		setSize(950, 600);
		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		currentPixmap = texture.getTextureData().consumePixmap();

		Table instrumentTable = new Table();
		getContentTable().add(instrumentTable).expandX().fillX().row();

		for (TextureEditorInstrument enumItem : TextureEditorInstrument.values()) {
			Button button = new VisImageButton(UiAssetsLoader.getDrawable(enumItem.getImage()));
			button.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					currentInstrument = enumItem;
					super.clicked(event, x, y);
				}

			});
			instrumentTable.add(button).pad(1f);
		}

		colors.add(Color.BLACK);
		colors.add(Color.WHITE);
		for (int i = 0; i < colors.size(); i++) {
			final int index = i;
			Button color = new Button() {

				private ShapeRenderer shape = new ShapeRenderer();

				@Override
				public void draw(Batch batch, float parentAlpha) {
					shape.setProjectionMatrix(batch.getProjectionMatrix());
					shape.setTransformMatrix(batch.getTransformMatrix());
					batch.end();
					shape.begin(ShapeType.Filled);
					shape.setColor(colors.get(index));
					shape.rect(getX(), getY(), getWidth(), getHeight());
					shape.end();
					batch.begin();
				}

			};
			color.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					ColorPicker picker = new ColorPicker() {

						@Override
						public void setColor(Color newColor) {
							colors.set(index, newColor);
							super.setColor(newColor);
						}

					};
					picker.setColor(colors.get(index));
					GameEngine.stage.addActor(picker);
					super.clicked(event, x, y);
				}

			});
			instrumentTable.add(color).size(20).pad(1f);
		}

		Table textureTable = new Table() {

			private int previousX, previousY;
			private Vector2 tmpVector = new Vector2();

			@Override
			public void draw(Batch arg0, float arg1) {
				if (tex != null) {
					setSize(tex.getWidth() * this.getScaleX(), texture.getHeight() * this.getScaleY());
					arg0.draw(tex, getX(), getY(), getWidth(), getHeight());
				}
				if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isButtonPressed(Buttons.RIGHT)) {
					if (!mouseDown) {
						tempPixmap = currentPixmap;
						tempPixmap.setColor(colors.get(0));
						tmpVector.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY() - 1);
						Vector2 pos = this.stageToLocalCoordinates(tmpVector);
						previousX = (int) pos.x;
						previousY = tempPixmap.getHeight() - (int) pos.y;
					}
					mouseDown = true;
				} else {

					if (mouseDown) {
						if (tempPixmap != null) {
							pixmaps.add(tempPixmap);
							currentPixmap = tempPixmap;
							tex = new Texture(currentPixmap);
						}
					}
					mouseDown = false;
				}

				if (mouseDown) {
					tmpVector.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
					Vector2 pos = this.stageToLocalCoordinates(tmpVector);
					// TODO: instruments
					int mouseX = (int) Math.floor(pos.x), mouseY = tempPixmap.getHeight() - (int) Math.ceil(pos.y);
					Color colorToUse = Gdx.input.isButtonPressed(Buttons.LEFT) ? colors.get(0) : colors.get(1);
					tempPixmap.setColor(colorToUse);
					if (touchable)
						switch (currentInstrument) {
						case PENCIL:
							tempPixmap.drawLine(previousX, previousY, mouseX, mouseY);
							break;
						case ERASER:
							Color color = new Color(tempPixmap.getPixel(mouseX, mouseY));
							color.a -= colorToUse.a;

							Blending blending = tempPixmap.getBlending();
							tempPixmap.setBlending(Pixmap.Blending.None);
							tempPixmap.setColor(color);
							tempPixmap.drawLine(previousX, previousY, mouseX, mouseY);
							tempPixmap.setColor(colorToUse);
							tempPixmap.setBlending(blending);
							break;
						case PICKER:
							colorToUse = new Color(tempPixmap.getPixel(mouseX, mouseY));
							colors.set(Gdx.input.isButtonPressed(Buttons.LEFT) ? 0 : 1, colorToUse);
							break;
						default:
							break;
						}
					tex = new Texture(tempPixmap);
					if (touchable) {
						previousX = mouseX;
						previousY = mouseY;
					}
					touchable = (GameEngine.stage.hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), false) == this ? true : false);
				}
				super.draw(arg0, arg1);
			}

		};

		editorPane = new PreviewWindow(textureTable);
		editorPane.setDraggable(false);
		getContentTable().add(editorPane).expand().fill();
	}

}
