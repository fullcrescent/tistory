package tistory.모던_자바_인_액션.chapter18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

public class Chapter18 {

	public static void main(String[] args) {
System.out.println("\n>> 18 함수형 관점으로 생각하기");
System.out.println("\n>> 18.1 시스템 구현과 유지보수");
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

private static List<List<Integer>> concat(List<List<Integer>> list1, List<List<Integer>> list2) {
	List<List<Integer>> list = new ArrayList<>(list1);
	list.addAll(list2);
	
	return list;
}

private static int factorialIterative(int n) {
	int r = 1;
	
	for(int i=1; i<n; i++) r *= i;
	
	return r;
}

private static long factorialRecursive(long n) {
	return n==1 ? 1 : n*factorialRecursive(n-1);
}

private static long factorialStreams(long n) {
	return LongStream.rangeClosed(1, n)
			.reduce(1, (a, b) -> a*b);
}

private static long factorialTailRecursive(long n) {
	return factorialHelper(1, n);
}

private static long factorialHelper(long acc, long n) {
	return n==1 ? acc : factorialHelper(acc*n, n-1);
}
}
