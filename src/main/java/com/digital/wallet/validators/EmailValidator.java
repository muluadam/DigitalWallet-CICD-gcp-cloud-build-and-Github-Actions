package com.digital.wallet.validators;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String>{

	@Override
	public boolean test(String email) {
		// TODO Auto-generated method stub
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
		
	}

}
