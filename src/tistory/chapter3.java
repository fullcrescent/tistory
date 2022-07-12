package tistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

public class chapter3 {

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
		IntPredicate i = null;
		Function f = null;
		
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
		supplier1.get().mydataPrint();
		
		supplier1 = User::new;					// 메서드 참조 사용
		supplier1.get().mydataPrint();
		
		BiFunction<String, Integer, User> biFunction1;
		// 시그니처 : (String, Integer) -> User
		biFunction1 = (s1, s2) -> new User(s1, s2);		// 시그니처에 따른 파라미터를 표현해야함.(s1, s2)
		biFunction1.apply("유저1", 10).mydataPrint();;
		
		biFunction1 = User::new;							// 전부 생략 가능
		biFunction1.apply("유저2", 20).mydataPrint();;
		
		
		Consumer<String> consumer1 = User::sMydataPrint;
		
		
		Function<Double, Double> f1 = (Double x) -> x+10;
		double answer = integrate(f1, 3.0, 7.0);
		System.out.println(answer);
		
	}

	public static double integrate(Function<Double, Double> f, double input1, double input2) {
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
	public static void sMydataPrint(String s) {
		System.out.println("static : " + s); 
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
