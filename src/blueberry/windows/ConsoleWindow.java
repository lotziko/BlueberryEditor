package blueberry.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;

import blueberry.project.Project;
import utils.ConsoleListener;

public class ConsoleWindow extends BlueberryNoTitleStandardWindow {

	private VisLabel label;
	private VisScrollPane scroll;
	
	public ConsoleWindow() {
		
		Table content = new Table();
		content.setFillParent(true);
		
		VisLabel console = new VisLabel("Console");
		content.add(console).left().pad(5f).padBottom(0f).row();
		
		label = new VisLabel("");
		
		Project.project.listener = new ConsoleListener() {

			@Override
			public void print(String line) {
				label.setText(label.getText() + line + "\n");
				scroll.layout();
				scroll.setScrollY(label.getHeight());
				super.print(line);
			}

			@Override
			public void clear() {
				label.setText("");
				super.clear();
			}
			
		};
		
		scroll = new VisScrollPane(label);
		scroll.setFadeScrollBars(false);
		content.add(scroll).expand().fill().pad(5f);
		
		addActor(content);
	}

}
