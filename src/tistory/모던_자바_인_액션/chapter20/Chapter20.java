package tistory.모던_자바_인_액션.chapter20;

import java.util.Map;
import java.util.stream.IntStream;

public class Chapter20 {

	public static void main(String[] args) {
System.out.println("\n>> 20 OOP와 FP의 조화 : 자바와 스칼라 비교");
System.out.println("\n>> 20.1 스칼라 소개");

IntStream.rangeClosed(2, 6).forEach(n -> System.out.println("Hello " + n + " bottles of beer"));
// 2 to 6 foreach {n => println(s"hello ${n} bottles of beer") }

Map<String, Integer> authorsToAge = Map.ofEntries(
		Map.entry("Raoul", 23),
		Map.entry("Maroi", 40),
		Map.entry("Alan", 53)
		);
// val authorsToAge = Map("Raoul" -> 23, "Mario" -> 40, "Alan" -> 53)

System.out.println("\n>> 20.2 함수");



System.out.println("\n>> 20.3 클래스와 트레이트");



	}

}
