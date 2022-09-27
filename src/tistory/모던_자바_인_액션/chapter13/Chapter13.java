package tistory.모던_자바_인_액션.chapter13;

public class Chapter13 {

	public static void main(String[] args) {
		System.out.println("\n>> 13 디폴트 메서드");
		System.out.println("\n>> 13.1 변화하는 API");
		
		System.out.println("\n>> 13.2 디폴트 메서드란 무엇인가?");
		
		
		
		System.out.println("\n>> 13.3 디폴트 메서드 활용 패턴");
		
		C1 c = new C1();
		c.def();
		
		
		System.out.println("\n>> 13.4 해석 규칙");
		
		
		
	}

}

interface Resizable {
	int getWidth();
	int getHeight();
	void setWidth(int width);
	void setHeight(int hegith);
	void setAbsoluteSize(int width, int height);
//	void setRelativeSize(int width, int height);			디폴트 메서드를 사용하지 않을 시 전부다 구현을 해야함.
	default void setRelativeSize(int width, int height) {
		setAbsoluteSize(getWidth()/width, getHeight()/height);
	};
}


interface I1{
	default void def() {
		System.out.println("I1");
	};
}

interface I2 extends I1{}

interface I3 extends I1{
	default void def() {
		System.out.println("I3");
	};
}

class C1 implements I2, I3{
	@Override
	public void def() {
		I3.super.def();		// I2로는 접근 불가
	}
}
