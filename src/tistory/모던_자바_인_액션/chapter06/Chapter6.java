package tistory.모던_자바_인_액션.chapter06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Chapter6 {

	public static void main(String[] args) {
		System.out.println("\n>> 6.1 컬렉터란 무엇인가?");

		List<String> stringList = Arrays.asList("User1", "User3", "User4" , "User2", "User111");
		Stream<String> listStream = stringList.stream().filter(s -> s.length()==5);
		List<String> filterList = listStream.collect(Collectors.toList());
		System.out.println(filterList);
		

		System.out.println("\n>> 6.2 리듀싱과 요약");
		
		stringList = Arrays.asList("User1", "User3", "User4" , "User2", "User111");
		Long count;
		
		count = stringList.stream().collect(Collectors.counting());
		System.out.println(count);				// 5
		
		count = stringList.stream().count();
		System.out.println(count);				// 5
		
		List<Integer> integerList = Arrays.asList(1, 2, 5, 6, 3, 4);
		Comparator<Integer> comp = Integer::compareTo;
		Optional<Integer> optionalInteger;
		
		optionalInteger = integerList.stream().collect(Collectors.maxBy(comp));
		System.out.println(optionalInteger);	// Optional[6]
		
		optionalInteger = integerList.stream().collect(Collectors.minBy(comp));
		System.out.println(optionalInteger);	// Optional[1]
		
		int total = integerList.stream().collect(Collectors.summingInt(Integer::intValue));
		System.out.println(total);				// 21
		
		double avg = integerList.stream().collect(Collectors.averagingDouble(Integer::intValue));
		System.out.println(avg);				// 3.5
		
		IntSummaryStatistics summary = integerList.stream().collect(Collectors.summarizingInt(Integer::intValue));
		System.out.println(summary);			// IntSummaryStatistics{count=6, sum=21, min=1, average=3.500000, max=6}
		
		System.out.println(
				stringList.stream().collect(Collectors.joining(", ", "<", ">")));					
		// <User1, User3, User4, User2, User111>
		
		total = integerList.stream().collect(Collectors.reducing(100, Integer::intValue, Math::addExact));
		System.out.println(total);				// 121
		total = integerList.stream().map(Integer::intValue).reduce(Integer::sum).get();
		System.out.println(total);				// 121
		total = integerList.stream().mapToInt(Integer::intValue).sum();
		System.out.println(total);				// 121
		
		
		System.out.println("\n>> 6.3 그룹화");
		
		List<User> userList = new ArrayList<>(Arrays.asList(
				 new User("User0", "emp", 0)
				,new User("User1", "emp", 10)
				,new User("User2", "usr", 20)
				,new User("User5", "usr", 50)
				,new User("User3", "emp", 30)
				,new User("User4", "adm", 40)
			)
		);
		
		// 그룹화
		Map<String, List<User>> userGroupTypeMap;
		userGroupTypeMap = userList.stream().collect(Collectors.groupingBy(User::getType));		
		System.out.println(userGroupTypeMap);				// {usr=[User2, User5], emp=[User0, User1, User3], adm=[User4]}
		
		// 다수준 그룹화
		Map<String, Map<String, List<User>>> userGroupTypeAgeMap;
		userGroupTypeAgeMap = userList.stream().collect(Collectors.groupingBy(User::getType, 
															Collectors.groupingBy(user -> {
																		if(user.getAge()>=30) {
																			return user.getType() + "MoreThan30";
																		}else {
																			return user.getType() + "Under30";
																		}
																	})));
		System.out.println(userGroupTypeAgeMap);
		// {usr={usrMoreThan30=[User5], usrUnder30=[User2]}, emp={empUnder30=[User0, User1], empMoreThan30=[User3]}, adm={admMoreThan30=[User4]}}
		
		// 서브 그룹으로 데이터 수집
		Map<String, Long> userGroupTypeCountMap;
		userGroupTypeCountMap = userList.stream().collect(Collectors.groupingBy(User::getType, Collectors.counting()));
		System.out.println(userGroupTypeCountMap);			// {usr=2, emp=3, adm=1}
		
		userGroupTypeCountMap = userList.stream().collect(Collectors.groupingBy(User::getType, 
															Collectors.summingLong(User::getAge)));
		System.out.println(userGroupTypeCountMap);			// {usr=70, emp=40, adm=40}
		
		Map<String, User> userGroupTypeMaxMap;
		userGroupTypeMaxMap = userList.stream().collect(Collectors.groupingBy(User::getType, 
															Collectors.collectingAndThen(Collectors.maxBy(
																	Comparator.comparingLong(User::getAge)), Optional::get)));
		System.out.println(userGroupTypeMaxMap);			// {usr=User5, emp=User3, adm=User4}
		
		
		System.out.println("\n>> 6.4 분할");
		
		userList = new ArrayList<>(Arrays.asList(
				 new User("User0", "emp", 0)
				,new User("User1", "emp", 10)
				,new User("User2", "usr", 20)
				,new User("User5", "usr", 50)
				,new User("User3", "emp", 30)
				,new User("User4", "adm", 40)
			)
		);
		
		Map<Boolean, List<User>> userPartitionMap;
		userPartitionMap = userList.stream()
								.collect(Collectors.partitioningBy(i -> i.getType().equals("emp")));
		System.out.println(userPartitionMap);				// {false=[User2, User5, User4], true=[User0, User1, User3]}
		System.out.println(userPartitionMap.get(true));		// [User0, User1, User3]
		
		Map<Boolean, User> userPartitionMaxMap;
		userPartitionMaxMap = userList.stream()
								.collect(Collectors.partitioningBy(i -> i.getType().equals("emp"), 
										Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(User::getAge)), Optional<User>::get)));
		System.out.println(userPartitionMaxMap.get(true));	// User3
		System.out.println(userPartitionMaxMap.get(false)); // User5

		Function<Integer, Boolean> function = new Function<Integer, Boolean>(){
			public Boolean apply(Integer input) {
				return IntStream.rangeClosed(2, (int) Math.sqrt(input)).noneMatch(i -> input%i==0);
			}
		};
		Map<Boolean, List<Integer>> primes = IntStream.rangeClosed(2, 20).boxed().collect(Collectors.partitioningBy(i -> function.apply(i)));
		System.out.println(primes.get(true));	// [2, 3, 5, 7, 11, 13, 17, 19]
		
		System.out.println("\n>> 6.5 Collector 인터페이스");
		
		Collector<User, List<User>, List<User>> collector = new Collector<User, List<User>, List<User>>() {
			@Override
			// 새로운 결과 컨테이너 생성
			public Supplier<List<User>> supplier() {
				return ArrayList::new;
			}

			@Override
			// 결과 컨테이너에 요소 추가
			public BiConsumer<List<User>, User> accumulator() {
				return List::add;
			}

			@Override
			// 두 결과 컨테이너 병합
			public BinaryOperator<List<User>> combiner() {
				return (i1, i2) -> {
					i1.addAll(i2);
					return i1;
				};
			}

			@Override
			// 최종 변환값을 결과 컨테이너로 적용
			public Function<List<User>, List<User>> finisher() {
				return Function.identity();			// 항등함수 : 값 자체가 실제 요소
			}

			@Override
			public Set<Characteristics> characteristics() {
				return Collections.unmodifiableSet(EnumSet.of(
													Characteristics.UNORDERED
													,Characteristics.CONCURRENT
													,Characteristics.IDENTITY_FINISH));
			}
		};
		
		
		System.out.println("\n>> 6.6 커스텀 컬렉터를 구현해서 성능 개선하기");
		
		BiFunction<List<Integer>, Integer, Boolean> primeFunction;
		primeFunction = new BiFunction<List<Integer>, Integer, Boolean>(){
			public Boolean apply(List<Integer> list, Integer input) {
				return list.stream().takeWhile(i -> i <= (int)Math.sqrt(input)).noneMatch(i -> input%i==0);
			}
		};
		Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> primeCollector;
		
		// 1단계 : 시그니처 정의
		primeCollector = new Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>>() {
			// 2단계 : 리듀싱 연산
			@Override
			public Supplier<Map<Boolean, List<Integer>>> supplier() {
				return () -> new HashMap<Boolean, List<Integer>>(){{
					put(true, new ArrayList<Integer>());
					put(false, new ArrayList<Integer>());
				}};
			}
			@Override
			public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
				return (acc, candidate) -> 
				{
					acc.get(primeFunction.apply(acc.get(true), candidate)).add(candidate);
				};
			}
			
			// 3단계 : 병렬 실행할 수 있는 컬렉터 만들기(순서가 무의미한 경우에 한해)
			@Override
			public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
				return (map1, map2) ->
				{
					map1.get(true).addAll(map2.get(true));
					map1.get(false).addAll(map2.get(false));
					return map1;
				};
			}
			
			// 4단계 : finisher 메서드와 컬렉터의 characteristics 메서드
			@Override
			public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
				return Function.identity();
			}
			@Override
			public Set<Characteristics> characteristics() {
				return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
			}
		};
		
		Map<Boolean, List<Integer>> primeMap;
		primeMap = IntStream.rangeClosed(2, 30).boxed()
					.collect(primeCollector);
		System.out.println(primeMap.get(true));
		// [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
		System.out.println(primeMap.get(false));
		// [4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30]
		
		Function<Integer, Map<Boolean, List<Integer>>> contraction;
		contraction = new Function<Integer, Map<Boolean, List<Integer>>>(){
			public Map<Boolean, List<Integer>> apply(Integer n) {
				return IntStream.rangeClosed(2, n).boxed()
						.collect(
								() -> new HashMap<Boolean, List<Integer>>(){{
									put(true, new ArrayList<Integer>());
									put(false, new ArrayList<Integer>());
								}},
								(acc, candidate) -> 
								{
									acc.get(primeFunction.apply(acc.get(true), candidate)).add(candidate);
								},
								(map1, map2) ->
								{
									map1.get(true).addAll(map2.get(true));
									map1.get(false).addAll(map2.get(false));
								});
			}
		};
		primeMap = contraction.apply(30);
		System.out.println(primeMap.get(true));
		// [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
		System.out.println(primeMap.get(false));
		// [4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30]
		
		
	}
}


class User{
	String name; 
	String type;
	int age;
	
	public User(String name, String type, int age) {
		super();
		this.name = name;
		this.type = type;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getAge() {
		return age;
	}
	@Override
	public String toString() {
		return name;
	}
}
