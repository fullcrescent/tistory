package tistory.모던_자바_인_액션.chapter8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Chapter8 {

	public static void main(String[] args) {
		System.out.println("\n>> 8.1 컬렉션 팩토리");
		
		List<String> list;
		list = new ArrayList<>();
		list.add("user1");
		list.add("user2");
		list.add("user3");
		System.out.println(list);		// [user1, user2, user3]
		
		list = Arrays.asList("user1", "user2", "user3");
		System.out.println(list);		// [user1, user2, user3]
		
		list = Arrays.asList("user~~", "user2");
		list.set(0, "user1");		
//		user.add("user3");				// java.lang.UnsupportedOperationException
		System.out.println(list);		// [user1, user2]
		
		
		Set<String> set;
		set = new HashSet<>(Arrays.asList("user1", "user2", "user3"));
		System.out.println(set);		// [user1, user2, user3]
		
		set = Stream.of("user1", "user2", "user3").collect(Collectors.toSet());
		System.out.println(set);		// [user1, user2, user3]
		
		System.out.println("\n>> 8.2 리스트와 집합 처리");
		
		
		System.out.println("\n>> 8.3 맵 처리");
		
		
		System.out.println("\n>> 8.4 개선된 ConcurrentHashMap");
		
		
		
	}

}
