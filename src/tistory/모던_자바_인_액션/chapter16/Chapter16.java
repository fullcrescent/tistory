package tistory.모던_자바_인_액션.chapter16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import java.util.stream.Stream;

import tistory.모던_자바_인_액션.chapter16.Discount.Code;
import tistory.모던_자바_인_액션.chapter16.ExchangeService.Money;

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
				// 해당 스레드를 실행하지 않을경우 java.util.concurrent.TimeoutException 발생
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
		
		/* 순차 */
		duration(priceFinder::findPricesDiscount1);
		
		/* 병렬 스트림 */
		duration(priceFinder::findPricesDiscount2);
		
		/* CompletableFuture로 비동기 호출 */
		duration(priceFinder::findPricesDiscount3);
		
		
		/* 자바7 기준 */
		duration(priceFinder::findPricesInUSD1);
		
		/* 자바9 기준 */
		duration(priceFinder::findPricesInUSD2);
		
		
		
		System.out.println("\n>> 16.5 CompletableFuture의 종료에 대응하는 방법");
		
		new PriceFinder().print();
	}

	private static void duration(Function<String, List<String>> f) {
		long start = System.nanoTime();
		System.out.println(f.apply("myPhone27S"));
		long duration = (System.nanoTime()-start)/1_000_000;
		System.out.println("총 실행시간 : " + duration + "msecs");
	}
}

class Util {
	private static Random random = new Random();
	
	public static void delay() {
		try {
			Thread.sleep(1000L);
		}catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}

	public static void randomDelay() {
		int delay = 500 + random.nextInt(2000);
		try {
			Thread.sleep(delay);
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
	
	public String getInfo(String product) {
		double price = calculatePrice(product);
		Discount.Code code = Discount.Code.values()[random.nextInt((Discount.Code.values().length))];
		return String.format("%s:%2f:%s", name, price, code);
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
		List<CompletableFuture<String>> priceFutures = 
				shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + Math.round(shop.getPrice(product)), executor))
				.collect(Collectors.toList());
		
		return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
	}
	
	/* 순차 */
	public List<String> findPricesDiscount1(String product){
		return shops.stream()
				.map(shop -> shop.getInfo(product))
				.map(Quote::parse)
				.map(Discount::applyDiscount)
				.collect(Collectors.toList());
	}
	
	/* 병렬 스트림 */
	public List<String> findPricesDiscount2(String product){
		return shops.parallelStream()
				.map(shop -> shop.getInfo(product))
				.map(Quote::parse)
				.map(Discount::applyDiscount)
				.collect(Collectors.toList());
	}
	
	/* CompletableFuture로 비동기 호출 */
	public List<String> findPricesDiscount3(String product){
		List<CompletableFuture<String>> priceFutures = 
				shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getInfo(product), executor))
				.map(future -> future.thenApply(Quote::parse))
				.map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
				.collect(Collectors.toList());
		
		return priceFutures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.toList());
	}
	
	/* 자바7 기준 */
	public List<String> findPricesInUSD1(String product) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<String>> priceFutures = new ArrayList<>();
		
		for(Shop shop : shops) {
			final Future<Double> futureRate = executor.submit(new Callable<Double>() {
				public Double call() {
					return ExchangeService.getRate(Money.EUR, Money.USD);
				}
			});
			
			Future<String> futurePriceInUSD = executor.submit(new Callable<String>() {
				public String call() throws InterruptedException, ExecutionException {
					double priceInEUR = shop.getPrice(product);
					return shop.getName() + " price is " + priceInEUR * futureRate.get();
				}
			});
			
			priceFutures.add(futurePriceInUSD);
		}
		executor.shutdown();
		
		List<String> prices = new ArrayList<>();
		
		for(Future<String> priceFuture : priceFutures) {
			try {
				prices.add(priceFuture.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return prices;
	}
	
	/* 자바9 기준 */
	public List<String> findPricesInUSD2(String product) {
		List<CompletableFuture<String>> priceFutures = 
				shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product))
						.thenCombine(
								CompletableFuture.supplyAsync(
										() -> ExchangeService.getRate(Money.EUR, Money.USD))
										.completeOnTimeout(ExchangeService.DEFAULT_RATE, 1, TimeUnit.SECONDS),
								(price, rate) -> price * rate)
						.orTimeout(3, TimeUnit.SECONDS)
						.thenApply(price -> shop.getName() + " price is " + price))
				.collect(Collectors.toList());
		
		return priceFutures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.toList());
	}
	
	public Stream<CompletableFuture<String>> findPricesStream(String product){
		return shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getInfo(product), executor))
				.map(future -> future.thenApply(Quote::parse))
				.map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscountRandomDelay(quote), executor)));
	}
	
	public void print() {
		long start = System.nanoTime();
		CompletableFuture[] futures = findPricesStream("myPhone27S")
				.map(f -> f.thenAccept(s -> System.out.println(s + " (done in " + ((System.nanoTime()-start)/1_000_000) + "msecs)")))
				.toArray(size -> new CompletableFuture[size]);
		
		CompletableFuture.allOf(futures).join();
		System.out.println("All shops have now responded in (" + ((System.nanoTime()-start)/1_000_000) + "msecs)");
	}
}

class Quote{
	private final String shopName;
	private final double price;
	private final Discount.Code discountCode;
	
	public Quote(String shopName, double price, Code discountCode) {
		super();
		this.shopName = shopName;
		this.price = price;
		this.discountCode = discountCode;
	}
	
	public static Quote parse(String s) {
		String[] split = s.split(":");
		String shopName = split[0];
		double price = Double.parseDouble(split[1]);
		Discount.Code discountCode = Discount.Code.valueOf(split[2]);
		return new Quote(shopName, price, discountCode);
	}

	public String getShopName() {
		return shopName;
	}

	public double getPrice() {
		return price;
	}

	public Discount.Code getDiscountCode() {
		return discountCode;
	}
}

class Discount{
	public enum Code{
		NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
		
		private final int percentage;
		
		Code(int percentage){
			this.percentage = percentage;
		}
	}
	
	public static String applyDiscount(Quote quote) {
		return quote.getShopName() + " price is " + Discount.applyDiscount(quote.getPrice(), quote.getDiscountCode());
	}

	private static double applyDiscount(double price, Code discountCode) {
		Util.delay();
		return Math.round(price * (100 - discountCode.percentage)/100);
	}
	
	public static String applyDiscountRandomDelay(Quote quote) {
		return quote.getShopName() + " price is " + Discount.applyDiscountRandomDelay(quote.getPrice(), quote.getDiscountCode());
	}

	private static double applyDiscountRandomDelay(double price, Code discountCode) {
		Util.randomDelay();
		return Math.round(price * (100 - discountCode.percentage)/100);
	}
}

class ExchangeService{
	public static final double DEFAULT_RATE = 1.35;
	
	public enum Money{
		USD(1.0), EUR(1.35387), GBP(1.69715), CAD(.92106), MXN(.07683);
		
		private final double rate;
		
		Money(double rate){
			this.rate = rate;
		}
	}
	
	public static double getRate(Money source, Money destination) {
		return getRateWithDelay(source, destination);
	}

	private static double getRateWithDelay(Money source, Money destination) {
		Util.delay();
		return destination.rate/source.rate;
	}
}
