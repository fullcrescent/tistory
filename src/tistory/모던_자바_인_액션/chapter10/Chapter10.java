package tistory.모던_자바_인_액션.chapter10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tistory.모던_자바_인_액션.chapter10.Trade.Type;
import static tistory.모던_자바_인_액션.chapter10.MethodChainingOrderBuilder.forCustomer;
import static tistory.모던_자바_인_액션.chapter10.NestedFunctionOrderBuilder.at;
import static tistory.모던_자바_인_액션.chapter10.NestedFunctionOrderBuilder.buy;
import static tistory.모던_자바_인_액션.chapter10.NestedFunctionOrderBuilder.on;
import static tistory.모던_자바_인_액션.chapter10.NestedFunctionOrderBuilder.order;
import static tistory.모던_자바_인_액션.chapter10.NestedFunctionOrderBuilder.sell;
import static tistory.모던_자바_인_액션.chapter10.NestedFunctionOrderBuilder.stock;
import static tistory.모던_자바_인_액션.chapter10.LambdaOrderBuilder.order;
import static tistory.모던_자바_인_액션.chapter10.MixedBuilder.forCustomer;
import static tistory.모던_자바_인_액션.chapter10.MixedBuilder.buy;
import static tistory.모던_자바_인_액션.chapter10.MixedBuilder.sell;

public class Chapter10 {

	public static void main(String[] args) throws IOException {
		System.out.println("\n>> 10.1 도메인 전용 언어");
		
		/* 코드의 잡음 */
		List<String> list = Arrays.asList("111", "222", "333");
		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		});
		
		list.forEach(s -> System.out.println(s));
		
		list.forEach(System.out::println);
		
		
		System.out.println("\n>> 10.2 최신 자바 API의 작은 DSL");
		
		List<String> error;
		error = new ArrayList<>();
		int errorCount = 0;
		BufferedReader br = new BufferedReader(new FileReader("test.txt"));
		String line1 = br.readLine();
		while(errorCount<5 && line1!=null) {
			if(line1.startsWith("ERROR")) {
				error.add(line1);
				errorCount++;
			}
			line1 = br.readLine();
		}
		br.close();
		System.out.println(error);	// [ERROR 1, ERROR 2, ERROR 3, ERROR 4, ERROR 5]
		
		error = Files.lines(Paths.get("test.txt"))
					.filter(line2 -> line2.startsWith("ERROR"))
					.limit(5)
					.collect(Collectors.toList());
		System.out.println(error);	// [ERROR 1, ERROR 2, ERROR 3, ERROR 4, ERROR 5]
		
		
		System.out.println("\n>> 10.3 자바로 DSL을 만드는 패턴과 기법");
		
		Order order;
		order = new Order();
		order.setCustomer("User1");
		
		Trade trade1 = new Trade();
		trade1.setType(Trade.Type.BUY);
		
		Stock stock1 = new Stock();
		stock1.setSymbol("AMD");
		stock1.setMarket("NASDAQ");
		
		trade1.setStock(stock1);
		trade1.setPrice(91.18);
		trade1.setQuantity(100);
		order.addTrade(trade1);
		
		Trade trade2 = new Trade();
		trade2.setType(Trade.Type.BUY);
		
		Stock stcok2 = new Stock();
		stcok2.setSymbol("SBUX");
		stcok2.setSymbol("NASDAQ");
		
		trade2.setStock(stcok2);
		trade2.setPrice(84.08);
		trade2.setQuantity(200);
		order.addTrade(trade2);
		
		System.out.println(order);
		//Order[customer=User1, trades=[
		//   Trade[type=BUY, stock=Stock[symbol=AMD, market=NASDAQ], quantity=100, price=91.18]
		//   Trade[type=BUY, stock=Stock[symbol=NASDAQ, market=null], quantity=200, price=84.08]
		//]]
		
		
		/* 메서드 체인 실행 */
		order = forCustomer("User1")
				.buy(100).stock("AMD").on("NASDAQ").at(91.18)
				.sell(200).stock("SBUX").on("NASDAQ").at(84.08)
				.end();
		
		System.out.println(order);
		//Order[customer=User1, trades=[
		//  Trade[type=BUY, stock=Stock[symbol=AMD, market=NASDAQ], quantity=100, price=91.18]
		//  Trade[type=SELL, stock=Stock[symbol=SBUX, market=NASDAQ], quantity=200, price=84.08]
		//]]
		
		
		/* 중첩된 함수 이용 실행 */
		order = order("User1", 
				buy(100, stock("AMD", on("NASDAQ")), at(91.18)),
				sell(200, stock("SBUX", on("NASDAQ")), at(84.08))
				);
		
		System.out.println(order);
		//Order[customer=User1, trades=[
		//  Trade[type=BUY, stock=Stock[symbol=AMD, market=NASDAQ], quantity=100, price=91.18]
		//  Trade[type=SELL, stock=Stock[symbol=SBUX, market=NASDAQ], quantity=200, price=84.08]
		//]]
		
		
		/* 람다 표현식을 이용한 함수 시퀀싱 실행 */
		order = order(o -> {
			o.forCustomer("User1");
			o.buy(t -> {
				t.quantity(100);
				t.price(91.18);
				t.stock(s -> {
					s.symbol("AMD");
					s.market("NASDAQ");
				});
			});
			o.sell(t ->{
				t.quantity(200);
				t.price(84.08);
				t.stock(s -> {
					s.symbol("SBUX");
					s.market("NASDAQ");
				});
			});
		});
		
		System.out.println(order);
		//Order[customer=User1, trades=[
		//  Trade[type=BUY, stock=Stock[symbol=AMD, market=NASDAQ], quantity=100, price=91.18]
		//  Trade[type=SELL, stock=Stock[symbol=SBUX, market=NASDAQ], quantity=200, price=84.08]
		//]]
		
		
		/* 조합하기 실행 */
		order = forCustomer("User1", 
				buy(t -> t.quantity(100).stock("AMD").on("NASDAQ").at(91.18)),
				sell(t -> t.quantity(200).stock("SBUX").on("NASDAQ").at(84.08))
				);
		
		System.out.println(order);
		//Order[customer=User1, trades=[
		//  Trade[type=BUY, stock=Stock[symbol=AMD, market=NASDAQ], quantity=100, price=91.18]
		//  Trade[type=SELL, stock=Stock[symbol=SBUX, market=NASDAQ], quantity=200, price=84.08]
		//]]
		
		double value = new TaxCalculator().with(Tax::regional)
										.with(Tax::surcharge)
										.calculate(order);
		System.out.println(value);		// 29953.770000000004
		
		
