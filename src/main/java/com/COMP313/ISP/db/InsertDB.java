package com.goldenwise.intellstockpicker.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class InsertDB {
	private java.sql.Connection conn = null;

	public static void reorgnizeData(Map<String, Map<String, Map<String, String>>> data, DateFormat formatter,
			String database, String userName, String passwd) {

		try {
			final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
			String myUrl = "jdbc:mariadb://localhost:3306/" + database;
			Class.forName("org.mariadb.jdbc.Driver");

			java.sql.Connection conn = DriverManager.getConnection(myUrl, userName, passwd);

			try {
				String query = "delete from BROKER_SIGNALS";
				PreparedStatement preparedStmt1 = conn.prepareStatement(query);
				preparedStmt1.execute();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// execute the preparedstatement

			data.forEach((k, v) -> {

				System.out.println(k + " process...");

				Map<String, String> rsiSigMap = v.get("RSISIG");

				Set<String> rsiDates = rsiSigMap.keySet();

				Map<String, String> bollSigsMap = v.get("BOLLSIG");

				Set<String> bollDates = bollSigsMap.keySet();

				Map<String, String> williamSIG = v.get("WILLIAMSIG");

				Set<String> williamDates = williamSIG.keySet();

				Map<String, String> kdjSigMap = v.get("KDJSIG");

				Set<String> kdjDates = kdjSigMap.keySet();

				Map<String, String> macdSigMap = v.get("MACDSIG");

				Set<String> macdDates = macdSigMap.keySet();

				Set<String> totalSets = new TreeSet<String>(rsiDates);

				totalSets.addAll(bollDates);

				totalSets.addAll(williamDates);

				totalSets.addAll(kdjDates);

				totalSets.addAll(macdDates);

				LocalDate todayLocalDate = LocalDate.now();

				java.sql.Date sqlDate = java.sql.Date.valueOf(todayLocalDate);

				String query1 = " insert into BROKER_SIGNALS(UPDATED_DATE, SYMBOL,ON_DATE,SIGNAL1,SIGNAL2,SIGNAL3,SIGNAL4,SIGNAL5)"
						+ " values (?, ?,  ?, ?,?,?,  ?, ?)";

				totalSets.forEach(t -> {
					try {
						PreparedStatement preparedStmt = conn.prepareStatement(query1);

						preparedStmt.setDate(1, sqlDate);
						preparedStmt.setString(2, k);
						preparedStmt.setDate(3, new java.sql.Date(formatter.parse(t).getTime()));

						if (rsiSigMap.containsKey(t)) {
							preparedStmt.setDouble(4, Double.valueOf(rsiSigMap.get(t)));
						} else {
							preparedStmt.setNull(4, java.sql.Types.DOUBLE);
						}

						if (bollSigsMap.containsKey(t)) {
							preparedStmt.setDouble(5, Double.valueOf(bollSigsMap.get(t)));
						} else {
							preparedStmt.setNull(5, java.sql.Types.DOUBLE);
						}

						if (williamSIG.containsKey(t)) {
							preparedStmt.setDouble(6, Double.valueOf(williamSIG.get(t)));
						} else {
							preparedStmt.setNull(6, java.sql.Types.DOUBLE);
						}

						if (kdjSigMap.containsKey(t)) {
							preparedStmt.setDouble(7, Double.valueOf(kdjSigMap.get(t)));
						} else {
							preparedStmt.setNull(7, java.sql.Types.DOUBLE);
						}

						if (macdSigMap.containsKey(t)) {
							preparedStmt.setDouble(8, Double.valueOf(macdSigMap.get(t)));
						} else {
							preparedStmt.setNull(8, java.sql.Types.DOUBLE);
						}

						preparedStmt.execute();
					} catch (SQLException | ParseException e) {
						
						e.printStackTrace();
					}

				});

			});
			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
		}

		// return data;

	}

	public static void insetResults(Map<String, Map<String, Map<String, String>>> data, DateFormat formatter)
			throws ClassNotFoundException, SQLException {
		try {
			final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
			String myUrl = "jdbc:mariadb://localhost:3306/whale";
			Class.forName("org.mariadb.jdbc.Driver");

			java.sql.Connection conn = DriverManager.getConnection(myUrl, "whale", "whale");



			LocalDate todayLocalDate = LocalDate.now();

			java.sql.Date sqlDate = java.sql.Date.valueOf(todayLocalDate);

	

			data.forEach((k, v) -> {

				System.out.println("insert : " + k);

				Map<String, String> rsiSigMap = v.get("RSISIG");

				String query1 = " insert into BROKER_SIGNALS_NEW (UPDATED_DATE, SYMBOL,ON_DATE,SIGNAL1)"
						+ " values (?, ?,  ?, ?)";
				PreparedStatement preparedStmt;
				try {
					preparedStmt = conn.prepareStatement(query1);
					rsiSigMap.forEach((k1, v1) -> {

						try {
							preparedStmt.setDate(1, sqlDate);
							preparedStmt.setString(2, k);

							preparedStmt.setDate(3, new java.sql.Date(formatter.parse(k1).getTime()));
							preparedStmt.setDouble(4, Double.valueOf(v1));
							preparedStmt.execute();
						} catch (SQLException e) {
							
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					});
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				;

				Map<String, String> bollSigsMap = v.get("BOLLSIG");

				String query2 = " insert into BROKER_SIGNALS_NEW (UPDATED_DATE, SYMBOL,ON_DATE,SIGNAL2)"
						+ " values (?, ?,  ?, ?)";
				PreparedStatement preparedStmt2;
				try {
					preparedStmt2 = conn.prepareStatement(query2);
					bollSigsMap.forEach((k1, v1) -> {
						try {
							preparedStmt2.setDate(1, sqlDate);
							preparedStmt2.setString(2, k);

							preparedStmt2.setDate(3, new java.sql.Date(formatter.parse(k1).getTime()));
							preparedStmt2.setDouble(4, Double.valueOf(v1));
							preparedStmt2.execute();
						} catch (SQLException e) {
							
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}

					});
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}

				Map<String, String> williamSIG = v.get("WILLIAMSIG");
				String query3 = " insert into BROKER_SIGNALS_NEW (UPDATED_DATE, SYMBOL,ON_DATE,SIGNAL3)"
						+ " values (?, ?,  ?, ?)";
				PreparedStatement preparedStmt3;
				try {
					preparedStmt3 = conn.prepareStatement(query3);
					williamSIG.forEach((k1, v1) -> {
						try {
							preparedStmt3.setDate(1, sqlDate);
							preparedStmt3.setString(2, k);

							preparedStmt3.setDate(3, new java.sql.Date(formatter.parse(k1).getTime()));
							preparedStmt3.setDouble(4, Double.valueOf(v1));
							preparedStmt3.execute();
						} catch (SQLException e) {
							
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}

					});
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}

				Map<String, String> kdjSigMap = v.get("KDJSIG");
				String query4 = " insert into BROKER_SIGNALS_NEW (UPDATED_DATE, SYMBOL,ON_DATE,SIGNAL4)"
						+ " values (?, ?,  ?, ?)";
				PreparedStatement preparedStmt4;
				try {
					preparedStmt4 = conn.prepareStatement(query4);
					kdjSigMap.forEach((k1, v1) -> {
						try {
							preparedStmt4.setDate(1, sqlDate);
							preparedStmt4.setString(2, k);

							preparedStmt4.setDate(3, new java.sql.Date(formatter.parse(k1).getTime()));
							preparedStmt4.setDouble(4, Double.valueOf(v1));
							preparedStmt4.execute();
						} catch (SQLException e) {
							
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}

					});
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}

				Map<String, String> macdSigMap = v.get("MACDSIG");
				String query5 = " insert into BROKER_SIGNALS_NEW (UPDATED_DATE, SYMBOL,ON_DATE,SIGNAL5)"
						+ " values (?, ?,  ?, ?)";
				PreparedStatement preparedStmt5;
				try {
					preparedStmt5 = conn.prepareStatement(query5);
					macdSigMap.forEach((k1, v1) -> {
						try {
							preparedStmt5.setDate(1, sqlDate);
							preparedStmt5.setString(2, k);

							preparedStmt5.setDate(3, new java.sql.Date(formatter.parse(k1).getTime()));
							preparedStmt5.setDouble(4, Double.valueOf(v1));
							preparedStmt5.execute();
						} catch (SQLException e) {
							
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}

					});
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}

			});
			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
		}

	}


}
