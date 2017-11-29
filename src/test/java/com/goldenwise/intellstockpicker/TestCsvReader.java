package com.goldenwise.intellstockpicker;

import org.junit.Test;

import com.goldenwise.intellstockpicker.serviceImpl.CSVReaderRunner;

public class TestCsvReader {
	
	@Test
	public void test()
	{
		CSVReaderRunner runner = new CSVReaderRunner();
		runner.loadSymbols("data/");
		//runner.readCsvForOneSymbol("/home/hjiang/git/stock/data/", "GOOG");
	}

}
