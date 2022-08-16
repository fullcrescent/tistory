package tistory.모던_자바_인_액션.chapter05;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Chapter5 {

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
		
		list1 = Arrays.asList(1,2,3,4,5,2,6);
		
		int sum1 = list1.stream().reduce(0, Integer::sum);
		System.out.println(sum1);											// 23
		
		Optional<Integer> sum2 = list1.stream().reduce(Integer::sum); 		// Optional[23]
		System.out.println(sum2);
		
		sum1 = list1.stream().filter(i->i>7).reduce(0, Integer::sum);		
		sum2 = list1.stream().filter(i->i>7).reduce(Integer::sum); 
		System.out.println(sum1);											// 0
		System.out.println(sum2);											// Optional.empty
		
		Optional<Integer> max = list1.stream().reduce(Integer::max);
		Optional<Integer> min = list1.stream().reduce(Integer::min);
		System.out.println(max);											// Optional[6]		
		System.out.println(min);											// Optional[1]
		
		Optional<Integer> count = list1.stream()
									.map(o -> 1)
									.reduce(Integer::sum);
		System.out.println(count);											// Optional[7]
		
		
		System.out.println("\n>> 5.6 실전 연습");
		
		userNameList = Arrays.asList("User1", "User3", "User~~~", "User2");
		System.out.println(
				userNameList.stream().sorted().reduce("", String::concat));		// User1User2User3User~~~
		System.out.println(
				userNameList.stream().sorted().collect(Collectors.joining()));	// User1User2User3User~~~
		
		list1 = Arrays.asList(1,2,3,4,5,2,6);
		System.out.println(
				list1.stream().min(Integer::compareTo));						// Optional[1]
		
		
		System.out.println("\n>> 5.7 숫자형 스트림");
		
		userNameList = Arrays.asList("User1", "User3", "User~~~", "User2");
		Integer totalLengthBoxed = userNameList.stream()
									.mapToInt(String::length)					// IntStream 반환
									.boxed()									// 일반 스트림으로 변환
									.reduce(0, Integer::sum);					// 박싱 비용 들면서 계산
		System.out.println(totalLengthBoxed);									// 22

		int totalLength = userNameList.stream()
							.mapToInt(String::length)							// IntStream 반환
							.sum();												// 리듀싱에 비해 박싱 비용 절약
		System.out.println(totalLength);										// 22

		OptionalInt optional = userNameList.stream()
								.mapToInt(String::length)
								.filter(i -> i>7)
								.max();
		System.out.println(optional);											// OptionalInt.empty
		System.out.println(optional.orElse(12345));								// 12345
		
		Stream<double[]> pythagoreanTriple = 
				IntStream.rangeClosed(1, 100)
							.boxed()									// 스트림으로 변환
							.flatMap(a -> IntStream.rangeClosed(a, 100)	// 생성된 각각의 스트림을 하나의 평준화된 스트림으로 변경 
													.mapToObj(b -> 
														new double[] {a, b, Math.sqrt(a*a + b*b)})
									)
							.filter(s -> s[2]%1==0);
		
		pythagoreanTriple.limit(2).forEach(s -> System.out.println((int)s[0] + ", " + (int)s[1] + ", " + (int)s[2]));
		// 3, 4, 5
		// 5, 12, 13

		
		System.out.println("\n>> 5.8 스트림 만들기");
		
		Stream<String> stream;
		
		// 값
		stream = Stream.of("Modern ", "Java ", "In ", "Action");
		stream.forEach(System.out::print);							// Modern Java In Action
		System.out.println();
		
		// 빈값
		stream = Stream.empty();
		stream.forEach(System.out::println);						// 출력 없음
		
		// null이 될 수 있는 객체
		stream = System.getProperty("user.home")==null ? Stream.empty() : Stream.of("null check");
		stream.forEach(System.out::println);						// null check
		
		stream = Stream.ofNullable(System.getProperty("user.home"));
		stream.forEach(System.out::println);						// home 경로 출력
		
		stream = Stream.of("config", "user.home", "home").flatMap(key -> Stream.ofNullable(System.getProperty(key)));
		stream.forEach(System.out::println);						// System에 속성값이 선언된 것만 출력
		
		// 배열
		String[] tempString = {"a", "b"};
		stream = Arrays.stream(tempString);
		stream.forEach(System.out::print);							// ab
		System.out.println();
		
		// 파일
		long wordCount = 0;
		try(Stream<String> lines = 
				Files.lines(Paths.get("README.md"), Charset.defaultCharset())){		// 스트림은 자원을 자동 해제 -> finally 필요 없음
			wordCount = lines.flatMap(line -> Arrays.stream(line.split(" ")))		// 단어 수 계산
								.distinct()											// 중복 제거
								.count();											// 카운트
		}catch(IOException e) {
			System.out.println("파일 오픈 중 예외 발생");
		}
		System.out.println(wordCount);												// 고유 단어 수 출력
		
		// 함수 -> 무한 스트림(언바운드 스트림) 생성
		Stream.iterate(new int[] {0, 1}, t -> new int[] {t[1], t[0]+t[1]})			// 피보나치 수열 생성
				.limit(10)															// 10개 요소
				.map(t -> t[0])														// 배열 중 첫 요소를 값으로 매핑
				.takeWhile(t -> t<13)												// 13미만인 경우로 제한
				.forEach(t -> System.out.print(t + " -> "));						// 0 -> 1 -> 1 -> 2 -> 3 -> 5 -> 8 -> 
		System.out.println();
		
		Stream.generate(Math::random)
				.limit(5)
				.forEach(System.out::println);										// 랜덤 숫자 5개 출력
		
		IntSupplier fib = new IntSupplier() {
			private int prev = 0;
			private int next = 1;
			@Override
			public int getAsInt() {
				int temp = prev;
				prev = next;
				next = temp+next;
				return temp;
			}
		};
		
		IntStream.generate(fib).limit(5).forEach(t -> System.out.print(t + " -> ")); 
		// 0 -> 1 -> 1 -> 2 -> 3 -> 
		System.out.println();

		System.out.println(
				IntStream.generate(fib).limit(25).max());			// 위와 연결됨
		// (고정) OptionalInt[514229]
		System.out.println(
				Stream.iterate(new int[] {0, 1}, t -> new int[] {t[1], t[0]+t[1]}).parallel().limit(30).map(t->t[0]).max(Integer::compareTo));
		// (고정) Optional[514229]
		System.out.println(
				IntStream.generate(fib).parallel().limit(30).max());
		// (랜덤) OptionalInt[1925023969] OptionalInt[1836311903] OptionalInt[1927860330] 등등
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
