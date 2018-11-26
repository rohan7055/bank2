package com.ilp.tcs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ilp.tcs.bean.Account;
import com.ilp.tcs.bean.Customer;
import com.ilp.tcs.bean.User;
import com.ilp.tcs.helpers.QueryHelper;
import com.ilp.tcs.interfaces.DaoInterface;
import com.ilp.tcs.utils.DatabaseUtil;
import com.ilp.tcs.utils.MD5;

public class ApplicationDao implements DaoInterface{
	
	//This Function Checks for the SSN id exist the database
	public boolean checkCustomer(long ssn) throws SQLException {
		boolean found=false;
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(QueryHelper.CHECK_CUSTOMER);
		ps.setLong(1, ssn);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			found=true;
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return found;
	}
	
	
	public Customer getCustomerBySSN(long ssn) throws SQLException {
		Customer customer=null;
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(QueryHelper.GET_CUSTOMER_BY_SSN);
		ps.setLong(1, ssn);
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			customer=new Customer();
			customer.setSsn(rs.getLong(1));
			customer.setCust_id(rs.getLong(2));
			customer.setCust_name(rs.getString(3));
			customer.setCust_addr(rs.getString(4));
			customer.setCust_age(rs.getInt(5));
			customer.setCust_city(rs.getString(6));
			customer.setCust_state(rs.getString(7));
			customer.setCust_status(rs.getString(8));
			
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return customer;
	}
	
	public Customer getCustomer(long cust_id) throws SQLException {
		Customer customer=null;
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(QueryHelper.GET_CUSTOMER_BY_ID);
		ps.setLong(1, cust_id);
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			customer=new Customer();
			customer.setSsn(rs.getLong(1));
			customer.setCust_id(rs.getLong(2));
			customer.setCust_name(rs.getString(3));
			customer.setCust_addr(rs.getString(4));
			customer.setCust_age(rs.getInt(5));
			customer.setCust_city(rs.getString(6));
			customer.setCust_state(rs.getString(7));
			customer.setCust_status(rs.getString(8));
			
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return customer;
	}
	
	public int insertCustomer(Customer customer) throws SQLException {
		//String sql1="insert into CUSTOMER_GROUPB values (123456789,custId_seq.nextval,'Rishabh','Rajasthan',19,'Deoli','Rajasthan','active',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        int updated=0;
		String sql="insert into CUSTOMER_GROUPB values (?,?,?,?,?,?,?,'active',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(QueryHelper.INSERT_CUSTOMER);
		ps.setLong(1, customer.getSsn());
		ps.setLong(2, customer.getCust_id());
		ps.setString(3, customer.getCust_name());
		ps.setString(4, customer.getCust_addr());
		ps.setInt(5, customer.getCust_age());
		ps.setString(6, customer.getCust_city());
		ps.setString(7, customer.getCust_state());
		updated=ps.executeUpdate();
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return updated;
	}
	
	public long getCustomerId() throws SQLException {
		long seqId=0;
		String sql="select custId_seq.nextval from dual";
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			seqId=rs.getLong(1);
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return seqId;
		
	}
	
	public long getAccountId() throws SQLException {
		long seqId=0;
		String sql="select acctId_seq.nextval from dual";
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			seqId=rs.getLong(1);
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return seqId;
		
	}
	
	public User login(long ssn,String password) throws SQLException {
		User user=null;
		String sql="select * from USERSTORE_GROUPB where ssn=? and password=?";
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(QueryHelper.LOGIN_QUERY);
		ps.setLong(1, ssn);
		ps.setString(2, MD5.passwordToMD5(password).toLowerCase());
		ResultSet rs=ps.executeQuery();
		
        while(rs.next()) {
        	user=new User();
        	user.setSsn(rs.getLong(1));
        	user.setCust_id(rs.getLong(3));
        	
        }
        DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
	    return user;
	}
	
