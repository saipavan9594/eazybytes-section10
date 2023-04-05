package com.example.section10accounts.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.section10accounts.model.Accounts;

public interface AccountsRepository extends CrudRepository<Accounts, Long> {
	
	Accounts findByCustomerId(int customerId);

}
