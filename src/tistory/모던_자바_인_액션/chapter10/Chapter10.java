package tistory.모던_자바_인_액션.chapter10;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Chapter10 {

	public static void main(String[] args) {
		System.out.println("\n>> 10.1 도메인 전용 언어");
		
		/* 코드의 잡음 */
		List<String> list = Arrays.asList("111", "222", "333");
		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		});
		
		list.forEach(s -> System.out.println(s));
		
		list.forEach(System.out::println);
		
		
		System.out.println("\n>> 10.2 최신 자바 API의 작은 DSL");
		
		
		
		System.out.println("\n>> 10.3 자바로 DSL을 만드는 패턴과 기법");
		
		System.out.println("\n>> 10.4 실생활의 자바8 DSL");
		
		
	}

}
