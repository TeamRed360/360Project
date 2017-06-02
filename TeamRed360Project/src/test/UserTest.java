package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.User;

/**
 * JUnit tests
 * 
 * @author Amanda Aldrich, Jimmy Best
 *
 */
public class UserTest {

	User tester;

	@Before
	public void setUp() throws Exception {
		tester = new User("amanda", "aldrich", "amlaldrich@gmail.com", "qwe123");
	}
	
	@Test
	public void testId() {
		User newUser = new User("amanda", "aldrich", "amlaldrich@gmail.com", "qwe123");
		assertEquals(tester.getId(), newUser.getId());
	}

	@Test
	public void testUserFirstName() {
		User newUser = new User("amanda", "aldrich", "amlaldrich@gmail.com", "qwe123");
		assertEquals(tester.getFirstName(), newUser.getFirstName());
	}

	@Test
	public void testUserLastName() {
		User newUser = new User("amanda", "aldrich", "amlaldrich@gmail.com", "qwe123");
		assertEquals(tester.getLastName(), newUser.getLastName());
	}

	@Test
	public void testUserEmail() {
		User newUser = new User("amanda", "aldrich", "amlaldrich@gmail.com", "qwe123");
		assertEquals(tester.getEmail(), newUser.getEmail());
	}
	
	@Test
	public void testUserPassword() {
		User newUser = new User("amanda", "aldrich", "amlaldrich@gmail.com", "qwe123");
		assertEquals(tester.getPassword(), newUser.getPassword());
	}

	@Test
	public void testSetID() {
		tester.setId(1);
		assertEquals(tester.getId(), 1);
	}
	
	@Test
	public void testSetEmail() {
		tester.setEmail("1");
		assertEquals(tester.getEmail(), "1");
	}
	
	@Test
	public void testSetPassword() {
		tester.setPassword("1");
		assertEquals(tester.getPassword(), "1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExceptionFirstName() {
		User nullUser = new User(null, "Kirk", "enterprise@starfleet.org", "qwe123");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExceptionLastName() {
		User nullUser = new User("James", null, "enterprise@starfleet.org", "qwe123");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExceptionPassword() {
		User nullUser = new User("James", "Kirk", "enterprise@starfleet.org", null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testException() {
		User nullUser = new User("James", "Kirk", null, "qwe123");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetEmailNull() {
		tester.setEmail(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetPasswordNull() {
		tester.setPassword(null);
	}
}