package com.revature.controllers;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.Account;
import com.revature.bank.AccountStatus;
import com.revature.services.AccountService;

@Path("/account")
public class AccountController {

	AccountService as = new AccountService();
	private static final Logger logger = LogManager.getLogger(AccountController.class);
	
	@Path("/getAccount")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Account getAccountByID(@QueryParam("id") int id)
	{
		logger.trace("Get Account By ID Request Recieved.");
		return as.getAccountByID(id);
	}
	
	@Path("/makeAccount")
	@POST
	public void makeAccount(@QueryParam("accountName") String name, @QueryParam("id") int id)
	{
		logger.trace("Get Account By Name and ID Request Recieved.");
		Account a = new Account(name, id);
		as.addAccount(a);
	}
	
	@Path("/getAllAccountsByID")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Account> getAllClientAccountsByID(@QueryParam("id") int id)
	{
		logger.trace("Get All Accounts By ID Request Recieved.");
		return as.getAccountsByOwnerID(id);
	}
	
	@Path("/getAllAccounts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Account> getAllClientAccountsByID()
	{
		logger.trace("Get All Accounts Request Recieved.");
		return as.getAllAccounts();
	}
	
	@Path("/getCoAccounts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Account> getCoAccounts(@QueryParam("id") int id)
	{
		logger.trace("Get CoOwned Accounts By ID Request Recieved.");
		return as.getCoOwnedAccounts(id);
	}
	
	@Path("/removeAccount")
	@DELETE
	public void removeAccount(@QueryParam("id") int id, @QueryParam("clientid") int cID)
	{
		logger.trace("Remove Account By ID Request Recieved.");
		if(as.getAccountByID(id).getOwnerID() == cID)
		{
			as.removeAccountByID(id);
		}
	}
	
	@Path("/withdraw")
	@POST
	public void withdraw(@QueryParam("id") int id, @QueryParam("amount") double amount,  @QueryParam("cID") int cID)
	{
		logger.trace("Withdraw Request Recieved.");
		
		if(as.isCoOwned(cID, id) || as.getAccountByID(id).getOwnerID() == cID)
		{
			as.withdraw(id, amount);
		}
		
		
	}
	
	@Path("/deposit")
	@POST
	public void deposit(@QueryParam("id") int id, @QueryParam("amount") double amount, @QueryParam("cID") int cID)
	{
		logger.trace("Deposit Request Recieved.");
		if(as.isCoOwned(cID, id) || as.getAccountByID(id).getOwnerID() == cID)
		{
			as.deposit(id, amount);
		}
		
	}
	
	@Path("/transfer")
	@POST
	public void transfer(@QueryParam("sender") int sender, @QueryParam("target") int target, @QueryParam("amount") double amount, @QueryParam("cID") int cID)
	{
		logger.trace("Transfer Request Recieved.");
		
		if(as.isCoOwned(cID, sender) || as.getAccountByID(sender).getOwnerID() == cID)
		{
			as.transfer(sender, target, amount);
		}
		
	}
	
	@Path("/changeStatus")
	@POST
	public void changeStatus (@QueryParam("id") int id, @QueryParam("status") String status)
	{
		logger.trace("Change Status of Account Request Recieved.");
		Account a = as.getAccountByID(id);
		status = status.toUpperCase();
		
		if(a != null)
		{	
			as.updateStatus(AccountStatus.valueOf(status), id);
			System.out.println(a.toString());

		}
	}
	
	@Path("/addCoSign")
	@POST
	public void addCoSign(@QueryParam("aID") int aID, @QueryParam("cID") int cID, @QueryParam("coID") int coID)
	{
		logger.trace("Add Co-Signer Request Recieved.");
		if(as.getAccountByID(aID).getOwnerID() == cID)
		{	
			as.addCoOwner(aID, coID);
		}
	}
}
