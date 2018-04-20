package blueberry.windows;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;

import blueberry.engine.IdeConfig;

public class IdeConfigWindow extends VisDialog {

	@Override
	protected void close() {
		fadeOut();
		super.close();
	}

	private VisTextField jdkField;
	
	public IdeConfigWindow() {
		super("Config");
		addCloseButton();
		
		Table table = new Table();
		
		table.add(new VisLabel("JDK directory")).space(5f);
		
		jdkField = new VisTextField(IdeConfig.config.getJdkLocation());
		table.add(jdkField).space(5f).expandX().fillX();
		
		VisTextButton chooseJdkLocation = new VisTextButton("...");
		chooseJdkLocation.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileChooserManager.setMode(Mode.OPEN);
				FileChooserManager.setMultiSelectionEnabled(false);
				FileChooserManager.setSelectionMode(SelectionMode.DIRECTORIES);
				FileChooserManager.setListener(new FileChooserListener() {
					
					@Override
					public void selected(Array<FileHandle> arg0) {
						jdkField.setText(arg0.get(0).path());
					}
					
					@Override
					public void canceled() {
						
					}
				});
				FileChooserManager.open();
				super.clicked(event, x, y);
			}
			
		});
		table.add(chooseJdkLocation);
		
		getContentTable().add(table).expand().fill().row();
		
		Table bottomButtons = new Table();
		
		VisTextButton cancelButton = new VisTextButton("Cancel");
		cancelButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				close();
				super.clicked(event, x, y);
			}
			
		});
		bottomButtons.add(cancelButton).width(80f).space(5f);
		
		VisTextButton okButton = new VisTextButton("OK");
		okButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				IdeConfig.config.setJdkLocation(jdkField.getText());
				IdeConfig.config.save();
				close();
				super.clicked(event, x, y);
			}
			
		});
		bottomButtons.add(okButton).width(80f);
		
		getContentTable().add(bottomButtons);
		
	}

}
