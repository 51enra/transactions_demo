package com.wcs.java.tx.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupUtil {
	
	public static Connection openConnection() throws SQLException {
		Connection con = DriverManager
				.getConnection("jdbc:mysql://my_stuff_user:my_$tuff_PWD1@localhost:3306/my_stuff?serverTimezone=CET");
		return con;
	}
	
	public static void setup() throws SQLException {
		Connection con = openConnection();
		AccountService accService = new AccountService(con);
		//createTable(con);
		accService.insertNewAccount("david", new BigDecimal("0"));
		accService.insertNewAccount("andre", new BigDecimal("0"));
		accService.insertNewAccount("markus", new BigDecimal("0"));
		con.close();
	}

	public static void tearDown() throws SQLException {
		Connection con = openConnection();
		AccountService accService = new AccountService(con);
		accService.deleteAllAccounts();
		con.close();
	}

	public static void createTable(Connection connection) throws SQLException {
		String createTable = "CREATE TABLE bankaccounts (" + "id INT NOT NULL AUTO_INCREMENT,"
				+ "  user VARCHAR(45) NOT NULL," + "  balance DECIMAL(12,2) NOT NULL DEFAULT 0,"
				+ "  PRIMARY KEY (id))";

		Statement createStatement = connection.createStatement();
		createStatement.execute(createTable);
		createStatement.close();
	}

}
