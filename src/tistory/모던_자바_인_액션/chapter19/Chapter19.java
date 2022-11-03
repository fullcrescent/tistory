package tistory.모던_자바_인_액션.chapter19;

import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

public class Chapter19 {
	public static void main(String[] args) {
		System.out.println("\n>> 19 함수형 프로그래밍 기법");
		System.out.println("\n>> 19.1 함수는 모든 곳에 존재한다");
		
		DoubleUnaryOperator convertCtoF = curriedConverter(9.0/5, 32);
		DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
		DoubleUnaryOperator convertKmtoMi = curriedConverter(0.6214, 0);
		
		System.out.printf("24℃ = %.2f℉%n", convertCtoF.applyAsDouble(24));
		System.out.printf("US＄100 = ￡%.2f%n", convertUSDtoGBP.applyAsDouble(100));
		System.out.printf("20km = %.2fmiles%n", convertKmtoMi.applyAsDouble(20));
		
		System.out.println("\n>> 19.2 영속 자료구조");
		
		System.out.println("\n>> 19.3 스트림과 게으른 평가");
		
		System.out.println("\n>> 19.4 패턴 매칭");
		
		System.out.println("\n>> 19.5 기타 정보");
		
	}

	private static DoubleUnaryOperator curriedConverter(double y, int z) {
		return (double x) -> x*y+z;
	}
}

class TrainJourney{
	public int price;
	public TrainJourney onward;
	
	public TrainJourney(int p, TrainJourney t) {
		price = p;
		onward = t;
	}
	
	static TrainJourney link(TrainJourney a, TrainJourney b) {
		if(a==null) {
			return b;
		}
		
		TrainJourney t = a;
		while(t.onward!=null) {
			t=t.onward;
		}
		t.onward=b;
		return a;
	}
	
	static TrainJourney append(TrainJourney a, TrainJourney b) {
		return a==null ? b : new TrainJourney(a.price, append(a.onward, b));
	}
	
	void visit(TrainJourney journey, Consumer<TrainJourney> c) {
		if(journey!=null) {
			c.accept(journey);
			visit(journey.onward, c);
		}
	}
}