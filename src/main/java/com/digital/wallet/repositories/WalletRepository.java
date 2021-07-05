package com.digital.wallet.repositories;

import com.digital.wallet.models.Wallet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {

	Wallet findById(long to);

	
	Wallet findByTag(long toTag);
	
//    @Query("SELECT w FROM Wallet  w WHERE w.walletId=:walletId")
//    Iterable<Wallet> findWalletById(@Param("walletId") Integer walletId);
}
