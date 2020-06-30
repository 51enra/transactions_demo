package com.wcs.java.tx.jdbc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransactionTest {

	Connection con;

	@Before
	public void setup() throws SQLException {
		SetupUtil.tearDown();
		SetupUtil.setup();
		this.con = SetupUtil.openConnection();
	}

	@After
	public void tearDown() throws SQLException {
		if (this.con != null && !this.con.isClosed()) {
			this.con.close();
		}
	}

	@Test
	public void testWithdraw() throws Exception {
		// 2 accounts with 1000 EUR
		AccountService accService = new AccountService(con);

		accService.deposit("david", new BigDecimal("1000"));

		BigDecimal balance = accService.getBalanceOfUser("david");
		// overdraw
		try {
			accService.withdraw("david", balance);
			assertTrue("Balance should be 0", new BigDecimal(0).compareTo(accService.getBalanceOfUser("david")) == 0);
		} catch (InsufficientFundsException e) {
			fail("Should not be able to withdraw his account");
		}

	}
	
	
	@Test
	public void testWithdrawTooMuch() throws Exception {
		AccountService accService = new AccountService(con);

		accService.deposit("david", new BigDecimal("1000"));

		BigDecimal balance = accService.getBalanceOfUser("david");
		// overdraw
		try {
			accService.withdraw("david", balance.add(new BigDecimal(1)));
			fail("Should not be able to overdraw his account");
		} catch (InsufficientFundsException e) {
			System.out.println("Correct exception:" + e);
		}
	}

	@Test
	public void testTransferGood() throws Exception {

		AccountService accService = new AccountService(con);
		TransferService transferService = new TransferService(accService, con);//TODO

		accService.deposit("david", new BigDecimal("1000"));
		accService.deposit("andre", new BigDecimal("1000"));

		transferService.transferMoney("david", "andre", new BigDecimal(1000));
		BigDecimal d = accService.getBalanceOfUser("david");
		assertTrue("Balance should be 0", new BigDecimal("0").compareTo(accService.getBalanceOfUser("david")) == 0);
		assertTrue("Balance should be 2000", new BigDecimal("2000").compareTo(accService.getBalanceOfUser("andre")) == 0);
		// TODO assert that davids balance is 0
		// TODO assert that andres balance is 2000
		//fail("TODO: implement checks");
	}

	@Test
	public void testTransferBad() throws Exception {

		AccountService accService = new AccountService(con);
		TransferService transferService = new TransferService(accService, con);//TODO

		accService.deposit("david", new BigDecimal("1000"));
		accService.deposit("andre", new BigDecimal("1000"));

		try {
			transferService.transferMoney("david", "andre", new BigDecimal(2000));
			fail("should not be able to transfer");
		} catch (InsufficientFundsException e) {
			System.out.println("Correct should not overdraw");
			assertTrue("Balance should be 1000", new BigDecimal("1000").compareTo(accService.getBalanceOfUser("david")) == 0);
			assertTrue("Balance should be 1000", new BigDecimal("1000").compareTo(accService.getBalanceOfUser("andre")) == 0);

			//fail("implement check if the balances are still at 1000");
		}
	}
}
