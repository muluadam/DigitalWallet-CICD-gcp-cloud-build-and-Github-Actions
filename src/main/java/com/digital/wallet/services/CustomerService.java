package com.digital.wallet.services;

import org.springframework.http.ResponseEntity;

import com.digital.wallet.modelRequests.CardInfo;
import com.digital.wallet.models.Customer;

public interface CustomerService {
	public Customer findById(long id);
	public Customer findByEmail(String email);
	//public ResponseEntity<?> getCards(String email);
	public ResponseEntity<String> chackAndAddCard(CardInfo card, String email);
	

}
