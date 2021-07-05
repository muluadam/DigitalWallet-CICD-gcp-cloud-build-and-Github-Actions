package com.digital.wallet.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.digital.wallet.models.Card;
import com.digital.wallet.models.Customer;


public interface CardRepository extends CrudRepository<Card,Long> {

	Card findByCardNumber(long cardNumber);


	//List<Card> findAllByCardHolder(Customer customer);
	Card findByCardHolder(Customer customer);

}
