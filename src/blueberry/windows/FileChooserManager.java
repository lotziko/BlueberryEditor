package blueberry.windows;

import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import blueberry.engine.GameEngine;

public class FileChooserManager {
	
	//Fields
	
	public static FileChooser fileChooser;
	
	//Methods
	
	public static void setMode(Mode mode) {
		fileChooser.setMode(mode);
	}
	
	public static void setSelectionMode(SelectionMode selectionMode) {
		fileChooser.setSelectionMode(selectionMode);
	}

	/** 
	 * Title example: Compressed project file (*.bbecp) 
	 */
	
	public static void setFilter(String title, String type) {
		FileTypeFilter filter = new FileTypeFilter(true);
		filter.addRule(title, type);
		fileChooser.setFileTypeFilter(filter);
	}
	
	public static void setMultiSelectionEnabled(boolean multiSelectionEnabled) {
		fileChooser.setMultiSelectionEnabled(multiSelectionEnabled);
	}

	public static void setListener(FileChooserListener newListener) {
		fileChooser.setListener(newListener);
	}

	public static void open() {
		GameEngine.stage.addActor(fileChooser.fadeIn());
	}
	
	public static void addFavorite(FileHandle favorite) {
		fileChooser.addFavorite(favorite);
	}
	
	public static boolean removeFavorite(FileHandle favorite) {
		return fileChooser.removeFavorite(favorite);
	}
	
	//Constructors
	
	static {
		FileChooser.setDefaultPrefsName("com.blueberry.engine.filechooser");
		fileChooser = new FileChooser(Mode.OPEN);
	}
}
