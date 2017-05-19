package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gui.User;

/**
 * JUnit tests
 * @author
 *
 */
public class UserTest {

	User tester;
	
	@Before
	public void setUp() throws Exception {
		tester = new User("amanda", "aldrich", "amlaldrich@gmail.com", "qwe123");
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
	
	@Test(expected = IllegalArgumentException.class)
	public void testExceptionFirstName(){
		User nullUser = new User(null, "Kirk", "enterprise@starfleet.org", "qwe123");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExceptionLastName(){
		User nullUser = new User("James", null, "enterprise@starfleet.org", "qwe123");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExceptionPassword(){
		User nullUser = new User("James", "Kirk", "enterprise@starfleet.org", null);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testException(){
		User nullUser = new User("James", "Kirk", null, "qwe123");
	}
} 