package blueberry.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import blueberry.windows.WindowManager;

public class GameEngine extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;
	public static Stage stage;
	public static WindowManager wManager;
	public static BitmapFont font;
	
	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Gdx.graphics.setTitle("Blueberry Engine");
		OrthographicCamera camera = new OrthographicCamera();
		Viewport viewport = new ScreenViewport(camera);
		VisUI.load();
		//String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"'<>";
		//font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("OpenSans-Regular.TTF"), characters, 12.5f, 7.5f, 9f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//open sans regular 9
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Regular.ttf"));
		//FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		//parameter.size = 15;
		//parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"'<>ÀÁÂÃÄÅ¨ÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕÖ×ØÙÚÛÜİŞßàáâãäå¸æçèéêëìíîïğñòóôõö÷øùúûüışÿ³¿º";
		//font = generator.generateFont(parameter);
		//generator.dispose();
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Regular.ttf"));
		//new VisLabel().getStyle().font = font;
		//new VisTextButton("").getStyle().font = font;
		//VisUI.getSkin().remove("default", BitmapFont.class);
		//VisUI.getSkin().add("default", font, BitmapFont.class);
		
		stage.setViewport(viewport);
		
		WindowManager.manager.openWelcomeWindow();
		
		IdeConfig.config.load();
		//content.add(new WelcomeWindow()).center().size(400, 300);
		//stage.addActor(content);
		
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		if (WindowManager.manager.dialog != null) {
			WindowManager.manager.dialog.setPosition(width/2 - 150, height/2 - 200);
		}
		super.resize(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
	}
	
	@Override
	public void dispose () {
		VisUI.dispose();
		IdeConfig.config.save();
		//batch.dispose();
		//img.dispose();
	}
}
