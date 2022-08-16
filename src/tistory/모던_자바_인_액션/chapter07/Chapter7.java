package tistory.모던_자바_인_액션.chapter07;

import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Chapter7 {
	public static void main(String[] args) {
		System.out.println("\n>> 7.1 병렬 스트림");
		
		int n = 10;
		long sum1 = Stream.iterate(1L, i -> i+1)
				.limit(n)
				.reduce(0L, (l1, l2) -> {
					System.out.print(l1 + "+" + l2 + "=" + (l1 + l2) + " ");
					return l1 + l2;
				});
		System.out.println(sum1);
		// 0+1=1 1+2=3 3+3=6 6+4=10 10+5=15 15+6=21 21+7=28 28+8=36 36+9=45 45+10=55 55
		
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "2");
		// 스트림의 parallel 메서드에서 스레드 생성 개수 설정
		// 기본값(권장)은 기기의 프로세서 수로 세팅
		long sum2 = Stream.iterate(1L, i -> i+1)
				.limit(n)
				.parallel()						// 청크로 분할
				.reduce(0L, (l1, l2) -> {
					System.out.print(l1 + "+" + l2 + "=" + (l1 + l2) + " ");
					return l1 + l2;
				});
		System.out.println(sum2);
		// 0+7=7 0+3=3 0+5=5 0+4=4 4+5=9 0+6=6 3+9=12 6+7=13 0+2=2 0+9=9 0+1=1 0+10=10 1+2=3 9+10=19 3+12=15 0+8=8 8+19=27 13+27=40 15+40=55 55+0=55 55
		// 위의 parallelism 값을 1로 바꿀 시 아래처럼 더 짧게 출력(0과 더하는 개수가 줄어듬) 
		// 0+6=6 6+7=13 0+9=9 9+10=19 0+8=8 8+19=27 13+27=40 0+3=3 0+4=4 4+5=9 3+9=12 0+1=1 1+2=3 3+12=15 15+40=55 55+0=55 55
		
//		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");
		sum2 = Stream.iterate(1L, i -> i+1)
				.limit(n)
				.parallel()						// 청크로 분할
				.reduce(0L, (l1, l2) -> {
					System.out.print(l1 + "+" + l2 + "=" + (l1 + l2) + " ");
					return l1 + l2;
				});
		System.out.println(sum2);
		
		long sum3 = Stream.iterate(1L, i -> i+1)
				.limit(n)
				.parallel()						// 청크로 분할
				.sequential()					// 순차 스트림으로 변환
				.reduce(0L, (l1, l2) -> {
					System.out.print(l1 + "+" + l2 + "=" + (l1 + l2) + " ");
					return l1 + l2;
				});
		System.out.println(sum3);
		// 0+1=1 1+2=3 3+3=6 6+4=10 10+5=15 15+6=21 21+7=28 28+8=36 36+9=45 45+10=55 55
		
		System.out.println(measurePerf(t ->{
			Sum sum = new Sum();
			LongStream.rangeClosed(1, 10_000_000L).parallel().forEach(sum::add);
			return sum.total;
		}, 10_000_000L) + " msecs");
		
		
		System.out.println("\n>> 7.2 포크/조인 프레임워크");
		
		System.out.println(measurePerf(t -> {
			long[] imsiNumbers = LongStream.rangeClosed(1, 10_000_000L).toArray();
			ForkJoinTask<Long> imsiTask = new ForkJoinSum(imsiNumbers);
			return new ForkJoinPool().invoke(imsiTask);
		}, 10_000_000L) + " msecs");
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//Result: 50000005000000
		//21 msecs
		
		System.out.println("\n>> 7.3 Spliterator 인터페이스");
		
		Spliterator<Character> spliterator = new WorCounnterSpliterator("A B C DEFG HI JK");
		Stream<Character> stream = StreamSupport.stream(spliterator, true);
		WordCounter wordCounter = stream.reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine);
		System.out.println(wordCounter.getCounter());		// 6
		
	}
	
	// 성능 측정 메소드
	public static long measurePerf(Function<Long, Long> f, Long input) {
		long fastest = Long.MAX_VALUE;
		
		for(int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			Long result = f.apply(input);
			long duration = (System.nanoTime() - start) / 1_000_000;
			
			System.out.println("Result: " + result);
			
			if (duration < fastest) {
				fastest = duration;
			}
		}
		return fastest;
	}
}

class Sum {
	public long total = 0;
	public void add(long value) {
		total += value;
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
		if(length<THRESHOLD) {					// 기준에 비해 작은 Task
			return computeSequentially();		// 순차적 계산
		}
		
		ForkJoinSum leftTask = new ForkJoinSum(numbers, start, start+length/2);	// 첫번째 서브 태스크 생성
		leftTask.fork();	// ForkJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기 실행
		
		ForkJoinSum rightTask = new ForkJoinSum(numbers, start+length/2, end);	// 두번째 서브 태스크 생성
		Long rightResult = rightTask.compute();	// 두번째 서브 태스크 동기 실행
		Long leftResult = leftTask.join();		// 첫번째 서브 태스크의 결과를 읽거나 대기
		
		return leftResult + rightResult;		// 결과 조합
	}

	private Long computeSequentially() {
		long sum=0;
		for(int i=start; i<end; i++) {
			sum += numbers[i];
		}
		return sum;
	}
}

class WordCounter {
	private final int counter;
	private final boolean lastSpace;
	
	public WordCounter(int counter, boolean lastSpace) {
		super();
		this.counter = counter;
		this.lastSpace = lastSpace;
	}
	
	public WordCounter accumulate(Character c) {
		if(Character.isWhitespace(c)) {
			return lastSpace ? this : new WordCounter(counter, true);
		}
		else {
			return lastSpace ? new WordCounter(counter+1, false) : this;
		}
	}
	public WordCounter combine(WordCounter wordCounter) {
		return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
	}
	public int getCounter() {
		return counter;
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
		action.accept(string.charAt(currentChar++));	// 현재 문자 소비
		return currentChar < string.length();			// 소비할 문자 있는지 판단
	}

	@Override
	public Spliterator<Character> trySplit() {
		int currentSize = string.length() - currentChar;
		if(currentSize<10) {	// if문을 기준으로 순차처리할 만큼 작아 졌음을 판단
			return null;
		}

		for(int splitPosition=currentSize/2+currentChar; splitPosition<string.length(); splitPosition++) {
			if(Character.isWhitespace(string.charAt(splitPosition))) {	// 공백 단위로 분할
				Spliterator<Character> spliterator = new WorCounnterSpliterator(string.substring(currentChar, splitPosition));
				currentChar = splitPosition;
				return spliterator;			// 새로 만든 Spliterator 반환
			}
		}
		
		return null;
	}

	@Override
	public long estimateSize() {			// 탐색해야할 요소의 개수를 의미
		return string.length()-currentChar;
	}

	@Override
	public int characteristics() {			// 프레임워크에 Spliterator의 특성을 알려줌.
		return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
	}
}


