package com.ilp.tcs.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ilp.tcs.bean.Account;
import com.ilp.tcs.bean.Customer;
import com.ilp.tcs.bean.User;

public interface DaoInterface 
{
	public boolean checkCustomer(long ssn) throws SQLException;
	
	public Customer getCustomer(long cust_id) throws SQLException;
	
	public int insertCustomer(Customer customer) throws SQLException;
	
	public long getCustomerId() throws SQLException;
	
	public User login(long ssn,String password) throws SQLException;
	
	public boolean isCustomer(long cust_id) throws SQLException;
	
	public int deleteCustomer(long cust_id) throws SQLException;
	
	public int updateCustomer(Customer customer)throws SQLException;
	
	public Customer searchCustomer(long cust_id) throws SQLException;
	
	public ArrayList<Account> viewAccount() throws SQLException;
	
	public Account getAccount(long acct_id) throws SQLException;
	
	public boolean isAccount(long acct_id) throws SQLException;
	
	public Account viewAccountById(long acct_id) throws SQLException;
	
	public int deleteAccount(long acct_id) throws SQLException;
	
	public int createAccount(Account a) throws SQLException;
	
	public ArrayList<Customer> viewAllCustomers() throws SQLException;
	
	public Customer getCustomerBySSN(long ssn) throws SQLException;
	
	public ArrayList<Account> viewAccountsByCustId(long cust_id) throws SQLException;
	
	public Account checkAccount(long ssn) throws SQLException;
	public int reActivateCustomer(long ssn)throws SQLException;

	
	public Account getAccountByType(long cust_id,String acct_type) throws SQLException;
	public long getAccountId() throws SQLException;
}
