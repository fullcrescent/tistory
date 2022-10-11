package tistory.모던_자바_인_액션.chapter15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Chapter15 {

public static void main(String[] args) throws InterruptedException, ExecutionException {
System.out.println("\n>> 15 CompletableFuture와 리액티브 프로그래밍 컨셉의 기초");

System.out.println("\n>> 15.1 동시성을 구현하는 자바 지원의 진화");
System.out.println("\n>> 15.2 동기 API와 비동기 API");
		
int x = 10;
Result result1, result4;

// 기본 형태
int y = Function.f(x);
int z = Function.g(x);
System.out.println(y+z);

// Thread
result1 = new Result();
Thread t1 = new Thread(() -> result1.left=Function.f(x));
Thread t2 = new Thread(() -> result1.right=Function.g(x));
t1.start();
t2.start();
t1.join();
t2.join();
System.out.println(result1.left + result1.right);

// ExecutorService
ExecutorService executorService = Executors.newFixedThreadPool(2);
Future<Integer> y2 = executorService.submit(() -> Function.f(x));
Future<Integer> z2 = executorService.submit(() -> Function.g(x));
System.out.println(y2.get() + z2.get());

// Future 형식 API
Future<Integer> y3 = Function.ff(x);
Future<Integer> z3 = Function.gg(x);
System.out.println(y3.get() + z3.get());

// 리액티브 형식 API
result4 = new Result();
f(x, y4 -> {
	result4.left = y4;
	System.out.println(result4.left + result4.right);
});
g(x, z4 ->{
	result4.right = z4;
	System.out.println(result4.left + result4.right);
});

ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

work1();
//Thread.sleep(10000);		지정된 시간동안 스레드의 자원을 점유하여 다른 작업이 실행되지 못함
scheduledExecutorService.schedule(Chapter15::work2, 5, TimeUnit.SECONDS);
scheduledExecutorService.shutdown();

System.out.println("\n>> 15.3 박스와 채널 모델");

// 다이어 그램을 구현
int t;

// 문제점 : 병렬성 X
t = Function.p(0);
System.out.println(Function.r(Function.f(t), Function.g(t)));

// 문제점 : p와 r을 Future로 감싸지 않음
t = Function.p(0);
Future<Integer> f1 = executorService.submit(() -> Function.f(x));
Future<Integer> f2 = executorService.submit(() -> Function.g(x));
System.out.println(Function.r(f1.get(), f2.get()));


System.out.println("\n>> 15.4 CompletableFuture와 콤비네이터를 이용한 동시성");

executorService = Executors.newFixedThreadPool(10);

int x4_1 = 10;
CompletableFuture<Integer> a_1 = new CompletableFuture<>();
executorService.submit(() -> a_1.complete(Function.f(x4_1)));
int b_1 = Function.g(x4_1);
System.out.println(a_1.get()+b_1);

int x4_2 = 10;
CompletableFuture<Integer> a_2 = new CompletableFuture<>();
CompletableFuture<Integer> b_2 = new CompletableFuture<>();
CompletableFuture<Integer> c_2 = a_2.thenCombine(b_2, (y4_2, z4_2) -> y4_2+z4_2);
executorService.submit(() -> a_2.complete(Function.f(x4_2)));
executorService.submit(() -> b_2.complete(Function.g(x4_2)));
System.out.println(c_2.get());


System.out.println("\n>> 15.5 발행-구독 그리고 리액티브 프로그래밍");

SimpleCell c1_1 = new SimpleCell("C1_1");
SimpleCell c2_1 = new SimpleCell("C2_1");
SimpleCell c3_1 = new SimpleCell("C3_1");

c1_1.subscribe(c3_1);
c1_1.onNext(10);
c2_1.onNext(20);
// C1_1:10
// C3_1:10
// C2_1:20
System.out.println();

SimpleCell c1_2 = new SimpleCell("C1_2");
SimpleCell c2_2 = new SimpleCell("C2_2");
ArithmeticCell c3_2 = new ArithmeticCell("C3_2");
c1_2.subscribe(c3_2::setLeft);
c2_2.subscribe(c3_2::setRight);

c1_2.onNext(10);
c2_2.onNext(20);
c1_2.onNext(15);
// C1_2:10
// C3_2:10
// C2_2:20
// C3_2:30
// C1_2:15
// C3_2:35
System.out.println();

SimpleCell c1_3 = new SimpleCell("C1_3");
SimpleCell c2_3 = new SimpleCell("C2_3");
SimpleCell c4_3 = new SimpleCell("C4_3");
ArithmeticCell c3_3 = new ArithmeticCell("C3_3");
ArithmeticCell c5_3 = new ArithmeticCell("C5_3");

c1_3.subscribe(c3_3::setLeft);
c2_3.subscribe(c3_3::setRight);
c3_3.subscribe(c5_3::setLeft);
c4_3.subscribe(c5_3::setRight);

c1_3.onNext(10);
c2_3.onNext(20);
c1_3.onNext(15);
c4_3.onNext(1);
c4_3.onNext(3);
// C1_3:10
// C3_3:10
// C5_3:10
// C2_3:20
// C3_3:30
// C5_3:30
// C1_3:15
// C3_3:35
// C5_3:35
// C4_3:1
// C5_3:36
// C4_3:3
// C5_3:38
System.out.println();


System.out.println("\n>> 15.6 리액티브 시스템 vs 리액티브 프로그래밍");





executorService.shutdown();
}
	
private static void f(int x, IntConsumer dealWithResult) {
	dealWithResult.accept(Function.f(x));
}

private static void g(int x, IntConsumer dealWithResult) {
	dealWithResult.accept(Function.g(x));
}

public static void work1() {
	System.out.println("Work1");
}

public static void work2() {
	System.out.println("Work2");
}

}