	public boolean isCustomer(long cust_id) throws SQLException
	{
		String sql = "select * from customer_groupB where cust_id=?";
		boolean found=false;
		
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.CHECK_CUSTOMER_ID);
		ps.setLong(1, cust_id);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
		{
			found=true;
		}
		
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return found;
		
	}

	public int deleteCustomer(long cust_id) throws SQLException
	{
		String sql = "update customer_groupB set cust_status=? where cust_id=?";

		//String sql = "delete from customer_groupB where cust_id=?";

		
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.DELETE_CUSTOMER);
		ps.setString(1, "inactive");
		ps.setLong(2, cust_id);
		
		int count = ps.executeUpdate();
		
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return count;
	}

	public int updateCustomer(Customer customer)throws SQLException
	{
		String s="update customer_groupB set cust_name=?,cust_addr=?,cust_age=? where ssn=?";
		Connection con=DatabaseUtil.getConnection();
		PreparedStatement ps=con.prepareStatement(s);
		ps.setString(1, customer.getCust_name());
		ps.setString(2, customer.getCust_addr());
		ps.setInt(3, customer.getCust_age());
		ps.setLong(4, customer.getSsn());
		int r=ps.executeUpdate();
		
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(con);
		return r;
	}

	
	public Customer searchCustomer(long cust_id) throws SQLException
	{
		Customer customer = new Customer();
		
		String sql="select * from customer_groupB where cust_id=?";
		
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.SEARCH_CUSTOMER);
		
		ps.setLong(1,cust_id);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
		{
			customer.setCust_id(cust_id);
			customer.setSsn(rs.getLong("ssn"));
			customer.setCust_name(rs.getString("cust_name"));
			customer.setCust_addr(rs.getString("cust_addr"));
			customer.setCust_age(rs.getInt("cust_age"));
			customer.setCust_city(rs.getString("cust_city"));
			customer.setCust_state(rs.getString("cust_state"));
			customer.setCust_status(rs.getString("cust_status"));
			customer.setCust_create_date(rs.getString("cust_create_date"));
			customer.setCust_last_updated(rs.getString("cust_last_updated"));
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return customer;
	}
	
	public ArrayList<Account> viewAccount() throws SQLException
	{
		String query="select * from account_groupB";
		ArrayList<Account> viewList=new ArrayList<Account>();
		Connection con=DatabaseUtil.getConnection();
		PreparedStatement ps=con.prepareStatement(query);
		ResultSet rs=ps.executeQuery();
		while(rs.next())
		{
			Account acc=new Account();
			acc.setAcct_id(rs.getLong("acct_id"));
			acc.setCust_id(rs.getLong("cust_id"));
			acc.setAcct_balance(rs.getDouble("acct_balance"));
			acc.setAcct_type(rs.getString("acct_type"));
			acc.setAcct_created_date(rs.getString("acct_created_date"));
			acc.setAcct_status(rs.getString("acct_status"));
			viewList.add(acc);
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(con);
		
		return viewList;
		
	}


	
	public Account getAccount(long acct_id) throws SQLException
	{
		
		Customer customer=null;
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(QueryHelper.GET_ACCOUNT_BY_ID);
		ps.setLong(1, acct_id);
		ResultSet rs=ps.executeQuery();
		Account account=new Account();
		while(rs.next()) {
						
			account.setAcct_id(rs.getLong(1));
			account.setCust_id(rs.getLong(2));
			account.setAcct_type(rs.getString(3));
			account.setAcct_balance(rs.getDouble(4));
			//account.setAcct_created_date(rs.getString(5));
			account.setAcct_status(rs.getString(6));
			
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return account;
		
	}
	
	public boolean isAccount(long acct_id) throws SQLException
	{
		boolean found=false;
		
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.CHECK_ACCOUNT_ID);
		ps.setLong(1, acct_id);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
		{
			found=true;
		}
		
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return found;
		
	}
	
	public Account viewAccountById(long acct_id) throws SQLException
	{
		Account account=null;
		
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.GET_ACCOUNT_BY_ID);
		
		ps.setLong(1, acct_id);
		
		ResultSet rs=ps.executeQuery();
		
		if(rs.next())
		{
			account=new Account();
			account.setAcct_id(acct_id);
			account.setCust_id(rs.getLong(2));
			account.setAcct_type(rs.getString(3));
			account.setAcct_balance(rs.getDouble(4));
			account.setAcct_created_date(rs.getString(5));
			account.setAcct_status(rs.getString(6));
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return account;
		
	}
	
	public int deleteAccount(long acct_id) throws SQLException
	{
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.DELETE_ACCOUNT);
		ps.setLong(1, acct_id);
		
		int count = ps.executeUpdate();
		
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return count;
	}

	public int createAccount(Account a) throws SQLException {
		//String sql1="insert into CUSTOMER_GROUPB values (123456789,custId_seq.nextval,'Rishabh','Rajasthan',19,'Deoli','Rajasthan','active',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        int status=0;
		String sql="insert into account_groupB values (?,?,?,?,CURRENT_TIMESTAMP,?)";
		Connection conn=DatabaseUtil.getConnection();
		PreparedStatement ps=conn.prepareStatement(sql);
		ps.setLong(1, a.getAcct_id());
		ps.setLong(2, a.getCust_id());
		ps.setString(3,a.getAcct_type());
		ps.setDouble(4,a.getAcct_balance());
		ps.setString(5, a.getAcct_status());		
		status=ps.executeUpdate();
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return status;
	}

	public ArrayList<Customer> viewAllCustomers() throws SQLException
	{
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement("select * from customer_groupB order by cust_create_date desc");
		ResultSet rs = null;
		rs=ps.executeQuery();
		ArrayList<Customer> customer = new ArrayList<Customer>();
		while(rs.next())
		{
			 int SSN  = rs.getInt("SSN");
			int pcust_id = rs.getInt("pcust_id");
			String cust_name = rs.getString("cust_name");
			String cust_addr = rs.getString("cust_addr");
			int cust_age = rs.getInt("cust_age");
			String cust_city = rs.getString("cust_city");
			String cust_state = rs.getString("cust_state");
			String cust_status = rs.getString("cust_status");
			String cust_create_date = rs.getString("cust_create_date");
			String cust_last_updated = rs.getString("cust_last_updated");
			Customer temp = new Customer(SSN, pcust_id, cust_name, cust_addr, cust_age, cust_city, cust_state, cust_status, cust_create_date, cust_last_updated);
			customer.add(temp);
		}
		return customer;
	}
	

	@Override
	public ArrayList<Account> viewAccountsByCustId(long cust_id) throws SQLException {
        ArrayList<Account> acctList=new ArrayList<>();;
		Account account=null;
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.GET_ACCOUNT_BY_CUSTID);
		
		ps.setLong(1, cust_id);
		
		ResultSet rs=ps.executeQuery();
		
		while(rs.next())
		{
			
			account=new Account();
			account.setAcct_id(rs.getLong(1));
			account.setCust_id(rs.getLong(2));
			account.setAcct_type(rs.getString(3));
			account.setAcct_balance(rs.getDouble(4));
			account.setAcct_created_date(rs.getString(5));
			account.setAcct_status(rs.getString(6));
			acctList.add(account);
		}
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return acctList;
	}


	



	

	@Override
	public Account checkAccount(long ssn) throws SQLException {
		
		return null;
	}


	@Override
	public Account getAccountByType(long cust_id, String acct_type) throws SQLException {
		
		Account account=null;
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.GET_ACCOUNT_BY_TYPE);
		
		ps.setLong(1, cust_id);
		ps.setString(2, acct_type);
		
		ResultSet rs=ps.executeQuery();
		
		while(rs.next())
		{
			account=new Account();
			account.setAcct_id(rs.getLong(1));
			account.setCust_id(rs.getLong(2));
			account.setAcct_type(rs.getString(3));
			account.setAcct_balance(rs.getDouble(4));
			account.setAcct_created_date(rs.getString(5));
			account.setAcct_status(rs.getString(6));
			
		}
		
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		
		return account;
	}
	
	@Override
	public int reActivateCustomer(long ssn) throws SQLException {
		Customer customer=null;
		int status=0;
		Connection conn = DatabaseUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(QueryHelper.REACTIVATE_CUSTOMER);
		ps.setString(1, "active");
		ps.setLong(2, ssn);
		status=ps.executeUpdate();
		DatabaseUtil.closeStatement(ps);
		DatabaseUtil.closeConnection(conn);
		return status;
		
	}



}