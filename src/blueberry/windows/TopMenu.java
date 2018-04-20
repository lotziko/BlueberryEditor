package blueberry.windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;

public class TopMenu extends MenuBar {

	public TopMenu() {
		
		Menu menu = new Menu("File");
		addMenu(menu);
		
		MenuItem newItem = new MenuItem("New");
		menu.addItem(newItem);
		
		MenuItem openItem = new MenuItem("Open");
		menu.addItem(openItem);
		
		menu.addSeparator();
		
		MenuItem preferencesItem = new MenuItem("Preferences");
		preferencesItem.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				WindowManager.manager.openConfigWindow();
				super.clicked(event, x, y);
			}
			
		});
		menu.addItem(preferencesItem);
		
	}
	
}
