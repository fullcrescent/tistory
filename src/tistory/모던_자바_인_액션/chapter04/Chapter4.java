package tistory.모던_자바_인_액션.chapter04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Chapter4 {
	public static void main(String[] args) {
		List<User> userList = new ArrayList<>(Arrays.asList(
				new User("User1", 10)
				,new User("User2", 20)
				,new User("User5", 50)
				,new User("User3", 30)
				,new User("User4", 40)
			)
		);
		
		List<String> userNameList = userList.stream()
				.filter(user -> user.getAge()>25)		// User5, User3, User4
				.map(User::getName)						// 각각의 객체에서 이름 값을 추출
				.sorted(Collections.reverseOrder())		// User5, User4, User3
				.limit(2)								// User5, User4
				.collect(Collectors.toList());			// List로 반환
		
		System.out.println(userNameList);
		
		Stream<User> userStream = userList.stream();
		System.out.println(userStream.map(User::getName).collect(Collectors.toList()));	
//		System.out.println(userStream.map(User::getName).collect(Collectors.toList()));	// 실행 시 에러
		
		userNameList = userList.stream()
				.sorted((input1, input2) -> input1.getName().compareTo(input2.getName()) )		
				.filter(user -> {
					System.out.println("filter : " + user.getName());
					return user.getAge()>25;
				})		
				.map(user -> {
					System.out.println("map : " + user.getName());
					return user.getName();
				})
				.limit(2)								
				.collect(Collectors.toList());	
		
		System.out.println(userNameList);
		
		userNameList = userList.stream()
				.filter(user -> {
					System.out.println("filter : " + user.getName());
					return user.getAge()>25;
				})		
				.map(user -> {
					System.out.println("map : " + user.getName());
					return user.getName();
				})
				.sorted((input1, input2) -> input1.compareTo(input2))		
				.limit(2)								
				.collect(Collectors.toList());	
		
		System.out.println(userNameList);
	}
}

class User{
	String name;
	int age;
	
	public User(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
}
