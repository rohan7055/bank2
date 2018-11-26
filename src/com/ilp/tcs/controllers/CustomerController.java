package com.ilp.tcs.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ilp.tcs.bean.Customer;
import com.ilp.tcs.dao.ApplicationDao;
import com.ilp.tcs.responsemodel.CustomerResponse;
import com.ilp.tcs.responsemodel.CustomerViewResponse;
import com.ilp.tcs.responsemodel.StatusModel;
import com.ilp.tcs.service.ApplicationService;
import com.ilp.tcs.utils.MessageConstants;
import com.ilp.tcs.utils.Utils;


@WebServlet("/customer/*")
public class CustomerController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if(urlSelector.equalsIgnoreCase("viewall")){
			//CustomerViewResponse customerViewResponse=ApplicationService.viewCustomers();
			//Utils.sendAsJson(response, customerViewResponse);
			return;
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
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
		//Start of Customer Delete API
		//These 2 APIs are used to delete(soft delete) the customer from db
		//1.To check the customer existence and inactive status
		//2.To soft delete  a customer
		
		if(urlSelector.equalsIgnoreCase("checkdeletecustomer")){
			String payLoad = Utils.getJSONAngular(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Customer customer = _gson.fromJson(payLoad, Customer.class);
			System.out.println(customer.getCust_id());
			CustomerResponse responseObj=service.checkCustomerDelete(customer.getCust_id());
			Utils.sendAsJson(response, responseObj);
			return;
		}
		else if(urlSelector.equalsIgnoreCase("delete"))
		{
			String payLoad = Utils.getJSONAngular(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Customer customer = _gson.fromJson(payLoad, Customer.class);
			System.out.println(customer.getCust_id());
			CustomerResponse responseObj = service.deleteCustomer(customer.getCust_id());
			Utils.sendAsJson(response, responseObj);
			return;
		} 
		
		//end of Customer delete API
		
		//start of Update Customer API
		//These 2 APIs are used to update an existing customer
		//1.To check the customer for inactive state 
		//2.To update the customer
		else if(urlSelector.equalsIgnoreCase("checkupdatecustomer")){
            String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Customer customer = _gson.fromJson(payLoad, Customer.class);
			CustomerResponse responseObj = service.checkCustomerUpdate(customer.getSsn());
			Utils.sendAsJson(response, responseObj);
			return;			
		}
		else if(urlSelector.equalsIgnoreCase("updatecustomer"))
		{
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Customer customer = _gson.fromJson(payLoad, Customer.class);
		//	System.out.println(customer.getCust_id());
			CustomerResponse responseObj = service.updateCustomer(customer);
			Utils.sendAsJson(response, responseObj);
			return;
			
		}
		//end of Update Customer API

		//start of View ALL  Customer API
		
		else if(urlSelector.equalsIgnoreCase("viewall")){
			CustomerViewResponse customerViewResponse=service.viewAllCustomers();
			Utils.sendAsJson(response, customerViewResponse);
			return;
		}
		
		//end of View ALL  Customer API
		
		//Start of Create Customer API
		//api used for checking customer status before registering the customer
		//2 APIS are created first to check ssn id then to register the customer if
		//all conditions are satisfied
				else if(urlSelector.equalsIgnoreCase("checkcustomerssn")){
					System.out.println("CHECK SSN");
					String payload=Utils.getJSONAngular(request);
					Gson gson=new Gson();
					Customer customer=gson.fromJson(payload, Customer.class);
					StatusModel checkCustomerRespOBJ=service.checkCustomer(customer.getSsn());
					Utils.sendAsJson(response, checkCustomerRespOBJ);
					return;
				}
		else if(urlSelector.equalsIgnoreCase("createcustomer")){
			String payload=Utils.getJSONAngular(request);
			Gson gson=new Gson();
			Customer customer=gson.fromJson(payload, Customer.class);
			CustomerResponse customerResponseOBJ=service.register(customer);
			Utils.sendAsJson(response, customerResponseOBJ);
		return;
		}
		

		
		//End of Create Customer API
		
		
		//start of search a customer API
		//1. customer search by customer Id
		//2. customer search by SSN id
		else if(urlSelector.equalsIgnoreCase("getcustomerbycustId")){
			String payload=Utils.getJSONAngular(request);
			Gson gson=new Gson();
			Customer customer=gson.fromJson(payload, Customer.class);
			CustomerResponse customerResponseOBJ=service.searchCustomer(customer.getCust_id());
			Utils.sendAsJson(response, customerResponseOBJ);
		return;
		}
		else if(urlSelector.equalsIgnoreCase("getcustomerbySSN")){
			String payload=Utils.getJSONAngular(request);
			Gson gson=new Gson();
			Customer customer=gson.fromJson(payload, Customer.class);
			CustomerResponse customerResponseOBJ=service.searchCustomerBySSN(customer.getSsn());
			Utils.sendAsJson(response, customerResponseOBJ);
		return;
		}
		
		//end of customer search API		
		
		//reactivate API
				else if(urlSelector.equalsIgnoreCase("reactivatecustomer")){
					String payload=Utils.getJSONAngular(request);
					Gson gson=new Gson();
					Customer customer=gson.fromJson(payload, Customer.class);
					CustomerResponse customerResponseOBJ=service.reActivateCustomer(customer.getSsn());
					Utils.sendAsJson(response, customerResponseOBJ);
				    return;
				}
		

		//This block to handle any invalid  urlSelector
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