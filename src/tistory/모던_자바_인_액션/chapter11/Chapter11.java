package tistory.모던_자바_인_액션.chapter11;

import java.util.Optional;

public class Chapter11 {

	public static void main(String[] args) {
		System.out.println("\n>> 11 null 대신 Optional 클래스");
		
		System.out.println("\n>> 11.1 값이 없는 상황을 어떻게 처리할까?");
		
		System.out.println("\n>> 11.2 Optional 클래스 소개");
		
		System.out.println("\n>> 11.3 Optional 적용 패턴");
		
		
		System.out.println("\n>> 11.4 Optional을 사용한 실용 예제 ");
		
		
		
	}

}

/* 객체 중첩 구조 */
class Person{
	private Optional<Car> car;
	
	public Optional<Car> getCar() {
		return car;
	}
}

class Car{
	private Optional<Insurance> insurance;

	public Optional<Insurance> getInsurance() {
		return insurance;
	}
}

class Insurance{
	private String name;		// Optional이 아님은 필수 값을 의미
	
	public String getName()	{
		return name;
	}
}