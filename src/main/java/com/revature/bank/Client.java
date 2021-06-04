package com.revature.bank;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7421988102066457787L;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String address;
	private final int clientID;
	
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getClientID() {
		return clientID;
	}

	public Client()
	{
		super();
		this.clientID = -1;
	}
	
	public Client(String userName, String password, String firstName, String lastName, String address) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.clientID = -1;

	}
	
	public Client(int id, String userName, String password, String firstName, String lastName, String address) {
		super();
		this.clientID = id;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;

	}
	@Override
	public String toString() {
		return "Client [userName=" + userName + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", address=" + address + ", clientID=" + clientID + "]";
	}
	
	public boolean equals(Client c)
	{
		if(c.getClientID() == this.clientID)
			return true;
		else
			return false;
	}
	
}
