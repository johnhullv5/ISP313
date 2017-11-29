package com.goldenwise.intellstockpicker.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Equity implements Instrument {
	
	private String instrumentName;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long instrumentID;
	
	public Equity(String instrumentName)
	{
		this.instrumentName = instrumentName;
	}

	@Override
	public long getInstrumentID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getInstrumentName() {
		// TODO Auto-generated method stub
		return null;
	}

}
