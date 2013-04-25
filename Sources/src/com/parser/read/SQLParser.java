package com.parser.read;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.lang.ClassNotFoundException;

// import com.parser.commons.utils.Util;

public class SQLParser {

	public String host;
	public String user;
	public String pass;
	public String query;

	public SQLParser(String host, String user, String pass, String query) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.query = query;
	}
	
	public SQLParser(String host, String user, String pass) {
		this(host, user, pass, "SELECT * FROM *;");
	}
	
	private Connection con;
	
	public ResultSet parse() {
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rset = stmt.executeQuery();
			return rset;
			//TODO: Return SQLParserData with table name and other similar info ?
		}
		catch (SQLException e) {
			System.out.println("[SQL] Error while reading to DataBase !");
			do {
				System.out.println("	SQLState : " + e.getSQLState());
				System.out.println("	Description :  " + e.getMessage());
				System.out.println("	Error Code :   " + e.getErrorCode());
				System.out.println("");
				e = e.getNextException();
			} while (e != null);
			return null;
		}
	}
	
	private void initConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(host, user, pass);
		}
		catch (SQLException e) {
			System.out.println("[SQL] Error while connecting to DataBase !");
			do {
				System.out.println("	SQLState : " + e.getSQLState());
				System.out.println("	Description :  " + e.getMessage());
				System.out.println("	Error Code :   " + e.getErrorCode());
				System.out.println("");
				e = e.getNextException();
			} while (e != null);
		}
		catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		}
	}
}