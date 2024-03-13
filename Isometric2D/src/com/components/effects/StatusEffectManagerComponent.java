package com.components.effects;

import java.util.ArrayList;
import java.util.List;
import com.components.Component;
import com.entities.Entity;

public class StatusEffectManagerComponent extends Component {
	private List<StatusEffect> currentEffects = new ArrayList<StatusEffect>();
	
	@Override
	public void start() {
	}

	@Override
	public void update(float dt) {
		for(int i = 0; i < currentEffects.size(); i++) {
			StatusEffect e = currentEffects.get(i);
			e.update(dt);
			if(e.checkDuration()) {
				e.exit();
				currentEffects.remove(i);
			}
		}
	}
	
	public void add(StatusEffect.effects effect, Entity target, float duration, float strength) {
		StatusEffect e = StatusEffect.statusEffects.get(effect).create(target, duration, strength);
		e.start();
		currentEffects.add(e);
	}
}
