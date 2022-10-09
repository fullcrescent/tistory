package tistory.모던_자바_인_액션.chapter15;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;

public class Chapter15 {

	public static void main(String[] args) {
		System.out.println("\n>> 15 CompletableFuture와 리액티브 프로그래밍 컨셉의 기초");
		
		System.out.println("\n>> 15.1 동시성을 구현하는 자바 지원의 진화");
		System.out.println("\n>> 15.2 동기 API와 비동기 API");
		
		int x = 10;
		Result result = new Result();
		
		f(x, y -> {
			result.left = y;
			System.out.println(result.left + result.right);
		});

		g(x, z ->{
			result.right = z;
			System.out.println(result.left + result.right);
		});
		
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		
		work1();
		
		scheduledExecutorService.schedule(Chapter15::work2, 10, TimeUnit.SECONDS);
		scheduledExecutorService.shutdown();
		
		
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