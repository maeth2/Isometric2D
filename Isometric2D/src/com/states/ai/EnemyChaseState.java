package com.states.ai;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector2i;

import com.Main;
import com.scenes.Scene;
import com.utils.Helper;
import com.utils.Maths;

public class EnemyChaseState extends EnemyState {
	private float elapsed;
	private float chaseDuration = 5f;
	
	public EnemyChaseState(EnemyStateMachine.state stateKey, EnemyContext context) {
		super(stateKey, context);
	}

	@Override
	public void enter() {
		elapsed = 0;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		elapsed += dt;
		Vector2i nextCell = getNextCell();
		float distance = Maths.getEuclideanDistance(
				context.getTarget().getTransform().getPosition().x, 
				context.getTarget().getTransform().getPosition().y, 
				context.getTargetEntity().getTransform().getPosition().x, 
				context.getTargetEntity().getTransform().getPosition().y
		);
		if(nextCell != null && distance >= this.context.chaseRadius) {
			travelToTarget((float)nextCell.x * Scene.UNIT_SIZE, (float)nextCell.y * Scene.UNIT_SIZE, dt);
		}else {
			nextState = EnemyStateMachine.state.Attack;
		}
		
		if(elapsed >= chaseDuration) {
			nextState = EnemyStateMachine.state.Roaming;
		}
	}
	
	public Vector2i getNextCell() {
		/*
		 * A-star algorithm
		 */
		
		int directions[][] = {
				{1, 0}, {-1, 0},
				{0, 1}, {0, -1},
				{1, 1}, {1, -1},
				{-1, 1}, {-1, -1},
		};
		
		int width = Main.getScene().getGridWidth();
		int height = Main.getScene().getGridHeight();
		float distances[][] = new float[height][width]; //Distance from start
		float hueristics[][] = new float[height][width]; //Hueristic distance
		int parents[][][] = new int[height][width][2]; //Parent Node
		boolean searched[][] = new boolean[height][width]; //Searched table
		
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				distances[r][c] = -1;
				hueristics[r][c] = Float.MAX_VALUE;
				parents[r][c][0] = -1;
				parents[r][c][1] = -1;
			}
		}
		
		List<Vector3f> pq = new ArrayList<Vector3f>();
		Vector2i start = context.getTarget().getGridPosition();
		Vector2i end = context.getTargetEntity().getGridPosition();
		
		if(start.x == end.x && start.y == end.y) {
			return null;
		}
	
		pq.add(new Vector3f(0, start.x, start.y));
		
		while(!pq.isEmpty()) {
			Vector3f cell = pq.get(0);
			int x = (int)cell.y;
			int y = (int)cell.z;
			
			if(x == end.x && y == end.y) {
				break;
			}
						
			pq.remove(0); //POP
			
			if(searched[y][x]) {
				continue;
			}
			searched[y][x] = true;
			
			for(int[] i : directions) {
				int nx = x + i[0];
				int ny = y + i[1];
				
				if(nx < 0 || nx >= width || ny < 0 || ny >= height) continue;
				if(searched[ny][nx] || Main.getScene().getLevelCollision()[ny][nx]) continue;
				 
				float md = Maths.getEuclideanDistance(end.x, end.y, nx, ny); //Manhattan distance
				float d = distances[y][x] + (i[0] == 0 || i[1] == 0 ? 1f : 1.41f); //Distance travelled
				float h = d + md; //Hueristic value
				
				if(distances[ny][nx] == -1 || hueristics[ny][nx] > h) {
					distances[ny][nx] = d;
					parents[ny][nx][0] = x;
					parents[ny][nx][1] = y;
					hueristics[ny][nx] = h;
					Vector3f c = new Vector3f(h, nx, ny);
					
					if(pq.size() > 0) {
						int j = Helper.binarySearch(
							pq, 
							c, 
							(a, b) -> {
								return a.x >= b.x;
							}
						);
						pq.add(j, c);
					}else {
						pq.add(c);
					}
				}
			}
		}
		
		//Find the next cell by traversing back from the final node to the initial node
		if(parents[end.y][end.x][0] != -1 && parents[end.y][end.x][1] != -1) {
			int x = end.x;
			int y = end.y;
			Vector2i ans = new Vector2i(x, y);
			while(!(x == start.x && y == start.y)) {
				ans.x = x;
				ans.y = y;
				x = parents[ans.y][ans.x][0];
				y = parents[ans.y][ans.x][1];
			}
			return ans;
		}
		return null; //Did not finish
	}
	
	public void reset() {
		elapsed = 0;
	}
}
