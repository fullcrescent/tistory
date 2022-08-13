package tistory.모던_자바_인_액션.chapter9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Chapter9 {

	public static void main(String[] args) {
		System.out.println("\n>> 9.1 가독성과 유연성을 개선하는 리팩터링");
		
		Runnable runnable;
		runnable = new Runnable() {
			public void run() {
				System.out.println("Hello1");
			}
		};
		runnable.run();					// Hello1
		
		runnable = () -> System.out.println("Hello2");
		runnable.run();					// Hello2
		
		int a = 0;
		runnable = new Runnable() {
			public void run() {
				int a = 1;
				System.out.println(a);
			}
		};
		runnable.run();					// 1
		
		runnable = () -> {
			//int a = 2;				// 컴파일 에러
			System.out.println(a);
		};
		runnable.run();					// 0
		
		//run(() -> System.out.println("test"));		컴파일 에러
		run((Runnable)() -> System.out.println("test"));
		run((Task)() -> System.out.println("test"));
		
		
		List<User> userList = new ArrayList<>(Arrays.asList(
				new User("User1", 10)
				,new User("User2", 20)
				,new User("User5", 50)
				,new User("User3", 30)
				,new User("User4", 40)
			)
		);
		
		userList.sort((User u1, User u2) -> -Integer.compare(u1.getAge(), u2.getAge()));
		System.out.println(userList);		// [User [name=User5, age=50], User [name=User4, age=40], User [name=User3, age=30], User [name=User2, age=20], User [name=User1, age=10]]
		
		userList.sort(Comparator.comparing(User::getAge));
		System.out.println(userList);		// [User [name=User1, age=10], User [name=User2, age=20], User [name=User3, age=30], User [name=User4, age=40], User [name=User5, age=50]]
		
		
		List<String> userName = new ArrayList<>();
		for(User u : userList) {
			if(u.getAge()>30) {
				userName.add(u.getName());
			}
		}
		System.out.println(userName);		// [User4, User5]
		
		userName = userList.stream()
					.filter(u -> u.getAge()<30)
					.map(User::getName)
					.collect(Collectors.toList());
		System.out.println(userName);		// [User1, User2]

						
		
		
		System.out.println("\n>> 9.2 람다로 객체지향 디자인 패턴 리팩터링하기");
		
		
		
		System.out.println("\n>> 9.3 람다 테스팅");
		
		
		
		System.out.println("\n>> 9.4 디버깅");
	}
	
	public static void run(Runnable r) {
		r.run();
	}
	public static void run(Task t) {
		t.run();
	}
}

interface Task{
	public void run();
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

	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}
}