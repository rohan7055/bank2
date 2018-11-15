package com.ilp.tcs.responsemodel;

import com.ilp.tcs.bean.Customer;

public class CustomerDeleteResponse extends StatusModel
{
	private Customer data;

	public Customer getData() 
	{
		return data;
	}

	public void setData(Customer data)
	{
		this.data = data;
	}
}
