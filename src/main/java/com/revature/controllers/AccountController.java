package com.revature.controllers;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.revature.bank.Account;
import com.revature.services.AccountService;

@Path("/account")
public class AccountController {

	AccountService as = new AccountService();
	
	@Path("/getAccount")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Account getAccountByID(@QueryParam("id") int id)
	{
		return as.getAccountByID(id);
	}
	
	@Path("/getAllAccountsByID")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Account> getAllClientAccounts(@QueryParam("id") int id)
	{
		return as.getAccountsByOwnerID(id);
	}
	
	@Path("/getCoAccounts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Account> getCoAccounts(@QueryParam("id") int id)
	{
		return as.getCoOwnedAccounts(id);
	}
	
	@Path("/removeAccount")
	@DELETE
	public void removeAccount(@QueryParam("id") int id)
	{
		System.out.println(as.removeAccountByID(id));
	}
	
	@Path("/withdraw")
	@POST
	public void withdraw(@QueryParam("id") int id, @QueryParam("amount") int amount)
	{
		as.withdraw(id, amount);
	}
	
	@Path("/deposit")
	@POST
	public void deposit(@QueryParam("id") int id, @QueryParam("amount") int amount)
	{
		as.deposit(id, amount);
	}
	
	@Path("/transfer")
	@POST
	public void deposit(@QueryParam("sender") int sender, @QueryParam("target") int target, @QueryParam("amount") int amount)
	{
		as.transfer(sender, target, amount);
	}
	
}
