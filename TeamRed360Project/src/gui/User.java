package gui;
/*
 * TCSS 360 - Software Development Team Red
 * User object stores the user's name and email.
 */
/**
 * 
 * @version 1.6 
 */
public class User {

	private String myEmail;
	
	private String myFirstName;
	
	private String myLastName;
	
	private String myPassword;
	
	//User Constructor
	public User(final String theFirstName, final String theLastName,
			final String theEmail, final String thePassword) {
		if(theFirstName == null || theLastName ==  null
				|| theEmail == null || thePassword == null) {
			throw new IllegalArgumentException("Illegal Parameters!");
		}
		
		myFirstName = theFirstName;
		myLastName = theLastName;
		myEmail = theEmail;
		myPassword = thePassword;
	}
	
	public String getFirstName(){
		return myFirstName;
	}
	
	public String getLastName(){
		return myLastName;
	}
	
	public String getEmail(){
		return myEmail;
	}
	
	public String getPassword(){
		return myPassword;
	}
}