package com.ilp.tcs.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ilp.tcs.bean.Customer;
import com.ilp.tcs.responsemodel.CustomerDeleteResponse;
import com.ilp.tcs.responsemodel.CustomerRegisterResponse;
import com.ilp.tcs.responsemodel.CustomerUpdateResponse;
import com.ilp.tcs.responsemodel.StatusModel;
import com.ilp.tcs.service.ApplicationService;
import com.ilp.tcs.utils.MessageConstants;
import com.ilp.tcs.utils.Utils;


@WebServlet("/customer/*")
public class CustomerController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
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
		
		if(urlSelector.equalsIgnoreCase("delete"))
		{
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Customer customer = _gson.fromJson(payLoad, Customer.class);
			System.out.println(customer.getCust_id());
			CustomerDeleteResponse responseObj = ApplicationService.deleteCustomer(customer.getCust_id());
			Utils.sendAsJson(response, responseObj);
			return;
		}
		else if(urlSelector.equalsIgnoreCase("update"))
		{
			String payLoad = Utils.getJSON(request);
			System.out.println(payLoad);
			Gson _gson = new Gson();
			Customer customer = _gson.fromJson(payLoad, Customer.class);
		//	System.out.println(customer.getCust_id());
			CustomerUpdateResponse responseObj = ApplicationService.updateCustomer(customer);
			Utils.sendAsJson(response, responseObj);
			return;
			
		}
		else{
			StatusModel statusModel = new StatusModel();
			statusModel.setStatus(false);
			statusModel.setMessage(MessageConstants.BAD_REQUEST);
			statusModel.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			Utils.sendAsJson(response, statusModel);
			return ;
			
		}
	}

}
