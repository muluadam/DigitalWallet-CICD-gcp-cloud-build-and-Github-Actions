package com.digital.wallet.modelRequests;

public class CardInfo {
	private long cardNumber;
	private int csv;
	private String expareDate;
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getCsv() {
		return csv;
	}
	public void setCsv(int csv) {
		this.csv = csv;
	}
	
	public CardInfo(long cardNumber, int csv, String date) {
		super();
		this.cardNumber = cardNumber;
		this.csv = csv;
		this.expareDate = date;
	}
	public String getExpareDate() {
		return expareDate;
	}
	public void setExpareDate(String expareDate) {
		this.expareDate = expareDate;
	}
	public CardInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
