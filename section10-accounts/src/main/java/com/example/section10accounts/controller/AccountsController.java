package com.example.section10accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.section10accounts.config.AccountsServiceConfig;
import com.example.section10accounts.model.Accounts;
import com.example.section10accounts.model.Cards;
import com.example.section10accounts.model.Customer;
import com.example.section10accounts.model.CustomerDetails;
import com.example.section10accounts.model.Loans;
import com.example.section10accounts.model.Properties;
import com.example.section10accounts.repository.AccountsRepository;
import com.example.section10accounts.service.client.CardsFeignClient;
import com.example.section10accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class AccountsController {
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	AccountsServiceConfig accountsConfig;
	
	@Autowired
	LoansFeignClient loansFeignClient;
	
	@Autowired
	CardsFeignClient cardsFeignClient;
	
	
	@PostMapping("/myAccount")
	public Accounts getAccountDetails(@RequestBody Customer customer) {
		
		Accounts accounts=accountsRepository.findByCustomerId(customer.getCustomerId());
		if(accounts!=null) {
			return accounts;
		}
		else
			return null;
	}
	
	@GetMapping(path="/accounts/properties", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}
	

	@PostMapping("/myCustomerDetails")

	@CircuitBreaker(name = "detailsForCustomerSupportApp" ,fallbackMethod ="myCustomerDetailsFallBack")
	@Retry(name = "retryCustomerDetails" ,fallbackMethod ="myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestHeader("eazybank-correlation-id") String correlationid, @RequestBody Customer customer) {
		
		Accounts accounts=accountsRepository.findByCustomerId(customer.getCustomerId());

		List<Loans> loans=loansFeignClient.getLoanDetails(correlationid,customer);
		List<Cards> cards=cardsFeignClient.getCardDetails(correlationid,customer);
		
		CustomerDetails customerDetails= new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setCards(cards);
		customerDetails.setLoans(loans);
		
		return customerDetails;
	}
	
	private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("eazybank-correlation-id") String correlationid, Customer customer, Throwable t) {
		
		Accounts accounts=accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans=loansFeignClient.getLoanDetails(correlationid,customer);
		
		CustomerDetails customerDetails= new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		
		return customerDetails;
	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello, Welcome to EazyBank";
	}

	private String sayHelloFallback(Throwable t) {
		return "Hi, Welcome to EazyBank(fallback)";
	}

}
