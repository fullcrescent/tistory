package tistory.모던_자바_인_액션.chapter16;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Chapter16 {

	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
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

executor.shutdown();



System.out.println("\n>> 16.2 비동기 API 구현");

Shop shop = new Shop("BestShop");
long start = System.nanoTime();

Future<Double> futurePrice = shop.getPriceAsync("product");

long startTime = ((System.nanoTime()-start)/1_000_000);
System.out.println("return start " + startTime + " msecs");

double price = futurePrice.get();
//double price = futurePrice.get(1, TimeUnit.SECONDS);	타임 아웃으로 제어할시 계산 중에 에러가 난 사항을 잡을 수 없음.
System.out.printf("Price is %.2f%n", price);

long endTime = ((System.nanoTime()-start)/1_000_000);
System.out.println("return end " + endTime + " msecs");



System.out.println("\n>> 16.3 비블록 코드 만들기");

PriceFinder priceFinder = new PriceFinder();

/* 순차 */
duration(priceFinder::findPrices1);

/* 병렬 스트림 */
duration(priceFinder::findPrices2);

/* CompletableFuture로 비동기 호출 */
duration(priceFinder::findPrices3);

System.out.println("\n>> 16.4 비동기 작업 파이프라인 만들기");
System.out.println("\n>> 16.5 CompletableFuture의 종료에 대응하는 방법");
	}

private static void duration(Function<String, List<String>> f) {
	long start = System.nanoTime();
	System.out.println(f.apply("myPhone27S"));
	long duration = (System.nanoTime()-start)/1_000_000;
	System.out.println("총 실행시간 : " + duration + "msecs");
}
	

}

class Util {
	private static final DecimalFormat formatter = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
	
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
//		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//		new Thread(() -> {
//			try {
//				double price = calculatePrice(product);
//				futurePrice.complete(price);
//			}
//			/* 예외 발생 시 get 메서드가 반활될 때까지 계속 기다려주는 것을 방지 */
//			catch(Exception e) {
//				futurePrice.completeExceptionally(e);		
//			}
//		}).start();
//		return futurePrice;
		
		/* 위의 주석 된 소스와 동일 */
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}
}

class PriceFinder {
	private final List<Shop> shops = Arrays.asList(
			new Shop("BestPrice")
			,new Shop("LetsSaveBig")
			,new Shop("MyFavoriteShop")
			,new Shop("BuyItAll")
			);
	
	private final Executor executor = Executors.newFixedThreadPool(shops.size(), (Runnable r) -> {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	});
	
	/* 순차 */
	public List<String> findPrices1(String product){
		return shops.stream()
				.map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
				.collect(Collectors.toList());
	}
	
	/* 병렬 스트림 */
	public List<String> findPrices2(String product){
		return shops.parallelStream()
				.map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
				.collect(Collectors.toList());
	}
	
	/* CompletableFuture로 비동기 호출 */
	public List<String> findPrices3(String product){
		List<CompletableFuture<String>> priceFutures = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + Math.round(shop.getPrice(product)), executor))
				.collect(Collectors.toList());
		
		return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
	}
}

class Discount{
	public enum Code{
		
	}
}
