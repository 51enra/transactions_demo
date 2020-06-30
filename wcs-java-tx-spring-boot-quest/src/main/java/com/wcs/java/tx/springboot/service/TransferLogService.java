package com.wcs.java.tx.springboot.service;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcs.java.tx.springboot.entities.TransferLogEntry;
import com.wcs.java.tx.springboot.repo.TransferLogEntryRepository;

@Service
public class TransferLogService {
	
	private TransferLogEntryRepository logRepo;

	@Autowired
	public TransferLogService(TransferLogEntryRepository logRepo) {
		this.logRepo = logRepo;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void logTransfer(String userFrom, String userTo, BigDecimal dec, String status) {
		TransferLogEntry logEntry = new TransferLogEntry();
		logEntry.setUserFrom(userFrom);
		logEntry.setUserTo(userTo);
		logEntry.setAmount(dec);
		logEntry.setStatus(status);
		this.logRepo.save(logEntry);
	}
	
}
