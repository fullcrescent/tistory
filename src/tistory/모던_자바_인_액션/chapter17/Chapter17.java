package tistory.모던_자바_인_액션.chapter17;

import java.util.Random;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class Chapter17 {

	public static void main(String[] args) {
		System.out.println("\n>> 17 리액티브 프로그래밍");
		
		System.out.println("\n>> 17.1 리액티브 매니패스토");
		System.out.println("\n>> 17.2 리액티브 스트림과 플로 API");
		System.out.println("\n>> 17.3 리액티브 라이브러리 RxJava 사용하기");
	}

}

class TempInfo{
	public static final Random random = new Random();
	
	private final String town;
	private final int temp;
	
	public TempInfo(String town, int temp) {
		this.town = town;
		this.temp = temp;
	}
	
	public static TempInfo fetch(String town) {
		if(random.nextInt(10)==0) throw new RuntimeException();
		
		return new TempInfo(town, random.nextInt(100));
	}

	@Override
	public String toString() {
		return town + " : " + temp;
	}

	public String getTown() {
		return town;
	}

	public int getTemp() {
		return temp;
	}
}

class TempSubscription implements Subscription{
	private final Subscriber<? super TempInfo> subscriber;
	private final String town;
	
	
	public TempSubscription(Subscriber<? super TempInfo> subscriber, String town) {
		this.subscriber = subscriber;
		this.town = town;
	}

	@Override
	public void request(long n) {
		for(long i = 0L; i<n; i++) {
			try {
				subscriber.onNext(TempInfo.fetch(town));
			}catch(Exception e) {
				subscriber.onError(e);
				break;
			}
		}
	}

	@Override
	public void cancel() {
		subscriber.onComplete();
	}
}

class TempSubscriber implements Subscriber<TempInfo>{
	private Subscription subscription;
	
	
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(TempInfo item) {
		System.out.println(item);
		subscription.request(1);
	}

	@Override
	public void onError(Throwable throwable) {
		System.err.println(throwable.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("Done!");
	}
	
}