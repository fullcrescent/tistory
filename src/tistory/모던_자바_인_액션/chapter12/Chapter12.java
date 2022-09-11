package tistory.모던_자바_인_액션.chapter12;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.TimeZone;

public class Chapter12 {

	public static void main(String[] args) {
System.out.println("\n>> 12 새로운 날짜와 시간 API");

System.out.println("\n>> 12.1 LocalDate, LocalTime, Instant, Duration, Period 클래스");

/* LocalDate */
LocalDate date;
int year, month, day, len;

date = LocalDate.now();
date = LocalDate.of(2022, 9, 11);
date = LocalDate.parse("2022-09-11");

year = date.getYear();
month = date.getMonthValue();
day = date.getDayOfMonth();
len = date.lengthOfMonth();
Month m = date.getMonth();
DayOfWeek dow = date.getDayOfWeek();
boolean leap = date.isLeapYear();

System.out.println("년 : " + year);			// 년 : 2022
System.out.println("월 : " + month);        	// 월 : 9
System.out.println("일 : " + day);          	// 일 : 11
System.out.println("월의 일수 : " + len);   	// 월의 일수 : 30
System.out.println("월이름 : " + m);        	// 월이름 : SEPTEMBER
System.out.println("요일 : " + dow);        	// 요일 : SUNDAY
System.out.println("윤년 여부 : " + leap);  	// 윤년 여부 : false

/* TemporalField : 시간 관련 객체에서 어떤 필드의 값에 접근할지 정의하는 인터페이스 - 특정 시간을 모델링하는 객체의 값에 대한 조작 정의 */
/* ChronoField : TemporalField 인터페이스를 정의 -> 열거자 요소를 이용해서 원하는 정보 접근 가능 */
year = date.get(ChronoField.YEAR);
month = date.get(ChronoField.MONTH_OF_YEAR);
day = date.get(ChronoField.DAY_OF_MONTH);

System.out.println("년 : " + year);			// 년 : 2022
System.out.println("월 : " + month);        	// 월 : 9
System.out.println("일 : " + day);          	// 일 : 11


/* LocalTime */
LocalTime time; 
int hour, minute, second;

time = LocalTime.of(15, 37, 40);
time = LocalTime.parse("15:37:40");

hour = time.getHour();
minute = time.getMinute();
second = time.getSecond();

System.out.println("시 : " + hour);      // 시 : 15
System.out.println("분 : " + minute);    // 분 : 37
System.out.println("초 : " + second);    // 초 : 40


/* LocalDateTime */
LocalDateTime ldt;
ldt = LocalDateTime.of(date, time);
ldt = LocalDateTime.of(2022, Month.SEPTEMBER, 11, 15, 37, 40);
ldt = LocalDateTime.of(2022, 9, 11, 15, 37, 40);
ldt = date.atTime(15, 37, 40);
ldt = date.atTime(time);
ldt = time.atDate(date);

date = ldt.toLocalDate();
time = ldt.toLocalTime();

System.out.println(ldt);     // 2022-09-11T15:37:40
System.out.println(date);    // 2022-09-11
System.out.println(time);    // 15:37:40


/* Instant */
Instant instant;
instant = Instant.ofEpochSecond(3);
instant = Instant.ofEpochSecond(3, 0);
instant = Instant.ofEpochSecond(2, 1000000000);
instant = Instant.ofEpochSecond(4, -1000000000);

System.out.println(instant);							// 1970-01-01T00:00:03Z
System.out.println(Instant.now());						// 2022-09-11T13:19:11.467248900Z
//System.out.println(instant.get(ChronoField.YEAR));	// java.time.temporal.UnsupportedTemporalTypeException


/* Duration */
Duration duration1, duration2, duration3;
duration1 = Duration.between(LocalTime.of(0, 0, 0), LocalTime.of(15, 57, 30));
duration2 = Duration.between(instant, Instant.now());
duration3 = Duration.of(3, ChronoUnit.MINUTES);

System.out.println(duration1);	// PT15H57M30S
System.out.println(duration2);  // PT461910H59M59.7986166S
System.out.println(duration3);  // PT3M


/* Period */
Period tenDays = Period.ofDays(10);
Period threeWeeks = Period.ofWeeks(3);
Period twoYearsSixMonthOneDay = Period.of(2, 6, 1);

System.out.println(tenDays);                   // P10D
System.out.println(threeWeeks);                // P21D
System.out.println(twoYearsSixMonthOneDay);    // P2Y6M1D



System.out.println("\n>> 12.2 날짜 조정, 파싱, 포매팅");


/* 절대적인 변경 */
date = LocalDate.of(2000, 1, 1);
date = date.withYear(2022);
date = date.with(ChronoField.MONTH_OF_YEAR, 2);
date = date.withDayOfMonth(22);
System.out.println(date);		// 2022-02-22 


/* 상대적인 변경 */
date = date.plusWeeks(1);
date = date.minusYears(2);
date = date.plus(10, ChronoUnit.MONTHS);
System.out.println(date);		// 2021-01-01


/* TemporalAdjuster - 인터페이스 */
/* TemporalAdjusters - 여러 TemporalAdjuster를 반환하는 정적 팩토리 메서드를 포함하는 클래스 */
/* TemporalAdjusters 알아보기 */
LocalDate date1, date2, date3;
date = LocalDate.of(2000, 1, 1);
date1 = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
date2 = date.with(TemporalAdjusters.lastDayOfMonth());	
System.out.println("가장 최근 일요일 : " + date1);				// 가장 최근 일요일 : 2000-01-02
System.out.println("월의 마지막 날 : " + date2);				// 월의 마지막 날 : 2000-01-31

TemporalAdjuster adjustInfo = TemporalAdjusters.ofDateAdjuster(
	t -> {
		DayOfWeek tDow = DayOfWeek.of(t.get(ChronoField.DAY_OF_WEEK));
		int tDayToAdd = 1;
		
		if(tDow==DayOfWeek.FRIDAY) tDayToAdd = 3;
		else if(tDow==DayOfWeek.SATURDAY) tDayToAdd = 2;
		
		return t.plus(tDayToAdd, ChronoUnit.DAYS);
	}
);
date3 = date.with(adjustInfo);
System.out.println("다음 일할 날 : " + date3);					// 다음 일할 날 : 2000-01-03


/* DateTimeFormatter */
date = LocalDate.of(2000, 1, 1);
System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));	// 20000101
System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));	// 2000-01-01

