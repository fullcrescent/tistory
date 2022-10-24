package tistory.모던_자바_인_액션.chapter17;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Chapter17 {

public static void main(String[] args) {
System.out.println("\n>> 17 리액티브 프로그래밍");

System.out.println("\n>> 17.1 리액티브 매니패스토");
System.out.println("\n>> 17.2 리액티브 스트림과 플로 API");

getTemperatures("New York").subscribe(new TempSubscriber());
getCelsiusTemperatures("New York").subscribe(new TempSubscriber());

System.out.println("\n>> 17.3 리액티브 라이브러리 RxJava 사용하기");

Observable<TempInfo> observable = getTemperaturesObservable("New York");
observable.blockingSubscribe(new TempObserver());

}

private static Publisher<TempInfo> getTemperatures(String town){
	return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
}

private static Publisher<TempInfo> getCelsiusTemperatures(String town){
	return subscriber -> {
		TempProcessor processor = new TempProcessor();
		processor.subscribe(subscriber);
		processor.onSubscribe(new TempSubscription(processor, town));
	};
}

private static Observable<TempInfo> getTemperaturesObservable(String town) {
	return Observable.create(emitter -> Observable.interval(1, TimeUnit.SECONDS)
			.subscribe(i -> {
				if(!emitter.isDisposed()) {
					if(i>=5) {
						emitter.onComplete();
					}else {
						try{
							emitter.onNext(TempInfo.fetch(town));
						}catch (Exception e) {
							emitter.onError(e);
						}
						
					}
				}
			}));
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
		if(random.nextInt(10)==0) throw new RuntimeException("Error Message");
		
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
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public TempSubscription(Subscriber<? super TempInfo> subscriber, String town) {
		this.subscriber = subscriber;
		this.town = town;
	}

	@Override
	public void request(long n) {
//		에러를 일부러 발생시키지 않을시 재귀 호출으로 스택이 오버플로 될때까지 반복해서 일어남		
//		for(long i = 0L; i<n; i++) {
//			try {
//				subscriber.onNext(TempInfo.fetch(town));
//			}catch(Exception e) {
//				subscriber.onError(e);
//				break;
//			}
//		}
		executor.submit(() ->{
			for(long i = 0L; i<n; i++) {
				try {
					subscriber.onNext(TempInfo.fetch(town));
				}catch(Exception e) {
					subscriber.onError(e);
					executor.shutdown();
					break;
				}
			}
		});
		
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

class TempProcessor implements Processor<TempInfo, TempInfo>{
	private Subscriber<? super TempInfo> subscriber;
	
	@Override
	public void onSubscribe(Subscription subscription) {
		subscriber.onSubscribe(subscription);
	}

	@Override
	public void onNext(TempInfo item) {
		subscriber.onNext(new TempInfo(item.getTown(), (item.getTemp()-32)*5/9));
	}

	@Override
	public void onError(Throwable throwable) {
		subscriber.onError(throwable);
	}

	@Override
	public void onComplete() {
		subscriber.onComplete();
	}

	@Override
	public void subscribe(Subscriber<? super TempInfo> subscriber) {
		this.subscriber = subscriber;
	}
}

class TempObserver implements Observer<TempInfo>{
	@Override
	public void onSubscribe(Disposable d) {}

	@Override
	public void onNext(TempInfo t) {
		System.out.println(t);
	}

	@Override
	public void onError(Throwable e) {
		System.out.println("Error : " + e.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("Done!");
	}
	
}