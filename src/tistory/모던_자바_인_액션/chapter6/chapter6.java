package tistory.모던_자바_인_액션.chapter6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class chapter6 {

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
		
		Map<String, List<User>> userGroupTypeMap;
		userGroupTypeMap = userList.stream().collect(Collectors.groupingBy(User::getType));
		System.out.println(userGroupTypeMap);				// {usr=[User2, User5], emp=[User0, User1, User3], adm=[User4]}
		
		Map<String, Long> userGroupTypeCountMap;
		userGroupTypeCountMap = userList.stream().collect(Collectors.groupingBy(User::getType, Collectors.counting()));
		System.out.println(userGroupTypeCountMap);			// {usr=2, emp=3, adm=1}
		
		
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

		
		System.out.println("\n>> 6.5 Collector 인터페이스");
		
		// 커밋 테스트
		
		
		
		
		
		System.out.println("\n>> 6.6 커스텀 컬렉터를 구현해서 성능 개선하기");
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
