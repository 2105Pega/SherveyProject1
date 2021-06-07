package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.Account;
import com.revature.bank.AccountStatus;
import com.revature.services.AccountService;
import com.revature.utils.ConnectionUtils;

public class AccountDAOIMP implements AccountDAO{

	private static final Logger logger = LogManager.getLogger(AccountDAOIMP.class);
	
	@Override
	public boolean addAccount(Account a) {
		logger.trace("addAccount Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into accounts (account_name, owner_id, balance, current_status) values (?, ?, ?, cast (? as status));";
			
			//System.out.println("Adding + " + a.toString() + " to DB");
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, a.getAccountName());
			statement.setInt(2, a.getOwnerID());
			statement.setFloat(3, (float) a.getBalance());
			statement.setString(4, a.getStatus().toString());
			statement.execute();
			return true;
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}

	@Override
	public boolean removeAccount(int id) {
		logger.trace("removeAccount Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "delete from co_owners where account_id =?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			statement.execute();
			
			sql = "delete from accounts where account_id =?;";
			
			statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			statement.execute();
			return true;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}

	@Override
	public boolean removeAccounts() {
		logger.trace("removeAccounts Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "delete from co_owners using accounts where " +
					"co_owners.account_id = accounts.account_id and "
					+ "(accounts.current_status = 'CANCELED' or accounts.current_status = 'DENIED');";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.execute();
			
			sql = "delete from accounts where current_status = 'CANCELED' or current_status = 'DENIED';";
			statement = conn.prepareStatement(sql);
			statement.execute();
			return true;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}

	@Override
	public Account getAccountByID(int id) {
		logger.trace("getAccountByID Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "select * from accounts where account_id = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet r = statement.executeQuery();
			r.next();
			Account a = makeAccount(r);
			return a;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}
	
	public ArrayList<Account> getCoownedAccounts(int id) {
		logger.trace("getCoownedAccounts Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "select * from accounts a inner join co_owners co on co.account_id = a.account_id where co.co_owner_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet r = statement.executeQuery();
			
			ArrayList<Account> aList = new ArrayList<Account>();
			
			while(r.next())
			{
				Account a = makeAccount(r);
				aList.add(a);
				
			}
			return aList;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public ArrayList<Account> getAccountsByCoOwnerID(int id) {
		logger.trace("getAccountsByCoOwnerID Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "select * from accounts a inner join co_owners co on co.account_id = a.account_id where co.co_owner_id = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setInt(1, id);
			
			ResultSet r = statement.executeQuery();
			
			ArrayList<Account> aList = new ArrayList<Account>();
			
			while(r.next())
			{
				Account a = makeAccount(r);
				aList.add(a);
				
			}
			return aList;
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public ArrayList<Account> getAccountsByOwnerID(int id) {
		logger.trace("getAccountsByOwnerID Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "select * from accounts where owner_id = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setInt(1, id);
			
			ResultSet r = statement.executeQuery();
			
			ArrayList<Account> aList = new ArrayList<Account>();
			
			while(r.next())
			{
				Account a = makeAccount(r);
				aList.add(a);
				
			}
			return aList;
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public ArrayList<Account> getAllAccounts() {
		logger.trace("getAllAccounts Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "select * from accounts;";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			
			ArrayList<Account> aList = new ArrayList<Account>();
			
			while(r.next())
			{
				Account a = makeAccount(r);
				aList.add(a);				
			}
			return aList;
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return null;
	}

	@Override
	public boolean withdraw(int id, double ammountChange) {
		logger.trace("withdraw Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update accounts set balance = balance - ? where account_id = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setFloat(1, (float) ammountChange);
			statement.setInt(2, id);
			statement.execute();
			
			return true;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}

	@Override
	public boolean deposit(int id, double ammountChange) {
		logger.trace("deposit Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update accounts set balance = balance + ? where account_id = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setFloat(1, (float) ammountChange);
			statement.setInt(2, id);
			statement.execute();
			
			return true;
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}

	public boolean transfer(int senderID, int targetID, double ammount) {
		logger.trace("transfer Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			
			String sql = "update accounts set balance = balance - ? where account_id = ?;";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setFloat(1, (float) ammount);
			statement.setInt(2, senderID);
			statement.execute();
			
			sql = "update accounts set balance = balance + ? where account_id = ?;";
			statement = conn.prepareStatement(sql);
			
			statement.setFloat(1, (float) ammount);
			statement.setInt(2, targetID);
			statement.execute();
			
			return true;
			
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}
	
	public boolean isCoOwned(int clientID, int accountID)
	{
		logger.trace("isCoOwned Called");
		ArrayList<Account> aL = this.getAccountsByCoOwnerID(clientID);
		
		for(Account a : aL)
		{
			if(a.getACCOUNT_ID()==accountID)
				return true;
		}
		return false;
	}
	
	private Account makeAccount(ResultSet r) throws SQLException
	{
		logger.trace("makeAccount Called");
		AccountStatus as;
		
		as = AccountStatus.valueOf(r.getString("current_status"));
		
		return new Account(r.getInt("account_id"), r.getString("account_name"), as,  (double) r.getFloat("balance"), r.getInt("owner_id"));
	}

	@Override
	public boolean addCoOwner(int aID, int coID) {
		logger.trace("addCoOwner Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "insert into co_owners(account_id, co_owner_id) values(?, ?);";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, aID);
			statement.setInt(2, coID);
			statement.execute();
			
			return true;
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}

	@Override
	public boolean updateStatus(AccountStatus s, int id) {
		logger.trace("updateStatus Called");
		try (Connection conn = ConnectionUtils.getConnection()){
			String sql = "update accounts set current_status = CAST(? AS status) where account_id = ?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, s.toString());
			statement.setInt(2, id);
			
			statement.execute();
			
			return true;
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
		return false;
	}

	
	
}
