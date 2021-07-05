package com.digital.wallet.modelRequests;

public class TransferMoneyRequest {
	private float amount;
	private long recieverTag;
	private String comment;
	private int pin;
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public TransferMoneyRequest(float amount, long recieverTag, String comment, int pin) {
		super();
		this.amount = amount;
		this.recieverTag = recieverTag;
		this.comment = comment;
		this.pin = pin;
	}
	public long getRecieverTag() {
		return recieverTag;
	}
	public void setRecieverTag(long recieverTag) {
		this.recieverTag = recieverTag;
	}
	public TransferMoneyRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
