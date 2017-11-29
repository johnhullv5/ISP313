package com.goldenwise.intellstockpicker;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import com.goldenwise.intellstockpicker.domain.HistoricalData;
import com.goldenwise.intellstockpicker.service.CrossRule;
import com.goldenwise.intellstockpicker.serviceImpl.CrossDownRule;
import com.goldenwise.intellstockpicker.serviceImpl.LargerThanRule;
import com.goldenwise.intellstockpicker.serviceImpl.LoadCSVService;
import com.goldenwise.intellstockpicker.serviceImpl.MathOperatorService;
import com.goldenwise.intellstockpicker.serviceImpl.RuleImplService;
import com.goldenwise.intellstockpicker.util.Printer;

import rx.Observable;

public class TestIndicator {
	
	
	@Test
	public void testMACD() throws IOException {
		
		LoadCSVService service = new LoadCSVService();
		RuleImplService util = new RuleImplService();
		
		service.setDataPath("./data/histData/","./data/funData/");
		
		Observable<Pair<DateTime, HistoricalData>> o = service.loadOneSymbol("ABAX");
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		int N = 10;
		int dataN = 1000;
		int williamPara = 10;
		
		Observable<Pair<DateTime,Double>> close = MathOperatorService.CLOSE(o).takeLast(dataN);
		Observable<Pair<DateTime, Triple<Double, Double, Double>>> macd = MathOperatorService.MACD(close,
				12, 26, 9);
		
		//macd.subscribe(System.out::println);
		
		LargerThanRule macdRule = new LargerThanRule();

		macdRule.setClose(macd.map(x -> new ImmutablePair(x.getLeft(), x.getRight().getRight())));

		macdRule.setLine1(0);

		Observable<Pair<DateTime, Double>> macdSignal = macdRule.runRule(util);

		List<Pair<DateTime, Double>> macdSignalList = macdSignal.toList().toBlocking().single();
		
		macdSignalList.forEach(t->{
			System.out.println(t.getKey()+"  :  "+t.getValue());
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		System.out.println("**************William********************");
//		Observable<Pair<DateTime,Double>> close = MathOperatorService.CLOSE(o).takeLast(dataN);
		Observable<Pair<DateTime,Double>> high = MathOperatorService.HIGH(o).takeLast(dataN);
		Observable<Pair<DateTime,Double>> low = MathOperatorService.LOW(o).takeLast(dataN);
//		
//		Observable<Pair<DateTime,Double>> std = MathOperatorService.STD(close, N);
//		
		Observable<Pair<DateTime, Double>> hoh = MathOperatorService.HOH(high, williamPara)
				.takeLast(dataN);

		Observable<Pair<DateTime, Double>> lol = MathOperatorService.LOL(low, williamPara)
				.takeLast(dataN);
		
//		
//		
		Observable<Pair<DateTime, Double>> william = MathOperatorService
				.WILLIAM(close, hoh, lol, williamPara).takeLast(dataN);
//		
		william.subscribe(Printer::printTS);
		
		LargerThanRule williamRule = new LargerThanRule();
		
		williamRule.setClose(william);

		williamRule.setLine1(-80);

		williamRule.setLine2(-20);
		
		williamRule.run2Rule(util).subscribe(Printer::printTS);
		
		Observable<Pair<DateTime, Double>> williamSignal = williamRule.run2Rule(util);
		
		williamSignal.subscribe(Printer::printTS);
//		
//		List<Pair<DateTime, Double>> williamSignalList = williamSignal.toList().toBlocking().single();
//		
//		
//		williamSignalList.forEach(x->{System.out.println(x.getKey()+"   :  "+x.getValue());});
//		
//		
//		Map<String, String> williamCrossUpMap2 = new TreeMap<String, String>();
//		
//		System.out.println("williamSignalList.size():   "+williamSignalList.size());
//		
//		for (int i = 0; i < williamSignalList.size(); i++) {
//			Pair<DateTime, Double> pair = williamSignalList.get(i);
//			
//			System.out.println(pair.getKey()+"  :  "+pair.getValue());
//			williamCrossUpMap2.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
//		}
//		
		
		//std.subscribe(Printer::printTS);
		
//		CrossRule crossDownRule = new CrossDownRule();
//		
//		crossDownRule.setClose(close.skip(N-1));
//		
//		Observable<Pair<DateTime,Triple<Double,Double,Double>>> boll = MathOperatorService.BOLL(close, N, 1.5);
//		
//		Observable<Pair<DateTime,Double>> boll_down = boll.map(x->new ImmutablePair(x.getLeft(),x.getRight().getLeft()));
//		
//		crossDownRule.setBase(boll_down);
//		
//		crossDownRule.runRule(util).subscribe(Printer::printTS);
//		
//		
//		
//		System.out.println("**************KDJ********************");
//		Observable<Triple<DateTime,Double,Double>> kdj = MathOperatorService.KDJ(close, high, low, 10, 3);
//		
//		
//		
//		
//		System.out.println("**************MACD********************");
//		Observable<Pair<DateTime,Triple<Double,Double,Double>>> macd = MathOperatorService.MACD(close, 12, 26,9);
//		
////		macd.subscribe(System.out::println);
////		System.out.println("**************MACD********************");
////		macd.subscribe(System.out::println);
////		macd
//		
//		LargerThanRule macdRule = new LargerThanRule();
//		
//		macdRule.setClose(macd.map(x->new ImmutablePair(x.getLeft(),x.getRight().getRight())));
//		
//		macdRule.setLine1(0);
//		
//		Observable<Pair<DateTime,Double>> macdSignal = macdRule.runRule(util);
//		
//		macdSignal.subscribe(Printer::printTS);
//		
//		List<Pair<DateTime, Double>> macdSignalList = macdRule.runRule(util).toList().toBlocking().single();
//		
//		//System.out.println( macdRule.runRule(util).toList().toBlocking().single().size());
//		
//		for (int i = 0; i < macdSignalList.size(); i++) {
//			System.out.println(macdSignalList.get(i).toString());
//		}
		
		
		
		
		
//		boll_down.subscribe(Printer::printTS);
//		close.subscribe(System.out::println);
//		System.out.println("**************HIGH********************");
////		Observable<Pair<DateTime,Double>> sma = MathOperatorService.SMA(close, 10);
//		high.take(20).subscribe(Printer::printTS6);
//		
//		
//		System.out.println("**************KDJ********************");
//		Observable<Triple<DateTime,Double,Double>> kdj = MathOperatorService.KDJ(close,high,low, 10);
////		System.out.println("**************EMA12 again!!!!!!!********************");
////		ema.subscribe(System.out::println);
////		
////		System.out.println("**************EMA12 again agian!!!!!!!********************");
////		ema.subscribe(Printer::printTS);
////		System.out.println("**************EMA26*********************");
////		Observable<Pair<DateTime,Double>> ema2 = MathOperatorService.EMA(close, 26);
////		ema2.subscribe(Printer::printTS);
//		System.out.println("**************MACD********************");
//		//Observable<Pair<DateTime,Double>> macd = MathOperatorService.MACD(close, 12, 26);
//		//macd.subscribe(Printer::printTS);
//		System.out.println("**************VMA********************");
//		Observable<Pair<DateTime,Double>> vma = MathOperatorService.VMA(close, 12, 0.5);
//		vma.subscribe(Printer::printTS);
//		System.out.println("**************moving avg and avg********************");
//		
//		Observable<Double> close_v = Observable.just(1.0,2.0,1.5,18.0,9.0);
//		close_v.subscribe(System.out::println);
//		System.out.println("**************");
//		Observable<Double> ma = MathOperatorService.movingAverage(close_v, 2);
//		ma.subscribe(System.out::println);
//		System.out.println("**************");
//		Observable<Double> avg = MathOperatorService.avg(close_v, 2);
//		avg.subscribe(System.out::println);
//		System.out.println("**************");
//		Observable<Double> delta = Observable.zip(MathOperatorService.movingAverage(close_v, 2), MathOperatorService.avg(close_v, 2), (a,b)->a-b);
//		delta.subscribe(System.out::println);
//		
//		System.out.println("**************STD********************");
//		MathOperatorService.STD(close, 12).subscribe(Printer::printTS);
//		
//		System.out.println("**************DIFF SOURCE********************");
//		close.take(5).subscribe(Printer::printTS);
//		System.out.println("**************DIFF result********************");
//		MathOperatorService.DIFF(close.take(5)).subscribe(Printer::printTS);
//		
//		
//		
//		
//		
//		System.out.println("**************SHIFT SOURCE********************");
//		close.take(5).subscribe(Printer::printTS);
//		System.out.println("**************SHIFT result********************");
//		MathOperatorService.SHIFT(close.take(5)).subscribe(Printer::printTriple);
//		
//		System.out.println("**************TR********************");
//		MathOperatorService.TR(high,low,close).subscribe(Printer::printTS);
//		
//		System.out.println("**************DMI********************");
//		MathOperatorService.DMI(high.take(40),low.take(40),close.take(40),14).subscribe(Printer::printTS);
//		
//		System.out.println("**************MACD********************");
//		MathOperatorService.MACD(close,12,26,9).subscribe(System.out::println);
//		
////		System.out.println("**************EMA12********************");
////		MathOperatorService.EMA(close,12).subscribe(System.out::println);
//		
//		System.out.println("**************RSI SOURCE********************");
//		close.take(20).subscribe(Printer::printTS);
//		System.out.println("**************RSI result********************");
//		MathOperatorService.RSI(close.take(20),14).subscribe(Printer::printTS);
//		
//		System.out.println("**************KDJ result********************");
//		MathOperatorService.KDJ(close.take(50),high.take(50),low.take(50),14).subscribe(System.out::println);
		
		
//		System.out.println("*************Fundamental test******************");
//		service.loadOneSymbolFun("AAPL").subscribe(System.out::println);
		
		//service.lo
////		
	}
}
