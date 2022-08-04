package tistory.모던_자바_인_액션.chapter8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		
		list = List.of("user~~", "user2");
//		list.set(0, "user1");			// java.lang.UnsupportedOperationException
//		list.add("user3");				// java.lang.UnsupportedOperationException
		System.out.println(list);		// [user~~, user2]
		
		set = Set.of("user1", "user2");
//		set.add("user3");				// java.lang.UnsupportedOperationException
		System.out.println(set);
		
		Map<String, Integer> map = Map.of("user1", 111, "user2", 222, "user3", 333);
		System.out.println(map);		// {user2=222, user1=111, user3=333}
		
		// 열개 초과의 키와 값 쌍을 가진맵을 인수로 받을때
		map = Map.ofEntries(Map.entry("user1", 111), Map.entry("user2", 222), Map.entry("user3", 333));
		System.out.println(map);		// {user3=333, user2=222, user1=111}
		
		
		System.out.println("\n>> 8.2 리스트와 집합 처리");
		
		
		list = new ArrayList<>();
		list.add("user1");
		list.add("user2");
		list.add("user3");
		
//		for(String s : list) {
//			list.remove(s);		// java.util.ConcurrentModificationException
//		}
		
//		for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
//			String s = iterator.next();
//			list.remove(s);					// java.util.ConcurrentModificationException
//		}
		
		for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
			iterator.next();
			iterator.remove();
		}
		System.out.println(list);				// []
		
		list.add("user1");
		list.add("user2");
		list.add("user3");
		
		list.removeIf(i -> i.equals("user1") || i.equals("user3"));
		System.out.println(list);				// [user2]
		
		
		System.out.println("\n>> 8.3 맵 처리");
		
		
		System.out.println("\n>> 8.4 개선된 ConcurrentHashMap");
		
		
		
	}

}
