package blueberry.windows.editors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import blueberry.render.Render;
import blueberry.resources.Sprite;

public class SpriteTable extends Table {

	private TextureRegionDrawable texture;
	private Sprite sprite;
	private int width, height;
	
	@Override
	public void draw(Batch arg0, float arg1) {
		setSize(width * getScaleX(), height * getScaleY());
		super.draw(arg0, arg1);
		if (texture != null) {
			texture.draw(arg0, getX(), getY(), getWidth(), getHeight());
			
			if (sprite != null) {
				Render.shape.setProjectionMatrix(arg0.getProjectionMatrix());
				Render.shape.setTransformMatrix(arg0.getTransformMatrix());
				arg0.end();
				Render.shape.begin(ShapeType.Filled);
				Render.shape.rect(getX() + sprite.getxOrigin() * getScaleX(), getY() + (height - sprite.getyOrigin()) * getScaleY(), getScaleX(), getScaleY());
				Render.shape.end();
				arg0.begin();	
			}
		}
		
	}
	
	public TextureRegionDrawable getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		if (texture != null) {
			this.texture = new TextureRegionDrawable(new TextureRegion(texture));
			width = this.texture.getRegion().getRegionWidth();
			height = this.texture.getRegion().getRegionHeight();
		}
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public SpriteTable(Texture texture) {
		setTexture(texture);
	}
	
}
