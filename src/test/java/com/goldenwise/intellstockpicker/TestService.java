package com.goldenwise.intellstockpicker;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.junit.Test;

import com.goldenwise.intellstockpicker.domain.HistoricalData;
import com.goldenwise.intellstockpicker.service.LoadService;
import com.goldenwise.intellstockpicker.serviceImpl.IndicatorService;
//import com.goldenwise.intellstockpicker.serviceImpl.LoadService;
import com.goldenwise.intellstockpicker.serviceImpl.LoadCSVService;
import com.goldenwise.intellstockpicker.serviceImpl.MathOperatorService;
import com.goldenwise.intellstockpicker.util.Printer;

import eu.verdelhan.ta4j.TimeSeries;
import rx.Observable;

public class TestService {

	@Test
	public void test() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Date dateobj = new Date();
//		System.out.println(df.format(dateobj));
		
		Date dateobj = new Date();
		System.out.println(df.format(dateobj));
		
		
		Date today = df.parse(df.format(dateobj));
//		LoadService service = new LoadCSVService();
//		service.loadOnePriceTimeseries("GOOG");
		//assertEquals(service.LoadInstruments().size(),1);
	}
	
	
	@Test
	public void testIndicatorService() throws IOException
	{
//		LoadService service = new LoadCSVService();
		
//		TimeSeries ts = service.loadOnePriceTimeseries("GOOG");
//		IndicatorService.makeClosePriceIndicator(ts);
		
//		LoadCSVService service = new LoadCSVService();
//		
//		service.setDataPath("./data/histData/");
//		
//		Observable<Pair<DateTime, HistoricalData>> o = service.loadOneSymbol("T");
//		
//		o.map(p->p.getRight().getClosePrice()).subscribe(System.out::println);
	}
	
//	@Test
//	public void testMath() throws IOException
//	{
//		List<Pair<DateTime,Double>> l = new ArrayList<Pair<DateTime,Double>>();
//		DateTime today = DateTime.now();
//		Pair<DateTime,Double> p1 = new ImmutablePair(today,10.0);
//		DateTime yesterday  = today.minusDays(1);
//		Pair<DateTime,Double> p2 = new ImmutablePair(yesterday,8.5);
//		DateTime today_2  = today.minusDays(2);
//		Pair<DateTime,Double> p3 = new ImmutablePair(today_2,9.0);
//		DateTime today_3  = today.minusDays(3);
//		Pair<DateTime,Double> p4 = new ImmutablePair(today_3,9.2);
//		l.add(p4);
//		l.add(p3);
//		l.add(p2);
//		l.add(p1);
//		Observable<Pair<DateTime,Double>> o = Observable.from(l);
//		Observable<Double> o2 = o.map(t->t.getRight());
//		Observable<DateTime> time = o.map(t->t.getLeft());
//		//time.skip(2).subscribe(System.out::println);
//		o.subscribe(System.out::println);
//		System.out.println("*****************************");
//		Observable.zip(time.skip(2), MathOperatorService.movingAverage(o2, 3).skipLast(2),(t,p)->new ImmutablePair(t,p)).subscribe(System.out::println);
//		//MathOperatorService.SMA(o, 3).subscribe(System.out::println);
//		System.out.println("*************SMA*************");
//		MathOperatorService.SMA(o, 2).subscribe(System.out::println);
//		System.out.println("*************HOH*************");
//		MathOperatorService.HOH(o, 2).subscribe(System.out::println);
//		System.out.println("*************LOL*************");
//		MathOperatorService.LOL(o, 2).subscribe(System.out::println);
//		//MathOperatorService.SMA_t(o, 2).subscribe(System.out::println);
//		
//		LoadCSVService service = new LoadCSVService();
//		
//		service.setDataPath("./data/histData/","./data/funData/");
//		
//		Observable<Pair<DateTime, HistoricalData>> o9 = service.loadOneSymbol("T");
//		
//		
//		System.out.println("*************Real WILLIAM*************");
//		//o10.subscribe(System.out::println);
//		int N = 14;
//		Observable<Pair<DateTime,Double>> close = MathOperatorService.CLOSE(o9);
//		Observable<Pair<DateTime,Double>> high = MathOperatorService.HIGH(o9);
//		Observable<Pair<DateTime,Double>> low = MathOperatorService.LOW(o9);
//		Observable<Pair<DateTime,Double>> hoh = MathOperatorService.HOH(high, N);
//		Observable<Pair<DateTime,Double>> lol = MathOperatorService.LOL(low, N);
//		
//		System.out.println("*************Test *************");
//		MathOperatorService.WILLIAM(close, hoh, lol, N).subscribe(Printer::printTS);
//		//MathOperatorService.WILLIAM(close, hoh, lol, N).subscribe(System.out::println);
//		System.out.println("*************Test  end*************");
////		MathOperatorService.SMA(close, 14).subscribe(System.out::println);
////		System.out.println("*************Real HOH*************");
////		MathOperatorService.HOH(o10, 14).subscribe(System.out::println);
////		System.out.println("*************Real LOL*************");
////		MathOperatorService.LOL(o10, 14).subscribe(System.out::println);
//		
//	}

}
