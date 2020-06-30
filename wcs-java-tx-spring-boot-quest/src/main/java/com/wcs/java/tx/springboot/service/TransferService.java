package com.wcs.java.tx.springboot.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcs.java.tx.springboot.entities.BankAccount;

@Service
public class TransferService {
	@Autowired
 	private AccountService service;

	@Autowired
	private TransferLogService logService;
	
	@Transactional(value = TxType.REQUIRED, rollbackOn = InsufficientFundsException.class)
	public List<BankAccount> transferMoney(String userFrom, String userTo, BigDecimal amount)
			throws InsufficientFundsException {
		BigDecimal fromBalance = service.getBalanceOfUser(userFrom);
		String status = "Startbalance is " + fromBalance;
		logService.logTransfer(userFrom, userTo, amount, status);
		BankAccount toAccount = service.deposit(userTo, amount);
		BankAccount fromAccount = service.withdraw(userFrom, amount);
		return Arrays.asList(fromAccount, toAccount);
	}
}
