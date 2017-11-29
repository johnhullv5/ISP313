package com.goldenwise.intellstockpicker.serviceImpl;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import com.goldenwise.intellstockpicker.service.CrossRule;
import com.goldenwise.intellstockpicker.service.Rule;

import rx.Observable;

public class CrossUpRule implements CrossRule {
	
	private Observable<Pair<DateTime, Double>> close;
	
	private Observable<Pair<DateTime, Double>> base;
	
	
	

	@Override
	public Observable<Pair<DateTime, Double>> runRule(RuleImplService ruleUtils) {
		// TODO Auto-generated method stub
		return ruleUtils.crossUP2(this.close, this.base);
	}
	
	
	@Override
	public Observable<Pair<DateTime, Double>> runRule2(RuleImplService ruleUtils) {
		// TODO Auto-generated method stub
		return ruleUtils.crossUP2(this.close, this.base);
	}

	@Override
	public void setClose(Observable<Pair<DateTime, Double>> close) {
		this.close = close;
		
	}

	@Override
	public void setBase(Observable<Pair<DateTime, Double>> base) {
		this.base = base;
		
	}


	@Override
	public Rule andRule(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

}
