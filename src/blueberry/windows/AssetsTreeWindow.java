package blueberry.windows;

import java.io.File;
import java.util.Map.Entry;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTree;

import blueberry.IO.AssetManager;
import blueberry.engine.GameEngine;
import blueberry.project.AssetConfig;
import blueberry.project.Project;
import blueberry.resources.UiAssetsLoader;
import utils.StringUtils;

public class AssetsTreeWindow extends BlueberryNoTitleStandardWindow {
	
	VisTree tree;
	Drawable folderIcon = UiAssetsLoader.getDrawable("folderIcon");
	Drawable fileIcon = UiAssetsLoader.getDrawable("newIcon");
	private Node rootNode;
	
	public AssetsTreeWindow() {
		super();
		
		tree = new VisTree();
		tree.setIconSpacing(5f, 5f);
		tree.setPadding(10f);
		tree.getStyle().plus = UiAssetsLoader.getDrawable("plus");
		tree.getStyle().minus = UiAssetsLoader.getDrawable("minus");
		
		VisScrollPane pane = new VisScrollPane(tree);
		tree.addListener(new ClickListener(Buttons.RIGHT) {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Node node = tree.getNodeAt(y);
				if (node != null) {
					PopupMenu menu = new PopupMenu();
					
					MenuItem edit = new MenuItem("Edit");
					edit.addListener(new ClickListener() {

						@Override
						public void clicked(InputEvent event, float x, float y) {
							for (Entry entry : Project.project.getResourceLog().entrySet()) {
								AssetConfig config = (AssetConfig)entry.getValue();
								String[] words = config.getPath().split("/");
								String text = ((VisLabel)node.getActor()).getText() + "";
								if (words[words.length - 1].equals(text)) {
									WindowManager.manager.openEditor(config.getType(), Project.project.getAsset(config.getType(), config.getPath()));
								}
							}
							super.clicked(event, x, y);
						}
						
					});
					menu.addItem(edit);
					
					MenuItem rename = new MenuItem("Rename");
					rename.addListener(new ClickListener() {
						
						@Override
						public void clicked(InputEvent event, float x, float y) {
							for (Entry entry : Project.project.getResourceLog().entrySet()) {
								AssetConfig config = (AssetConfig)entry.getValue();
								String[] words = config.getPath().split("/");
								String text = ((VisLabel)node.getActor()).getText() + "";
								if (words[words.length - 1].equals(text)) {
									
									VisDialog dialog = new VisDialog("Rename");
									dialog.addCloseButton();
									Table table = new Table();
									VisTextField nameField = new VisTextField(((VisLabel)node.getActor()).getText() + "");
									table.add(new VisLabel("name"));
									table.add(nameField).pad(5f).row();
									dialog.getContentTable().add(table).expand().fill().row();
									VisTextButton confirm = new VisTextButton("OK");
									confirm.addListener(new ClickListener() {
										
										@Override
										public void clicked(InputEvent event, float x, float y) {
											if (nameField.getText() != "") {
												words[words.length - 1] = nameField.getText();
												Project.project.renameAsset(config.getType(), config.getPath(), StringUtils.join("/", words));
												loadAssetsToTree();
												dialog.fadeOut();
											}
										}
										
									});
									dialog.getContentTable().add(confirm).width(90);
									dialog.show(GameEngine.stage).fadeIn();
									dialog.setSize(250, 150);
									//WindowManager.manager.openEditor(config.getType(), Project.project.getAsset(config.getType(), config.getPath()));
								}
							}
							super.clicked(event, x, y);
						}
						
					});
					menu.addItem(rename);
					
					MenuItem duplicate = new MenuItem("Duplicate");
					duplicate.addListener(new ClickListener() {
						
						@Override
						public void clicked(InputEvent event, float x, float y) {
							for (Entry entry : Project.project.getResourceLog().entrySet()) {
								AssetConfig config = (AssetConfig)entry.getValue();
								String[] words = config.getPath().split("/");
								String text = ((VisLabel)node.getActor()).getText() + "";
								if (words[words.length - 1].equals(text)) {
									
									VisDialog dialog = new VisDialog("Duplicate");
									dialog.addCloseButton();
									Table table = new Table();
									VisTextField nameField = new VisTextField(((VisLabel)node.getActor()).getText() + "Copy");
									table.add(new VisLabel("name"));
									table.add(nameField).pad(5f).row();
									dialog.getContentTable().add(table).expand().fill().row();
									VisTextButton confirm = new VisTextButton("OK");
									confirm.addListener(new ClickListener() {
										
										@Override
										public void clicked(InputEvent event, float x, float y) {
											if (nameField.getText() != "") {
												words[words.length - 1] = nameField.getText();
												Class<?> type = config.getType();
												Object asset = AssetManager.manager.getAsset(type, config.getPath());
												AssetManager.manager.create(type, StringUtils.join("/", words), asset);
												loadAssetsToTree();
												dialog.fadeOut();
											}
										}
										
									});
									dialog.getContentTable().add(confirm).width(90);
									dialog.show(GameEngine.stage).fadeIn();
									dialog.setSize(250, 150);
								}
							}
						}
						
					});
					menu.addItem(duplicate);
					
					/* Because before calculating position you need to put buttons in menu */
					
					Vector2 coords = tree.localToParentCoordinates(new Vector2(x, y));
					menu.setPosition(coords.x, coords.y - menu.getHeight());
					addActor(menu);
				}
				super.clicked(event, x, y);
			}
			
		});
		pane.setFadeScrollBars(false);
		
		loadAssetsToTree();
		
		Table content = new Table();
		content.setFillParent(true);
		content.add(pane).expandY().fillY().width(300f);
		
		addActor(content);
		
	}
	
	public void loadAssetsToTree() {
		tree.clearChildren();
		rootNode = new Node(new Actor());
		
		recursiveFill(rootNode, new File(Project.project.getAssetPath()));
		
		for (Node nodeFound : rootNode.getChildren()) {
			tree.add(nodeFound);
		}
	}
	
	private boolean hasDirectoriesInside(File file) {
		File[] list = file.listFiles();
		for (File file2 : list) {
			if (file2.isDirectory()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasFilesInside(File file) {
		File[] list = file.listFiles();
		if (list != null) {
			for (File file2 : list) {
				if (file2.isFile()) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	
	private void recursiveFill(Node node, File file) {
		if (node != null && file != null) {
			File[] files = file.listFiles();
			
			if (!hasFilesInside(file)) {
				node.setIcon(folderIcon);
			} else {
				node.setIcon(fileIcon);
			}
			/*Arrays.sort(files, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return o1.getName().length()-o2.getName().length();
				}
			});*/
			
			for (File fileFound : files) {
				String path = fileFound.getPath().replaceAll("\\\\", "/");
				if (fileFound.isDirectory() && (node.equals(rootNode) || hasDirectoriesInside(fileFound) || Project.project.resourceLogContainsPath(path.replaceAll(Project.project.getAssetPath(), "")))) {
					VisLabel label = new VisLabel(fileFound.getName());
					Node nodeFound = new Node(label);
					//Tooltip tooltip = new Tooltip();
					//tooltip.setTarget(label);
					node.getChildren().add(nodeFound);
					nodeFound.setIcon(folderIcon);
					recursiveFill(nodeFound, fileFound);
				}
			}
		}
	}
	
}
