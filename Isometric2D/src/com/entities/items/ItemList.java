package com.entities.items;

import java.util.HashMap;
import java.util.Map;

import com.entities.items.weapons.specialstick.SpecialStickWeapon;
import com.entities.items.weapons.syringe.SyringeWeapon;

public class ItemList {
	public static final Map<String, Item> weapons = new HashMap<String, Item>();
	static {
		weapons.put("Syringe", new SyringeWeapon("Syringe", null));
		weapons.put("Special_stick", new SpecialStickWeapon("Special_stick", null));
	}
	
	public static Item get(String name) {
		return weapons.get(name);
	}
}

