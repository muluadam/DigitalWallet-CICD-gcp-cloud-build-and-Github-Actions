package com.digital.wallet.models;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.digital.wallet.utils.GetRandom;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "wallets")
public class Wallet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private float amount;
	private boolean isActive;

	private long tag;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@JsonIgnore
	private Customer walletHolder;

	@CreationTimestamp
	private LocalDate dateCreated;
	@UpdateTimestamp
	private LocalDate dateUpdated;

	public Wallet(float amount) {
		super();
		this.amount = amount;
		this.isActive = true;
		// generate UUID
		setTag(GetRandom.generate(6));

	}

	public Wallet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getWalletId() {
		return id;
	}

	public void setWalletId(long walletId) {
		this.id = walletId;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Customer getWalletHolder() {
		return walletHolder;
	}

	public void setWalletHolder(Customer walletHolder) {
		this.walletHolder = walletHolder;
	}

	

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getTag() {
		return tag;
	}

	public void setTag(long tag) {
		this.tag = tag;
	}

	

	

}
