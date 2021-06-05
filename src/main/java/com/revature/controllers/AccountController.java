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
	
	@Path("/makeAccount")
	@POST
	public void makeAccount(@QueryParam("accountName") String name, @QueryParam("id") int id)
	{
		Account a = new Account(name, id);
		as.addAccount(a);
	}
	
	@Path("/getAllAccountsByID")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Account> getAllClientAccountsByID(@QueryParam("id") int id)
	{
		return as.getAccountsByOwnerID(id);
	}
	
	@Path("/getAllAccounts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Account> getAllClientAccountsByID()
	{
		return as.getAllAccounts();
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
	public void removeAccount(@QueryParam("id") int id, @QueryParam("clientid") int cID)
	{
		if(as.getAccountByID(id).getOwnerID() == cID)
		{
			as.removeAccountByID(id);
		}
	}
	
	@Path("/withdraw")
	@POST
	public void withdraw(@QueryParam("id") int id, @QueryParam("amount") int amount,  @QueryParam("cID") int cID)
	{
		
		boolean isCo = as.isCoOwned(cID, id);
		boolean isOwned = as.getAccountByID(id).getOwnerID() == cID;
		
		System.out.println("cID: " + cID + " ID: " + id + " isCo: " + isCo + " isOwned: " + isOwned);
		
		if(as.isCoOwned(cID, id) || as.getAccountByID(id).getOwnerID() == cID)
		{
			as.withdraw(id, amount);
		}
		
		
	}
	
	@Path("/deposit")
	@POST
	public void deposit(@QueryParam("id") int id, @QueryParam("amount") int amount, @QueryParam("cID") int cID)
	{
		
		if(as.isCoOwned(cID, id) || as.getAccountByID(id).getOwnerID() == cID)
		{
			as.deposit(id, amount);
		}
		
	}
	
	@Path("/transfer")
	@POST
	public void transfer(@QueryParam("sender") int sender, @QueryParam("target") int target, @QueryParam("amount") int amount, @QueryParam("cID") int cID)
	{
		if(as.isCoOwned(cID, sender) || as.getAccountByID(sender).getOwnerID() == cID)
		{
			as.transfer(sender, target, amount);
		}
		
	}
	
}
