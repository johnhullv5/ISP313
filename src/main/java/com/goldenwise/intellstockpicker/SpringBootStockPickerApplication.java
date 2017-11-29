package com.goldenwise.intellstockpicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldenwise.intellstockpicker.config.InputParameterSet;
import com.goldenwise.intellstockpicker.config.MyConfiguration;
import com.goldenwise.intellstockpicker.db.InsertDB;
import com.goldenwise.intellstockpicker.domain.HistoricalData;

import com.goldenwise.intellstockpicker.service.CrossRule;
import com.goldenwise.intellstockpicker.service.Rule;
import com.goldenwise.intellstockpicker.serviceImpl.CrossDownRule;
import com.goldenwise.intellstockpicker.serviceImpl.CrossUpRule;
import com.goldenwise.intellstockpicker.serviceImpl.LargerThanRule;
import com.goldenwise.intellstockpicker.serviceImpl.LoadCSVService;
import com.goldenwise.intellstockpicker.serviceImpl.MathOperatorService;
import com.goldenwise.intellstockpicker.serviceImpl.RuleImplService;
import com.goldenwise.intellstockpicker.util.Printer;
import com.google.gson.Gson;

import rx.Observable;

@SpringBootApplication
@Import(MyConfiguration.class)
@RestController
public class SpringBootStockPickerApplication {

	@Autowired
	private CrossRule getCrossUPRule;

	@Autowired
	private CrossRule getCrossDownRule;

	@Autowired
	private RuleImplService getRuleImplService;

	@Autowired
	private LoadCSVService getLoadCSVService;

	@Autowired
	private MathOperatorService getMathOperatorService;

	@Autowired
	private InputParameterSet getInputParameterSet;

	@Autowired
	private Map<String, Map<String, Map<String, String>>> initData;

	@Autowired
	private DateTimeFormatter getDateTimeFormatter;

	@Autowired
	private LargerThanRule getLargerThanRule;

	@Autowired
	private List<String> getSymbols;

	@Autowired
	private DateFormat getDateFormat;

