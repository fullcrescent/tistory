package tistory.모던_자바_인_액션.chapter03;

import static java.util.Comparator.comparing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Chapter3 {

	public static void main(String[] args) throws IOException {
		Interface1 i1 = new Interface1() {		// 익명 클래스 생성
			public int function1(int i) {
				return 0;
			}
		};
		
		Interface2 i2 = new Interface2() {		// 익명 클래스 생성
			public int function1(int i) {
				return 0;
			}

			public int function2() {
				return 0;
			}
		};
		
		i1 = (int i) -> 0;							// 익명 클래스 생성 없이 함수의 동작을 정의(함수의 이름을 알 수 없음)
//		i2 = () -> 0;								// 익명 클래스 생성 없이 함수의 동작을 정의하지 못함
		
		Runnable r = () -> {};
		Interface3 i3 = () -> {};					// Runnable 인터페이스의 시그니처와 동일
		
		User user = new User("홍길동", 29, "2022-07-07");
		infoGetAge(user);
		
		infoGet(user, (User u) -> u.getName());
//		infoGet(user, (User u) -> u.getAge());			// 반환 형태가 String이여야 한다.
		infoGet(user, (User u) -> u.getBirth());
		
		Consumer c = null;
		IntPredicate intPredicate = null;
		Function function = null;
		
//		errorOccur((BufferedReader br) -> {
//			try {
//				br.read();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
		
		List<Integer> list = new ArrayList<>();
		
		c = s -> list.add(1);
//		c = s -> 0==0;
		
		String a = "AAA";
//		a += "BB";									컴파일 에러
		infoGet(user, (u) -> a+u.getName());
		infoGet(user, (u) -> a+u.getAge());
		infoGet(user, (u) -> a+u.getBirth());
//		a += "BB";									컴파일 에러
		
		System.out.println("\n>> 3.6 메서드 참조");
		
		Supplier<User> supplier1;
		// 시그니처 : () -> T 
		
		supplier1 = () -> new User();			// 람다 표현식 사용
		supplier1.get().mydataPrint();			// print -> "이름 : null, 나이 : null, 생일 : null"
		
		supplier1 = User::new;					// 메서드 참조 사용
		supplier1.get().mydataPrint();			// print -> "이름 : null, 나이 : null, 생일 : null"
		
		BiFunction<String, Integer, User> biFunction1;
		// 시그니처 : (T, U) -> R
		
		biFunction1 = (s1, s2) -> new User(s1, s2);			// 시그니처에 따른 파라미터를 표현해야함.(s1, s2)
		biFunction1.apply("유저1", 10).mydataPrint();			// print -> "이름 : 유저1, 나이 : 10, 생일 : null"
		
		biFunction1 = User::new;							// 전부 생략 가능
		biFunction1.apply("유저2", 20).mydataPrint();;		// print -> "이름 : 유저2, 나이 : 20, 생일 : null"
		
		// 메소드 참조 유형
		// 1. 정적 메소드 참조 
		Consumer<String> consumer1 = User::staticMydataPrint;	// 전달한 파라미터가 정적 메소드의 파라미터로 전달
		consumer1.accept("정적 메소드 실행");						
		// print -> "정적 메소드 실행"
		
		// 2. 기존 객체의 인스턴스 메서드 참조
		User user2 = new User("consumer2", 2, "2222-02-22");
		Consumer<String> consumer2 = user2::instanceMydataPrint;	// 전달한 파라미터가 인스턴스 메소드의 파라미터로 전달
		consumer2.accept("인스턴스 메소드 실행 - ");						
		// print -> "인스턴스 메소드 실행 - 이름 : consumer2, 나이 : 2, 생일 : 2222-02-22"

		// 3. 다양한 형식의 인스턴스 메서드 참조
		BiConsumer<User, String> biConsumer1 = User::instanceMydataPrint;	
		// 첫번째 파라미터의 인스턴스 메소드 실행, 두번째 파라미터는 인스턴스 메소드의 파라미터로 전달
		
		biConsumer1.accept(new User("biConsumer1", 1, "1111-11-11"), "다양한 형식의 인스턴스 메소드 실행 - ");		
		// print -> "다양한 형식의 인스턴스 메소드 실행 - 이름 : biConsumer1, 나이 : 1, 생일 : 1111-11-11"
		
		Map<String, Supplier<List<?>>> map = new HashMap<>();
		map.put("array", ArrayList::new);
		map.put("linked", LinkedList::new);
		
		System.out.println(map.get("array").get().getClass());	// class java.util.ArrayList
		System.out.println(map.get("linked").get().getClass());	// class java.util.LinkedList
		
		System.out.println("\n>> 3.7 람다 , 메서드 참조 활용하기");
		
		// 1단계 : 코드 전달
		new Impl7().function7(1);			

		// 2단계 : 익명 클래스
		new Interface7() {					
			@Override
			public void function7(int i) {
				System.out.println(i);
			}
		}.function7(2);
		
		// 3단계 : 람다 표현식
		Interface7 interface7 = (int input) -> System.out.println(input);	
		interface7.function7(3);
		
		interface7 = (input) -> System.out.println(input);					// 형식 추론
		interface7.function7(3);
		
		// 4단계 : 메서드 참조
		interface7 =  System.out::println;
		interface7.function7(4);
		
		System.out.println("\n>> 3.8 람다 표현식을 조합할 수 있는 유용한 메서드");
		
		// Comparator
		List<User> temp8 = new ArrayList<>(Arrays.asList(
				  new User("User1", 3, "1111-11-11")
				, new User("User2", 1, "1111-11-11")
				, new User("User3", 8, "2222-22-22")
				, new User("User4", 9, "3333-33-33")
				)
			);
		
		temp8.sort(
	    		comparing(User::getBirth)				// User1, User2, User3, User4
				.reversed()								// User4, User3, User1, User2
				.thenComparing(User::getAge)			// User4, User3, User2, User1
			);
		temp8.forEach((input) -> {
			if(temp8.indexOf(input)==temp8.size()-1) {
				System.out.println(input.getName());
			}else {
				System.out.print(input.getName() + ", ");
			}
		});
		
		// Predicate
		Predicate<User> age1Excess = (User user8) -> user8.getAge()>1;  										// 나이 1 초과
		Predicate<User> notAge1Excess = age1Excess.negate();													// not(나이 1 초과)
		Predicate<User> age1ExcessAndUser1 = age1Excess.and((User user8) -> user8.getName().equals("User1"));	// And(나이 1초과, 이름 User1)
		Predicate<User> age1ExcessOrUser2 = age1Excess.or((User user8) -> user8.getName().equals("User2"));		// or(나이 1초과, 이름 User2)
		
		System.out.println(temp8.stream().filter(age1Excess).count());				// 3
		System.out.println(temp8.stream().filter(notAge1Excess).count());			// 1
		System.out.println(temp8.stream().filter(age1ExcessAndUser1).count());		// 1
		System.out.println(temp8.stream().filter(age1ExcessOrUser2).count());		// 4
		
		// Function
		Function<Integer, Integer> f = x -> x+10;
		Function<Integer, Integer> g = x -> x*3;
		System.out.println("f(g(x)) = " + f.andThen(g).apply(1));	// f(g(x)) = 33
		System.out.println("g(f(x)) = " + g.andThen(f).apply(1));	// g(f(x)) = 11
		
		System.out.println("\n>> 3.9 비슷한 수학적 개념");
		
		Function<Double, Double> f9_1 = (Double x) -> x+10;
		double answer9_1 = integrate(f9_1, 3.0, 7.0);
		System.out.println(answer9_1);
		
		Function<Double, Double> f9_2 = (Double x) -> Math.sin(x);						// 적분 값 : 2
		double start = 0;
		double end = 180;
		
		double answer9_2 = 0;
		answer9_2 = integrate(f9_2, Math.toRadians(start), Math.toRadians(end));
		System.out.println(answer9_2);													// 1.9236706937217898E-16
		
		double answer9_3 = 0;
		for(double i=start; i<end; i=i+0.01) {
			answer9_3 +=integrate(f9_2, Math.toRadians(i), Math.toRadians(i+0.01));		// 기준을 0.01로
		}
		System.out.println(answer9_3);													// 1.9999999796921677
	}

	public static double integrate(Function<Double, Double> f, double input1, double input2) {	// 구분구적법
		return (f.apply(input1) + f.apply(input2)) * (input2-input1) / 2;
	}
	
	public static void infoGet(User user, Info info){
		String result = "****";
		result += info.function(user);
		result += "****";
		System.out.println(result);
	}
	
	public static void infoGetAge(User user){
		String result = "****";
		result += user.getAge();
		result += "****";
		System.out.println(result);
	}
	
	public static void errorOccur(Error error){
		error.function(new BufferedReader(null));
	}
}

@FunctionalInterface
interface Interface7{
	void function7(int i);
}

class Impl7 implements Interface7{
	@Override
	public void function7(int i) {
		System.out.println(i);
	}
}

@FunctionalInterface
interface Interface1{
	int function1(int i);
	default int function2() {return 0;}
	boolean equals(Object obj);
	String toString();
	int hashCode();
}

interface Interface2{
	int function1(int i);
	int function2();
}

interface Interface3{
	void function1();
}

class User{
	String name;
	Integer age;
	String birth;
	
	public User() {
	}
	public User(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	public User(String name, int age, String birth) {
		super();
		this.name = name;
		this.age = age;
		this.birth = birth;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public String getBirth() {
		return birth;
	}
	public void mydataPrint() {
		System.out.println("이름 : " + name  + ", 나이 : " + age + ", 생일 : " + birth); 
	}
	public static void staticMydataPrint(String s) {
		System.out.println(s); 
	}
	public void instanceMydataPrint(String s) {
		System.out.print(s); 
		mydataPrint();
	}
}

@FunctionalInterface
interface Info{
	String function(User user);
}

@FunctionalInterface
interface Error{
	void function(BufferedReader bf);
}
