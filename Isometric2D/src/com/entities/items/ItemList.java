package com.entities.items;

import java.util.HashMap;
import java.util.Map;

import com.entities.items.weapons.specialstick.SpecialStickWeapon;
import com.entities.items.weapons.syringe.SyringeWeapon;

public class ItemList {
	public static enum item{
		Syringe,
		SpecialStick
	}
	
	public static final Map<ItemList.item, Item> weapons = new HashMap<ItemList.item, Item>();
	static {
		weapons.put(ItemList.item.Syringe, new SyringeWeapon(null, null));
		weapons.put(ItemList.item.SpecialStick, new SpecialStickWeapon(null, null));
	}
	
	public static Item get(ItemList.item i) {
		return weapons.get(i);
	}
}

