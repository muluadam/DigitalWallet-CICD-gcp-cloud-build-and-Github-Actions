package com.digital.wallet.services.impl;

import java.time.LocalDateTime;
import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digital.wallet.modelRequests.RegisterRequest;
import com.digital.wallet.models.ConfirmationToken;
import com.digital.wallet.models.Customer;
import com.digital.wallet.models.Wallet;
import com.digital.wallet.repositories.CustomerRepository;
import com.digital.wallet.services.ConfirmationTokenService;
import com.digital.wallet.services.RegistrationService;
import com.digital.wallet.utils.EmailConstructor;
import com.digital.wallet.validators.EmailValidator;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private EmailValidator emailValidator;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private ConfirmationTokenService confirmationTokenService;
	@Autowired
	private EmailConstructor emailConstructor;

	public ResponseEntity<String> register(RegisterRequest req) {
	
		boolean isValidEmail = emailValidator.test(req.getEmail());
		if (!isValidEmail) {
			return new ResponseEntity<>("invalide email", HttpStatus.BAD_REQUEST);
		}

		if (customerRepo.findByEmail(req.getEmail()) != null)
			return new ResponseEntity<>("email already exists ", HttpStatus.BAD_REQUEST);

		Customer c = mapReqToCustomer(req);
		ConfirmationToken token = new ConfirmationToken(LocalDateTime.now(), LocalDateTime.now().plusHours(10L), c);
		
			emailConstructor.send(c.getLastName() +" "+ c.getFirstName(), c.getEmail(), token.getToken());
			customerRepo.save(c);
			confirmationTokenService.save(token);
			
		return new ResponseEntity<>("Verfication email sent "+token.getToken(), HttpStatus.OK);

	}

	private Customer mapReqToCustomer(RegisterRequest req) {
		Wallet w = new Wallet(0);
		Customer c = new Customer(req.getFirstName(), req.getLastName(), req.getEmail(),
				encoder.encode(req.getPassword()), Arrays.asList(w));
		w.setWalletHolder(c);
		return c;

	}
}
