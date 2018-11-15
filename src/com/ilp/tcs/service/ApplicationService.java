package com.ilp.tcs.service;

import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.ilp.tcs.bean.Customer;
import com.ilp.tcs.bean.User;
import com.ilp.tcs.dao.ApplicationDao;
import com.ilp.tcs.responsemodel.CustomerDeleteResponse;
import com.ilp.tcs.responsemodel.CustomerLoginResponse;
import com.ilp.tcs.responsemodel.CustomerRegisterResponse;
import com.ilp.tcs.responsemodel.CustomerUpdateResponse;
import com.ilp.tcs.utils.MessageConstants;

public class ApplicationService {
	public static CustomerRegisterResponse register(Customer customer){
		CustomerRegisterResponse response=new CustomerRegisterResponse();
		ApplicationDao dao=new ApplicationDao();
		try {
			if(!dao.checkCustomer(customer.getSsn())) {
				Customer custrespOBJ;
				long nextCustId=dao.getCustomerId();
				customer.setCust_id(nextCustId);
				if(dao.insertCustomer(customer)>0)
				{  
					custrespOBJ=dao.getCustomer(customer.getCust_id());
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
	
	public static CustomerLoginResponse login(long ssn,String password) {
		CustomerLoginResponse customerLoginResponse=new CustomerLoginResponse();
		ApplicationDao dao=new ApplicationDao();
		try {
			User user=dao.login(ssn, password);
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		return customerLoginResponse;
		}
	}
	
	public static CustomerDeleteResponse deleteCustomer(long cust_id)
	{
		ApplicationDao d = new ApplicationDao();
		CustomerDeleteResponse response = new CustomerDeleteResponse();
		try
		{
			System.out.println(d.isCustomer(cust_id)+"#");
			if(d.isCustomer(cust_id))
			{
				Customer temp = new Customer();
				Customer customer =  d.getCustomer(cust_id);
				temp.setCust_id(customer.getCust_id());
				temp.setCust_name(customer.getCust_name());
				if(d.deleteCustomer(cust_id) > 0)
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
	
	public static CustomerUpdateResponse updateCustomer(Customer customer)
	{
		CustomerUpdateResponse customerUpdateResponse=new CustomerUpdateResponse();
		ApplicationDao d=new ApplicationDao();
		try {
			if(d.checkCustomer(customer.getSsn()))
			{
				if(d.updateCustomer(customer)>0)
				{
					Customer cust=d.getCustomer(customer.getCust_id());
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

}
