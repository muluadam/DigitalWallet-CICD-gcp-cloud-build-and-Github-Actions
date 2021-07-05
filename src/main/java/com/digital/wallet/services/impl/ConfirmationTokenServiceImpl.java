package com.digital.wallet.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.digital.wallet.models.ConfirmationToken;
import com.digital.wallet.models.Customer;
import com.digital.wallet.repositories.ConfirmationTokenRepository;
import com.digital.wallet.repositories.CustomerRepository;
import com.digital.wallet.services.ConfirmationTokenService;
import com.digital.wallet.utils.EmailConstructor;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	@Autowired
	private EmailConstructor emailSender;
	@Autowired
	private CustomerRepository customerRepository;

	public void save(ConfirmationToken token) {
		confirmationTokenRepository.save(token);
	}

	@Override
	public ResponseEntity<String> verifyToken(String token, int pin) {
		// TODO Auto-generated method stub
		String str =String.valueOf(pin);
		ConfirmationToken tokenExist = confirmationTokenRepository.findByToken(token);
		if (tokenExist == null )
			return new ResponseEntity<>("Invalide token", HttpStatus.BAD_REQUEST);
		if (str.length()!=4 )
			return new ResponseEntity<>("Invalide PIN", HttpStatus.BAD_REQUEST);
		LocalDateTime expiredAt = tokenExist.getExpiresAt();
		Customer c = tokenExist.getCustomer();
		if (expiredAt.isBefore(LocalDateTime.now())) {
			ConfirmationToken newToken = new ConfirmationToken(LocalDateTime.now(),LocalDateTime.now().plusHours(10),c);
			emailSender.send(c.getFirstName()+" "+c.getLastName(), c.getEmail(), newToken.getToken());
			confirmationTokenRepository.save(newToken);
			confirmationTokenRepository.delete(tokenExist);
			return new ResponseEntity<>("token expired, an email was sent to verify your account",
					HttpStatus.BAD_REQUEST);
		}
		else {
			c.setEnabled(true);
			c.setCustomerPin(pin);
			customerRepository.save(c);
			confirmationTokenRepository.delete(tokenExist);
		}

		return new ResponseEntity<>("account verified and pin created",
				HttpStatus.OK);
	}

	/*
	 * public int setConfirmedAt(String token) { return
	 * confirmationTokenRepository.updateConfirmedAt( token, LocalDateTime.now()); }
	 */
}
