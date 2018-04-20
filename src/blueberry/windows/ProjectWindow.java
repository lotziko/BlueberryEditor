package blueberry.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisWindow;

import blueberry.resources.UiAssetsLoader;

public class ProjectWindow extends VisWindow {

	public static AssetsTreeWindow tree;
	public static ConsoleWindow console;
	
	public ProjectWindow() {
		super("");
		background(UiAssetsLoader.getDrawable("standardWindowBackground"));
		Table table = new Table();
		table.setFillParent(true);
		table.add(new TopMenu().getTable()).expandX().fillX().top().row();
		table.add(new ButtonsMenu()).expandX().fillX().top().row();
		Table main = new Table();
		
		main.add((tree = new AssetsTreeWindow()).fadeIn()).expandY().fillY().width(300);
		Table mainPane = new Table();
		mainPane.add(new Table()).expand().fill().row();
		mainPane.add((console = new ConsoleWindow()).fadeIn()).expandX().fillX().height(140);
		main.add(mainPane).expand().fill();
		
		table.add(main).expand().fill().row();
		addActor(table);
	}

}
