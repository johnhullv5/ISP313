package com.goldenwise.intellstockpicker.domain;

import org.joda.time.DateTime;

public class FunData {
	
	private String symbol;
	
	private DateTime date;
	
	private double TrillingPE = Double.NaN;
	private double PB= Double.NaN;
	private double EV= Double.NaN;
	private double PriceToSales= Double.NaN;
	
	private double revenueGrowth= Double.NaN;
	
	private double earningsQuarterlyGrowth= Double.NaN;
	private double netIncomeToCommon= Double.NaN;
	private double operatingCashflow= Double.NaN;
	private double returnOnEquity= Double.NaN;
	private double yield= Double.NaN;
	
	private double surprise =  Double.NaN;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public double getTrillingPE() {
		return TrillingPE;
	}

	public void setTrillingPE(double trillingPE) {
		TrillingPE = trillingPE;
	}

	public double getPB() {
		return PB;
	}

	public void setPB(double pB) {
		PB = pB;
	}

	public double getEV() {
		return EV;
	}

	public void setEV(double eV) {
		EV = eV;
	}

	public double getPriceToSales() {
		return PriceToSales;
	}

	public void setPriceToSales(double priceToSales) {
		PriceToSales = priceToSales;
	}

	public double getRevenueGrowth() {
		return revenueGrowth;
	}

	public void setRevenueGrowth(double revenueGrowth) {
		this.revenueGrowth = revenueGrowth;
	}

	public double getEarningsQuarterlyGrowth() {
		return earningsQuarterlyGrowth;
	}

	public void setEarningsQuarterlyGrowth(double earningsQuarterlyGrowth) {
		this.earningsQuarterlyGrowth = earningsQuarterlyGrowth;
	}

	public double getNetIncomeToCommon() {
		return netIncomeToCommon;
	}

	public void setNetIncomeToCommon(double netIncomeToCommon) {
		this.netIncomeToCommon = netIncomeToCommon;
	}

	public double getOperatingCashflow() {
		return operatingCashflow;
	}

	public void setOperatingCashflow(double operatingCashflow) {
		this.operatingCashflow = operatingCashflow;
	}

	public double getReturnOnEquity() {
		return returnOnEquity;
	}

	public void setReturnOnEquity(double returnOnEquity) {
		this.returnOnEquity = returnOnEquity;
	}

	public double getYield() {
		return yield;
	}

	public void setYield(double yield) {
		this.yield = yield;
	}

	public double getSurprise() {
		return surprise;
	}

	public void setSurprise(double surprise) {
		this.surprise = surprise;
	}
	
	public boolean isValid()
	{
		
//		private double TrillingPE = Double.NaN;
//		private double PB= Double.NaN;
//		private double EV= Double.NaN;
//		private double PriceToSales= Double.NaN;
//		
//		private double revenueGrowth= Double.NaN;
//		
//		private double earningsQuarterlyGrowth= Double.NaN;
//		private double netIncomeToCommon= Double.NaN;
//		private double operatingCashflow= Double.NaN;
//		private double returnOnEquity= Double.NaN;
//		private double yield= Double.NaN;
		double testResult = TrillingPE+PB+EV+PriceToSales+revenueGrowth+earningsQuarterlyGrowth+netIncomeToCommon+operatingCashflow+returnOnEquity+yield;
		
		if (testResult==Double.NaN)
			return false;
		else
			return true;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
