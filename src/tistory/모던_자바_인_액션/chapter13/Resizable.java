package tistory.모던_자바_인_액션.chapter13;

public interface Resizable extends Drawable{
	int getWidth();
	int getHeight();
	void setWidth(int width);
	void setHeight(int hegith);
	void setAbsoluteSize(int width, int height);
//	default void setRelativeSize(int width, int height);
}
