package com.digital.wallet.services;


import org.springframework.http.ResponseEntity;

import com.digital.wallet.modelRequests.RegisterRequest;


public interface RegistrationService {
	
	public ResponseEntity<String> register(RegisterRequest req);
}
