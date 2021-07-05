package com.digital.wallet.services;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.digital.wallet.modelRequests.CardInfo;
import com.digital.wallet.models.Card;
import com.digital.wallet.models.Customer;
import com.digital.wallet.repositories.CardRepository;

@Service
public class CardService {
	@Autowired
	private CardRepository cardRepo;
	@Autowired
	private BanckService banckService;

	public ResponseEntity<String> chackAndAddCard(CardInfo cardInfo, Customer customer) {

		String date = cardInfo.getExpareDate() + "-01";
		LocalDate d = null;
		try {
			d = LocalDate.parse(date);
		} catch (DateTimeParseException e) {
			return error("invalide expary date for your card");
		}
		Card card = new Card(cardInfo.getCardNumber(), cardInfo.getCsv(), d);
		if (banckService.findAndCheckValidity(card) != null) {

			Card cardExist = findByCardNumber(card.getCardNumber());

			if (cardExist == null || (cardExist != null && !cardExist.equals(card))) {
				card.setCardHolder(customer);
				cardRepo.save(card);
			}
			return new ResponseEntity<>("Card added", HttpStatus.OK);
		}
		return error("Card Invalide");
	}

	public Card findByCardNumber(long cardNumber) {
		return cardRepo.findByCardNumber(cardNumber);
	}

	/*
	 * public ResponseEntity<?> getCards(Customer customer) {
	 * 
	 * return new ResponseEntity<>(cardRepo.findAllByCardHolder(customer),
	 * HttpStatus.OK); }
	 */

	private ResponseEntity<String> error(String message) {
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	public void save(Card card) {
		cardRepo.save(card);

	}

	public int checkAndToUp(Customer c, float amount) {
		Card card = cardRepo.findByCardHolder(c);
		 return banckService.validAndEnoughMoney(amount, card);
		 
	}

	

}
