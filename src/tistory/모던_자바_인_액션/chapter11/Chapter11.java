package tistory.모던_자바_인_액션.chapter11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

public class Chapter11 {

	public static void main(String[] args) {
		System.out.println("\n>> 11 null 대신 Optional 클래스");
		
		System.out.println("\n>> 11.1 값이 없는 상황을 어떻게 처리할까?");
		
		System.out.println("\n>> 11.2 Optional 클래스 소개");
		
		System.out.println("\n>> 11.3 Optional 적용 패턴");
				
		Optional<Car> optCar;
		optCar = Optional.empty();
		System.out.println(optCar);				// Optional.empty
		
		Car car = null;
		//optCar = Optional.of(car);				// java.lang.NullPointerException
		optCar = Optional.ofNullable(car);
		System.out.println(optCar);				// Optional.empty
		
		car = new Car();
		optCar = Optional.of(car);				
		System.out.println(optCar);				// Optional[tistory.모던_자바_인_액션.chapter11.Car@3a4afd8d]
		
		
		Optional<String> name;
		Insurance insurance = new Insurance();
		
		Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
		name = optInsurance.map(Insurance::getName);
		System.out.println(name);				// Optional[AAA]
		
		
		Person person = new Person();
		insurance.name = "AAA";
		car.insurance = Optional.of(insurance);
		person.car = Optional.of(car);
		
		Optional<Person> optPerson;
		optPerson = Optional.of(person);
		
		/* 컴파일 되지 않음 -> Person::getCar의 반환값이 Optional<Optional<Car>> 형식이기 때문 */
		//name = optPerson.map(Person::getCar)
		//				.map(Car::getInsurance)
		//				.map(Insurance::getName);
		
		name = optPerson.flatMap(Person::getCar)
						.flatMap(Car::getInsurance)
						.map(Insurance::getName);
		System.out.println(name);				// Optional[AAA]
		
		person.car = Optional.empty();
		optPerson = Optional.of(person);
		name = optPerson.flatMap(Person::getCar)
						.flatMap(Car::getInsurance)
						.map(Insurance::getName);
		System.out.println(name);				// Optional.empty
		
		
		List<Person> list = new ArrayList<>();
		person.car = Optional.of(car);
		list.add(person);
		list.add(person);
		list.add(person);
		
		System.out.println(
				list.stream()
					.map(Person::getCar)
					.map(optionalCar -> optionalCar.flatMap(Car::getInsurance))
					.map(optionalIns -> optionalIns.map(Insurance::getName))
					.flatMap(Optional::stream)
					.toList()
				);				// [AAA, AAA, AAA]
		
		Stream<Optional<String>> stream = list.stream()
												.map(Person::getCar)
												.map(optionalCar -> optionalCar.flatMap(Car::getInsurance))
												.map(optionalIns -> optionalIns.map(Insurance::getName));
		System.out.println(
				stream.filter(Optional::isPresent)
						.map(Optional::get)
						.toList()
				);				// [AAA, AAA, AAA]
		
		System.out.println(
				findSafeNull(Optional.empty(), Optional.empty())
				);				// Optional.empty
				
				
		System.out.println("\n>> 11.4 Optional을 사용한 실용 예제 ");
		
		/* 잠재적 null 대상 */
		Map<String, Object> map = new HashMap<>();
		//Object value = map.get("key");
		//System.out.println(value.hashCode());		// java.lang.NullPointerException
		Optional<Object> optValue = Optional.ofNullable(map.get("key"));
		System.out.println(optValue.hashCode());	// 0
		
		/* 문자열 정수 Optional로 변환 */
		Optional<Integer> stringToInt;
		try {
			stringToInt = Optional.of(Integer.parseInt("가나다"));
		}catch(NumberFormatException e) {
			stringToInt = Optional.empty();
		}
		System.out.println(stringToInt);			// Optional.empty
		
		Properties props = new Properties();
		props.setProperty("a", "5555");
		props.setProperty("b", "true");
		props.setProperty("c", "-333");
		
		System.out.println(readProps(props, "a"));				// 5555
		System.out.println(readProps(props, "b"));				// 0
		System.out.println(readProps(props, "c"));				// 0
		
		System.out.println(readPropsOptional(props, "a"));		// 5555
		System.out.println(readPropsOptional(props, "b"));		// 0
		System.out.println(readPropsOptional(props, "c"));		// 0
			
	}

	public static int readProps(Properties props, String name) {
		String value = props.getProperty(name);
		if(value != null) {
			try {
				int i = Integer.parseInt(value);
				if(i>0) return i;
			}catch(NumberFormatException e) {}
		}
		return 0;
	}
	
	public static int readPropsOptional(Properties props, String name) {
		return Optional.ofNullable(props.getProperty(name))
						.flatMap(i -> {
							try {
								return Optional.of(Integer.parseInt(i));
							}catch (NumberFormatException e) {
								return Optional.empty();
							}
						})
						.filter(i -> i>0)
						.orElse(0);
	}
	
	/* 사람과 차 정보를 기반으로 보험을 찾는 메서드라고 가정 */
	public static Insurance find(Person person, Car car) {
		return new Insurance();
	}
	
	public static Optional<Insurance> findSafeNull(Optional<Person> person, Optional<Car> car){
		return person.flatMap(p -> car.map(c -> find(p, c)));
	}
	
}

/* 객체 중첩 구조 */
class Person{
	public Optional<Car> car;
	
	public Optional<Car> getCar() {
		return car;
	}
}

class Car{
	public Optional<Insurance> insurance;

	public Optional<Insurance> getInsurance() {
		return insurance;
	}
}

class Insurance{
	public String name;		// Optional이 아님은 필수 값을 의미
	
	public String getName()	{
		return name;
	}
}