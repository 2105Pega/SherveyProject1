package com.revature.controllers;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.Account;
import com.revature.bank.Client;
import com.revature.services.AccountService;
import com.revature.services.ClientService;
import com.revature.utils.ConnectionUtils;
import com.revature.utils.LogInPack;

@Path("/client")
public class ClientController {

	ClientService cs = new ClientService();
	private static final Logger logger = LogManager.getLogger(ClientController.class);
	
	@Path("/hello")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() 
	{
		return "Hello";
	}
	
	@Path("/user")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Client getUserID(@QueryParam("id") int id) 
	{
		logger.trace("Get User By ID Request Recieved.");
		return cs.getClientByID(id);

	}
	
	@Path("/userByUP")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Client getUserUserAndPass(LogInPack lp) 
	{
		logger.trace("Get User By Username and Password Request Recieved.");
		return cs.getClientByUserAndPass(lp.getLiusername(), lp.getLipassword());
	}
	
	@Path("/makeUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON) //How we read JSON in request
	public void postUser(Client newUser)
	{
		logger.trace("Create User Request Recieved.");
		cs.addClient(newUser);
	}
	
	@Path("/removeUserByID")
	@DELETE
	public void removeUserByID(@QueryParam("id") int id)
	{
		logger.trace("Remove User By ID Request Recieved.");
		try {
		AccountService as = new AccountService();
		
		ArrayList<Account> al = as.getAccountsByOwnerID(id);
		
		for(Account a : al)
		{
			as.removeAccountByID(a.getACCOUNT_ID());
		}
		
		logger.info("Removed: " + cs.removeClientByID(id));
		} catch (Exception e){
			logger.error(e.getStackTrace().toString());
		}
	}
	
	@Path("/UpdateUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON) //How we read JSON in request
	public void updateUser(Client c)
	{
		logger.trace("Update User Request Recieved.");
		logger.info("Updated: " + cs.updateClient(c));
	}
	
	@Path("/getAllClients")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Client> getAllUsers()
	{
		logger.trace("Get All Users Request Recieved.");
		ArrayList<Client> list = cs.getAllClients();
		
		logger.info(list.toString());
		
		return list;
	}
	
}
