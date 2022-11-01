package tistory.모던_자바_인_액션.chapter18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

public class Chapter18 {

	public static void main(String[] args) {
		System.out.println("\n>> 18 함수형 관점으로 생각하기");
		
		System.out.println("\n>> 18.1 시스템 구현과 유지보수");
		
		List<Transaction> transactions = new ArrayList<>(
				Arrays.asList(new Transaction(1)
						,new Transaction(2)
						,new Transaction(5)
						,new Transaction(4)
						,new Transaction(3)));
		
		/* 명령형 프로그래밍 - '어떻게'에 집중 */
		Transaction mostExpensive1 = transactions.get(0);
		if(mostExpensive1==null) {
			throw new IllegalArgumentException("Empty list of transactions");
		}
		for(Transaction t : transactions.subList(1, transactions.size())) {
			if(t.getValue()>mostExpensive1.getValue()) {
				mostExpensive1 = t;
			}
		}
		System.out.println(mostExpensive1.getValue());
		
		/* 선언형 프로그래밍 - '무엇을'에 집중 */
		Optional<Transaction> mostExpensive2 = transactions.stream().max(Comparator.comparing(Transaction::getValue));
		System.out.println(mostExpensive2.get().getValue());
		
		
		
		System.out.println("\n>> 18.2 함수형 프로그래밍이란 무엇인가?");
		
		List<List<Integer>> subs = subsets(Arrays.asList(1, 4, 9));
		subs.forEach(System.out::println);
		
		
		
		System.out.println("\n>> 18.3 재귀와 반복");
		
		factorialIterative(4);
		factorialRecursive(4);
		factorialStreams(4);
		factorialTailRecursive(4);
	}

	private static List<List<Integer>> subsets(List<Integer> list){
		if(list.isEmpty()) {
			List<List<Integer>> answer = new ArrayList<>();
			answer.add(Collections.emptyList());
			return answer;
		}
		Integer first = list.get(0);
		List<Integer> rest = list.subList(1, list.size());
		List<List<Integer>> subanswer1 = subsets(rest);
		List<List<Integer>> subanswer2 = insertAll(first, subanswer1);
		return concat(subanswer1, subanswer2);
	}
	
	private static List<List<Integer>> insertAll(Integer first, List<List<Integer>> lists) {
		List<List<Integer>> result = new ArrayList<>();
		
		for(List<Integer> list : lists) {
			List<Integer> copyList = new ArrayList<>();
			copyList.add(first);
			copyList.addAll(list);
			result.add(copyList);
		}
		
		return result;
	}
	
	/* 실무에서는 아래와 같이 사용하지 않도록 주의 */
	private static List<List<Integer>> concat(List<List<Integer>> list1, List<List<Integer>> list2) {
		List<List<Integer>> list = new ArrayList<>(list1);
		list.addAll(list2);
		
		return list;
	}
	
	/* 반복 방식 */
	private static int factorialIterative(int n) {
		int r = 1;
		
		for(int i=1; i<n; i++) r *= i;
		
		return r;
	}
	
	/* 재귀 방식 */
	private static long factorialRecursive(long n) {
		return n==1 ? 1 : n*factorialRecursive(n-1);
	}
	
	/* 스트림 */
	private static long factorialStreams(long n) {
		return LongStream.rangeClosed(1, n)
				.reduce(1, (a, b) -> a*b);
	}
	
	/* 꼬리 재귀 */
	private static long factorialTailRecursive(long n) {
		return factorialHelper(1, n);
	}
	private static long factorialHelper(long acc, long n) {
		return n==1 ? acc : factorialHelper(acc*n, n-1);
	}
}

class Transaction{
	private int value;
	
	public Transaction(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}