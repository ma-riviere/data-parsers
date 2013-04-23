package com.parser.read;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.parser.commons.utils.Util;

public abstract class SQLParser {

	public String host;
	public String user;
	public String pass;

	public SQLParser(String host, String user, String pass) {
		this.host = host;
		this.user = user;
		this.pass = pass;
	}
	
	// public ResultSet parse() {
	
	// }
}