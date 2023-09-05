package util;

public class Texture {
	public static final int TYPE_OUTPUT = 0;
	public static final int TYPE_COLOR = 1;
	public static final int TYPE_ALPHA = 2;

	private int id;
	private float width, height;
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
		this.width = t.getWidth();
		this.height = t.getHeight();
		this.type = t.getType();
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getType() {
		return this.type;
	}
	
	public float getAspectRatio() {
		return width / height;
	}
}
