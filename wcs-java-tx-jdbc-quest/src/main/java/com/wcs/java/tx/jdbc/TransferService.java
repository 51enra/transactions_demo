package com.wcs.java.tx.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class TransferService {

	private AccountService accountService;
	private Connection con;

	TransferService(AccountService accountService, Connection con) {
		this.accountService = accountService;
		this.con = con;
	}

	public void transferMoney(String userFrom, String userTo, BigDecimal amount)
			throws SQLException, InsufficientFundsException {
		try {
			con.setAutoCommit(false);
			accountService.withdraw(userFrom, amount);
			accountService.deposit(userTo, amount);
			con.commit();
		} catch (InsufficientFundsException ex) {
			con.rollback();
			throw ex;
		}
//		throw new IllegalStateException("Not implemented");
	}

}
