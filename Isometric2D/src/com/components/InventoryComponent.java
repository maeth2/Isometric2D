package com.components;

import java.util.ArrayList;
import java.util.List;
import com.entities.items.Item;

public class InventoryComponent extends Component {
	private List<Item> inventory = new ArrayList<Item>();
	private Item selected;
	
	@Override
	public void start() {}

	@Override
	public void update(float dt) {}
	
	public List<Item> getInventory(){
		return this.inventory;
	}
	
	public void add(Item i) {
		inventory.add(i);
	}
	
	public void remove(Item i) {
		//TODO
	}
	
	public void setSelected(Item i) {
		this.selected = i;
	}
	
	public Item getSelected() {
		return this.selected;
	}
}
