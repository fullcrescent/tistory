package tistory.모던_자바_인_액션.chapter5;

import java.util.Arrays;
import java.util.List;
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
		
		
		
				
		
	}

}