class Result{
	int left;
	int right;
}

class Function{
	public static int f(int x) {
		return x*2;
	}
	
	public static int g(int x) {
		return x+1;
	}
	
	public static Future<Integer> ff(int x){
		return new CompletableFuture<Integer>().completeAsync(() -> Integer.valueOf(x*2));
	}
	
	public static Future<Integer> gg(int x){
		return new CompletableFuture<Integer>().completeAsync(() -> Integer.valueOf(x+1));
	}
	
	public static int p(int x) {
		return 10;
	}
	
	public static int r(int x, int y) {
		return x*y;
	}
}

class SimpleCell implements Publisher<Integer>, Subscriber<Integer>{
	private int value = 0;
	private String name;
	private List<Subscriber<? super Integer>> subscribers = new ArrayList<>();
	
	public SimpleCell(String name) {
		this.name = name;
	}
	
	@Override
	public void subscribe(Subscriber<? super Integer> subscriber) {
		subscribers.add(subscriber);
	}
	
	public void subscribe(Consumer<? super Integer> subscriber) {
		subscribers.add(new Subscriber<>() {
			@Override
			public void onSubscribe(Subscription subscription) {}

			@Override
			public void onNext(Integer item) {
				subscriber.accept(item);
			}

			@Override
			public void onError(Throwable throwable) {
				throwable.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("아무 이벤트도 일어나지 않음");
			}
		});
	}
	
	@Override
	public void onNext(Integer item) {
		value = item;
		System.out.println(name + ":" + value);
		notifyAllSubscribers();
	}
	
	private void notifyAllSubscribers() {
		subscribers.forEach(subscriber -> subscriber.onNext(value));
	}
	
	@Override
	public void onSubscribe(Subscription subscription) {}

	@Override
	public void onError(Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void onComplete() {
		System.out.println("아무 이벤트도 일어나지 않음");
	}
}

class ArithmeticCell extends SimpleCell{
	private int left;
	private int right;
	
	public ArithmeticCell(String name) {
		super(name);
	}

	public void setLeft(int left) {
		this.left = left;
		onNext(left+right);
	}

	public void setRight(int right) {
		this.right = right;
		onNext(right+left);
	}
}