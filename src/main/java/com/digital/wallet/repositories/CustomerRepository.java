package com.digital.wallet.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.digital.wallet.models.Customer;


@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Customer findByEmail(String str);
	Customer findById(long id);
//    @Query("SELECT c FROM Customer c WHERE c.email=:email")
//    Iterable<Customer> findCustomerByEmail(@Param("email") String email);

}
