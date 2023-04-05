package com.example.section10cards.model;

import javax.persistence.Id;

import lombok.Data;

@Data
public class Customer {
	
	@Id
	private int customerId;

}
