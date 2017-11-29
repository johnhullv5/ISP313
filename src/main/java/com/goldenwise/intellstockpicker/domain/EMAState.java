package com.goldenwise.intellstockpicker.domain;

import org.joda.time.DateTime;

public class EMAState {
	
	public DateTime t;
	
	public double value;
	
	public int n;
	
	public EMAState(DateTime t,double value,int n)
	{
		this.t= t;
		this.value = value;
		this.n = n;
		
	}
	

}
