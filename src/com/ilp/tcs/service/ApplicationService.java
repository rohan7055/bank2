package com.ilp.tcs.service;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ilp.tcs.bean.Account;
import com.ilp.tcs.bean.Customer;
import com.ilp.tcs.bean.User;
import com.ilp.tcs.dao.ApplicationDao;
import com.ilp.tcs.interfaces.DaoInterface;
import com.ilp.tcs.responsemodel.AccountResponse;
import com.ilp.tcs.responsemodel.AccountViewResponse;
import com.ilp.tcs.responsemodel.CustomerLoginResponse;
import com.ilp.tcs.responsemodel.CustomerResponse;
import com.ilp.tcs.responsemodel.CustomerViewResponse;
import com.ilp.tcs.responsemodel.StatusModel;
import com.ilp.tcs.responsemodel.AccountResponse;
import com.ilp.tcs.utils.AppConstants;
import com.ilp.tcs.utils.MessageConstants;

public class ApplicationService {
	
	private DaoInterface daoInterface = null;
	
	public ApplicationService(DaoInterface daoInterface)
	{
		this.daoInterface = daoInterface;
	}
	
	public CustomerResponse register(Customer customer){
		CustomerResponse response=new CustomerResponse();
		
		try {
			if(!daoInterface.checkCustomer(customer.getSsn())) {
				Customer custrespOBJ;
				long nextCustId=daoInterface.getCustomerId();
				customer.setCust_id(nextCustId);
				if(daoInterface.insertCustomer(customer)>0)
				{  
					custrespOBJ=daoInterface.getCustomer(customer.getCust_id());
					response.setStatus(true);
					response.setStatusCode(HttpServletResponse.SC_OK);
					response.setMessage(MessageConstants.REGISTER_SUCCESS);
					response.setData(custrespOBJ);
				}else {
					response.setStatus(false);
					response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
					response.setMessage(MessageConstants.REGISTER_FAILED);
					
				}
				
				
				
			}else{
			
			response.setStatus(false);
			response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			response.setMessage(MessageConstants.CUSTOMER_ALREADY_EXIST);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(false);
			response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			response.setMessage(e.getMessage());
		}
		finally {
			return response;
		}
	}
	
	public CustomerLoginResponse login(long ssn,String password) {
		CustomerLoginResponse customerLoginResponse=new CustomerLoginResponse();
		
		try {
			User user=daoInterface.login(ssn, password);
			if(user!=null) {
				customerLoginResponse.setStatus(true);
				customerLoginResponse.setMessage(MessageConstants.LOGIN_SUCCESS);
				customerLoginResponse.setStatusCode(HttpServletResponse.SC_OK);
				customerLoginResponse.setData(user);
			}else {
				customerLoginResponse.setStatus(false);
				customerLoginResponse.setMessage(MessageConstants.LOGIN_FAILED);
				customerLoginResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			customerLoginResponse.setStatus(false);
			customerLoginResponse.setMessage(e.getMessage());
			customerLoginResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		}
		finally {
		return customerLoginResponse;
		}
	}
	
	public CustomerResponse deleteCustomer(long cust_id)
	{
		
		CustomerResponse response = new CustomerResponse();
		try
		{
			
			//System.out.println(d.isCustomer(cust_id)+"#");
			if(daoInterface.isCustomer(cust_id))
			{
				Customer temp = new Customer();
				Customer customer =  daoInterface.getCustomer(cust_id);
				if(customer.getCust_status().equalsIgnoreCase("active")){
					CustomerResponse responseObj=checkCustomerDelete(cust_id);
					if(responseObj.isStatus()) {				
					if(daoInterface.deleteCustomer(cust_id) > 0)
					{
						response.setStatus(true);
						response.setStatusCode(200);
						response.setMessage("Deletion Success");
						customer=daoInterface.getCustomer(cust_id);
						response.setData(customer);
					}
					else
					{
						response.setStatus(false);
						response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
						response.setMessage("Deletion failed");
					}
				}
					else {
						response.setStatus(responseObj.isStatus());
						response.setStatusCode(responseObj.getStatusCode());
						response.setMessage(responseObj.getMessage());
					}
				}
				else if(customer.getCust_status().equalsIgnoreCase("inactive")){
					response.setStatus(false);
					response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
					response.setMessage("Customer is already deactivated ");
				}
				
				
			}
			else
			{
				response.setStatus(false);
				response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				response.setMessage("Customer doesn't exist");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		finally
		{
			return response;
		}
	}
	
	public CustomerResponse updateCustomer(Customer customer)
	{
		CustomerResponse customerUpdateResponse=new CustomerResponse();
	
		try {
			if(daoInterface.checkCustomer(customer.getSsn()))
			{
				if(daoInterface.updateCustomer(customer)>0)
				{
					Customer cust=daoInterface.getCustomer(customer.getCust_id());
					customerUpdateResponse.setStatus(true);
					customerUpdateResponse.setMessage("update successful");
                    customerUpdateResponse.setStatusCode(HttpServletResponse.SC_OK);
                    customerUpdateResponse.setData(cust);
				}
				else{
				customerUpdateResponse.setStatus(false);
				customerUpdateResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customerUpdateResponse.setMessage("updation failed");
				}
				
			}
			else{
				customerUpdateResponse.setStatus(false);
				customerUpdateResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customerUpdateResponse.setMessage("customer does'nt exists");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			customerUpdateResponse.setStatus(false);
			customerUpdateResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			customerUpdateResponse.setMessage(e.getMessage());
			
		}
		
		finally
		{
			return customerUpdateResponse;
		}
		
	}
	
	public CustomerResponse searchCustomer(long cust_id)
	{
		CustomerResponse searchCustomerResponse = new CustomerResponse();
		
		try
		{
			Customer customer = new Customer();
			customer = daoInterface.searchCustomer(cust_id);
			if (customer !=null)
			{	
			searchCustomerResponse.setData(customer);
			searchCustomerResponse.setMessage("Successful");
			searchCustomerResponse.setStatus(true);
			searchCustomerResponse.setStatusCode(HttpServletResponse.SC_OK);
			}
			else{
				searchCustomerResponse.setMessage("customer does not exist");
				searchCustomerResponse.setStatus(false);
				searchCustomerResponse.setStatusCode(HttpServletResponse.SC_OK);
				
			}
		}
		catch(SQLException exception)
		{
			exception.printStackTrace();
			searchCustomerResponse.setMessage("Failed");
			searchCustomerResponse.setStatus(false);
			searchCustomerResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		}
		finally
		{
			return searchCustomerResponse;
		}
		
	}
	
	public CustomerResponse searchCustomerBySSN(long SSN)
	{
		CustomerResponse searchCustomerResponse = new CustomerResponse();
		
		try
		{
			Customer customer = new Customer();
			customer = daoInterface.getCustomerBySSN(SSN);
			if(customer !=null)
			{
			searchCustomerResponse.setData(customer);
			searchCustomerResponse.setMessage("Successful");
			searchCustomerResponse.setStatus(true);
			searchCustomerResponse.setStatusCode(HttpServletResponse.SC_OK);
			}
			else
			{
				searchCustomerResponse.setMessage("customer does not exist");
				searchCustomerResponse.setStatus(false);
				searchCustomerResponse.setStatusCode(HttpServletResponse.SC_OK);
			}
		}
		catch(SQLException exception)
		{
			exception.printStackTrace();
			searchCustomerResponse.setMessage("Failed");
			searchCustomerResponse.setStatus(false);
			searchCustomerResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		}
		finally
		{
			return searchCustomerResponse;
		}
		
	}

	public AccountViewResponse viewAccount()
	{
		AccountViewResponse accountViewResponse=new AccountViewResponse();
		
		try
		{
			ArrayList<Account> viewList=daoInterface.viewAccount();
			if(!viewList.isEmpty())
			{
				accountViewResponse.setStatus(true);
				accountViewResponse.setMessage("success");
                accountViewResponse.setStatusCode(HttpServletResponse.SC_OK);
               accountViewResponse.setAccounts(viewList);
				
			}
			else
			{
				accountViewResponse.setStatus(false);
				accountViewResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				accountViewResponse.setMessage("view failed");
				
			}
				
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			accountViewResponse.setStatus(false);
			accountViewResponse.setMessage(e.getMessage());
			accountViewResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		}
		finally {
		return accountViewResponse;
		}

	}
	public AccountResponse createAccount(Account account)
	{
		AccountResponse accountResponse=new AccountResponse();
		
		Account data= new Account();
		Account checkAccount= new Account();
		
		try
		{
			Customer customer=daoInterface.getCustomer(account.getCust_id());
			if(customer!=null){
				if(customer.getCust_status().equals("inactive")){
					accountResponse.setStatus(false);
					accountResponse.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);
					accountResponse.setMessage("Customer is deactivated");
					accountResponse.setData(data);
				}else if(customer.getCust_status().equals("active")){
					String acct_type=account.getAcct_type();
					checkAccount= daoInterface.getAccountByType(account.getCust_id(), acct_type);
					if(checkAccount!= null)
					{
						if(checkAccount.getAcct_status().equals("active"))
						{
							accountResponse.setStatus(false);
							accountResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
							accountResponse.setMessage(MessageConstants.ACCOUNT_ALREADY_EXIST);
						}
						else
						{
							checkAccount.setAcct_status("active");
							accountResponse.setStatus(false);
							accountResponse.setStatusCode(HttpServletResponse.SC_OK);
							accountResponse.setMessage(MessageConstants.ACCOUNT_INTO_ACTIVE_STATE);
						}
					}
					else
					{
						account.setAcct_id(daoInterface.getAccountId());
						account.setAcct_status("active");
						int status=daoInterface.createAccount(account);
						System.out.println(status);
						if(status>0)
						{
							data=daoInterface.getAccount(account.getAcct_id());
							accountResponse.setStatus(true);
							accountResponse.setStatusCode(HttpServletResponse.SC_OK);
							accountResponse.setMessage(MessageConstants.ACCOUNT_SUCCESS);
							accountResponse.setData(data);
						}
						else
						{
							accountResponse.setStatus(false);
							accountResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
							accountResponse.setMessage(MessageConstants.ACCOUNT_FAILURE);
							
						}
					}
					
					
				}
			}else{
				accountResponse.setStatus(false);
				accountResponse.setStatusCode(AppConstants.ERROR_DNE);
				accountResponse.setMessage(MessageConstants.ACCOUNT_FAILURE);
			}
			
					
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				accountResponse.setStatus(false);
				accountResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				accountResponse.setMessage(MessageConstants.ACCOUNT_FAILURE);
				
			}
			finally {
			return accountResponse;
			}
	}
	
	public AccountResponse viewAccountDetails(long acct_id)
	{
		AccountResponse accountResponse = new AccountResponse ();
		
		
		try
		{
			Account acct=daoInterface.viewAccountById(acct_id);
			if(acct!=null)
			{
				accountResponse.setStatus(true);
				accountResponse.setMessage("Success");
				accountResponse.setStatusCode(HttpServletResponse.SC_OK);
				accountResponse.setData(acct);
			}
			else
			{
				accountResponse.setStatus(false);
				accountResponse.setMessage(MessageConstants.ACCOUNT_DOES_NOT_EXIST);
				accountResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			accountResponse.setStatus(false);
			accountResponse.setMessage(e.getMessage());
			accountResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		}
		finally
		{
			return accountResponse;
		}
	}
	
	public AccountResponse deleteAccount(long acct_id, String account_type)
	{
		AccountResponse response=new AccountResponse();
		
		try
		{
			System.out.println(daoInterface.isAccount(acct_id)+"#");
			if(daoInterface.isAccount(acct_id))
			{
				
				Account temp = new Account();
				Account account=daoInterface.getAccount(acct_id);
				
				temp.setAcct_id(account.getAcct_id());
				temp.setAcct_status(account.getAcct_status());

				if(temp.getAcct_type().equals(account_type))
				{
					
					if(daoInterface.deleteCustomer(acct_id) > 0)
					{
						response.setStatus(true);
						response.setStatusCode(200);
						response.setMessage("Deletion Success");
						response.setData(temp);
					}
					else
					{
						response.setStatus(false);
						response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
						response.setMessage("Deletion failed");
					}
				}
				else
				{
					response.setStatus(false);
					response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
					response.setMessage("Account Type does not match with the specified Account ID");
				}
			}
			else
			{
				response.setStatus(false);
				response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				response.setMessage("Account doesn't exist");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		finally
		{
			return response;
		}
		
	}
	
	
	
	public  CustomerViewResponse viewAllCustomers()
	{
		CustomerViewResponse customerViewResponse=new CustomerViewResponse();
		
		try
		{
			ArrayList<Customer> viewCustomers=daoInterface.viewAllCustomers();
			if(!viewCustomers.isEmpty())
			{   
				customerViewResponse.setCustomers(viewCustomers);
				customerViewResponse.setStatus(true);
				customerViewResponse.setMessage("success");
				customerViewResponse.setStatusCode(HttpServletResponse.SC_OK);
              
				
			}
			else
			{
				customerViewResponse.setStatus(false);
				customerViewResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
				customerViewResponse.setMessage("view failed");
				
			}
				
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			customerViewResponse.setStatus(false);
			customerViewResponse.setMessage(e.getMessage());
			customerViewResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		}
		finally {
		return customerViewResponse;
		}

	}
	
	
	public CustomerResponse checkCustomer(long ssn){
		CustomerResponse statusModel=new CustomerResponse();
		System.out.println(ssn);
		try {
			Customer customer=daoInterface.getCustomerBySSN(ssn);
			if(customer!=null){
				System.out.println("not null");

				if(customer.getCust_status().equals("active")){
					statusModel.setStatus(Boolean.FALSE);
					statusModel.setMessage("Customer Already Exist with SSN ID: "+ssn);
					statusModel.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);	
					statusModel.setData(customer);
				}else if(customer.getCust_status().equalsIgnoreCase("inactive")){
					statusModel.setStatus(Boolean.FALSE);
					statusModel.setMessage("Customer Already Registered with SSN ID: "+ssn+".Do you want to reactivate");
					statusModel.setStatusCode(AppConstants.STATUS_SHOWPROMPT);	
					statusModel.setData(customer);

				}
			}else {
				statusModel.setStatus(Boolean.TRUE);
				statusModel.setMessage("Customer Doesnt Exist .Proceed to create");
				statusModel.setStatusCode(AppConstants.STATUS_OK);	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			statusModel.setStatus(Boolean.FALSE);
			statusModel.setMessage(e.getMessage());
			statusModel.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
		}
		finally{
		return statusModel;
		}
	}
	
	public CustomerResponse checkCustomerAccount(long cust_id){
		CustomerResponse customerResp=new CustomerResponse();
		try {
			Customer customer=daoInterface.getCustomer(cust_id);
			if(customer!=null){
					
				if(customer.getCust_status().equals("active")){
					customerResp.setStatus(Boolean.TRUE);
					customerResp.setMessage("Customer Retrieved Successfully");
					customerResp.setStatusCode(AppConstants.STATUS_OK);	
					customerResp.setData(customer);
				}else if(customer.getCust_status().equalsIgnoreCase("inactive")){
					customerResp.setStatus(Boolean.FALSE);
					customerResp.setMessage("Customer is Deactivated");
					customerResp.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);	
					customerResp.setData(customer);
				}
				else{
					customerResp.setStatus(Boolean.FALSE);
					customerResp.setMessage("DB ERROR OCCURED");
					customerResp.setStatusCode(AppConstants.STATUS_SHOWPROMPT);	
				}
			} 
			else{
				customerResp.setStatus(Boolean.FALSE);
				customerResp.setMessage("Customer Cust Id Doesnt not exist");
				customerResp.setStatusCode(AppConstants.ERROR_DNE);	
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			customerResp.setStatus(Boolean.FALSE);
			customerResp.setMessage(e.getMessage());
			customerResp.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
		}
		finally{
		return customerResp;
		}

	}
	
	public CustomerResponse checkCustomerUpdate(long ssn){
		CustomerResponse customerResp=new CustomerResponse();
		try {
			Customer customer=daoInterface.getCustomerBySSN(ssn);
			if(customer!=null){
					
				if(customer.getCust_status().equals("active")){
					customerResp.setStatus(Boolean.TRUE);
					customerResp.setMessage("Customer Retrieved Successfully");
					customerResp.setStatusCode(AppConstants.STATUS_OK);	
					customerResp.setData(customer);
				}else if(customer.getCust_status().equalsIgnoreCase("inactive")){
					customerResp.setStatus(Boolean.FALSE);
					customerResp.setMessage("Customer Already Registered with SSN ID: "+ssn+".Do you want to reactivate");
					customerResp.setStatusCode(AppConstants.STATUS_SHOWPROMPT);	
					customerResp.setData(customer);
				}
				else{
					customerResp.setStatus(Boolean.FALSE);
					customerResp.setMessage("DB ERROR OCCURED");
					customerResp.setStatusCode(AppConstants.STATUS_SHOWPROMPT);	
				}
			} 
			else{
				customerResp.setStatus(Boolean.FALSE);
				customerResp.setMessage("Customer SSN Doesn not exist");
				customerResp.setStatusCode(AppConstants.ERROR_DNE);	
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			customerResp.setStatus(Boolean.FALSE);
			customerResp.setMessage(e.getMessage());
			customerResp.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
		}
		finally{
		return customerResp;
		}
	}
	
	
	public CustomerResponse checkCustomerDelete(long custid){
		CustomerResponse customerResp=new CustomerResponse();
		try {
			Customer customer=daoInterface.getCustomer(custid);
			if(customer!=null){
					
				 if(customer.getCust_status().equalsIgnoreCase("active")){
						ArrayList<Account> acct=daoInterface.viewAccountsByCustId(custid);
                       
					 if(acct.size()==0||acct==null) {
						 customerResp.setStatus(Boolean.TRUE);
							customerResp.setMessage("No Accounts are Created for the customer.Customer can be deleted");
							customerResp.setStatusCode(AppConstants.STATUS_OK);	
							customerResp.setData(customer);
						 
					   }
					 else {
						if(checkAccountsforCustomerOnDelete(customer.getCust_id())){
							customerResp.setStatus(Boolean.TRUE);
							customerResp.setMessage("All Customers Accounts are deactivated.Customer can be deleted");
							customerResp.setStatusCode(AppConstants.STATUS_OK);	
							customerResp.setData(customer);
						}
						else{
							customerResp.setStatus(Boolean.FALSE);
							customerResp.setMessage("Some Accounts are activated for the customer.Please deactivate account(s) first");
							customerResp.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);	
							customerResp.setData(customer);
						     }
					      }
					 }
				 else if(customer.getCust_status().equalsIgnoreCase("inactive")){
					customerResp.setStatus(Boolean.FALSE);
					customerResp.setMessage("Customer Already Registered with CUST ID: "+custid+".And is already Deactivated");
					customerResp.setStatusCode(AppConstants.STATUS_SHOWPROMPT);	
					customerResp.setData(customer);
				}
				else{
					customerResp.setStatus(Boolean.FALSE);
					customerResp.setMessage("DB ERROR OCCURED");
					customerResp.setStatusCode(AppConstants.STATUS_SHOWPROMPT);	
				}
			} 
			else{
				customerResp.setStatus(Boolean.FALSE);
				customerResp.setMessage("Customer Doesnt not exist");
				customerResp.setStatusCode(AppConstants.ERROR_DNE);	
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			customerResp.setStatus(Boolean.FALSE);
			customerResp.setMessage(e.getMessage());
			customerResp.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
		}
		finally{
		return customerResp;
		}
	}
	
	private boolean checkAccountsforCustomerOnDelete(long cust_id){
		boolean valid=false;
		try {
			ArrayList<Account> acctList=daoInterface.viewAccountsByCustId(cust_id);
			if(acctList!=null){
				int acctSize=acctList.size();
				if(acctSize==0){
					valid=true;
				}else if(acctSize==1){
					if(acctList.get(0).getAcct_status().equalsIgnoreCase("inactive")){
						valid=true;
					}
				}else if(acctSize==2){
					if(acctList.get(0).getAcct_status().equals("inactive")&&
							acctList.get(1).getAcct_status().equals("inactive")	){
						valid=true;
						
					}
				}else{
					valid=false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return valid;
			
		}finally{
		return valid;
		}
		
	}
	
	public  AccountViewResponse getAcctDetails(long cust_id)
	{
		AccountViewResponse accountViewResponse = new AccountViewResponse ();
		
		try {
			ArrayList<Account> acct=daoInterface.viewAccountsByCustId(cust_id);
			
			if(acct!=null&&!acct.isEmpty())
			{
				accountViewResponse.setStatus(true);
				accountViewResponse.setMessage("Success");
				accountViewResponse.setStatusCode(HttpServletResponse.SC_OK);
				accountViewResponse.setAccounts(acct);
			}
			else
			{
				accountViewResponse.setStatus(false);
				accountViewResponse.setMessage(MessageConstants.ACCOUNT_DOES_NOT_EXIST);
				accountViewResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			accountViewResponse.setStatus(false);
			accountViewResponse.setMessage(e.getMessage());
			accountViewResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		}
		finally
		{
			return accountViewResponse;
		}
	}
	

	public AccountResponse checkAccount(long cust_id,String acct_type) {
		
		Account account = null;
		AccountResponse accountResponse = new AccountResponse();
		try{
		account = daoInterface.getAccountByType(cust_id,acct_type);
		if(account!=null){
			if(account.getAcct_status().equals("active")){
				accountResponse.setStatus(false);
				accountResponse.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);
				accountResponse.setMessage(account.getAcct_type()+" account type already exists");
			}else if(account.getAcct_status().equals("inactive")){
				accountResponse.setStatus(true);
				accountResponse.setStatusCode(AppConstants.STATUS_OK);
				accountResponse.setMessage("Activate the account");
				accountResponse.setData(account);
			}
			}
			else
			{
				accountResponse.setStatus(true);
				accountResponse.setStatusCode(AppConstants.STATUS_OK);
				accountResponse.setMessage("Proceed to create account");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			accountResponse.setStatus(Boolean.FALSE);
			accountResponse.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);
			accountResponse.setMessage(e.getMessage());
		}
		finally{
			return accountResponse;
		}
	}
	
	
public AccountResponse checkAccountDelete(long acct_id) {
		
		Account account = null;
		AccountResponse accountResponse = new AccountResponse();
		try{
		account = daoInterface.getAccount(acct_id);
		if(account!=null){
			if(account.getAcct_status().equals("inactive")){
				accountResponse.setStatus(Boolean.FALSE);
				accountResponse.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);
				accountResponse.setMessage(account.getAcct_type()+" account type already deactivated");
			}else if(account.getAcct_status().equals("active")){
				accountResponse.setStatus(Boolean.TRUE);
				accountResponse.setStatusCode(AppConstants.STATUS_OK);
				accountResponse.setMessage("Deactivate the account");
				accountResponse.setData(account);
			}
			}
			else
			{
				accountResponse.setStatus(Boolean.FALSE);
				accountResponse.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);
				accountResponse.setMessage("Account Does not Exist");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			accountResponse.setStatus(Boolean.FALSE);
			accountResponse.setStatusCode(AppConstants.STATUS_SHOWFORM_ERROR);
			accountResponse.setMessage(e.getMessage());
		}
		finally{
			return accountResponse;
		}
	}


public CustomerResponse reActivateCustomer(long ssn) {
	CustomerResponse response=checkCustomer(ssn);
	CustomerResponse responseOBJ=new CustomerResponse();
	try {
	if(!response.isStatus()) {
		if(response.getData().getCust_status().equals("active")) {
		responseOBJ.setStatus(Boolean.FALSE);
		responseOBJ.setStatusCode(AppConstants.STATUS_SHOWPROMPT);
		responseOBJ.setData(response.getData());
		responseOBJ.setMessage("Cannot Reactivate already active customer");
		}else if(response.getData().getCust_status().equals("inactive")) {
			if(daoInterface.reActivateCustomer(ssn)>0) {
				responseOBJ.setStatus(Boolean.TRUE);
				responseOBJ.setStatusCode(AppConstants.STATUS_OK);
				response.setData(daoInterface.getCustomerBySSN(ssn));
				responseOBJ.setData(response.getData());
				responseOBJ.setMessage("Successfully Reactivated the Customer");
			}else {
				responseOBJ.setStatus(Boolean.FALSE);
				responseOBJ.setStatusCode(AppConstants.STATUS_SHOWPROMPT);
				responseOBJ.setData(response.getData());
				responseOBJ.setMessage("Error Reactivating the Customer,ID :"+response.getData().getCust_id());
			}
		}
	}else {
		
	}
	}catch (SQLException e) {
		e.printStackTrace();
	}
	
	return responseOBJ;
}



}