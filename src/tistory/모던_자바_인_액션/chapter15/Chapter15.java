package tistory.모던_자바_인_액션.chapter15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;

public class Chapter15 {

public static void main(String[] args) throws InterruptedException, ExecutionException {
System.out.println("\n>> 15 CompletableFuture와 리액티브 프로그래밍 컨셉의 기초");

System.out.println("\n>> 15.1 동시성을 구현하는 자바 지원의 진화");
System.out.println("\n>> 15.2 동기 API와 비동기 API");
		
int x = 10;
Result result1, result2, result3;

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
result2 = new Result();
ExecutorService executorService = Executors.newFixedThreadPool(2);
Future<Integer> y2 = executorService.submit(() -> Function.f(x));
Future<Integer> z2 = executorService.submit(() -> Function.g(x));
System.out.println(y2.get() + z2.get());
executorService.shutdown();

// 리액티브 형식
result3 = new Result();
f(x, y3 -> {
	result3.left = y3;
	System.out.println(result3.left + result3.right);
});
g(x, z3 ->{
	result3.right = z3;
	System.out.println(result3.left + result3.right);
});


ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

work1();
//Thread.sleep(10000);		지정된 시간동안 스레드의 자원을 점유하여 다른 작업이 실행되지 못함
scheduledExecutorService.schedule(Chapter15::work2, 5, TimeUnit.SECONDS);
scheduledExecutorService.shutdown();

Callback c1 = new Callback("C1");
Callback c2 = new Callback("C2");
Callback c3 = new Callback("C3");

c1.subscribe(c3);

c1.onNext(10);
c2.onNext(20);

System.out.println("\n>> 15.3 박스와 채널 모델");
System.out.println("\n>> 15.4 CompletableFuture와 콤비네이터를 이용한 동시성");
System.out.println("\n>> 15.5 발행-구독 그리고 리액티브 프로그래밍");
System.out.println("\n>> 15.6 리액티브 시스템 vs 리액티브 프로그래밍");
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

private static void f(int x, Subscriber<Integer> s) {
	s.onNext(x);
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
}

class Callback implements Subscriber<Integer>{
	private int value = 0;
	private String name;
	private List<Subscriber<Integer>> subscribers = new ArrayList<>();
	
	public Callback(String name) {
		this.name = name;
	}
	
	public void subscribe(Subscriber<Integer> subscriber) {
		subscribers.add(subscriber);
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