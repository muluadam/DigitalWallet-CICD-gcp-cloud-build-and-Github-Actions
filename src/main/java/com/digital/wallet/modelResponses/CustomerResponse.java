package com.digital.wallet.modelResponses;

import java.time.LocalDate;
import java.util.List;

import com.digital.wallet.models.Wallet;

public class CustomerResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private int pin;
	private LocalDate dateCreated;
	private List<Wallet> wallets;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public LocalDate getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}
	public List<Wallet> getWallets() {
		return wallets;
	}
	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}
	public CustomerResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerResponse(Long id, String firstName, String lastName, String email, int pin, LocalDate dateCreated,
			List<Wallet> wallets) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.pin = pin;
		this.dateCreated = dateCreated;
		this.wallets = wallets;
	}

}
