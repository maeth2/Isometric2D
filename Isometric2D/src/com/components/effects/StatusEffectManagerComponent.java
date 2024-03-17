package com.components.effects;

import java.util.HashMap;
import java.util.Map;

import com.components.Component;
import com.entities.Entity;

public class StatusEffectManagerComponent extends Component {
	private Map<Enum<?>, StatusEffect> currentEffects = new HashMap<Enum<?>, StatusEffect>();
	
	@Override
	public void start() {
	}

	@Override
	public void update(float dt) {
		for(Enum<?> i : currentEffects.keySet()) {
			StatusEffect e = currentEffects.get(i);
			if(e == null) continue;
			e.update(dt);
			if(e.checkDuration() || e.getFinished()) {
				e.exit();
				currentEffects.put(i, null);
			}
		}
	}
	
	public void add(StatusEffect.effects effect, Entity target, float duration, float strength) {
		if(currentEffects.containsKey(effect) && currentEffects.get(effect) != null) {
			currentEffects.get(effect).stack(duration);
		}else {
			StatusEffect e = StatusEffect.statusEffects.get(effect).create(target, duration, strength);
			e.start();
			currentEffects.put(effect, e);
		}
	}
}
