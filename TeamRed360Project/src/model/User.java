package model;

/**
 * TCSS 360 - Software Development Team Red User object stores the user's name
 * and email.
 * 
 * @version 1.6
 */
public final class User {

	private int id;

	private String myEmail;

	private String myFirstName;

	private String myLastName;

	private String myPassword;

	// User Constructor
	public User(final String theFirstName, final String theLastName, final String theEmail, final String thePassword) {
		if (theFirstName == null || theLastName == null || theEmail == null || thePassword == null) {
			throw new IllegalArgumentException("Illegal Parameters!");
		}
		id = -1; // this is set later on, needs an SQL response to determine
		myFirstName = theFirstName;
		myLastName = theLastName;
		myEmail = theEmail;
		myPassword = thePassword;
	}

	/**
	 * Sets the ID of the user.
	 * 
	 * @param id
	 *            The id to set to.
	 * @author Stan Hu
	 */
	public void setId(final int theId) {
		this.id = theId;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Needs to throw exception - Jimmy
	public void setEmail(final String email) {
		myEmail = email;
	}

	// Needs to throw exception - Jimmy
	public void setPassword(final String password) {
		myPassword = password;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the ID of the user.
	 * 
	 * @param id
	 *            The id of the user in the database.
	 * @author Stan Hu
	 */
	public int getId() {
		return id;
	}

	public String getFirstName() {
		return myFirstName;
	}

	public String getLastName() {
		return myLastName;
	}

	public String getEmail() {
		return myEmail;
	}

	public String getPassword() {
		return myPassword;
	}
}