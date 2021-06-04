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

import com.revature.bank.Client;
import com.revature.services.ClientService;

@Path("/client")
public class ClientController {

	ClientService cs = new ClientService();

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
		
		return cs.getClientByID(id);

	}
	
	@Path("/userByUP")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Client getUserUserAndPass(@QueryParam("username") String username, @QueryParam("password") String password) 
	{
		
		return cs.getClientByUserAndPass(username, password);

	}
	
	@Path("/makeUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON) //How we read JSON in request
	public void postUser(Client newUser)
	{
		System.out.println(newUser.toString());
		System.out.println("Client Added: " + cs.addClient(newUser));
	}
	
	@Path("/removeUserByID")
	@DELETE
	public void removeUserByID(@QueryParam("id") int id)
	{
		System.out.println("Removed: " + cs.removeClientByID(id));
	}
	
	@Path("/UpdateUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON) //How we read JSON in request
	public void updateUser(Client c)
	{
		System.out.println("Updated: " + cs.updateClient(c));
	}
	
	@Path("/GetAllClients")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Client> getAllUsers()
	{
		ArrayList<Client> list = cs.getAllClients();
		
		System.out.println(list.toString());
		
		return list;
	}
	
}
