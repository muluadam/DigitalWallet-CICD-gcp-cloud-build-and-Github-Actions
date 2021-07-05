package com.digital.wallet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.digital.wallet.models.Customer;
import com.digital.wallet.models.Transaction;
import com.digital.wallet.models.Wallet;
import com.digital.wallet.repositories.CustomerRepository;
import com.digital.wallet.repositories.TansactionRepository;
import com.digital.wallet.repositories.WalletRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
public class WalletApplication implements ApplicationRunner {
	private final CustomerRepository customerRepository;
	private final WalletRepository walletRepo;
	private final TansactionRepository transactionRepo;
	
	@Autowired
	private PasswordEncoder encoder;
//    private JavaMailSender javaMailSender;

	public WalletApplication(CustomerRepository customerRepository, WalletRepository walletRepo,
			TansactionRepository transactionRepo) {
		this.customerRepository = customerRepository;
		this.walletRepo = walletRepo;
		this.transactionRepo = transactionRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);

	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Init Customers .....");
		  Wallet w1 = new Wallet( 100f); 
		  walletRepo.save(w1);

			/* 
		 * Customer c1 = new Customer( "Jean", "KD", "devmedk@gmail.com",
		 * encoder.encode("1234"), Arrays.asList(w1)); Customer c2 = new Customer(
		 * "Alehegn", "KJL", "amdagachew@gmail.com", encoder.encode("1234"),
		 * Arrays.asList(w2)); c1.setEnabled(true); c2.setEnabled(true);
		 */

		/*
		 * w1.setWalletHolder(c1); w2.setWalletHolder(c2); customerRepository.save(c1);
		 * customerRepository.save(c2);
		 */
		
	}
	
	

}
