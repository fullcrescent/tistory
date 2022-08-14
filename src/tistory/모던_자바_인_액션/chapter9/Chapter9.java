package tistory.모던_자바_인_액션.chapter9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/* 추가사항 */
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


/* 조건부 연기실행 */
Logger logger = Logger.getGlobal();
logger.setLevel(Level.INFO);

/* 문제점 : logger의 상태가 노출 / 메시지를 로깅할 때마다 상태를 확인 */
if(logger.isLoggable(Level.INFO)) {
	logger.info("111");
}

/* 문제점 : 인수로 전달된 메시지 수준에서 logger가 활성화되어 있지 않아도 평가 진행 */
logger.log(Level.INFO, "222");

/* 특정 조건에서만 메시지가 생성될 수 있도록 생성 과정은 지연 */
logger.log(Level.INFO, () -> "333");

System.out.println("\n>> 9.2 람다로 객체지향 디자인 패턴 리팩터링하기");

User temp = new User("tempUser", 40);
/* 람다 X */
Validator more = new Validator(new More());
System.out.println(
		more.validate(temp));		// true
Validator less = new Validator(new Less());
System.out.println(
		less.validate(temp));		// false

/* 람다 O */
more = new Validator(i -> i.getAge()>30);
System.out.println(
		more.validate(temp));		// true
less = new Validator(i -> i.getAge()<30);
System.out.println(
		less.validate(temp));		// false

/* 람다 X */
new GrowTime().process(temp);
System.out.println(temp);			// User [name=tempUser, age=50]

/* 람다 O */
process(temp, i -> i.age+=30);
System.out.println(temp);			// User [name=tempUser, age=80]


/* 람다 X */
Feed feed = new Feed();
feed.registObservers(new Join());
feed.notifyObservers(temp);
// tempUser님이 가입하셨습니다.

/* 람다 O */
feed.registObservers(user -> 
	System.out.println(user.getName() + "님이" + user.getAge() +"살에 가입하셨습니다."));
feed.notifyObservers(temp);
// tempUser님이 가입하셨습니다.
// tempUser님이80살에 가입하셨습니다.
// 위에서 등록된 옵저버로 인해 두번 출력


temp = new User("tempUser", 40);
/* 람다 X */
Process<User> p1 = new NameProcess();
Process<User> p2 = new AgeProcess();
p1.setSuccessor(p2);
p1.handle(temp);
System.out.println(temp);	// User [name=tempUser Process Success, age=100]

/* 람다 O */
UnaryOperator<User> nameProcess = i -> {
	i.name += "~~~~~";
	return i;
};
UnaryOperator<User> ageProcess = i -> {
	i.age += 30;
	return i;
};
Function<User, User> pipeline = nameProcess.andThen(ageProcess);
pipeline.apply(temp);
System.out.println(temp);	// User [name=tempUser Process Success~~~~~, age=100]





System.out.println("\n>> 9.3 람다 테스팅");



System.out.println("\n>> 9.4 디버깅");
}

/* 람다를 사용할 메소드 */
public static void process(User User, Consumer<User> grow) {
	grow.accept(User);
}

public static void run(Runnable r) {
	r.run();
}
public static void run(Task t) {
	t.run();
}

}

interface Human{};
class Employer implements Human {};
class Employee implements Human {};

class UserFactory{
	final static private Map<String, Supplier<Human>> factoryMap = new HashMap<>();
	static {
		factoryMap.put("employer", Employer::new);
		factoryMap.put("Employee", Employee::new);
	}
	
	public static Human createUser(String type) {
		switch(type) {
			case "employer" : return new Employer();
			case "employee" : return new Employee();
			default : throw new RuntimeException("No such User");
		}
	}
	
	public static Human createUserLambda(String type) {
		Supplier<Human> s = factoryMap.get(type);
		if(s==null) {
			throw new RuntimeException("No such User");
		}
		return s.get();
	}
}





abstract class Process<T>{
	private Process<T> successor;

	public void setSuccessor(Process<T> successor) {
		this.successor = successor;
	}
	public T handle(T user) {
		T temp = handleWork(user);
		if(successor!=null) {
			return successor.handle(temp);
		}
		return temp;
	}
	abstract protected T handleWork(T user);
}

class NameProcess extends Process<User>{
	protected User handleWork(User user) {
		user.name += " Process Success";
		return user;
	}
}

class AgeProcess extends Process<User>{
	protected User handleWork(User user) {
		user.age += 30;
		return user;
	}
}


interface Observer{
	void inform(User user);
}

class Join implements Observer{
	public void inform(User user) {
		System.out.println(user.getName() + "님이 가입하셨습니다.");
	}
}

interface Subject{
	void registObservers(Observer observer);
	void notifyObservers(User user);
}

class Feed implements Subject{
	private final List<Observer> observer = new ArrayList<>();
	public void registObservers(Observer o) {
		observer.add(o);
	}
	public void notifyObservers(User user) {
		observer.forEach(o -> o.inform(user));
	}
}

/* 동작을 정의하는 추상 클래스 */
abstract class Time{
	public void process(User user) {
		grow(user);
	}
	abstract void grow(User user);
}

class GrowTime extends Time{
	void grow(User user) {
		user.age += 10;
	}
}


/* 알고리즘을 나타내는 인터페이스 */
interface Valid{
	boolean execute(User user);
}

/* 한개 이상의 인터페이스 구현 */
class More implements Valid{
	public boolean execute(User user) {
		return user.getAge()>30;
	}
}

class Less implements Valid{
	public boolean execute(User user) {
		return user.getAge()<30;
	}
}

/* 전략 객체를 사용하는 한 개 이상의 클라이언트 */
class Validator{
	private final Valid valid;

	public Validator(Valid valid) {
		super();
		this.valid = valid;
	}
	
	public boolean validate(User user) {
		return valid.execute(user);
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