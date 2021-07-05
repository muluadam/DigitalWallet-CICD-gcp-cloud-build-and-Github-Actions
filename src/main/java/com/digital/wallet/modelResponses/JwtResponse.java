package com.digital.wallet.modelResponses;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
	private final String token;
	

	public JwtResponse(String token ) {
		this.token = token;
		
	}


	public String getToken() {
		return token;
	}

}
