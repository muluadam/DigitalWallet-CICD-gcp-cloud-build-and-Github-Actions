package com.digital.wallet.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.digital.wallet.models.Card;

@Service
public class BanckService {
	private static List<BankCard> CARDS = Arrays.asList(
			new BankCard(5399356490908888L, 123, LocalDate.of(2020, 7, 1), 1000),
			new BankCard(5399355748399388L, 123, LocalDate.of(2022, 12, 1), -50),
			new BankCard(5399359383763352L, 123, LocalDate.of(2025, 3, 1), 100),
			new BankCard(5399350987676775L, 123, LocalDate.of(2023, 3, 1), 2000),
			new BankCard(4299356490908888L, 321, LocalDate.of(2022, 7, 1), 20),
			new BankCard(4299355748399388L, 321, LocalDate.of(2022, 12, 1), 30),
			new BankCard(4299359383763352L, 321, LocalDate.of(2025, 3, 1), 6000),
			new BankCard(4299350987676775L, 321, LocalDate.of(2032, 2, 8), 10000),
			new BankCard(4299350666849088L, 321, LocalDate.of(2023, 3, 1), -100));


	public BankCard findAndCheckValidity(Card card) {
		for (BankCard c : CARDS) {
			if (c.getCardNumber() == card.getCardNumber() && c.getCardCSV() == card.getCardCSV())
				//if (c.getCardCSV() == card.getCardCSV())
					if (c.getExpiryDate().isEqual(card.getExpiryDate()) && c.getExpiryDate().isAfter(LocalDate.now()))

						return c;
		}
		return null;
	}

	public int validAndEnoughMoney(double amount, Card card) {
		BankCard bCard = findAndCheckValidity(card);
		int ENOUGH_MONEY = 0;
		if (bCard != null) {
			if (amount <= bCard.getBalance()) {
				ENOUGH_MONEY = 1;
				bCard.setBalance(bCard.getBalance() - amount);
			}
			else
			ENOUGH_MONEY = -1;

		}
		return ENOUGH_MONEY;
	}

}

class BankCard extends Card {

	private double balance;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public BankCard(long cardNumber, int cardCSV, LocalDate expiryDate, double balance) {
		super(cardNumber, cardCSV, expiryDate);
		this.balance = balance;
	}
}
