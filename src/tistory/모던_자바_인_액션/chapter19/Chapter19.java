package tistory.모던_자바_인_액션.chapter19;

import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


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
		//24℃ = 75.20℉
		//US＄100 = ￡60.00
		//20km = 12.43miles
		
		
		System.out.println("\n>> 19.2 영속 자료구조");
		
		TrainJourney first;
		TrainJourney second;
		
		first = new TrainJourney(1, null);
		second = new TrainJourney(2, null);
		TrainJourney link = TrainJourney.link(first, second);
		System.out.println("명령형 : " + link);
		// 명령형 : TrainJourney[1] -> TrainJourney[2] -> null
		
		first = new TrainJourney(1, null);
		second = new TrainJourney(2, null);
		TrainJourney append = TrainJourney.append(first, second);
		System.out.println("함수형 : " + append);
		// 함수형 : TrainJourney[1] -> TrainJourney[2] -> null
		
		
		Tree tree = new Tree("Mary", 22,
				new Tree("Emily", 20, 
						new Tree("Alan", 50, null, null),
						new Tree("Georgie", 23, null, null)
						),
				new Tree("Tian", 29,
						new Tree("Raoul", 23, null, null),
						null)
				);
		
		System.out.println(TreeProcessor.lookup("Raoul", -1, tree));	// 23
		System.out.println(TreeProcessor.lookup("Will", -1, tree));		// -1
		
		Tree f = TreeProcessor.fupdate("Will", 26, tree);
		System.out.println(TreeProcessor.lookup("Will", -1, tree));		// -1
		System.out.println(TreeProcessor.lookup("Will", -1, f));		// 26
		
		Tree u = TreeProcessor.update("Will", 40, tree);
		System.out.println(TreeProcessor.lookup("Will", -1, tree));		// 40
		System.out.println(TreeProcessor.lookup("Will", -1, u));		// 40
		
		
		
		System.out.println("\n>> 19.3 스트림과 게으른 평가");
		
		System.out.println(primes(25).map(String::valueOf).collect(Collectors.joining(", ")));
		// 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
		
		MyList<Integer> list = new MyLinkedList<>(5, new MyLinkedList<>(10, new Empty<>()));
		System.out.println(list.head());
		// 5
		
		LazyList<Integer> numbers = from(2);
		int two = numbers.head();
		int three = numbers.tail().head();
		int four = numbers.tail().tail().head();
		System.out.println(two + " " + three + " " + four);
		// 2 3 4
		
		int prime_two = primes(numbers).head();
		int prime_three = primes(numbers).tail().head();
		int prime_four = primes(numbers).tail().tail().head();
		System.out.println(prime_two + " " + prime_three + " " + prime_four);
		// 2 3 5
		
		/* 꼬리 호출 제거 기능이 없음 -> 스택 오버플로가 발생할 때까지 실행 */
		//printAll(numbers);
				
				
				
		System.out.println("\n>> 19.4 패턴 매칭");
		
		Expr e = new BinOp("+", new Number(5), new BinOp("*", new Number(3), new Number(4)));
		Expr match = simplify(e);
		System.out.println(match);
		
		
		System.out.println("\n>> 19.5 기타 정보");
		
		System.out.println(repeat(3, (Integer x) -> 2*x).apply(10));
	}

	private static DoubleUnaryOperator curriedConverter(double y, int z) {
		return (double x) -> x*y+z;
	}
		
	private static Stream<Integer> primes(int n){
		return Stream.iterate(2, i -> i+1)
				.filter(Chapter19::isPrime)
				.limit(n);
	}
	
	private static boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt(candidate);
		return IntStream.rangeClosed(2, candidateRoot)
				.noneMatch(i -> candidate%i==0);
	}
	
	private static LazyList<Integer> from(int n){
		return new LazyList<>(n, ()->from(n+1));
	}
	
	private static MyList<Integer> primes(MyList<Integer> numbers){
		return new LazyList<>(
				numbers.head(), 
				() -> primes(
						numbers.tail().filter(n -> n%numbers.head()!=0)));
	}
	
//	private static <T> void printAll(MyList<T> list) {
//	//	while(!list.isEmpty()) {
//	//		System.out.println(list.head());
//	//		list = list.tail();
//	//	}
//	
//		if(list.isEmpty()) return;
//		
//		System.out.println(list.head());
//		printAll(list.tail());
//	}
		
	private static Expr simplify(Expr e) {
		TriFunction<String, Expr, Expr, Expr> binopcase = 
				(opname, left, right) -> {
					if("+".equals(opname)) {
						if(left instanceof Number && ((Number) left).val==0) {
							return right;
						}
						if(right instanceof Number && ((Number) right).val==0) {
							return left;
						}
					}
					
					if("*".equals(opname)) {
						if(left instanceof Number && ((Number) left).val==1) {
							return right;
						}
						if(right instanceof Number && ((Number) right).val==1) {
							return left;
						}
					}
					
					return new BinOp(opname, left, right);
				};
		
		Function<Integer, Expr> numcase = val -> new Number(val);
		Supplier<Expr> defaultcase = () -> new Number(0);
		
		return patternMatchExpr(e, binopcase, numcase, defaultcase);
	}
	
	private static <T> T patternMatchExpr(Expr e
			, TriFunction<String, Expr, Expr, T> binopcase
			, Function<Integer, T> numcase
			, Supplier<T> defaultcase) {
		return (e instanceof BinOp) ? 
				binopcase.apply(((BinOp)e).opname, ((BinOp)e).left, ((BinOp)e).right) :
				(e instanceof Number) ?
					numcase.apply(((Number)e).val) :
					defaultcase.get();
	}
	
	private static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f){
		return x -> g.apply(f.apply(x));
	}
	
	private static <A> Function<A, A> repeat(int n, Function<A, A> f){
		return n==0 ? x -> x : compose(f, repeat(n-1, f));
	}
}

