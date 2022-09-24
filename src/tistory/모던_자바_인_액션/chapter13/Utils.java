package tistory.모던_자바_인_액션.chapter13;

import java.util.List;

public class Utils {
	public static void paint(List<Resizable> list) {
		list.forEach(r -> {
			r.setAbsoluteSize(0, 0);
			r.draw();
		});
	}
}
