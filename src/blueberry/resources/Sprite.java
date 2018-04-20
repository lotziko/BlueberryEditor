package blueberry.resources;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.annotations.Expose;

public class Sprite {
	
	private Texture[] frames;
	@Expose
	private int width, height, xOrigin, yOrigin, frameCount;

	@Expose
	private String folder;
	
	public Texture[] getFrames() {
		return frames;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setFrames(Texture... frames) {
		this.frames = frames;
		frameCount = frames.length;
	}
	
	public int getxOrigin() {
		return xOrigin;
	}
	
	public void setxOrigin(int xOrigin) {
		this.xOrigin = xOrigin;
	}

	public int getyOrigin() {
		return yOrigin;
	}
	
	public void setyOrigin(int yOrigin) {
		this.yOrigin = yOrigin;
	}
	
	public int getFrameCount() {
		return frameCount;
	}
	
	public Sprite(Sprite sprite) {
		this(sprite.frames);
	}
	
	public Sprite(Texture... frames) {
		this.frames = frames;
		setFrames(frames);
	}
}
