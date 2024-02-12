package util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Sort {
	public static <T> List<T> sort(List<T> a, BiFunction<T, T, Boolean> compare){
		List<T> sorted = new ArrayList<T>();
		for(T i : a) {
			int l = 0;
			int r = sorted.size() - 1;
			while(l <= r) {
				int m = (l + r) / 2;
				if(compare.apply(i, sorted.get(m))) {
					l = m + 1;
				}else {
					r = m - 1;
				}
			}
			if(sorted.size() == 0) {
				sorted.add(i);
			}else {
				sorted.add(l, i);
			}
		}
		return sorted;
	}
}