class TrainJourney{
	public int price;
	public TrainJourney onward;
	
	public TrainJourney(int p, TrainJourney t) {
		price = p;
		onward = t;
	}
	
	/* 자료 구조가 파괴적으로 갱신 */
	public static TrainJourney link(TrainJourney a, TrainJourney b) {
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
	
	/* 자료구조 갱신X(함수형) */
	public static TrainJourney append(TrainJourney a, TrainJourney b) {
		return a==null ? b : new TrainJourney(a.price, append(a.onward, b));
	}
	
	public void visit(TrainJourney journey, Consumer<TrainJourney> c) {
		if(journey!=null) {
			c.accept(journey);
			visit(journey.onward, c);
		}
	}
	
	@Override
	public String toString() {
		return String.format("TrainJourney[%d] -> %s", price, onward);
	}
}

class Tree{
	public String key;
	public int val;
	public Tree left, right;
	
	public Tree(String key, int val, Tree left, Tree right) {
		this.key = key;
		this.val = val;
		this.left = left;
		this.right = right;
	}
}

class TreeProcessor{
	public static int lookup(String key, int defaultval, Tree tree) {
		if(tree==null) return defaultval;
		if(key.equals(tree.key)) return tree.val;
		return lookup(key, defaultval, key.compareTo(tree.key)<0 ? tree.left : tree.right);
	}
	
	public static Tree update(String key, int newval, Tree tree) {
		if(tree==null) {
			tree = new Tree(key, newval, null, null);
		}
		else if(key.equals(tree.key)) {
			tree.val = newval;
		}
		else if(key.compareTo(tree.key)<0) {
			tree.left = update(key, newval, tree.left);
		}
		else {
			tree.right = update(key, newval, tree.right);
		}
		
		return tree;
	}
	
	/* 함수형 접근 - 자료구조 변경X */
	public static Tree fupdate(String key, int newval, Tree tree) {
		return (tree==null) ? 
				new Tree(key, newval, null, null) : 
				key.equals(tree.key) ? 
						new Tree(key, newval, tree.left, tree.right) : 
						key.compareTo(tree.key)<0 ? 
								new Tree(tree.key, tree.val, fupdate(key, newval, tree.left), tree.right) :
								new Tree(tree.key, tree.val, tree.left, fupdate(key, newval, tree.right)); 
	}
}

interface MyList<T>{
	T head();
	
	MyList<T> tail();
	
	default boolean isEmpty() {
		return true;
	}
	
	MyList<T> filter(Predicate<T> p);
}

class MyLinkedList<T> implements MyList<T>{
	private final T head;
	private final MyList<T> tail;
	
	public MyLinkedList(T head, MyList<T> tail) {
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public T head() {
		return head;
	}

	@Override
	public MyList<T> tail() {
		return tail;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public MyList<T> filter(Predicate<T> p) {
		return isEmpty() ? 
				this : 
				p.test(head()) ? 
					new MyLinkedList<>(head, tail().filter(p)) : 
					tail().filter(p);
	}
}

class Empty<T> implements MyList<T>{
	@Override
	public T head() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MyList<T> tail() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MyList<T> filter(Predicate<T> p) {
		return this;
	}
}

class LazyList<T> implements MyList<T>{
	private final T head;
	private final Supplier<MyList<T>> tail;
	
	public LazyList(T head, Supplier<MyList<T>> tail) {
		this.head = head;
		this.tail = tail;
	}

	@Override
	public T head() {
		return head;
	}

	@Override
	public MyList<T> tail() {
		return tail.get();
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public MyList<T> filter(Predicate<T> p) {
		return isEmpty() ? 
				this : 
				p.test(head()) ? 
					new LazyList<>(head(), () -> tail().filter(p)) :
					tail().filter(p);
	}
}

interface TriFunction<S, T, U, R>{
	R apply(S s, T t, U u);
}

class Expr{}

class Number extends Expr{
	int val;
	
	public Number(int val) {
		this.val = val;
	}
	
	@Override
	public String toString() {
		return "" + val;
	}
}

class BinOp extends Expr{
	String opname;
	Expr left, right;
	
	public BinOp(String opname, Expr left, Expr right) {
		this.opname = opname;
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String toString() {
		return "(" + left + " " + opname + " " + right + ")";
	}
}