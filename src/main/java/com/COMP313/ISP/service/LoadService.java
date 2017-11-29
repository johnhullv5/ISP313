package com.goldenwise.intellstockpicker.service;

import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import com.goldenwise.intellstockpicker.domain.HistoricalData;

import rx.Observable;

public interface LoadService {
	
	 Observable<Pair<DateTime,HistoricalData>> loadOneSymbol(String symbol) throws IOException;
	
	

}
