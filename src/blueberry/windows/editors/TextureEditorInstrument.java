package blueberry.windows.editors;

public enum TextureEditorInstrument {
	PENCIL("paintIcon"), ERASER("eraserIcon"), PICKER("pickerIcon");
	private String image;
	
	public String getImage() {
		return image;
	}
	
	private TextureEditorInstrument(String image) {
		this.image = image;
	}
	
}
