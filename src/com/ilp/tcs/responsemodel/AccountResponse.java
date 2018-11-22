package com.ilp.tcs.responsemodel;

import com.ilp.tcs.bean.Account;

public class AccountResponse extends StatusModel
{
	private Account data;

	public Account getData() 
	{
		return data;
	}

	public void setData(Account data) 
	{
		this.data = data;
	}
	
	
}
