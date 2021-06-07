package com.revature.services;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.Client;
import com.revature.daos.ClientDAO;
import com.revature.daos.ClientDOAIMP;

public class ClientService {

	private ClientDAO cD = new ClientDOAIMP();
	private static final Logger logger = LogManager.getLogger(ClientService.class);
	
	public Client getClientByID(int id)
	{
		logger.trace("getClientByID Called");
		return cD.getClientByID(id);
	}
	
	public Client getClientByUserAndPass(String username, String password)
	{
		logger.trace("getClientByUserAndPass Called");
		return cD.getClientByUserAndPass(username, password);
	}
	
	public boolean addClient(Client c)
	{
		logger.trace("addClient Called");
		Client check = cD.getClientByUserAndPass(c.getUserName(), c.getPassword());
		
		if(check == null)
		{
			logger.debug("Adding Client " + c.toString());
			return cD.addClient(c);
		}
		else
		{
			logger.warn("Client could not be found in system.");
			return false;
		}
	}
	
	public boolean removeClientByUserAndPassword(String username, String password)
	{
		logger.trace("removeClientByUserAndPassword Called");
		return cD.removeClientByUserAndPassword(username, password);
	}
	
	public boolean removeClientByID(int id)
	{
		logger.trace("removeClientByID Called");
		return cD.removeClientByID(id);
	}
	
	public boolean updateClient(Client c)
	{
		logger.trace("updateClient Called");
		return cD.updateClient(c);
	}
	
	public Client getClientByUsername(String username)
	{
		logger.trace("getClientByUsername Called");
		return cD.getClientByUsername(username);
	}
	
	public ArrayList<Client> getAllClients()
	{
		logger.trace("getAllClients Called");
		return cD.getAllClients();
	}
}
