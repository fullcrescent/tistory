package tistory.모던_자바_인_액션.chapter09;

import java.util.Arrays;
import java.util.List;

public class Chapter9_Debugging {
	public static void main(String[] args) {
		List<Integer> debuggingList;
		debuggingList = Arrays.asList(1, 2, 3, null);
		debuggingList.stream().map(i -> i*2).forEach(System.out::println);
	}
}
