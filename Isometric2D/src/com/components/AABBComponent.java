package com.components;

import org.joml.Vector2f;

import com.utils.AssetManager;
import com.utils.Texture;

public class AABBComponent extends Component {
	private Vector2f offset;
	public Vector2f scale;
	public Vector2f position;
	
	public static final Texture HITBOX_TEXTURE = AssetManager.getTexture("assets/textures/hitbox.png");
	public static final Texture BOX_TEXTURE = AssetManager.getTexture("assets/textures/blank.png");
	private Texture texture = HITBOX_TEXTURE;
	
	/**
	 * Initialise AABB (Axis Aligned Bounding Box)
	 * 
	 * @param offset		Offset from Attached Game Object Position
	 * @param scale			Size of the Bounding Box
	 */
	public AABBComponent(Vector2f offset, Vector2f scale) {
		this.offset = offset;
		this.scale = scale;
		this.position = new Vector2f();
	}

	/**
	 * Update Bounding Box Position
	 * 
	 * @param dt			Time Delta
	 */
	@Override
	public void update(float dt) {
		this.position.x = this.gameObject.transform.position.x + offset.x;
		this.position.y = this.gameObject.transform.position.y + offset.y;
	}
	
	/**
	 * Check for AABB Swept Collisions
	 * 
	 * @param vel			Velocity of current AABB
	 * @param target		Target AABB to Check
	 * @return Time to the nearest Hit Point
	 */
	public float getAABBSweptCollision(Vector2f vel, AABBComponent target) {
		return getAABBSweptCollision(vel, target, new Vector2f());
	}
	
	/**
	 * Check for AABB Swept Collisions
	 * 
	 * @param vel				Velocity of current AABB
	 * @param target			Target AABB to Check
	 * @return contactNormal		Surface Normal at the Point of Contact
	 * @return Time to the nearest Hit Point
	 */
	public float getAABBSweptCollision(Vector2f vel, AABBComponent target, Vector2f contactNormal) {
		if(target == null) return -1;

		float targetLeft = target.position.x - target.scale.x - this.scale.x;
		float targetRight = target.position.x + target.scale.x + this.scale.x;
		float targetTop = target.position.y - target.scale.y - this.scale.y;
		float targetBot = target.position.y + target.scale.y + this.scale.y;
		
		float nearX = (targetLeft - this.position.x) / vel.x;
		float farX = (targetRight - this.position.x) / vel.x;
		float nearY = (targetTop - this.position.y) / vel.y;
		float farY = (targetBot - this.position.y) / vel.y;
	
		if(farX < nearX) {
			float temp = farX;
			farX = nearX;
			nearX = temp;
		}
		
		if(farY < nearY) {
			float temp = farY;
			farY = nearY;			
			nearY = temp;
		}
		
		float hitNear = nearX > nearY ? nearX : nearY;
		
		if(nearX < farY && nearY < farX && hitNear <= 1 && hitNear >= 0) {
			if(nearX > nearY) {
				contactNormal.y = 0;
				if(vel.x < 0) {
					contactNormal.x = 1;
				}else if(vel.x > 0) {
					contactNormal.x = -1;
				}
			}else {
				contactNormal.x = 0;
				if(vel.y < 0) {
					contactNormal.y = 1;
				}else if(vel.y > 0) {
					contactNormal.y = -1;
				}
			}
			return Math.abs(hitNear);
		}
		
		return -1;
	}
	
	/**
	 * Get AABB Normal Bound Collision
	 * 
	 * @param target		Target AABB to Check
	 * @return				Has Collision
	 */
	public boolean getCollision(AABBComponent target) {
		if(target == null) return false;
		
		Vector2f tl = new Vector2f(this.position.x - this.scale.x, this.position.y - this.scale.y);
		Vector2f tr = new Vector2f(this.position.x + this.scale.x, this.position.y - this.scale.y);
		Vector2f bl = new Vector2f(this.position.x - this.scale.x, this.position.y + this.scale.y);
		Vector2f br = new Vector2f(this.position.x + this.scale.x, this.position.y + this.scale.y);
		
		float targetLeft = target.position.x - target.scale.x;
		float targetRight = target.position.x + target.scale.x;
		float targetTop = target.position.y - target.scale.y;
		float targetBot = target.position.y + target.scale.y;

		boolean tlCollision = tl.x >= targetLeft && tl.x <= targetRight && tl.y >= targetTop && tl.y <= targetBot;
		boolean trCollision = tr.x >= targetLeft && tr.x <= targetRight && tr.y >= targetTop && tr.y <= targetBot;
		boolean blCollision = bl.x >= targetLeft && bl.x <= targetRight && bl.y >= targetTop && bl.y <= targetBot;
		boolean brCollision = br.x >= targetLeft && br.x <= targetRight && br.y >= targetTop && br.y <= targetBot;
		
		return tlCollision || trCollision || blCollision || brCollision;
	}

	@Override
	public void start() {}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	public void setTexture(Texture t) {
		this.texture = t;
	}
}