System.out.println("\n>> 10.4 실생활의 자바8 DSL");
	}

}

/* 플루언트 형식 빌더 */
class GroupingBuilder<T, D, K>{
	private final Collector<? super T, ?, Map<K, D>> collector;
	
	private GroupingBuilder(Collector<? super T, ?, Map<K, D>> collector) {
		this.collector = collector;
	}
	
	public Collector<? super T, ?, Map<K, D>> get(){
		return collector;
	}
	
	public <J> GroupingBuilder<T, Map<K, D>, J> after(Function<? super T, ? extends J> classifier){
		return new GroupingBuilder<>(Collectors.groupingBy(classifier, collector));
	}
	
	public static <T, D, K> GroupingBuilder<T, List<T>, K> groupOn(Function<? super T, ? extends K> classifier){
		return new GroupingBuilder<>(Collectors.groupingBy(classifier));
	}
}

/* 메서드 체인 */
class MethodChainingOrderBuilder{
	public final Order order = new Order();	
	
	private MethodChainingOrderBuilder(String customer) {
		order.setCustomer(customer);
	}
	
	public static MethodChainingOrderBuilder forCustomer(String customer) {
		return new MethodChainingOrderBuilder(customer);
	}
	
	private MethodChainingOrderBuilder addTrade(Trade trade) {
		order.addTrade(trade);
		return this;
	}
	
	public static class TradeBuilderWithStock{
		private final MethodChainingOrderBuilder builder;
		private final Trade trade;
		
		public TradeBuilderWithStock(MethodChainingOrderBuilder builder, Trade trade) {
			this.builder = builder;
			this.trade = trade;
		}
		
		public MethodChainingOrderBuilder at(double price) {
			trade.setPrice(price);
			return builder.addTrade(trade);
		}
	}
	
	public static class StockBuilder {
		private final MethodChainingOrderBuilder builder;
		private final Trade trade;
		private final Stock stock = new Stock();
		
		public StockBuilder(MethodChainingOrderBuilder builder, Trade trade, String symbol) {
			this.builder = builder;
			this.trade = trade;
			stock.setSymbol(symbol);
		}
		
		public TradeBuilderWithStock on(String market) {
			stock.setMarket(market);
			trade.setStock(stock);
			return new TradeBuilderWithStock(builder, trade);
		}
	}
	
	public static class TradeBuilder{
		private final MethodChainingOrderBuilder builder;
		public final Trade trade = new Trade();
		
		public TradeBuilder(MethodChainingOrderBuilder builder, Trade.Type type, int quantity) {
			this.builder = builder;
			trade.setQuantity(quantity);
			trade.setType(type);
		}
		
		public StockBuilder stock(String symbol) {
			return new StockBuilder(builder, trade, symbol);
		}
	}
	
	public TradeBuilder buy(int quantity) {
		return new TradeBuilder(this, Trade.Type.BUY, quantity);
	}
	
	public TradeBuilder sell(int quantity) {
		return new TradeBuilder(this, Trade.Type.SELL, quantity);
	}
	
