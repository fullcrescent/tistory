package tistory.모던_자바_인_액션.chapter16;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Chapter16 {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
System.out.println("\n>> 16 CompletableFuture : 안정적 비동기 프로그래밍");

System.out.println("\n>> 16.1 Future의 단순 활용");

/* 자바8 이전의 코드 */
ExecutorService executor = Executors.newCachedThreadPool();
Future<Double> future = executor.submit(new Callable<Double>() {	// 작업 수행 시작
	public Double call() throws Exception {
		Thread.sleep(2000);
		return 0D; 
	}
});

System.out.println("start");
Thread.sleep(1100);		// 해당 스레드를 실행하지 않을경우 java.util.concurrent.TimeoutException 발생
try {
	System.out.println(future.get(1, TimeUnit.SECONDS));	// 최대 1초까지만 대기
}catch(TimeoutException e) {
	System.out.println(e);
}
System.out.println("end");


System.out.println("\n>> 16.2 비동기 API 구현");

Shop shop = new Shop("BestShop");
long start = System.nanoTime();

Future<Double> futurePrice = shop.getPriceAsync("product");

long startTime = ((System.nanoTime()-start)/1_000_000);
System.out.println("return start " + startTime + " msecs");

double price = futurePrice.get();
System.out.printf("Price is %.2f%n", price);

long endTime = ((System.nanoTime()-start)/1_000_000);
System.out.println("return end " + endTime + " msecs");


System.out.println("\n>> 16.3 비블록 코드 만들기");
System.out.println("\n>> 16.4 비동기 작업 파이프라인 만들기");
System.out.println("\n>> 16.5 CompletableFuture의 종료에 대응하는 방법");




	}

}

class Util {
	public static void delay() {
		try {
			Thread.sleep(1000L);
		}catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}
}

class Shop {
	private final String name;
	private final Random random;
	
	public Shop(String name) {
		this.name = name;
		random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}
	
	public double getPrice(String product) {
		return calculatePrice(product);
	}

	private double calculatePrice(String product) {
		Util.delay();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}
	
	public String getName() {
		return name;
	}
	
	public Future<Double> getPriceAsync(String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		new Thread(() -> {
			double price = calculatePrice(product);
			futurePrice.complete(price);
		}).start();
		return futurePrice;
	}
}
