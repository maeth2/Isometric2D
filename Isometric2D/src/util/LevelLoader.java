package util;

import org.joml.Vector2f;

import com.GameObject;
import com.components.AABBComponent;
import com.components.TextureComponent;

public class LevelLoader {
	
	public static GameObject[][] loadLevel(int width, int height, float unitSize, int[][] level) {
		level = Helper.reverse(level);
		GameObject grid[][] = new GameObject[height][width];
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				GameObject block = new GameObject( 
						"test",
						new Transform(
								new Vector2f(c * unitSize, r * unitSize), new Vector2f(unitSize / 2, unitSize / 2), new Vector2f(180f, 0f)
						)
					);
				if(level[r][c] == 2) {
					block.addComponent(new AABBComponent(new Vector2f(0, 0), new Vector2f(unitSize / 2, unitSize / 2)));
				}else if(level[r][c] == 1){
					block.addComponent(new TextureComponent(
							AssetManager.getTexture("assets/textures/walls.png"), 
							false,
							new Vector2f(1, 2),
							new Vector2f(16, 16)
					));
				}
				grid[r][c] = block;
			}
		}
		
		//First pass loads all the walls with side connections
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				int sx = -1;
				int sy = -1;
				if(level[r][c] == 0 || grid[r][c] == null || grid[r][c].getComponent(TextureComponent.class) != null) continue;
				
				boolean right = (c - 1) >= 0 ? level[r][c - 1] == 2 : false;
				boolean left = (c + 1) < width ? level[r][c + 1] == 2 : false;
				boolean up = (r + 1) < height ? level[r + 1][c] == 2 : false;
								
				if(!left && !right && !up) {
					sx = 1;
					sy = 0;
				}else if(left && right) {
					sx = 1;
					sy = 1;
				}
					
				if(sx != -1 && sy != -1) {
					grid[r][c].addComponent(new TextureComponent(
							AssetManager.getTexture("assets/textures/walls.png"), 
							true,
							new Vector2f(sx, sy),
							new Vector2f(16, 16)
					));
				}
			}
		}
		
		//Second pass loads all the walls with no side connections
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				int sx = -1;
				int sy = -1;
				if(level[r][c] == 0 || grid[r][c] == null || grid[r][c].getComponent(TextureComponent.class) != null) continue;
				
				int right = (c - 1) >= 0 ? level[r][c - 1] : 0;
				int left = (c + 1) < width ? level[r][c + 1] : 0;
				int up = (r + 1) < height ? level[r + 1][c] : 0;								
				
				if(right == 2 && left == 0 && up != 2) {
					sx = 0;
					sy = 1;
				}else if(right == 0 && left == 2 && up != 2) {
					sx = 2;
					sy = 1;
				}else if(right == 2 && left == 1 && up != 2) {
					sx = 3;
					sy = 1;
				}else if(right == 1 && left == 2 && up != 2) {
					sx = 4;
					sy = 1;
				}else if(right == 2 && left == 0 && up == 2) {
					sx = 0;
					sy = 3;
				}else if(right == 0 && left == 2 && up == 2) {
					sx = 2;
					sy = 3;
				}else if(right == 2 && left == 1 && up == 2) {
					sx = 3;
					sy = 2;
				}else if(right == 1 && left == 2 && up == 2) {
					sx = 4;
					sy = 2;
				}else if(right == 1 && left == 0) {
					sx = 0;
					sy = 2;
				}else if(right == 0 && left == 1) {
					sx = 2;
					sy = 2;
				}
				
				if(sx != -1 && sy != -1) {
					grid[r][c].addComponent(new TextureComponent(
							AssetManager.getTexture("assets/textures/walls.png"), 
							true,
							new Vector2f(sx, sy),
							new Vector2f(16, 16)
					));
				}
			}
		}
		return grid;
	}
}
