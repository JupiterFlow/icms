package kr.ac.ync.its.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ItsQueryContainer {
	// QueryCollection queryCol;
	ItsJsonBuilder jsonParser = new ItsJsonBuilder();

	final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://.....?serverTimezone=GMT";
	final String USERNAME = "....";
	final String PASSWORD = "....";

	Connection conn = null;
	Statement stmt = null;

	public ItsQueryContainer() {
		Connect();
	}

	private void Connect() {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String Query(String query, Object... params) throws SQLException {
		// query = SELECT * FROM `member` WHERE id='%s'
		String sql = String.format(query, params);
		ResultSet rs;
		rs = stmt.executeQuery(sql);
		
		while (rs.next()) {
			ResultSetMetaData rmd = rs.getMetaData();
			jsonParser.clearHashMap();
			jsonParser.clearJsonObj();
			for (int i = 1; i <= rmd.getColumnCount(); i++) {
				jsonParser.put(rmd.getColumnName(i),rs.getString(rmd.getColumnName(i)));
			}
			jsonParser.buildObj();
			jsonParser.appendToArr();
		}
		return jsonParser.arrToString();
	}
	public boolean Update(String query, Object... params) throws SQLException {
		String sql = String.format(query, params);
		int rs = stmt.executeUpdate(sql);
		if (rs>0)
			return true;
		else
			return false;
	}
}
