package com.wcs.java.tx.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) throws Exception {

		Class.forName("com.mysql.jdbc.Driver");
		
		// 3 konten angelegt
				
		//	user:password@server:port/database

		// 3 Konten anlegen
//		SetupUtil.setup();
		
		Connection con = DriverManager
				.getConnection("jdbc:mysql://my_stuff_user:my_$tuff_PWD1@localhost:3306/my_stuff?serverTimezone=CET");
		
		SetupUtil.setup();
			
		// Database Settings
		// spring.datasource.url=jdbc:mysql://localhost:3306/my_stuff?serverTimezone=CET
		// spring.datasource.username=my_stuff_user
		// spring.datasource.password=my_$tuff_PWD1

		try {
		con.setAutoCommit(false);
//		Statement st = con.createStatement();
//		st.execute("UPDATE bankaccounts SET balance=1000 WHERE user='david'");
//		st.execute("UPDATE bankaccounts SET balance=2000 WHERE user='andre'");
		AccountService accountService = new AccountService(con);
		TransferService transferService = new TransferService(accountService, con);
		transferService.transferMoney("andre", "david", new BigDecimal(500));
		con.commit();

		} catch(Exception ex) {
			con.rollback();
		}finally {
			con.close();
		}

	}

}
