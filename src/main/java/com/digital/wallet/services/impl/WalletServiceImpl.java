package com.digital.wallet.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.digital.wallet.enums.Status;
import com.digital.wallet.modelRequests.CardInfo;
import com.digital.wallet.modelRequests.TransferMoneyRequest;
import com.digital.wallet.models.Card;
import com.digital.wallet.models.Customer;
import com.digital.wallet.models.Transaction;
import com.digital.wallet.models.Wallet;
import com.digital.wallet.repositories.CardRepository;
import com.digital.wallet.repositories.TansactionRepository;
import com.digital.wallet.repositories.WalletRepository;
import com.digital.wallet.services.BanckService;
import com.digital.wallet.services.CardService;
import com.digital.wallet.services.CustomerService;
import com.digital.wallet.services.WalletService;

@Service
public class WalletServiceImpl implements WalletService {
	@Autowired
	private WalletRepository walletRepo;
	@Autowired
	private TansactionRepository transactionRepo;

	@Autowired
	private CustomerService customerService;
	@Autowired
	private BanckService banckService;
	@Autowired
	private CardService cardService;

	@Override
	public ResponseEntity<String> transferAmount(long from, TransferMoneyRequest transfer, String userEmail) {
		long toTag = transfer.getRecieverTag();
		float amount = transfer.getAmount();
		String comment = transfer.getComment();
		int pin = transfer.getPin();

		Customer customer = customerService.findByEmail(userEmail);

		if(customer.getCustomerPin() != pin)
				return error("Invalid pin "+pin);

		Wallet wReciever = walletRepo.findByTag(toTag);
		if (wReciever == null)
			return error("Wallet Tag : " + toTag + " not found");

		if (wReciever.getWalletId() == from)
			return error("You cant transfer Money to your wallet");

		if (checkCustomerHasWallet(from, userEmail) == null)
			return error("Not your wallet");
		Wallet wSender = walletRepo.findById(from);

		if (wSender != null) {
			Transaction t = new Transaction(amount, from, toTag,comment,
					LocalDateTime.now());
			if (wSender.getAmount() < amount) {
				t.setStatus(Status.FAILED);
				transactionRepo.save(t);
				return error("Not enough money in your wallet");
			}

			wReciever.setAmount(wReciever.getAmount() + amount);
			wSender.setAmount(wSender.getAmount() - amount);
			t.setStatus(Status.SUCCESS);
			transactionRepo.save(t);
			walletRepo.save(wSender);
			walletRepo.save(wReciever);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
		return error("Wrong WalletId");
	}

	@Override
	public ResponseEntity<?> findWalletTransactions(long walletId, String email) {

		if (checkCustomerHasWallet(walletId, email) == null)
			return error("you are Trying to access wrong wallet id :" + walletId);

		return new ResponseEntity<>(transactionRepo.findWalletTransactions(walletId), HttpStatus.OK);
	}

	private Customer checkCustomerHasWallet(long walletId, String email) {
		Customer c = customerService.findByEmail(email);
		if (c != null) {
			boolean hisWallet = false;
			for (Wallet w : c.getWallets()) {
				if (w.getWalletId() != walletId)
					hisWallet = false;
				else {
					hisWallet = true;
					break;
				}
			}
			if (hisWallet)
				return c;
			else
				return null;
		}
		return c;
	}

	/*
	 * @Override public ResponseEntity<String> topUpMoney(long walletId, CardInfo
	 * cardInfo, float amount, String email) { Customer customer =
	 * checkCustomerHasWallet(walletId, email); if (customer == null) return
	 * error("Wrong wallet Id ");
	 * 
	 * String date = cardInfo.getExpareDate() + "-01"; LocalDate d = null; try { d =
	 * LocalDate.parse(date); } catch (DateTimeParseException e) { // Throw invalid
	 * date message return error("invalide expary date for your card");
	 * 
	 * } Card card = new Card(cardInfo.getCardNumber(), cardInfo.getCsv(), d); Card
	 * isCardExist = cardService.findByCardNumber(cardInfo.getCardNumber());
	 * 
	 * card.setCardHolder(customer);
	 * 
	 * switch (banckService.validAndEnoughMoney(amount, card)) { case -1:
	 * if(isCardExist == null) cardService.save(card); return
	 * error("Not Enough Money on your card"); case 1: Wallet w =
	 * walletRepo.findById(walletId); w.setAmount(w.getAmount() + amount);
	 * if(isCardExist == null) cardService.save(card); walletRepo.save(w); return
	 * new ResponseEntity<>("Your card was debited seccussfly", HttpStatus.OK);
	 * 
	 * 
	 * 
	 * default: return error("Card Invalide"); }
	 * 
	 * }
	 */

	private ResponseEntity<String> error(String msgError) {
		return new ResponseEntity<>(msgError, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<String> topUpMoney(long walletId,int pin, float amount, String email) {
		Customer c = customerService.findByEmail(email);
		if(c.getCustomerPin() != pin)
			return error("Invalid pin "+pin);
		Wallet w = c.getWallets().get(0);
		if (w == null || w.getWalletId() != walletId)
			return error("Invalid wallet");
		if(c.getCard()==null)
			return error("Please add a card");
		switch (banckService.validAndEnoughMoney(amount, c.getCard())) {
		case -1:
			return error("Not Enough Money on your card");
		case 1:
			w.setAmount(w.getAmount() + amount);
			walletRepo.save(w);
			return new ResponseEntity<>("Your card was debited seccussfly", HttpStatus.OK);
		default:
			return error("Card Invalide");

		}

	}

	

	
}
