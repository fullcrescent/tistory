package tistory.모던_자바_인_액션.chapter5;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class chapter5 {

	public static void main(String[] args) {
		System.out.println("\n>> 5.1 필터링");
		
		List<Integer> list1 = Arrays.asList(1,2,3,4,5,2,6);
		
		// 프레디케이트
		System.out.println(
				list1.stream().filter(i -> i%2==0).collect(Collectors.toList()));				// [2, 4, 2, 6]
		// 프레디케이트 + 고유 요소
		System.out.println(
				list1.stream().filter(i -> i%2==0).distinct().collect(Collectors.toList()));	// [2, 4, 6]
		
		
		System.out.println("\n>> 5.2 스트림 슬라이싱");
		
		System.out.println(
				list1);																	// [1, 2, 3, 4, 5, 2, 6]
		System.out.println(
				list1.stream().filter(i -> i<4).collect(Collectors.toList()));			// [1, 2, 3, 2]
		// takewhile
		System.out.println(
				list1.stream().takeWhile(i -> i<4).collect(Collectors.toList()));		// [1, 2, 3]
		// dropwhile
		System.out.println(
				list1.stream().dropWhile(i -> i<4).collect(Collectors.toList()));		// [4, 5, 2, 6]
		// limit
		System.out.println(
				list1.stream().filter(i -> i<4).limit(2).collect(Collectors.toList()));	// [1, 2]
		// skip
		System.out.println(
				list1.stream().filter(i -> i<4).skip(2).collect(Collectors.toList()));	// [3, 2]
		
		
		System.out.println("\n>> 5.3 매핑");
		
		List<String> userNameList = Arrays.asList("User1", "User3", "User~~~", "User2");
		
		List<Integer> userNameLengthList = userNameList.stream()
											.map(User::new)						// User 객체 이름으로 생성
											.map(User::getName)					// "User1", "User3", "User~~~", "User2"
											.map(String::length)				// 5, 5, 7, 5
											.collect(Collectors.toList());		// [5, 5, 7, 5]
		System.out.println(userNameLengthList);

		List<String[]> stringArrayList = userNameList.stream()
											.map(input -> input.split(""))
											.distinct()
											.collect(Collectors.toList());
		stringArrayList.forEach(input -> System.out.print(Arrays.stream(input).collect(Collectors.joining("", "<", "> "))));
		// <User1> <User3> <User~~~> <User2> 				
		System.out.println();
		
		List<String> stringList = userNameList.stream()
									.map(input -> input.split(""))
									.flatMap(Arrays::stream)			// 정보를 하나로 합침
									.distinct()
									.collect(Collectors.toList());	
		System.out.println(stringList);		// [U, s, e, r, 1, 3, ~, 2]
		
		
		System.out.println("\n>> 5.4 검색과 매칭");
		
		userNameList = Arrays.asList("User1", "User3", "User~~~", "User2");		// length [5, 5, 7, 5]
		// anyMatch
		System.out.println(
				userNameList.stream().anyMatch(input -> input.length()==5));	// true
		// allMatch
		System.out.println(
				userNameList.stream().allMatch(input -> input.length()==5));	// false
		// noneMatch
		System.out.println(
				userNameList.stream().noneMatch(input -> input.length()==6));	// true
		
		
		Optional<String> name = userNameList.stream()
									.filter(input -> input.length()==6)
									.findAny();
		System.out.println(name);												// Optional.empty
		System.out.println(name.isPresent());									// false				
		System.out.println(name.orElse("defalut"));								// defalut
//		System.out.println(name.get());											// java.util.NoSuchElementException
		
		name = userNameList.stream()
						.filter(input -> input.length()==5)
						.findAny();
		System.out.println(name);												// Optional[User1]
		System.out.println(name.isPresent());									// true			
		System.out.println(name.orElse("defalut"));								// User1
		System.out.println(name.get());											// User1
		name.ifPresent(System.out::println);									// User1
		
		
		name = userNameList.parallelStream()
				.filter(input -> input.length()==5)
				.findAny();
		System.out.println(name);												// Optional[User2]
		
		name = userNameList.parallelStream()
				.filter(input -> input.length()==5)
				.findFirst();
		System.out.println(name);												// Optional[User1]
		
		
		System.out.println("\n>> 5.5 리듀싱");
		
		
		
		System.out.println("\n>> 5.6 실전 연습");
		
		
		
		System.out.println("\n>> 5.7 숫자형 스트림");
		
		
		
		System.out.println("\n>> 5.8 스트림 만들기");
				
		
	}

}

class User{
	String name;

	public User(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
