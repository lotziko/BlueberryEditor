package blueberry.windows.editors;

public enum TilemapEditorInstrument {
	PENCIL("paintIcon"), ERASER("eraserIcon"), PICKER("pickerIcon"), SELECTOR ("selectorIcon"), MOVE_AREA ("moveAreaIcon");
	private String image;
	
	public String getImage() {
		return image;
	}
	
	private TilemapEditorInstrument(String image) {
		this.image = image;
	}
	
}

