package blueberry.windows.editors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import blueberry.render.Render;
import blueberry.resources.Tile;
import blueberry.resources.Tilemap;
import blueberry.resources.UiAssetsLoader;

public class TilemapTable extends Table {

	private Tilemap tilemap;
	private int originalWidth, originalHeight;
	private Drawable removeTile = UiAssetsLoader.getDrawable("tileRemove"), selectionRectangle = UiAssetsLoader.getNinepatchDrawable("selectionRectangle");
	
	@Override
	public void draw(Batch arg0, float arg1) {
		setSize(originalWidth * getScaleX(), originalHeight * getScaleY());
		
		Render.shape.setProjectionMatrix(arg0.getProjectionMatrix());
		Render.shape.setTransformMatrix(arg0.getTransformMatrix());
		
		arg0.end();
		
		Render.shape.begin(ShapeType.Point);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Color color = Render.shape.getColor();
		color.a = arg0.getColor().a;
		Render.shape.setColor(color);
		
		float x;
		for(int i = 1; i < tilemap.getWidth(); i++) {
			dottedLine(x = getX() + i * tilemap.getCellWidth() * getScaleX(), getY() + 1, x, getY() + getHeight() - 1, 2);
		}
		float y;
		for(int i = 1; i < tilemap.getHeight(); i++) {
			dottedLine(getX(), y = getY() + i * tilemap.getCellHeight() * getScaleY(), getX() + getWidth() - 1, y, 2);
		}
		Render.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		arg0.begin();
		
		super.draw(arg0, arg1);
		tilemap.draw(arg0, getX(), getY(), getScaleX());
	}
	
	private void dottedLine(float x1, float y1, float x2, float y2, float dist) {
		Vector2 dir = new Vector2(x2, y2).sub(new Vector2(x1, y1));
		float len = dir.len();
		for(int i = 0; i < len; i += dist) {
			dir.clamp(len - i, len - i);
			Render.shape.point(x1 + dir.x, y1 + dir.y, 0);
		}
	}

	public void drawSelectionRectangle(Batch batch, int x1, int y1, int x2, int y2) {
		float width = (x2 - x1) * tilemap.getCellWidth() * getScaleX(), height = (y2 - y1) * tilemap.getCellHeight() * getScaleY();
		if (width != 0 && height != 0)
		selectionRectangle.draw(batch, getX() + x1 * tilemap.getCellWidth() * getScaleX(), getY() + y1 * tilemap.getCellHeight() * getScaleY(), width, height);
	}
	
	public void drawTileShadow(Batch batch, Tile tile, int x, int y) {
		Color color = batch.getColor();
		Color alphaColor = new Color(color);
		alphaColor.a = (float) Math.sin(System.currentTimeMillis() / 100) * 0.05f + 0.4f;
		batch.setColor(alphaColor);
		batch.draw(tile.getImage(), getX() + x * tilemap.getCellWidth() * getScaleX(), getY() + y * tilemap.getCellHeight() * getScaleY(), tile.getImage().getWidth() * getScaleX(), tile.getImage().getHeight() * getScaleY());
		batch.setColor(color);
	}
	
	public void drawRemoveTile(Batch batch, int x, int y) {
		Color temp = new Color(batch.getColor());
		batch.setColor(temp.r, temp.g, temp.b, (float) Math.sin(System.currentTimeMillis() / 100) * 0.05f + 0.9f);
		removeTile.draw(batch, getX() + x * tilemap.getCellWidth() * getScaleX(), getY() + y * tilemap.getCellHeight() * getScaleY(), tilemap.getCellWidth() * getScaleX(), tilemap.getCellHeight() * getScaleY());
		batch.setColor(temp);
	}
	
	public void resize() {
		originalWidth = tilemap.getWidth()*tilemap.getCellWidth();
		originalHeight = tilemap.getHeight()*tilemap.getCellHeight();
	}
	
	public TilemapTable(Tilemap tilemap) {
		this.tilemap = tilemap;
		background(UiAssetsLoader.getNinepatchDrawable("previewPaneBorder"));
		originalWidth = tilemap.getWidth()*tilemap.getCellWidth();
		originalHeight = tilemap.getHeight()*tilemap.getCellHeight();
	}
	
}
