package com.ilp.tcs.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ilp.tcs.bean.Account;
import com.ilp.tcs.bean.Customer;
import com.ilp.tcs.dao.ApplicationDao;
import com.ilp.tcs.responsemodel.AccountResponse;
import com.ilp.tcs.responsemodel.AccountViewResponse;
import com.ilp.tcs.responsemodel.CustomerResponse;
import com.ilp.tcs.responsemodel.StatusModel;
import com.ilp.tcs.service.ApplicationService;
import com.ilp.tcs.utils.MessageConstants;
import com.ilp.tcs.utils.Utils;


@WebServlet("/account/*")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
  	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        ApplicationDao dao = new ApplicationDao();
		
		ApplicationService service = new ApplicationService(dao);
		String pathInfo = request.getPathInfo();
		
		if(pathInfo == null || pathInfo.equals("/"))
		{
			StatusModel statusModel = new StatusModel();
			statusModel.setStatus(false);
			statusModel.setMessage(MessageConstants.WRONG_URL);
			statusModel.setStatusCode(404);
			
			Utils.sendAsJson(response, statusModel);
			return ;
		}
		
		String split[] = pathInfo.split("/");
		
		if(split.length!=2)
		{
			StatusModel statusModel = new StatusModel();
			statusModel.setStatus(false);
			statusModel.setMessage(MessageConstants.BAD_REQUEST);
			statusModel.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			
			Utils.sendAsJson(response, statusModel);
			return ;
		}
		
		String urlSelector = split[1];
		try {
		
		//Start of create account API
		//Create account has 3 API calls 
		//1.To check customer DNE or Exist or inactive state
		//2.To check max number of accounts
		//3.To simply create account
		if(urlSelector.equalsIgnoreCase("checkaccount")){
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Account account = _gson.fromJson(payLoad, Account.class);
			AccountResponse responseObj=service.checkAccount(account.getCust_id(),account.getAcct_type());
			Utils.sendAsJson(response, responseObj);
			return;
		}else if(urlSelector.equalsIgnoreCase("checkcustomer")){
            String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Customer customer = _gson.fromJson(payLoad, Customer.class);
			CustomerResponse responseObj = service.checkCustomerAccount(customer.getCust_id());
			Utils.sendAsJson(response, responseObj);
			return;			
		}
		else if(urlSelector.equalsIgnoreCase("createaccount")){
            String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Account account = _gson.fromJson(payLoad, Account.class);
			AccountResponse responseObj = service.createAccount(account);
			Utils.sendAsJson(response, responseObj);
			return;			
		}
		//End of Create account API
		
		
		//Start of delete account API
		//1.Check Account for deletion
		//2.If exist with given type delete it
		else if(urlSelector.equalsIgnoreCase("checkdelete")){
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Account account = _gson.fromJson(payLoad, Account.class);
			AccountResponse responseObj=service.checkAccountDelete(account.getAcct_id());
			Utils.sendAsJson(response, responseObj);
			return;
		}else if(urlSelector.equalsIgnoreCase("deleteaccount")){
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Account account = _gson.fromJson(payLoad, Account.class);
			AccountResponse responseObj=service.deleteAccount(account.getAcct_id(),account.getAcct_type());
			Utils.sendAsJson(response, responseObj);
			return;
		}
		
		//end of Delete Account API
		
		//Start of Account search API 
		//1.Account By Account ID
		//2.Account By Customer ID
		else if(urlSelector.equalsIgnoreCase("viewbyacctid")){
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Account account = _gson.fromJson(payLoad, Account.class);
			AccountResponse accountResponseOBJ=service.viewAccountDetails(account.getAcct_id());
			Utils.sendAsJson(response, accountResponseOBJ);
			return;
		}
		else if(urlSelector.equalsIgnoreCase("viewbycustid")){
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Account account = _gson.fromJson(payLoad, Account.class);
			AccountViewResponse accountViewResponseOBJ=service.getAcctDetails(account.getCust_id());
			Utils.sendAsJson(response, accountViewResponseOBJ);
			return;
		}
		//End of search account API
		
		//start of view all accounts API
		else if(urlSelector.equalsIgnoreCase("viewallaccounts")){
			AccountViewResponse accountViewResponseOBJ=service.viewAccount();
			Utils.sendAsJson(response, accountViewResponseOBJ);
			return;
			}
		// end of view all accounts API
		
		
		
		else{
			StatusModel statusModel = new StatusModel();
			statusModel.setStatus(false);
			statusModel.setMessage(MessageConstants.BAD_REQUEST);
			statusModel.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			Utils.sendAsJson(response, statusModel);
			return ;	
		}
	}
		catch (Exception e) {
			e.printStackTrace();
			StatusModel statusModel=new StatusModel();
            statusModel.setStatus(false);
            statusModel.setMessage("Some Error Occured");
            statusModel.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
            Utils.sendAsJson(response, statusModel);
			return;
		}
		

	}

}
