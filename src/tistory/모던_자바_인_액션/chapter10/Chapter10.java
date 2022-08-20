package tistory.모던_자바_인_액션.chapter10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Chapter10 {

	public static void main(String[] args) throws IOException {
		System.out.println("\n>> 10.1 도메인 전용 언어");
		
		/* 코드의 잡음 */
		List<String> list = Arrays.asList("111", "222", "333");
		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		});
		
		list.forEach(s -> System.out.println(s));
		
		list.forEach(System.out::println);
		
		
		System.out.println("\n>> 10.2 최신 자바 API의 작은 DSL");
		
		List<String> error;
		error = new ArrayList<>();
		int errorCount = 0;
		BufferedReader br = new BufferedReader(new FileReader("test.txt"));
		String line1 = br.readLine();
		while(errorCount<5 && line1!=null) {
			if(line1.startsWith("ERROR")) {
				error.add(line1);
				errorCount++;
			}
			line1 = br.readLine();
		}
		br.close();
		System.out.println(error);	// [ERROR 1, ERROR 2, ERROR 3, ERROR 4, ERROR 5]
		
		error = Files.lines(Paths.get("test.txt"))
					.filter(line2 -> line2.startsWith("ERROR"))
					.limit(5)
					.collect(Collectors.toList());
		System.out.println(error);	// [ERROR 1, ERROR 2, ERROR 3, ERROR 4, ERROR 5]
		
		
		System.out.println("\n>> 10.3 자바로 DSL을 만드는 패턴과 기법");
		
		System.out.println("\n>> 10.4 실생활의 자바8 DSL");
		
		
	}

}
