package tistory.모던_자바_인_액션.chapter10;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
	private String customer;
	private List<Trade> trades = new ArrayList<>();
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public void addTrade(Trade trade) {
		trades.add(trade);
	}
	public double getValue() {
		return trades.stream().mapToDouble(Trade::getValue).sum();
	}

	public String toString() {
		String s = trades.stream().map(t -> "  " + t).collect(Collectors.joining("\n", "[\n", "\n]"));
		return String.format("Order[customer=%s, trades=%s]", customer, s);
	}
	
}
