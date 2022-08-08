package tistory.모던_자바_인_액션.chapter9;

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
//			int a = 2;				// 컴파일 에러
			System.out.println(a);
		};
		runnable.run();					// 0
		
		
		
		System.out.println("\n>> 9.2 람다로 객체지향 디자인 패턴 리팩터링하기");
		
		
		
		System.out.println("\n>> 9.3 람다 테스팅");
		
		
		
		System.out.println("\n>> 9.4 디버깅");
	}
	
}
