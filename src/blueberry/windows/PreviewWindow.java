package blueberry.windows;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kotcrab.vis.ui.widget.VisWindow;

import blueberry.resources.UiAssetsLoader;

/*
 * TODO: Crappy code, refactor it
 */

public class PreviewWindow extends VisWindow {

	//Fields

	private Actor item;
	private float[] scales;
	private int scaleIndex;
	private boolean draggable = true;
	
	//Getters and setters
	
	public boolean isDraggable() {
		return draggable;
	}
	
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}
	
	public void setContent(Table content) {
		item = content;
		item.setTouchable(Touchable.enabled);
		//TODO: сделать иначе без возвращения
		scaleIndex = 5;
		clearChildren();
		final Table table = new Table();
		table.background(UiAssetsLoader.getTiledDrawable("opacityBackground"));
		// item = new Type();
		item.setPosition((getWidth() - item.getWidth()) / 2, (getHeight() - item.getHeight()) / 2);
		table.addActor(item);
		addListener(new DragListener() {

			private float oldX, oldY;

			@Override
			public void dragStart(InputEvent event, float x, float y, int pointer) {
				if (draggable) {
					oldX = x;
					oldY = y;
				}
				super.dragStart(event, x, y, pointer);
			}

			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				if (draggable) {
					float scaledX = item.getWidth() * item.getScaleX();
					float scaledY = item.getHeight() * item.getScaleY();
					item.setX(clamp(item.getX() + x - oldX, -scaledX + 5, table.getWidth() - 5));
					item.setY(clamp(item.getY() + y - oldY, -scaledY + 5, table.getHeight() - 5));
					oldX = x;
					oldY = y;
				}
				super.drag(event, x, y, pointer);
			}
		});

		addListener(new InputListener() {

			// TODO: scale position problem
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				getStage().setScrollFocus(item);
				super.enter(event, x, y, pointer, fromActor);
			}

			@Override
			public boolean scrolled(InputEvent event, float x, float y, int amount) {
				
				float oldZoom = getScale();
				if (amount < 0) {
					increaseScale();
				} else {
					decreaseScale();
				}
				float newZoom = getScale();
				Vector2 mouseOldPosition = new Vector2(clamp((x - item.getX()), 0, item.getWidth() * oldZoom), clamp((y - item.getY()), 0, item.getHeight() * oldZoom));
				Vector2 mouseNewPosition = new Vector2(clamp((x - item.getX()) * newZoom / oldZoom, 0, item.getWidth() * newZoom), clamp((y - item.getY()) * newZoom / oldZoom, 0, item.getHeight() * newZoom));
				
				item.setPosition(item.getX() + (mouseOldPosition.x - mouseNewPosition.x), item.getY() + (mouseOldPosition.y - mouseNewPosition.y));
				
				return super.scrolled(event, x, y, amount);
			}

		});
		add(table).expand().fill();
	}
	
	public Actor getContent() {
		return item;
	}
	
	//Methods
	
	private void increaseScale() {
		if (scaleIndex < scales.length - 1) {
			++scaleIndex;
		}
		item.setScale(getScale());
	}
	
	private void decreaseScale() {
		if (scaleIndex > 0) {
			--scaleIndex;
		}
		item.setScale(getScale());
	}
	
	public float getScale() {
		return scales[scaleIndex];
	}
	
	private float clamp(float value, float min, float max) {
		return value < min ? min : (value > max ? max : value);
	}
	
	//Constructors
	
	public PreviewWindow(final Table it) {
		super("");
		
		scales = new float[] { 0.02f, 0.05f, 0.1f, 0.2f, 0.5f, 1f, 1.25f, 1.5f, 1.75f, 2f, 2.5f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 12f, 14f, 15f, 16f };
		
		setContent(it);

		setMovable(false);
		background(UiAssetsLoader.getTiledDrawable("windowBlueBackground"));
		getTitleTable().padLeft(5f);
	}

	
}
