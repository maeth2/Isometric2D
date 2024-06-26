package com.components;

import org.joml.Vector2f;

import com.GameObject;
import com.utils.AssetManager;
import com.utils.Texture;
import com.utils.Transform;

public class AABBComponent extends Component {
	private Vector2f offset;
	private Transform transform;
	private boolean hasCollision = true;
	
	public static final Texture HITBOX_TEXTURE = AssetManager.getTexture("assets/textures/hitbox.png");
	public static final Texture BOX_TEXTURE = AssetManager.getTexture("assets/textures/blank.png");
	private TextureComponent texture;
	
	/**
	 * Initialise AABB (Axis Aligned Bounding Box)
	 * 
	 * @param offset		Offset from Attached Game Object Position
	 * @param scale			Size of the Bounding Box
	 */
	public AABBComponent(GameObject o, Vector2f offset, Vector2f scale) {
		this.offset = offset;
		this.transform = new Transform(new Vector2f(o.getTransform().getPosition().x + offset.x, o.getTransform().getPosition().y + offset.y), scale);
		this.texture = new TextureComponent(HITBOX_TEXTURE);
	}

	/**
	 * Update Bounding Box Position
	 * 
	 * @param dt			Time Delta
	 */
	@Override
	public void update(float dt) {
		this.transform.getPosition().x = this.gameObject.getTransform().getPosition().x + offset.x;
		this.transform.getPosition().y = this.gameObject.getTransform().getPosition().y + offset.y;
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
		if(!target.hasCollision) return -1;
		
		Transform t1 = this.getTransform();
		Transform t2 = target.getTransform();
		
		float targetLeft = t2.getPosition().x - (t2.getScale().x / 2) - (t1.getScale().x / 2);
		float targetRight = t2.getPosition().x + (t2.getScale().x / 2) + (t1.getScale().x / 2);
		float targetTop = t2.getPosition().y - (t2.getScale().y / 2) - (t1.getScale().y / 2);
		float targetBot = t2.getPosition().y + (t2.getScale().y / 2) + (t1.getScale().y / 2);
		
		float nearX = (targetLeft - t1.getPosition().x) / vel.x;
		float farX = (targetRight - t1.getPosition().x) / vel.x;
		float nearY = (targetTop - t1.getPosition().y) / vel.y;
		float farY = (targetBot - t1.getPosition().y) / vel.y;
	
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
		return getCollision(target, null);
	}
	
	/**
	 * Get AABB Normal Bound Collision
	 * 
	 * @param target		Target AABB to Check
	 * @param interact		Type to interact
	 * @return				Has Collision
	 */
	public boolean getCollision(AABBComponent target, Class<?> interact) {
		if(target == null) return false;
		if(!target.hasCollision) return false;
		if(interact != null && !interact.isAssignableFrom(target.gameObject.getClass())) return false;
		
		Transform t1 = this.getTransform();
		Transform t2 = target.getTransform();
		
		Vector2f tl = new Vector2f(t1.getPosition().x - (t1.getScale().x / 2), t1.getPosition().y - (t1.getScale().y / 2));
		Vector2f tr = new Vector2f(t1.getPosition().x + (t1.getScale().x / 2), t1.getPosition().y - (t1.getScale().y / 2));
		Vector2f bl = new Vector2f(t1.getPosition().x - (t1.getScale().x / 2), t1.getPosition().y + (t1.getScale().y / 2));
		Vector2f br = new Vector2f(t1.getPosition().x + (t1.getScale().x / 2), t1.getPosition().y + (t1.getScale().y / 2));
		
		float targetLeft = t2.getPosition().x - (t2.getScale().x / 2);
		float targetRight = t2.getPosition().x + (t2.getScale().x / 2);
		float targetTop = t2.getPosition().y - (t2.getScale().y / 2);
		float targetBot = t2.getPosition().y + (t2.getScale().y / 2);

		boolean tlCollision = tl.x >= targetLeft && tl.x <= targetRight && tl.y >= targetTop && tl.y <= targetBot;
		boolean trCollision = tr.x >= targetLeft && tr.x <= targetRight && tr.y >= targetTop && tr.y <= targetBot;
		boolean blCollision = bl.x >= targetLeft && bl.x <= targetRight && bl.y >= targetTop && bl.y <= targetBot;
		boolean brCollision = br.x >= targetLeft && br.x <= targetRight && br.y >= targetTop && br.y <= targetBot;
		
		return tlCollision || trCollision || blCollision || brCollision;
	}

	@Override
	public void start() {
	}
	
	public Transform getTransform() {
		return this.transform;
	}
	
	public TextureComponent getTexture() {
		return this.texture;
	}
	
	public void setCollision(boolean i) {
		this.hasCollision = i;
	}
	
	public boolean hasCollision() {
		return this.hasCollision;
	}
}
