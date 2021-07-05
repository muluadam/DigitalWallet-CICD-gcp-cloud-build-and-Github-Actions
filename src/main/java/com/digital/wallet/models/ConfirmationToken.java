package com.digital.wallet.models;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.digital.wallet.utils.GetRandom;


@Entity
public class ConfirmationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "customer_id"
    )
    private Customer customer;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


  

    public ConfirmationToken(LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             Customer customer) {
        this.token = String.valueOf(GetRandom.generate(6));
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.customer = customer;
    }

	public ConfirmationToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
