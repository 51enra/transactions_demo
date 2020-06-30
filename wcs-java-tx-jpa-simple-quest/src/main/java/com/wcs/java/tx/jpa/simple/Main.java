package com.wcs.java.tx.jpa.simple;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bank");
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		
		SetupUtil.tearDown();
		SetupUtil.setup();

		tx.begin();
		BankAccount toAccount = (BankAccount) em.createQuery("FROM BankAccount b where b.user = 'andre'")
				.getSingleResult();

		toAccount.setBalance(toAccount.getBalance().add(new BigDecimal("1000")));
		em.merge(toAccount);

		BankAccount fromAccount = (BankAccount) em.createQuery("FROM BankAccount b where b.user = 'david'")
				.getSingleResult();
		
		BigDecimal fromBalance = fromAccount.getBalance();
		BigDecimal newBalance = fromBalance.subtract(new BigDecimal("1000"));
		if (newBalance.intValue() < 0) {
			// Konto kann nicht überzogen werden
			tx.rollback();
		} else {

			fromAccount.setBalance(fromAccount.getBalance().subtract(new BigDecimal("1000")));
			em.merge(fromAccount);
			tx.commit();
		}
		em.close();

	}

}