	// main computation method for the application.
	@Bean
	InitializingBean computeAndSaveIntoDatabase() {
		return () -> {
			DateFormat df = getDateFormat;
			List<String> symbols = getSymbols;
			InputParameterSet params = getInputParameterSet;
			DateTimeFormatter fmt = getDateTimeFormatter;
			RuleImplService util = getRuleImplService;
			LoadCSVService service = getLoadCSVService;
			MathOperatorService mathService = getMathOperatorService;

			long startTime_ = System.nanoTime();

			for (String symbol : symbols) {
				long startTime = System.nanoTime();
				try {

					CrossRule kDrule = new CrossUpRule();

					CrossRule bollRule = new CrossDownRule();

					LargerThanRule rsiRule = new LargerThanRule();

					Observable<Pair<DateTime, HistoricalData>> o9 = service.loadOneSymbol(symbol);

					Observable<Pair<DateTime, Double>> close = mathService.CLOSE(o9).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> high = mathService.HIGH(o9).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> low = mathService.LOW(o9).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> vol = mathService.VOLUME(o9).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> hoh = mathService.HOH(high, params.getWILL_N())
							.takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> lol = mathService.LOL(low, params.getWILL_N())
							.takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> rsi = mathService.RSI(close, params.getRSI_N())
							.takeLast(params.getTake_N());

					Observable<Pair<DateTime, Triple<Double, Double, Double>>> boll = mathService.BOLL(close,
							params.getBBAND_N(), params.getBBAND_A());

					Observable<Pair<DateTime, Double>> boll_down = boll
							.map(x -> new ImmutablePair(x.getLeft(), x.getRight().getLeft()));

					Observable<Pair<DateTime, Double>> william = mathService
							.WILLIAM(close, hoh, lol, params.getWILL_N()).takeLast(params.getTake_N());

					Observable<Triple<DateTime, Double, Double>> kdj = mathService.KDJ(close, high, low,
							params.getKDJ_N(), params.getKDJ_A());

					LargerThanRule williamRule = new LargerThanRule();

					williamRule.setClose(william);

					williamRule.setLine1(params.getWillLn1());

					williamRule.setLine2(params.getWillLn2());

					rsiRule.setClose(rsi);

					rsiRule.setLine1(params.getRSI_Line1());

					rsiRule.setLine2(params.getRSI_Line2());

					bollRule.setClose(close.skip(params.getBBAND_N() - 1));

					bollRule.setBase(boll_down);

					kDrule.setClose(kdj.map(x -> new ImmutablePair(x.getLeft(), x.getMiddle())));

					kDrule.setBase(kdj.map(x -> new ImmutablePair(x.getLeft(), x.getRight())));

					Observable<Pair<DateTime, Triple<Double, Double, Double>>> macd = MathOperatorService.MACD(close,
							params.getMACDN1(), params.getMACDN2(), params.getMACDS());

					LargerThanRule macdRule = new LargerThanRule();

					macdRule.setClose(macd.map(x -> new ImmutablePair(x.getLeft(), x.getRight().getRight())));

					macdRule.setLine1(params.getMacdLine1());

					Observable<Pair<DateTime, Double>> macdSignal = macdRule.runRule(util);

					List<Pair<DateTime, Double>> macdSignalList = macdSignal.toList().toBlocking().single();

					Observable<Pair<DateTime, Double>> williamSignal = williamRule.run2Rule(util);

					Observable<Pair<DateTime, Double>> bollSignal = bollRule.runRule(util);

					Observable<Pair<DateTime, Double>> kdjSignal = kDrule.runRule(util);

					Observable<Pair<DateTime, Double>> rsiSignal = rsiRule.run2Rule(util);

					List<Pair<DateTime, Double>> rsiSignalList = rsiSignal.toList().toBlocking().single();

					List<Pair<DateTime, Double>> williamList = william.toList().toBlocking().single();

					List<Pair<DateTime, Double>> williamSignalList = williamSignal.toList().toBlocking().single();

					List<Pair<DateTime, Double>> bollSignalList = bollSignal.toList().toBlocking().single();

					List<Pair<DateTime, Double>> kdjSignalList = kdjSignal.toList().toBlocking().single();

					List<Pair<DateTime, Triple<Double, Double, Double>>> macdList = macd.toList().toBlocking().single();

					Map<String, String> williamCrossUpMap2 = new TreeMap<String, String>();
					Map<String, String> bollCrossDownMap2 = new TreeMap<String, String>();

					Map<String, String> rsiSigMap = new TreeMap<String, String>();
					Map<String, String> williamMap = new TreeMap<String, String>();

					Map<String, String> macdMap = new TreeMap<String, String>();
					Map<String, String> macdSigMap = new TreeMap<String, String>();

					Map<String, String> kdjCrossMap = new TreeMap<String, String>();

					for (int i = 0; i < rsiSignalList.size(); i++) {
						Pair<DateTime, Double> pair = rsiSignalList.get(i);
						rsiSigMap.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
					}

					for (int i = 0; i < kdjSignalList.size(); i++) {
						Pair<DateTime, Double> pair = kdjSignalList.get(i);
						kdjCrossMap.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
					}

					for (int i = 0; i < macdSignalList.size(); i++) {
						Pair<DateTime, Double> pair = macdSignalList.get(i);
						macdSigMap.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
					}

					for (int i = 0; i < bollSignalList.size(); i++) {
						Pair<DateTime, Double> pair = bollSignalList.get(i);
						bollCrossDownMap2.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
					}

					for (int i = 0; i < williamSignalList.size(); i++) {
						Pair<DateTime, Double> pair = williamSignalList.get(i);
						williamCrossUpMap2.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
					}

					for (int i = 0; i < williamList.size(); i++) {
						Pair<DateTime, Double> pair = williamList.get(i);
						williamMap.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
					}

					for (int i = 0; i < macdList.size(); i++) {
						Pair<DateTime, Triple<Double, Double, Double>> pair = macdList.get(i);
						macdMap.put(pair.getKey().toString(fmt),
								String.format("%.2f", pair.getValue().getLeft()) + ","
										+ String.format("%.2f", pair.getValue().getMiddle()) + ","
										+ String.format("%.2f", pair.getValue().getRight()));
					}

					Map<String, Map<String, String>> ruleMap = new HashMap<String, Map<String, String>>();

					ruleMap.put("RSISIG", rsiSigMap);
					ruleMap.put("WILLIAM", williamMap);
					ruleMap.put("WILLIAMSIG", williamCrossUpMap2);
					ruleMap.put("BOLLSIG", bollCrossDownMap2);
					ruleMap.put("MACD", macdMap);
					ruleMap.put("MACDSIG", macdSigMap);
					ruleMap.put("KDJSIG", kdjCrossMap);
					initData.put(symbol, ruleMap);

				} catch (Exception e) {
					e.printStackTrace();
				}
				;

				long endtime = System.nanoTime();
				System.out.println(symbol + " run with " + (endtime - startTime));

			}
			long insertStart = System.nanoTime();

			// ****************************************insert into the
			// database**************************************************
			//
			//InsertDB.reorgnizeData(initData, df, params.getDataBase(), params.getDBusername(), params.getDBPasswd());
			// ****************************************insert into the database
			// end**************************************************

			System.out.println(" total insert db with " + (System.nanoTime() - insertStart));
			System.out.println(" total run with " + (System.nanoTime() - startTime_));

		};
	}

	@RequestMapping("/GOOG/CLOSE")
	public String signal() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("CLOSE");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/SMA")
	public String sma() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("SMA");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/EMA")
	public String ema() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("EMA");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/TMA")
	public String tma() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("TMA");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/RSI")
	public String rsi() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("RSI");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/RSISIG")
	public String rsiSig() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("RSISIG");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/ADO")
	public String ado() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("ADO");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/WILLIAM")
	public String william() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("WILLIAM");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/MSG/WILLIAMSIG")
	public String williamsig() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("MSG").get("WILLIAMSIG");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/BOLL")
	public String boll() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("BOLL");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/BOLLSIG")
	public String bollSig() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("BOLLSIG");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/MSG/MACD")
	public String macd() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("MACD");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/MSG/MACDSIG")
	public String macdSig() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("MSG").get("MACDSIG");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/KDJ")
	public String kdj() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("KDJ");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/KDJSIG")
	public String kdjSig() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("KDJSIG");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/UP")
	public String up() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("CloseCrossUpSMA");
		String json = gson.toJson(data);
		return json;
	}

	@RequestMapping("/GOOG/UP2")
	public String up2() {
		Gson gson = new Gson();
		Map<String, String> data = initData.get("GOOG").get("CloseCrossUpSMA2");
		String json = gson.toJson(data);
		return json;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStockPickerApplication.class, args).close();
		System.out.println("work done.");

	}
}