package tistory.모던_자바_인_액션.chapter6;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class chapter6 {

	public static void main(String[] args) {
		System.out.println("\n>> 6.1 컬렉터란 무엇인가?");
		
		List<String> list = Arrays.asList("User1", "User3", "User4" , "User2", "User111");
		Stream<String> listStream = list.stream().filter(s -> s.length()==5);
		List<String> filterList = listStream.collect(Collectors.toList());
		System.out.println(filterList);
		

		System.out.println("\n>> 6.2 리듀싱과 요약");
		
		
		
		System.out.println("\n>> 6.3 그룹화");
		
		
		
		System.out.println("\n>> 6.4 분할");
		
		
		
		System.out.println("\n>> 6.5 Collector 인터페이스");
		
		
		
		System.out.println("\n>> 6.6 커스텀 컬렉터를 구현해서 성능 개선하기");
	}

}
