package tistory.모던_자바_인_액션.chapter16;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Chapter16 {

	public static void main(String[] args) {
		System.out.println("\n>> 16 CompletableFuture : 안정적 비동기 프로그래밍");
		
		System.out.println("\n>> 16.1 Future의 단순 활용");
		System.out.println("\n>> 16.2 비동기 API 구현");
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
