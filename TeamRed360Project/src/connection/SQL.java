package connection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import model.Item;
import model.Project;
import model.User;

/**
 * A class to handle SQL/database related operations.
 */
public class SQL {

	private static final int[] INFO = { 313, 328, 343, 289, 328, 313, 346, 361, 358, 283, 328, 301, 355, 310, 343 };

	private static final int[] INFO2 = { 298, 313, 352, 301, 340, 313, 346, 361, 136, 328, 301, 346 };

	private static final int[] INFO3 = { 313, 328, 343, 289, 328, 313, 346, 361, 358 };

	private static final int[] INFO4 = { 142, 220, 316, 163, 316, 202, 259, 268, 142, 289 };

	private static final int[] INFO5 = { 334, 289, 343, 343, 355, 331, 340, 298 };

	private static final int[] INFO6 = { 349, 343, 301, 340 };

	private static final Properties properties;

	private static User lastUser = null;

	static {
		properties = new Properties();
		properties.put(make(INFO6), make(INFO3));
		properties.put(make(INFO5), make(INFO4));
	}

	public static Connection connection = null;

	public static boolean isOnline() {
		return connection != null;
	}

	/**
	 * Establishes a connection with the database. Called from the main method.
	 * 
	 * @throws Exception
	 *             If there is an error connecting to the database.
	 */
	public static synchronized void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + make(INFO2) + "/" + make(INFO), properties);
			System.out.println("Successfully connected to the database.");
		} catch (Exception e) {
			System.out.println("Error connecting to the database: " + e);
			connection = null;
		}
	}

	/**
	 * Disconnects from the database.
	 */
	public static synchronized void disconnect() {
		connection = null;
		System.out.println("Disconnected from the database.");
	}

	/**
	 * Cleans the string to prevent SQL injections.
	 * 
	 * @return A cleaned version of the string.
	 */
	public static String clean(final String theString) {
		String toReturn = theString;
		toReturn = toReturn.replaceAll("\"", "");
		toReturn = toReturn.replaceAll("'", "");
		toReturn = toReturn.replaceAll("`", "");
		return toReturn;
	}

	/**
	 * Constructs and returns a list of all projects from the user.
	 * 
	 * @param theClient
	 *            The user to get the projects from
	 * @return A list of that user's projects
	 */
	public static ArrayList<Project> getProjects(final User theClient) {
		ArrayList<Project> toReturn = new ArrayList<Project>();
		String query = "SELECT * FROM `projects` WHERE `user_id` = '" + theClient.getId() + "'";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while (results.next()) { // loop through all the projects
				int projectId = results.getInt(1);
				int userId = results.getInt(2);
				String name = results.getString(3);
				String description = results.getString(4);
				Project newProject = new Project(userId, name, description);
				newProject.setId(projectId);
				String subQuery = "SELECT * FROM `items` WHERE `project_id` = '" + projectId + "'";
				Statement subStatement = connection.createStatement();
				ResultSet subResults = subStatement.executeQuery(subQuery);
				while (subResults.next()) { // loop through all the items
					int itemId = subResults.getInt(1);
					String itemName = subResults.getString(3);
					int price = subResults.getInt(4); // price is stored in
														// cents
					int quantity = subResults.getInt(5);
					Item newItem = new Item(itemName, (double) price / 100.0, quantity);
					newItem.setProjectId(projectId);
					newItem.setId(itemId);
					newProject.add(newItem);
				}
				toReturn.add(newProject);
			}
			return toReturn;
		} catch (SQLException e) {
			System.out.println("Get project error: " + e);
			disconnect();
			return toReturn;
		}
	}

	/**
	 * Updates (or creates if it doesn't exist) the credentials of a user in the
	 * database. Should be called upon any change to the user.
	 * 
	 * @param theClient
	 *            The user to be added.
	 * @return The new user id.
	 */
	public static synchronized int updateUser(final User theClient) {
		removeFromDB(theClient);
		int oldId = theClient.getId();
		int newId = addToDB(theClient);
		repairProjects(oldId, newId);
		return newId;
	}

	/**
	 * Updates (or creates if it doesn't exist) the credentials of a user in the
	 * database. Should be called upon any change to the user.
	 * 
	 * @param theClient
	 *            The user to be added.
	 * @return The new user id.
	 */
	public static synchronized int updateUser(User theClient, String theEmail) {
		removeFromDB(theClient);
		theClient.setEmail(theEmail);
		int oldId = theClient.getId();
		int newId = addToDB(theClient);
		repairProjects(oldId, newId);
		return newId;
	}

	/**
	 * Updates (or creates if it doesn't exist) a project in the database.
	 * Should be called upon any change to a project's properties (items not
	 * included).
	 * 
	 * @param theProject
	 *            The project to be added.
	 * @return The new id of the project.
	 */
	public static synchronized int updateProject(final Project theProject) {
		if (theProject.getId() == -1) { // it doesn't exist
			return createProject(theProject).getId();
		} else { // remove, and re-add to update
			removeFromDB(theProject);
			int oldId = theProject.getId();
			int newId = addToDB(theProject);
			repairItems(oldId, newId);
			return newId;
		}
	}

	/**
	 * Updates (or creates if it doesn't exist) a single item in the database.
	 * Should be called upon any change to an item's properties.
	 * 
	 * @param theProject
	 *            The project attached to the item, to get its id
	 * @param theItem
	 *            The item to be added
	 */
	public static synchronized void updateItem(final Project theProject, final Item theItem) {
		try {
			if (theItem.getId() == -1) { // it doesn't exist
				createItem(theProject.getId(), theItem);
			} else { // remove, and re-add to update
				removeFromDB(theItem);
				addToDB(theItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Deletes the project, and any items associated with it.
	 */
	public static synchronized void deleteProject(final int projectId) {
		try {
			connection.createStatement().execute("DELETE FROM `projects` WHERE `id` = '" + projectId + "'");
			connection.createStatement().execute("DELETE FROM `items` WHERE `project_id` = '" + projectId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Returns the the last user who signed in.
	 * 
	 * @return The last user who signed in.
	 */
	public static User getLastUser() {
		return lastUser;
	}

	/**
	 * Returns a list of users.
	 * 
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
				String firstName = results.getString(2);
				String lastName = results.getString(3);
				String email = results.getString(4);
				String password = results.getString(5);
				User user = new User(firstName, lastName, email, password);
				sb.append(user.getFirstName() + " | " + user.getLastName() + " | " + user.getEmail());
				sb.append('\n');
			}
		} catch (SQLException e) {
			System.out.println("Login error: " + e);
			disconnect();
			return ""; // error
		}
		return sb.toString();
	}

	/**
	 * Checks whether the given email address exists in the DB.
	 * 
	 * @param theEmail
	 *            The email to check.
	 * @return True if the email exists in the DB, false otherwise.
	 */
	public static synchronized boolean emailExists(final String theEmail) {
		String query = "SELECT * FROM `users` WHERE email = \"" + theEmail + "\"";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			if (results.next()) {
				System.out.println("email already found");
				return true;
			}
			return false;
		} catch (SQLException e) {
			System.out.println("Existing email check error: " + e);
			disconnect();
			return false; // error
		}
	}

	/**
	 * Attempts to log in the given client.
	 * 
	 * @param theClient
	 *            The user information that is attempting to log in.
	 * @return 0 if the email is not found, 1 if successful login, 2 if password
	 *         doesn't match, and 3 if there is a misc login error.
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
				lastUser.setId(id);
				if (password.equals(theClient.getPassword())) {
					return 1; // login success
				} else {
					return 2; // login failure
				}
			}
			return 0; // email not found
		} catch (Exception e) {
			System.out.println("Login error: " + e);
			disconnect();
			return 3; // error
		}
	}

	/**
	 * Attempts to log in the given client.
	 * 
	 * @param theClient
	 *            The user information that is attempting to log in.
	 * @return 0 if the email is not found, 1 if successful login, 2 if password
	 *         doesn't match, and 3 if there is a misc login error.
	 */
	public static synchronized User getUser(final User theClient) {
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
				User returnuser = new User(firstName, lastName, email, password);
				returnuser.setId(id);
				return returnuser;
			}
			return null; // email not found
		} catch (SQLException e) {
			System.out.println("get user error: " + e);
			disconnect();
			return null; // error
		}
	}

	/**
	 * Attempts to create the given project. Should only be called when a
	 * project is first created.
	 * 
	 * @param theProject
	 *            The project to be created.
	 * @return An updated project reference with ids on the project and its
	 *         items if successful, null otherwise
	 */
	public static synchronized Project createProject(final Project theProject) {
		Project toReturn = new Project(-1, theProject.getName(), theProject.getDesc());
		Set<Item> contents = new TreeSet<Item>();
		String query = "SELECT * FROM `projects` ORDER BY id DESC";
		Statement statement = null;
		Item newItem, currentItem;
		int projectId;
		try {
			addToDB(theProject); // add the project to db
			statement = connection.createStatement(); // now get the project's
														// ID
			ResultSet results = statement.executeQuery(query);
			results.next();
			projectId = results.getInt(1);
			toReturn.setId(projectId);
			for (int i = 0; i < theProject.getListOfItems().length; i++) {
				currentItem = theProject.getListOfItems()[i];
				newItem = createItem(projectId, currentItem);
				contents.add(newItem);
			}
			toReturn.setListOfItems(contents);
			return toReturn;
		} catch (SQLException e) {
			System.out.println("Create project error " + e);
			disconnect();
			return null; // error
		}
	}

	/**
	 * Attempts to create the given item. Should only be called when an item is
	 * first created.
	 * 
	 * @param theProjectId
	 *            The project id the item is associated with.
	 * @param theItem
	 *            The item to be created.
	 * @return An updated item reference with its database id if successful,
	 *         null otherwise
	 */
	public static synchronized Item createItem(int theProjectId, final Item theItem) {
		String query;
		Statement statement = null;
		Item newItem;
		try {
			query = "SELECT * FROM `items` ORDER BY id DESC";
			addToDB(theProjectId, theItem); // add the item to db
			statement = connection.createStatement(); // now get the item's ID
			ResultSet results = statement.executeQuery(query);
			results.next();
			newItem = new Item(theItem);
			newItem.setId(results.getInt(1));
			newItem.setProjectId(theProjectId);
			return newItem;
		} catch (Exception e) {
			System.out.println("Create item error " + e);
			disconnect();
			return null; // error
		}
	}

	/**
	 * Repairs the projects when the user id is updated.
	 * 
	 * @param oldUserId
	 *            The old user id.
	 * @param newUserId
	 *            The new user id.
	 */
	private static synchronized void repairProjects(final int oldUserId, final int newUserId) {
		try {
			String query = "SELECT * FROM `projects` WHERE `user_id` = '" + oldUserId + "'";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while (results.next()) {
				int oldProjectId = results.getInt(1); // we'll need this to
														// update the items
				String name = results.getString(3);
				String description = results.getString(4);
				Project newProject = new Project(newUserId, name, description);
				int newProjectId = addToDB(newProject);
				repairItems(oldProjectId, newProjectId);
			}
			connection.createStatement().execute("DELETE FROM `projects` WHERE `user_id` = '" + oldUserId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Repairs the items when the project id is updated.
	 * 
	 * @param oldProjectId
	 *            The old id.
	 * @param newProjectId
	 *            The new project id.
	 */
	private static synchronized void repairItems(final int oldProjectId, final int newProjectId) {
		try {
			String query = "SELECT * FROM `items` WHERE `project_id` = '" + oldProjectId + "'";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while (results.next()) {
				String name = results.getString(3);
				int price = results.getInt(4);
				int quantity = results.getInt(5);
				Item newItem = new Item(name, (double) price, quantity);
				newItem.setProjectId(newProjectId);
				addToDB(newItem);
			}
			connection.createStatement().execute("DELETE FROM `items` WHERE `project_id` = '" + oldProjectId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Saves the credentials of the given user.
	 * 
	 * @param theClient
	 *            The user to save.
	 * @return The new id of the user.
	 */
	private static synchronized int addToDB(final User theClient) {
		try {
			connection.createStatement().execute(generateQuery(theClient));
			String query = "SELECT * FROM `users` ORDER BY id DESC";
			Statement statement = connection.createStatement(); // now get the
																// item's ID
			ResultSet results = statement.executeQuery(query);
			results.next();
			return results.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
			return -1;
		}
	}

	/**
	 * Deletes the credentials of the user. Assumes email addresses are unique.
	 * 
	 * @param theClient
	 *            The user to delete.
	 */
	private static synchronized void removeFromDB(final User theClient) {
		try {
			connection.createStatement().execute("DELETE FROM `users` WHERE `email` = '" + theClient.getEmail() + "'");
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Saves the project.
	 * 
	 * @param theProject
	 *            The project to save.
	 * @return The new id of the project.
	 */
	private static synchronized int addToDB(final Project theProject) {
		try {
			connection.createStatement().execute(generateQuery(theProject));
			String query = "SELECT * FROM `projects` ORDER BY id DESC";
			Statement statement = connection.createStatement(); // now get the
																// item's ID
			ResultSet results = statement.executeQuery(query);
			results.next();
			return results.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
			return -1;
		}
	}

	/**
	 * Deletes the project. Assumes the ID of the project is properly set.
	 * 
	 * @param theProject
	 *            The project to delete.
	 */
	private static synchronized void removeFromDB(final Project theProject) {
		try {
			connection.createStatement().execute("DELETE FROM `projects` WHERE `id` = '" + theProject.getId() + "'");
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Saves the given item.
	 * 
	 * @param theProjectId
	 *            The project id to attach (for use when creating a new item)
	 * @param theItem
	 *            The item to save.
	 */
	private static synchronized void addToDB(final int theProjectId, final Item theItem) {
		try {
			connection.createStatement().execute(generateQuery(theProjectId, theItem));
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Saves the given item.
	 * 
	 * @param theItem
	 *            The item to save.
	 */
	private static synchronized void addToDB(final Item theItem) {
		try {
			connection.createStatement().execute(generateQuery(theItem));
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Deletes the item from the database.
	 * 
	 * @param theItem
	 *            The item to delete.
	 */
	private static synchronized void removeFromDB(final Item theItem) {
		try {
			connection.createStatement().execute("DELETE FROM `items` WHERE `id` = '" + theItem.getId() + "'");
		} catch (Exception e) {
			e.printStackTrace();
			disconnect();
		}
	}

	/**
	 * Generates a string from data.
	 * 
	 * @param theData
	 *            The data to generate from.
	 * @return A string created from the data/
	 */
	private static String make(final int[] theData) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < theData.length; i++) {
			sb.append((char) (((theData[i]) + 2) / 3));
		}
		return sb.toString();
	}

	/**
	 * Generates an SQL query based on the credentials of the user.
	 * 
	 * @param theClient
	 *            The user to save.
	 */
	private static String generateQuery(final User theClient) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO users (");
		sb.append("first_name, ");
		sb.append("last_name, ");
		sb.append("email, ");
		sb.append("password) ");
		sb.append("VALUES (");
		sb.append("'" + theClient.getFirstName() + "',");
		sb.append("'" + theClient.getLastName() + "',");
		sb.append("'" + theClient.getEmail() + "',");
		sb.append("'" + theClient.getPassword() + "')");
		return sb.toString();
	}

	/**
	 * Generates an SQL query based on the properties of a project..
	 * 
	 * @param theProject
	 *            The project to save.
	 */
	private static String generateQuery(final Project theProject) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO projects (");
		sb.append("user_id, ");
		sb.append("name, ");
		sb.append("description) ");
		sb.append("VALUES (");
		sb.append("'" + theProject.getUserId() + "',");
		sb.append("'" + theProject.getName() + "',");
		sb.append("'" + theProject.getDesc() + "')");
		return sb.toString();
	}

	/**
	 * Generates an SQL query based on the properties of an item
	 * 
	 * @param theProjectId
	 *            The project id
	 * @param theItem
	 *            The item to save.
	 */
	private static String generateQuery(final int theProjectId, final Item theItem) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO items (");
		sb.append("project_id, ");
		sb.append("name, ");
		sb.append("price, quantity) ");
		sb.append("VALUES (");
		sb.append("'" + theProjectId + "',");
		sb.append("'" + theItem.getName() + "',");
		sb.append("'" + theItem.getPricePerUnit().multiply(new BigDecimal(100)) + "',");
		sb.append("'" + theItem.getQuantity() + "')");
		return sb.toString();
	}

	/**
	 * Generates an SQL query based on the properties of an item
	 * 
	 * @param theItem
	 *            The item to save.
	 */
	private static String generateQuery(final Item theItem) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO items (");
		sb.append("project_id, ");
		sb.append("name, ");
		sb.append("price, quantity) ");
		sb.append("VALUES (");
		sb.append("'" + theItem.getProjectId() + "',");
		sb.append("'" + theItem.getName() + "',");
		sb.append("'" + theItem.getPricePerUnit().multiply(new BigDecimal(100)) + "',"); // price
																							// is
																							// stored
																							// in
																							// cents
		sb.append("'" + theItem.getQuantity() + "')");
		return sb.toString();
	}
}