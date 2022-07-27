package tistory.모던_자바_인_액션.chapter7;

import java.util.stream.Stream;

public class chapter7 {
	public static void main(String[] args) {
		System.out.println("\n>> 7.1 병렬 스트림");
		
		int n = 10;
		long sum1 = Stream.iterate(1L, i -> i+1)
				.limit(n)
				.reduce(0L, (l1, l2) -> {
					if(l2==n)	System.out.print(l2 + "=");
					else		System.out.print(l2 + "+");
					return l1 + l2;
				});
		System.out.println(sum1);
		
		long sum2 = Stream.iterate(1L, i -> i+1)
				.limit(n)
				.parallel()
				.reduce(0L, (l1, l2) -> {
					if(l2==n)	System.out.print(l2 + "=");
					else		System.out.print(l2 + "+");
					return l1 + l2;
				});
		System.out.println(sum2);
		
		
		System.out.println("\n>> 7.2 포크/조인 프레임워크");
		
		System.out.println("\n>> 7.3 Spliterator 인터페이스");

		
	}
}
