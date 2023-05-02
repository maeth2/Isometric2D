package util;

public class Texture {
	public static final int TYPE_OUTPUT = 0;
	public static final int TYPE_COLOR = 1;
	public static final int TYPE_NORMAL = 2;
	public static final int TYPE_DEPTH = 3;
	public static final int TYPE_OUTLINE = 4;
	public static final int TYPE_LINEAR_DEPTH = 5;
	public static final int TYPE_PIXEL = 6;

	private int id;
	private int width, height;
	private int type;

	public Texture(int id, int width, int height) {
		init(0, id, width, height);
	}
	
	public Texture(int type, int id, int width, int height) {
		init(type, id, width, height);
	}
	
	public Texture() {
		init(0, 0, 0, 0);
	}
	
	public void init(int type, int id, int width, int height) {
		this.type = type;
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public void copy(Texture t) {
		this.id = t.getID();
		this.width = t.getHeight();
		this.height = t.getWidth();
		this.type = t.getType();
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getType() {
		return this.type;
	}
}
