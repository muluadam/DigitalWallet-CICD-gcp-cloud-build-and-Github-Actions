package com.digital.wallet.security;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.digital.wallet.models.Customer;
import com.digital.wallet.repositories.CustomerRepository;

@Service
public class CustomerDetailsService implements UserDetailsService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Customer customer = customerRepository.findByEmail(username);
		if(customer != null) {
			System.out.println("customer is not null");
			return customer;
		}
		

		
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				"", 
				"", 
				true, 
				false, 
				false, 
				false, 
				Arrays.asList(new SimpleGrantedAuthority("USER")));

		System.out.println("customer is  null");
		return userDetails;
	}
	
	
	


}