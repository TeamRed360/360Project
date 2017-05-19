package connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import gui.User;

import java.io.*;

/**
 * A class to handle SQL/database related operations. 
 */

public class SQL {
	
	private static final int[] INFO = {
		313,328,343,289,328,313,346,361,358,283,328,301,355,310,343
	};
	
	private static final int[] INFO2 = {
		298,313,352,301,340,313,346,361,136,328,301,346		
	};
	
	private static final int[] INFO3 = {
		313,328,343,289,328,313,346,361,358
	};
	
	private static final int[] INFO4 = {
		142,220,316,163,316,202,259,268,142,289
	};
	
	private static final int[] INFO5 = {
		334,289,343,343,355,331,340,298
	};
	
	private static final int[] INFO6 = {
		349,343,301,340
	};

	private static final Properties properties;
	
	private static User lastUser = null;
	
	static {
		properties = new Properties();
		properties.put(make(INFO6), make(INFO3));
		properties.put(make(INFO5), make(INFO4));
	}
	
	public static Connection connection = null;
	
	/**
	 * Establishes a connection with the database.
	 * Called from the main method.
	 * @throws Exception If there is an error connecting to the database.
	 */
	public static synchronized void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + make(INFO2) + "/" + make(INFO), properties);
			System.out.println("Successfully connected to the database.");
		} catch (Exception e) {
			System.out.println("Error connecting to the database: "+ e);
		}
	}
	
	/**
	 * Updates (or creates if it doesn't exist) the credentials of a user.
	 * Should be called upon any change to the user.
	 * @param theClient
	 */
	public static synchronized void updateUser(final User theClient) {
		removeFromDB(theClient);
		addToDB(theClient);
	}
	
	/**
	 * Returns the the last user who signed in.
	 * @return The last user who signed in.
	 */
	public static User getLastUser() {
		return lastUser;
	}
	
	/**
	 * Returns a list of users.
	 * @return A list of users.
	 */
	public static synchronized String getAllUsers() {
		String query = "SELECT * FROM `users`";
		Statement statement = null;
        final StringBuilder sb = new StringBuilder();
        sb.append("First Name | Last Name | Email");
		try {
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while (results.next()) {
				int id = results.getInt(1);
				String firstName = results.getString(2);
				String lastName = results.getString(3);
				String email = results.getString(4);
				String password = results.getString(5);
				User user = new User(firstName, lastName, email, password);
		        sb.append(user.getFirstName() + " | " + user.getLastName() + 
		        		" | " + user.getEmail());
		        sb.append('\n');
			}
		} catch (SQLException e) {
			System.out.println("Login error: " + e);
			return ""; //error
		}
        return sb.toString();
	}
	
	/**
	 * Attempts to log in the given client.
	 * @param theClient The user information that is attempting to log in.
	 * @return 0 if the email is not found, 1 if successful login, 2 if password doesn't match,
	 * and 3 if there is a misc login error.
	 */
	public static synchronized int login(final User theClient) {
		String query = "SELECT * FROM `users` WHERE email = \"" + theClient.getEmail() + "\"";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while (results.next()) {
				int id = results.getInt(1);
				String firstName = results.getString(2);
				String lastName = results.getString(3);
				String email = results.getString(4);
				String password = results.getString(5);
				lastUser = new User(firstName, lastName, email, password);
				if (password.equals(theClient.getPassword())) {
					return 1; //login success
				} else {
					return 2; //login failure
				}
			}
			return 0; //email not found
		} catch (SQLException e) {
			System.out.println("Login error: " + e);
			return 3; //error
		}
	}
	
	/**
	 * Saves the credentials of the given user.
	 * @param theClient The user to save.
	 */
	private static synchronized void addToDB(final User theClient) {
		try {
			connection.createStatement().execute(generateQuery(theClient));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the credentials of the user. Assumes email addresses are unique.
	 * @param theClient The user to delete.
	 */
	private static synchronized void removeFromDB(final User theClient) {
		try {
			connection.createStatement().execute(
					"DELETE FROM `users` WHERE `email` = '"
							+ theClient.getEmail() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a string from data.
	 * @param theData The data to generate from.
	 * @return A string created from the data/
	 */
	private static String make(final int[] theData) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < theData.length; i++) {
			sb.append((char)(((theData[i])+2)/3));
		}
		return sb.toString();
	}
	
	/**
	 * Generates an SQL query based on the credentials of the user.
	 * @param theClient The user to save.
	 */
	private static String generateQuery(final User theClient) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO users (");
		sb.append("first_name, ");
		sb.append("last_name, ");
		sb.append("email, ");
		sb.append("password) ");
		sb.append("VALUES (");
		sb.append("'" + theClient.getFirstName() +"',");
		sb.append("'" + theClient.getLastName() +"',");
		sb.append("'" + theClient.getEmail() +"',");
		sb.append("'" + theClient.getPassword() +"')");
		return sb.toString();
	}
}