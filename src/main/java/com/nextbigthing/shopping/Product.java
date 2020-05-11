package com.nextbigthing.shopping;

public class Product {

	private String description;
	private Value cost;

	@SuppressWarnings("unused")
	private Product() {}
	
	public Product(String description, Value cost) {
		super();
		this.description = description;
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public Value getCost() {
		return cost;
	}

}
