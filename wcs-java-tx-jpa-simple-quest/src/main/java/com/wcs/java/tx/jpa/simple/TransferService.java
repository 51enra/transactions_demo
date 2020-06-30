package com.wcs.java.tx.jpa.simple;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


public class TransferService {

	private EntityManager em;
	
	public TransferService(EntityManager em) {
		super();
		this.em = em;
	}

	public List<BankAccount> transferMoney(String userFrom, String userTo, BigDecimal amount)
			throws InsufficientFundsException {
		
		EntityTransaction tx = em.getTransaction();
		AccountService accountService = new AccountService(em);
			try {
				tx.begin();
				BankAccount fromAccount = accountService.withdraw(userFrom, amount);
				BankAccount toAccount = accountService.deposit(userTo, amount);
				tx.commit();
				return Arrays.asList(fromAccount, toAccount);
			} catch (InsufficientFundsException ex) {
				tx.setRollbackOnly();
				throw ex;
			}
			
//			throw new IllegalStateException("Not implemented");

	}
	

}
