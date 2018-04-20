package blueberry.windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import blueberry.IO.AssetManager;
import blueberry.project.Project;
import blueberry.resources.Sprite;

public class CreateSpriteWindow extends VisDialog {
	
	private VisTextField nameField;
	
	public CreateSpriteWindow() {
		super("New sprite");
		addCloseButton();
		
		Table table = new Table();
		table.pad(5f);
		
		table.add(new VisLabel("name"));
		table.add(nameField = new VisTextField()).pad(5f).row();
		
		getContentTable().add(table).row();
		
		VisTextButton confirm = new VisTextButton("OK");
		confirm.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				String name = "sprites/" + (nameField.getText().equals("")?("sprite" + Project.project.getSpriteCounter()) : nameField.getText());
				AssetManager.manager.create(Sprite.class, name, null);
				close();
				super.clicked(event, x, y);
			}
			
		});
		
		getContentTable().add(confirm).width(90f);
	}
	
}
