package tistory.모던_자바_인_액션.chapter08;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.Map.Entry;
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
		
		
		/* 내부적으로 불필요한 객체 할당을 필요로 하고, 데이터의 추가 및 삭제 불가, 수정만 가능 */
		list = Arrays.asList("user1", "user2", "user3");
		System.out.println(list);		// [user1, user2, user3]
		list.set(0, "user~~");		
		//list.add("user3");			// java.lang.UnsupportedOperationException
		System.out.println(list);		// [user~~, user2, user3]
					
		Set<String> set;
		set = new HashSet<>(Arrays.asList("user1", "user2", "user3"));
		System.out.println(set);		// [user1, user2, user3]
		set = Stream.of("user1", "user2", "user3").collect(Collectors.toSet());
		System.out.println(set);		// [user1, user2, user3]
		
		
		/* 내부적으로 불필요한 객체 할당이 없고, 데이터의 추가, 삭제, 수정 불가 */
		list = List.of("user~~", "user2");
		//list.set(0, "user1");			// java.lang.UnsupportedOperationException
		//list.add("user3");			// java.lang.UnsupportedOperationException
		System.out.println(list);		// [user~~, user2]
				
		set = Set.of("user1", "user2");
		//set.add("user3");				// java.lang.UnsupportedOperationException
		System.out.println(set);
				
		Map<String, Integer> map = Map.of("user1", 111, "user2", 222, "user3", 333);
		//map.put("user4", 444);		// java.lang.UnsupportedOperationException
		System.out.println(map);		// {user2=222, user1=111, user3=333}
		
		/* 열개 초과의 키와 값 쌍을 가진맵을 인수로 받을때 */
		map = Map.ofEntries(Map.entry("user1", 111), Map.entry("user2", 222), Map.entry("user3", 333));
		//map.put("user4", 444);		// map.put("user4", 444);			
		System.out.println(map);		// {user3=333, user2=222, user1=111}
		
		
		
		System.out.println("\n>> 8.2 리스트와 집합 처리");
		
		/* removeIf */
		list = new ArrayList<>();
		list.add("user1");
		list.add("user2");
		list.add("user3");
				
		//for(String s : list) {
		//	list.remove(s);					// java.util.ConcurrentModificationException
		//}
		
		/* 위의 작업과 동일 */
		//for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
		//	String s = iterator.next();
		//	list.remove(s);					// java.util.ConcurrentModificationException
		//}
		
		for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
			if(iterator.next().equals("user2")) {
				iterator.remove();
			}
		}
		System.out.println(list);				// [user1, user3]
		
		list.removeIf(i -> i.equals("user1"));
		System.out.println(list);				// [user3]
		
		
		/* replaceAll */
		list = new ArrayList<>();
		list.add("user1");
		list.add("user2");
		list.add("user3");
		
		System.out.println(
			list.stream()
				.map(i -> i.replaceAll("user", "replace"))
				.collect(Collectors.toList()));					// [replace1, replace2, replace3] 문자열을 생성
		
		for(ListIterator<String> iterator = list.listIterator();iterator.hasNext();) {
			String temp = iterator.next();
			iterator.set(temp.replaceAll("user", "replace"));	
		}
		System.out.println(list);		// [replace1, replace2, replace3]
		
		list.replaceAll(i -> i.replaceAll("replace", "user"));
		System.out.println(list);		// [user1, user2, user3]
				
				
		
		System.out.println("\n>> 8.3 맵 처리");
		
		map = Map.of("user1", 111, "user2", 222, "user3", 333);
		
		/* foreach */
		for(Map.Entry<String, Integer> entry : map.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			System.out.print(key + ":" + value + "/");				// user3:333/user1:111/user2:222/
		}
		System.out.println();
		
		map.forEach((s, i) -> System.out.print(s + ":" + i + "/"));	// user3:333/user1:111/user2:222/
		System.out.println();
		
		/* 정렬 */
		map.entrySet().stream().sorted(Entry.comparingByKey()).forEachOrdered(System.out::print);	// user1=111user2=222user3=333
		System.out.println();
		
		/* getOrDefault */
		System.out.println(
				map.getOrDefault("user4", 444));					// 444
		
		/* 계산 */
		Map<String, byte[]> userData = new HashMap<>();
		userData.put("user2", "2222".getBytes());
		
		map.forEach((s, i) -> userData.computeIfAbsent(s, Chapter8::calculate));
		userData.forEach((s, b) -> System.out.println(new String(b)));				// "2222" 만 정상 출력
		
		/* 삭제 패턴 */
		map = new HashMap<>();
		map.put("user1", 111);
		map.put("user2", 222);
		map.put("user3", 333);
		
		String key = "user2";
		Integer value = 222;
		if(map.containsKey("user2") && Objects.equals(map.get(key), value)) {
			map.remove(key);
		}
		System.out.println(map);		// {user1=111, user3=333}
		
		map.remove("user1", 1);
		map.remove("user3", 333);
		System.out.println(map);		// {user1=111}
		
		
		/* 교체 패턴 */
		map = new HashMap<>();
		map.put("user1", 111);
		map.put("user2", 222);
		map.put("user3", 333);
		
		map.replace("user2", 22222);
		map.replaceAll((s, i) -> {
			if(s.equals("user1")) {
				return 0;
			}
			return i;
		});
		System.out.println(map);		// {user1=0, user2=22222, user3=333}
		
		/* 합침 */
		Map<String, Integer> map1 = Map.ofEntries(Map.entry("user",111), Map.entry("user1",1), Map.entry("user11", 11));
		Map<String, Integer> map2 = Map.ofEntries(Map.entry("user",222), Map.entry("user2",2), Map.entry("user22", 22));
		Map<String, Integer> map3 = new HashMap<>(map1);
		
		map2.forEach((s, i) -> map3.merge(s, i, (i1, i2) -> i1+i2));
		System.out.println(map3);		// {user22=22, user1=1, user11=11, user2=2, user=333}
		
		System.out.println("\n>> 8.4 개선된 ConcurrentHashMap");
		
		ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
		long parallelismThreshold = 4;
		concurrentMap.put("user1", 111);
		concurrentMap.put("user2", 222);
		concurrentMap.put("user3", 333);
		
		System.out.println( 
				concurrentMap.searchKeys(parallelismThreshold, t -> t.equals("user1")));	// true
		System.out.println( 
				concurrentMap.reduceValues(parallelismThreshold, Integer::max));			// 333
		System.out.println( 
				concurrentMap.mappingCount());												// 3
		System.out.println(
				concurrentMap.keySet());													// [user1, user2, user3]
		
		Set<String> set1 = ConcurrentHashMap.newKeySet();									// ConcurrentHashMap으로 유지되는 집합 생성
		System.out.println(set1);															// []
	}

	private static byte[] calculate(String key) {
		MessageDigest messageDigest =null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
	}
}