	public Order end() {
		return order;
	}
}

/* 중첩된 함수 이용 */
class NestedFunctionOrderBuilder{
	public static Order order(String customer, Trade... trades) {
		Order order = new Order();
		order.setCustomer(customer);
		Stream.of(trades).forEach(order::addTrade);
		return order;
	}
	
	public static String on(String market) {
		return market;
	}
	
	public static double at(double price) {
		return price;
	}
	
	private static Trade buildTrade(int quantity, Stock stock, double price, Type buy) {
		Trade trade = new Trade();
		trade.setPrice(price);
		trade.setQuantity(quantity);
		trade.setStock(stock);
		trade.setType(buy);
		return trade;
	}
	
	public static Trade buy(int quantity, Stock stock, double price) {
		return buildTrade(quantity, stock, price, Trade.Type.BUY);
	}
	
	public static Trade sell(int quantity, Stock stock, double price) {
		return buildTrade(quantity, stock, price, Trade.Type.SELL);
	}
	
	public static Stock stock(String symbol, String market) {
		Stock stock = new Stock();
		stock.setMarket(market);
		stock.setSymbol(symbol);
		return stock;
	}
}

/* 람다 표현식을 이용한 함수 시퀀싱 */
class LambdaOrderBuilder{
	private Order order = new Order();
	
	public static Order order(Consumer<LambdaOrderBuilder> consumer) {
		LambdaOrderBuilder builder = new LambdaOrderBuilder();
		consumer.accept(builder);
		return builder.order;
	}
	
	public void forCustomer(String customer) {
		order.setCustomer(customer);
	}
	
	public static class StockBuilder{
		private Stock stock = new Stock();
		
		public void symbol(String symbol) {
			stock.setSymbol(symbol);
		}
		
		public void market(String market) {
			stock.setMarket(market);
		}
	}
	
	public static class TradeBuilder{
		private Trade trade = new Trade();
		
		public void quantity(int quantity) {
			trade.setQuantity(quantity);
		}
		
		public void price(double price) {
			trade.setPrice(price);
		}
		
		public void stock(Consumer<StockBuilder> consumer) {
			StockBuilder builder = new StockBuilder();
			consumer.accept(builder);
			trade.setStock(builder.stock);
		}
	}
	
	private void trade(Consumer<TradeBuilder> consumer, Trade.Type type) {
		TradeBuilder builder = new TradeBuilder();
		builder.trade.setType(type);
		consumer.accept(builder);
		order.addTrade(builder.trade);
	}
	
	public void buy(Consumer<TradeBuilder> consumer) {
		trade(consumer, Trade.Type.BUY);
	}
	
	public void sell(Consumer<TradeBuilder> consumer) {
		trade(consumer, Trade.Type.SELL);
	}
}

/* 조합하기 */
class MixedBuilder{
	public static class TradeBuilder{
		private Trade trade = new Trade();
		
		public TradeBuilder quantity(int quantity) {
			trade.setQuantity(quantity);
			return this;
		}
		
		public TradeBuilder at(double price) {
			trade.setPrice(price);
			return this;
		}
		
		public StockBuilder stock(String symbol) {
			return new StockBuilder(this, trade, symbol);
		}
	}
	
	public static class StockBuilder{
		private final TradeBuilder builder;
		private final Trade trade;
		private final Stock stock = new Stock();
		
		private StockBuilder(TradeBuilder builder, Trade trade, String symbol) {
			this.builder = builder;
			this.trade = trade;
			stock.setSymbol(symbol);
		}
		
		public TradeBuilder on(String market) {
			stock.setMarket(market);
			trade.setStock(stock);
			return builder;
		}
	}
	
	public static Order forCustomer(String customer, TradeBuilder... builders) {
		Order order = new Order();
		order.setCustomer(customer);
		Stream.of(builders).forEach(b -> order.addTrade(b.trade));
		return order;
	}
	
	private static TradeBuilder buildTrade(Consumer<TradeBuilder> consumer, Type buy) {
		TradeBuilder builder = new TradeBuilder();
		builder.trade.setType(buy);
		consumer.accept(builder);
		return builder;
	}
	
	public static TradeBuilder buy(Consumer<TradeBuilder> consumer) {
		return buildTrade(consumer, Trade.Type.BUY);
	}
	
	public static TradeBuilder sell(Consumer<TradeBuilder> consumer) {
		return buildTrade(consumer, Trade.Type.SELL);
	}
}

class TaxCalculator{
	public DoubleUnaryOperator taxFunction = d -> d;
	
	public TaxCalculator with(DoubleUnaryOperator f) {
		taxFunction = taxFunction.andThen(f);
		return this;
	}
	
	public double calculate(Order order) {
		return taxFunction.applyAsDouble(order.getValue());
	}
}



