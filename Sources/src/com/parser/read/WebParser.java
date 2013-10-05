package com.parser.read;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WebParser {

	URL url = null;
	HttpURLConnection conn = null;
	
	public WebParser(String _url) {
		this.url = new URL(_url);
		this.conn = (HttpURLConnection) url.openConnection();
	}
	
	public String getValue(String keyword) throws IOException {
		String value = null;
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while (rd.readLine() != null) {
			if (rd.readLine().toUpperCase().contains(">" + keyword.toUpperCase() + "<")) {
				Pattern VALUE_PATTERN = Pattern.compile(">\\S+<");
				Matcher m = VALUE_PATTERN.matcher(rd.readLine());
				while (m.find()) {
					value = m.group(1);
				}
				break;
			}
        }
		// System.out.println(value);
		return value;
	}
}
