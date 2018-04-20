package blueberry.windows;

import com.kotcrab.vis.ui.widget.VisWindow;

import blueberry.resources.UiAssetsLoader;

public class BlueberryNoTitleStandardWindow extends VisWindow {

	public BlueberryNoTitleStandardWindow() {
		super("");
		getTitleTable().clearChildren();
		setMovable(false);
		background(UiAssetsLoader.getNinepatchDrawable("grayStandardWindow"));
	}

}
