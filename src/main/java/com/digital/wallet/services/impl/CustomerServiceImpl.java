package com.digital.wallet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.digital.wallet.modelRequests.CardInfo;
import com.digital.wallet.models.Customer;
import com.digital.wallet.repositories.CustomerRepository;
import com.digital.wallet.services.CardService;
import com.digital.wallet.services.CustomerService;
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private CardService cardService;
	
	
	@Override
	public Customer findById(long id) {
		return customerRepo.findById(id);
	}

	@Override
	public Customer findByEmail(String email) {
		return customerRepo.findByEmail(email);
	}

	/*
	 * @Override public ResponseEntity<?> getCards(String email) {
	 * 
	 * Customer customer = customerRepo.findByEmail(email); return
	 * cardService.getCards(customer); }
	 */

	@Override
	public ResponseEntity<String> chackAndAddCard(CardInfo card, String email) {
		
		Customer customer = customerRepo.findByEmail(email);
		if(customer == null) 
			return new ResponseEntity<>("error customer not found",HttpStatus.BAD_REQUEST);		
		return cardService.chackAndAddCard(card, customer);
	}

	

	

}
