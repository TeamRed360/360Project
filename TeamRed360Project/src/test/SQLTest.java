/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import connection.SQL;
import gui.User;

/**
 * JUnit tests for SQL.java for import/export functionality.
 * @author Taylor Riccetti
 */
public class SQLTest {
	
	private final User testUser = new User("John", "Doe", "jdoe@gmail.com", "123jdoe");

	
	/**
	 * Test method for {@link connection.SQL#login(gui.User)}.
	 */
	@Before
	public void setup() {
		SQL.connect();
		SQL.login(testUser);
		SQL.updateUser(testUser);
	}
	
	/**
	 * Test method for {@link connection.SQL#connect()}.
	 */
	@Test
	public void testConnect() {
		SQL.connect(); 
	}

	/**
	 * Test method for {@link connection.SQL#updateUser(gui.User)}.
	 */
	@Test
	public void testUpdateUser() {
		SQL.updateUser(testUser);
		assertNotNull(SQL.getLastUser());
	}

	/**
	 * Test method for {@link connection.SQL#getLastUser()}.
	 */
	@Test
	public void testGetLastUser() { ;
		User returnedUser = SQL.getLastUser(); 
		assertEquals(returnedUser.getFirstName(), testUser.getFirstName());
		assertEquals(returnedUser.getLastName(), testUser.getLastName());
		assertEquals(returnedUser.getEmail(), testUser.getEmail());
		assertEquals(returnedUser.getPassword(), testUser.getPassword());
	}

	
	/**
	 * Test method for {@link connection.SQL#getAllUsers()}.
	 */
	@Test
	public void testGetAllUsers() {
		assertNotNull(SQL.getAllUsers());
	}

	

}
