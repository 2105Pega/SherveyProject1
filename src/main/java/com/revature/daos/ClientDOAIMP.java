package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.utils.ConnectionUtils;
import com.revature.bank.Client;

public class ClientDOAIMP implements  ClientDAO {

	private static final Logger logger = LogManager.getLogger(ClientDOAIMP.class);
	
	@Override
	public Client getClientByID(int id) {
		logger.trace("getClientByID Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "select * from clients where client_id = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);

			ResultSet r = statement.executeQuery();

			if(r.next())
			{
				return makeClient(r);
			}
			
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return null;
	}

	public Client getClientByUsername(String username)
	{
		logger.trace("getClientByUsername Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "select * from clients where username = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet r = statement.executeQuery();

			if(r.next())
			{
				return makeClient(r);
			}

			
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return null;
	}
	
	
	@Override
	public Client getClientByUserAndPass(String username, String password) {
		logger.trace("getClientByUserAndPass Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "select * from clients where username = ?and password = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			
			ResultSet r = statement.executeQuery();

			if(r.next())
			{
				return makeClient(r);
			}

			
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return null;
	}

	@Override
	public boolean addClient(Client c) {
		logger.trace("addClient Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into clients(username, password, firstname, lastname, address) "
					+ "values (?, ?, ?, ?, ?);"; //REMEMBER SMART ONE, INDEX STARTS AT 1 FOR SQL!!!
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, c.getUserName());
			statement.setString(2, c.getPassword());
			statement.setString(3, c.getFirstName());
			statement.setString(4, c.getLastName());
			statement.setString(5, c.getAddress());
			
			
			statement.execute();
			return true;
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return false;
	}

	@Override
	public boolean removeClientByID(int id) {
		logger.trace("removeClientByID Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "delete from clients where client_id =?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			
			statement.execute();
			
			return true;
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return false;
	}
	
	@Override
	public boolean removeClientByUserAndPassword(String username, String password) {
		logger.trace("removeClientByUserAndPassword Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "delete from clients where username = ? and password = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1,username);
			statement.setString(2, password);
			
			statement.execute();
			return true;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return false;
	}

	@Override
	public boolean updateClient(Client c) {
		logger.trace("updateClient Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update clients set username = ?, password = ?, firstname = ?, "
					+ "lastname = ?, address = ? where client_id = ?; ";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, c.getUserName());
			statement.setString(2, c.getPassword());
			statement.setString(3, c.getFirstName());
			statement.setString(4, c.getLastName());
			statement.setString(5, c.getAddress());
			statement.setInt(6, c.getClientID());
			
			statement.execute();
			return true;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return false;
	}

	private Client makeClient(ResultSet r) {
		logger.trace("makeClient Called");
		try {
			return new Client(r.getInt("client_id"), r.getString("username"), r.getString("password"), 
					r.getString("firstname"), r.getString("lastname"), r.getString("address"));
		} catch (SQLException e) {
			logger.error(e.getStackTrace());
		}
		return null;
	}

	@Override
	public ArrayList<Client> getAllClients() {
		logger.trace("getAllClients Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "select * from clients;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			ResultSet r = statement.executeQuery();
			ArrayList<Client> cList = new ArrayList<Client>();
			while(r.next())
			{
				
				cList.add(this.makeClient(r));
			}

			return cList;
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return null;
	}
	
	

}
