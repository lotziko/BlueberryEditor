package blueberry.windows;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;

import blueberry.project.Project;

public class WelcomeWindow extends VisDialog {

	@Override
	protected void close() {
		fadeOut();
		super.close();
	}

	public WelcomeWindow() {
		super("Welcome!");
		setMovable(false);

		VisTextButton openProject = new VisTextButton("Open");
		openProject.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileChooserManager.setFilter("Project file (*.bbep)", "bbep");
				FileChooserManager.setMode(Mode.OPEN);
				FileChooserManager.setMultiSelectionEnabled(false);
				FileChooserManager.setSelectionMode(SelectionMode.FILES);
				FileChooserManager.setListener(new FileChooserListener() {

					@Override
					public void selected(Array<FileHandle> arg0) {
						Project.project = Project.load(arg0.get(0).path());
						FileChooserManager.removeFavorite(arg0.get(0).parent());
						FileChooserManager.addFavorite(arg0.get(0).parent());
						if (Project.project != null) {
							close();
							Project.project.loadAllAssets();
							WindowManager.manager.openProjectWindow();
						}
					}

					@Override
					public void canceled() {
						
					}
				});
				FileChooserManager.open();
				super.clicked(event, x, y);
			}

		});
		getContentTable().add(openProject);
		
		VisTextButton createProject = new VisTextButton("Create");
		createProject.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				FileChooserManager.setFilter("Project file (*.bbep)", "bbep");
				FileChooserManager.setMode(Mode.SAVE);
				FileChooserManager.setMultiSelectionEnabled(false);
				FileChooserManager.setSelectionMode(SelectionMode.FILES);
				FileChooserManager.setListener(new FileChooserListener() {

					@Override
					public void selected(Array<FileHandle> arg0) {
						Project.project = Project.create(arg0.get(0).nameWithoutExtension(), arg0.get(0).path().replaceAll(arg0.get(0).name(), ""));
						if (Project.project != null) {
							close();
							Project.project.save();
							Project.project.loadAllAssets();
							Project.project.createStandardDirectories();
							WindowManager.manager.openProjectWindow();
						}
					}

					@Override
					public void canceled() {
						
					}
				});
				FileChooserManager.open();
				super.clicked(event, x, y);
			}

		});
		getContentTable().add(createProject);
	}
}
