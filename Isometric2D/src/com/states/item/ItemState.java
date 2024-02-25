package com.states.item;

import org.joml.Vector2f;

import com.Main;
import com.listeners.MouseListener;
import com.states.State;
import com.utils.Animation;

public abstract class ItemState extends State<ItemStateMachine.itemStates> {
	protected ItemContext context;
	protected Animation frames;

	public ItemState(ItemContext context, ItemStateMachine.itemStates stateKey) {
		super(stateKey);
		this.context = context;
	}
	
	public void pointToMouse() {
		float sx = this.context.getItem().transform.position.x;
		float sy = this.context.getItem().transform.position.y;
		float mx = MouseListener.getX() + Main.getScene().getCamera().transform.position.x;
		float my = MouseListener.getY() + Main.getScene().getCamera().transform.position.y - context.getItem().transform.scale.y;
		float angle = (float) Math.toDegrees(Math.atan2((my - sy), (mx - sx))) - 90f;
		this.context.getItem().transform.rotation.x = angle;

		context.getItem().transform.scale.x = Math.abs(context.getItem().transform.scale.x) * (mx > sx ? -1 : 1);
		this.context.getItem().setDirty(true);
	}
	
	public void stickToEntity() {
		Vector2f entityPos = context.getItem().getEntity().transform.position;
		Vector2f entityScale = context.getItem().getEntity().transform.scale;
		
		this.context.getItem().transform.position.x = entityPos.x - entityScale.x * 0.3f;
		this.context.getItem().transform.position.y = entityPos.y - entityScale.y * 0.2f;
		
		Main.getScene().updateGrid(context.getItem());
		this.context.getItem().setDirty(true);
	}
	
	public abstract ItemState create(ItemContext context, ItemStateMachine.itemStates stateKey);
}
