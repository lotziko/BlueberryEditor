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
import blueberry.resources.Tilemap;

public class CreateTilemapWindow extends VisDialog {

	private VisTextField nameField, widthField, heightField, cellWidthField, cellHeightField;
	
	public CreateTilemapWindow() {
		super("New tilemap");
		addCloseButton();
		
		Table table = new Table();
		table.pad(5f);
		
		table.add(new VisLabel("name"));
		table.add(nameField = new VisTextField()).pad(5f).row();
		
		table.add(new VisLabel("width"));
		table.add(widthField = new VisTextField()).pad(5f).row();
		
		table.add(new VisLabel("height"));
		table.add(heightField = new VisTextField()).pad(5f).row();
		
		table.add(new VisLabel("cell width"));
		table.add(cellWidthField = new VisTextField()).pad(5f).row();
		
		table.add(new VisLabel("cell height"));
		table.add(cellHeightField = new VisTextField()).pad(5f).row();
		
		getContentTable().add(table).row();
		
		VisTextButton confirm = new VisTextButton("OK");
		confirm.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				String name = "tilemaps/" + (nameField.getText().equals("")?("tilemap" + Project.project.getTilemapCounter()):nameField.getText());
				
				int width = Integer.parseInt(widthField.getText());
				int height = Integer.parseInt(heightField.getText());
				int cellWidth = Integer.parseInt(cellWidthField.getText());
				int cellHeight = Integer.parseInt(cellHeightField.getText());
				
				AssetManager.manager.create(Tilemap.class, name, new Tilemap(width, height, cellWidth, cellHeight));
				close();
				super.clicked(event, x, y);
			}
			
		});
		
		getContentTable().add(confirm).width(90f);
	}

}
