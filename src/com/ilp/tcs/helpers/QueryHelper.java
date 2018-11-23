package com.ilp.tcs.helpers;

public class QueryHelper {
	
	public static final String CHECK_CUSTOMER="select * from customer_groupB where ssn=?";
	public static final String GET_CUSTOMER_BY_ID="select * from customer_groupB where cust_id=?";
	public static final String GET_CUSTOMER_BY_SSN="select * from customer_groupB where ssn=?";
	public static final String REACTIVATE_CUSTOMER="update customer_groupB set cust_status=? where ssn=?";

	public static final String INSERT_CUSTOMER="insert into CUSTOMER_GROUPB values (?,?,?,?,?,?,?,'active',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
    public static final String LOGIN_QUERY="select * from USERSTORE_GROUPB where ssn=? and password=?";
    public static final String CHECK_CUSTOMER_ID="select * from customer_groupB where cust_id=?";
    
   public static final String DELETE_CUSTOMER="update customer_groupB set cust_status=? where cust_id=?";
    public static final String SEARCH_CUSTOMER="select * from customer_groupB where cust_id=?";

    public static final String CHECK_ACCOUNT_ID="select * from account_groupB where acct_id=?";
    public static final String DELETE_ACCOUNT="update account_groupB set acct_status ='inactive' where acct_id=?";
    public static final String GET_ACCOUNT_BY_ID="select * from account_groupB where acct_id=?";
    public static final String GET_ACCOUNT_BY_CUSTID="select * from account_groupB where cust_id=?";
    public static final String GET_ACCOUNT_BY_TYPE="SELECT * FROM account_groupB where cust_id=? and acct_type=?";
}
