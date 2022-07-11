package tistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
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
		
		infoGetAge();
		infoGet(new User(), (User user) -> user.getName());
		infoGet(new User(), (User user) -> user.getAge());
		infoGet(new User(), (User user) -> user.getBirth());
		
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
		
		infoGet(new User(), (user) -> a+user.getName());
		infoGet(new User(), (user) -> a+user.getAge());
		infoGet(new User(), (user) -> a+user.getBirth());
		
//		a += "BB";									컴파일 에러
		
		System.out.println("\n>> 3.6 메서드 참조");
		Supplier<User> s1 = User::new;
		infoGet(s1.get(), User::getName);
		
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
	
	public static void infoGetAge(){
		User user = new User();
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
	String name = "홍길동";
	String age = "29";
	String birth = "2022-07-07";
	
	public String getName() {
		return name;
	}
	public String getAge() {
		return age;
	}
	public String getBirth() {
		return birth;
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
