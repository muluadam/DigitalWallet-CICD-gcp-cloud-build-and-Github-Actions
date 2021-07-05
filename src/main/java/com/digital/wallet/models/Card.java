package com.digital.wallet.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "cards")
public class Card{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long cardNumber;
	


	

	private int cardCSV;
	private LocalDate expiryDate;
	@OneToOne
	@JoinColumn(name = "customer_id", nullable = false)
	@JsonIgnore
	private Customer cardHolder;
	
	

	public Card(long cardNumber, int cardCSV, LocalDate expiryDate) {
		
		this.cardNumber = cardNumber;
		this.cardCSV = cardCSV;
		this.expiryDate = expiryDate;
	}
	
	
	public Card() {
		// TODO Auto-generated constructor stub
	}


	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getCardCSV() {
		return cardCSV;
	}
	public void setCardCSV(int cardCSV) {
		this.cardCSV = cardCSV;
	}
	
	
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Customer getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(Customer cardHolder) {
		this.cardHolder = cardHolder;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		
		Card other = (Card) obj;
		if (cardCSV != other.cardCSV)
			return false;
		if (cardNumber != other.cardNumber)
			return false;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		return true;
	}
	
	
	

}
