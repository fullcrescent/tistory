package tistory.모던_자바_인_액션.chapter7;

import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Consumer;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
		
		long[] numbers = LongStream.rangeClosed(1, 100).toArray();
		ForkJoinTask<Long> task = new ForkJoinSum(numbers);
		long answer = new ForkJoinPool().invoke(task);
		System.out.println(answer);
		
		Spliterator<Character> spliterator = new WorCounnterSpliterator("A B C DEFG");
		Stream<Character> stream = StreamSupport.stream(spliterator, true);
		
		System.out.println("\n>> 7.3 Spliterator 인터페이스");

		
	}
}

class ForkJoinSum extends RecursiveTask<Long>{
	private final long[] numbers;
	private final int start;
	private final int end;
	public static final long THRESHOLD=10_000;
	
	public ForkJoinSum(long[] numbers) {
		this(numbers, 0, numbers.length);
	}
	
	public ForkJoinSum(long[] numbers, int start, int end) {
		super();
		this.numbers = numbers;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		int length = end -start;
		if(length<=THRESHOLD) {
			return computeSequentially();
		}
		
		ForkJoinSum leftTask = new ForkJoinSum(numbers, start, start+length/2);
		leftTask.fork();
		
		ForkJoinSum rightTask = new ForkJoinSum(numbers, start+length/2, end);
		Long rightResult = rightTask.compute();
		Long leftResult = leftTask.join();
		return leftResult + rightResult;
	}

	private Long computeSequentially() {
		long sum=0;
		for(int i=start; i<end; i++) {
			sum += numbers[i];
		}
		return sum;
	}
}

class WorCounnterSpliterator implements Spliterator<Character>{
	private final String string;
	private int currentChar = 0;
	
	public WorCounnterSpliterator(String string) {
		this.string = string;
	}

	@Override
	public boolean tryAdvance(Consumer<? super Character> action) {
		action.accept(string.charAt(currentChar++));
		return currentChar < string.length();
	}

	@Override
	public Spliterator<Character> trySplit() {
		int currentSize = string.length() - currentChar;
		if(currentSize<10) {
			return null;
		}

		for(int splitPos = currentSize/2+currentChar; splitPos<string.length();splitPos++) {
			if(Character.isWhitespace(string.charAt(splitPos))) {
				Spliterator<Character> spliterator = new WorCounnterSpliterator(string.substring((currentChar), splitPos));
				currentChar = splitPos;
				return spliterator;
			}
		}
		
		return null;
	}

	@Override
	public long estimateSize() {
		return string.length()-currentChar;
	}

	@Override
	public int characteristics() {
		return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
	}
	
}