date1 = LocalDate.parse("20000101", DateTimeFormatter.BASIC_ISO_DATE);
date2 = LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_LOCAL_DATE);

DateTimeFormatter formatter;
formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
System.out.println(date.format(formatter));						// 01/01/2000

formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.JAPAN);
System.out.println(date.format(formatter));						// 1. 1月 2000

formatter = new DateTimeFormatterBuilder()
				.appendText(ChronoField.DAY_OF_MONTH)
				.appendLiteral(". ")
				.appendText(ChronoField.MONTH_OF_YEAR)
				.appendLiteral(" ")
				.appendText(ChronoField.YEAR)
				.parseCaseInsensitive()
				.toFormatter(Locale.JAPAN);
System.out.println(date.format(formatter));						// 1. 1月 2000


System.out.println("\n>> 12.3 다양한 시간대와 캘린더 활용 방법");


/* ZoneId */
ZoneId romeZone = ZoneId.of("Europe/Rome");			// {지역}/{도시} 형식으로 이루어짐
System.out.println(romeZone);						// Europe/Rome
ZoneId zoneId = TimeZone.getDefault().toZoneId();
System.out.println(zoneId);							// Asia/Seoul

date = LocalDate.of(2000, 1, 1);
ldt = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
ZonedDateTime zdt1, zdt2, zdt3, zdt4;

zdt1 = date.atStartOfDay(romeZone);
zdt2 = ldt.atZone(romeZone);
zdt3 = Instant.now().atZone(romeZone);
zdt4 = Instant.now().atZone(zoneId);
ldt = LocalDateTime.ofInstant(Instant.now(), romeZone);

System.out.println(zdt1);				// 2000-01-01T00:00+01:00[Europe/Rome]
System.out.println(zdt2);               // 2000-01-01T00:00+01:00[Europe/Rome]
System.out.println(zdt3);               // 2022-09-11T15:19:11.530080200+02:00[Europe/Rome]
System.out.println(zdt4);               // 2022-09-11T22:19:11.531078500+09:00[Asia/Seoul]
System.out.println(ldt);				// 2022-09-11T15:19:11.531078500

/* ZoneOffset */
ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");		// 서머타임을 제대로 처리할 수 없으므로 권장하지는 않음
ldt = LocalDateTime.ofInstant(Instant.now(), zoneId);
OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(ldt, newYorkOffset);
System.out.println(dateTimeInNewYork);					// 2022-09-11T22:19:11.531078500-05:00

/* 캘린더 시스템 - 전세계 통용 : ISO-8601, 자바8에서 추가로 제공 : ThaiBuddhistDate, MinguoDate, JapaneseDate, HijrahDate */
date = LocalDate.of(2000, 1, 1);
JapaneseDate japaneseDate = JapaneseDate.from(date);
System.out.println(date);								// 2000-01-01
System.out.println(japaneseDate);						// Japanese Heisei 12-01-01

Chronology c = Chronology.ofLocale(Locale.JAPAN);		// LocalDate를 권장하고 해당 기능은 권장하지 않음.
ChronoLocalDate now = c.dateNow();
System.out.println(now);								// 2022-09-11

	}

}
