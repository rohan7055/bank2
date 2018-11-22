package com.ilp.tcs.responsemodel;

import java.util.ArrayList;

import com.ilp.tcs.bean.Customer;

public class CustomerViewResponse extends StatusModel 
{
	private ArrayList<Customer> customers = new ArrayList<>();

	public ArrayList<Customer> getCustomers() 
	{
		return customers;
	}

	public void setCustomers(ArrayList<Customer> customers) 
	{
		this.customers = customers;
	}
	
	
}
