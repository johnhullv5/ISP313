package com.goldenwise.intellstockpicker.util;

import java.text.DecimalFormat;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class Printer {
	
	private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	public static void printTS(Pair<DateTime,Double> ts)
	{
		 System.out.println(Printer.fmt.print( ts.getLeft())+" , "+  String.format( "%.2f", ts.getRight() ) );
		
	}
	
	
	
	public static void printTS6(Pair<DateTime,Double> ts)
	{
		 System.out.println(Printer.fmt.print( ts.getLeft())+" , "+  String.format( "%.6f", ts.getRight() ) );

		
	}
	
	public static void printTriple(Triple<DateTime,Double,Double> ts)
	{
		 System.out.println(Printer.fmt.print( ts.getLeft())+" , "+  String.format( "%.2f", ts.getMiddle() )+ " , "+  String.format( "%.2f", ts.getRight() ));

	}

}
