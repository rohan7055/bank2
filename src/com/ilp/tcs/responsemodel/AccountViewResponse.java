package com.ilp.tcs.responsemodel;

import java.util.ArrayList;

import com.ilp.tcs.bean.Account;

public class AccountViewResponse extends StatusModel
{
	private ArrayList<Account> accounts = new ArrayList<Account>();

	public ArrayList<Account> getAccounts() 
	{
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) 
	{
		this.accounts = accounts;
	}
	
}
