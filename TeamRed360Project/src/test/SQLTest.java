/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import connection.SQL;
import model.Item;
import model.Project;
import model.User;
 
/**
 * JUnit tests for SQL.java for import/export functionality.
 * 
 * @author Taylor Riccetti
 * @author Josh Lau
 */
public class SQLTest {

	private final User testUser = new User("John", "Doe", "jdoe@gmail.com", "123jdoe");
	private final Project testProject = new Project(10, "Not A Fence", "Not a lovely fence");
	private final Item item1 = new Item("Not A Nail", 1.00, 1);
	private final Item item2 = new Item("Not A Board", 10.00, 1);
	private final Item item3 = new Item("'faker'", 10.00, 1);
	
	

	/**
	 * Test method for {@link connection.SQL#login(model.User)}.
	 */
	@Before
	public void setup() {
		
		
		item1.setId(-1);
		item1.setProjectId(-10);

		
		item2.setId(-2);
		item2.setProjectId(-10);

		testProject.add(item1);
		testProject.add(item2);
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
	 * Test method for {@link connection.SQL#connect()}.
	 */
	@Test (expected = Exception.class)
	public void testConnectoffline() {
		SQL.connect();
	}

	@Test
	public final void testDisconnect() {
		SQL.disconnect();
		assertEquals(SQL.isOnline(), false);
	}

	/**
	 * Test method for {@link connection.SQL#updateUser(model.User)}.
	 */
	@Test
	public void testUpdateUser() {
		SQL.updateUser(testUser);
		assertNotNull(SQL.getLastUser());
	}
	/**
	 * Test method for {@link connection.SQL#updateUser(model.User)}.
	 */
	@Test 
	public void testUpdateUser2() {
		testUser.setEmail("\"wow\"");
		SQL.updateUser(testUser);
		assertNotNull(SQL.getLastUser());
	}
	/**
	 * Test method for {@link connection.SQL#getLastUser()}.
	 */
	@Test
	public void testGetLastUser() {
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


	@Test
	public final void testIsOnline() {
		assertEquals(SQL.isOnline(), true);
	}


	@Test
	public final void testGetProjects() {
		ArrayList <Project> projects = SQL.getProjects(testUser);
		assertEquals(projects.get(0).toString(), testProject.toString());
	}


	@Test
	public final void testUpdateUserString() {
		SQL.updateUser(testUser, "jdizzle@gmail.com");
		testUser.setEmail("jdizzle@gmail.com");
		User newuser = SQL.getUser(testUser);
		assertEquals(newuser.getEmail(), testUser.getEmail());
	}
	@Test
	public final void testgetUser() {
		testUser.setEmail("sandwichcity@gmail.com");
		assertNull(SQL.getUser(testUser));
		
	}
	@Test
	public final void testgetUser2() {
		testUser.setEmail("\"wow\"");
		assertNull(SQL.getUser(testUser));
	
	
	}
	@Test
	public final void testUpdateProject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testUpdateItem() {
		SQL.updateItem(testProject, item3); // TODO
	}

	@Test
	public final void testDeleteProject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEmailExists() {
		assertEquals(SQL.emailExists("butdoesit@gmail.com"), false);
	}
	@Test
	public final void testEmailExists2() {
		assertEquals(SQL.emailExists("jdoe@gmail.com"), true);
	}
	@Test
	public final void testEmailExists3() {
		SQL.emailExists("\"jdoe@gmail.com\"");
	
	}
	@Test(expected = SQLException.class)
	public final void testEmailExistsOffline() {
		assertEquals(SQL.emailExists("jdoe@gmail.com"), true);
	}

	@Test
	public final void testLogin() {
		assertEquals(SQL.login(testUser), 1);
		
	}
	@Test
	public final void testLogin2() {
		testUser.setEmail("\"wow\"");
		assertEquals(SQL.login(testUser), 3);
		
	}
	@Test
	public final void testLogin3() {
		testUser.setPassword("nope");
		assertEquals(SQL.login(testUser), 2);
		
	}
	public final void testLoginIncorrectPassword() {
		testUser.setPassword("wrong");
		System.out.println(SQL.login(testUser));
		assertEquals(SQL.login(testUser), 2);
		
	}
	@Test(expected = Exception.class)
	public final void testLoginNull() {
		SQL.login(null);
		
	}

	@Test
	public final void testCreateProject() {
		SQL.createProject(testProject);
		
	}
	@Test
	public final void testCreateProject2() {
		Project testProject2 = new Project(10, "Not A Fence", "\"Not a lovely fence\"");
		SQL.createProject(testProject2);
		
	}

	@Test
	public final void testCreateItem() {
		SQL.createItem(0, item3); // TODO
	}

}



