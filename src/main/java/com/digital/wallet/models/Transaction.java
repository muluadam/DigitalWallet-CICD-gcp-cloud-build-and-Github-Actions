package com.digital.wallet.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.digital.wallet.enums.Status;

@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transactionId;
	private float amount;
	private long walletSender;
	private long walletReciever;
	private LocalDateTime transactionDate;
	private String comment;
	//@Enumerated(EnumType.STRING)
	private Status status;

	public Transaction( float amount, long walletSender, long walletReciever,String comment, LocalDateTime date) {
		super();
		this.amount = amount;
		this.transactionDate = date;
		this.walletSender = walletSender;
		this.walletReciever = walletReciever;
		this.comment=comment;
		
	}

	/*
	 * @ManyToMany(mappedBy = "transactions") // @JoinTable(name =
	 * "wallet_transactions", joinColumns = { // @JoinColumn(name = "transactionId")
	 * }, inverseJoinColumns = { @JoinColumn(name = "walletId") }) private
	 * List<Wallet> wallets;
	 */

	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Transaction(float amount, long walletSender, long walletReciever, LocalDateTime transactionDate,Status status ) {
		super();
		this.amount = amount;
		this.walletSender = walletSender;
		this.walletReciever = walletReciever;
		this.transactionDate = transactionDate;
		this.status=status;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public long getWalletSender() {
		return walletSender;
	}

	public void setWalletSender(long walletSender) {
		this.walletSender = walletSender;
	}

	public long getWalletReciever() {
		return walletReciever;
	}

	public void setWalletReciever(long walletReciever) {
		this.walletReciever = walletReciever;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Status getStatus() {
		return status;
	}

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
