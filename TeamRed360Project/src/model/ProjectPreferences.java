package model;

/**
 * An enum class for the types of DIY
 * projects one can have.
 * 
 * @author Taylor Riccetti
 */
public enum ProjectPreferences {
	
	GARDEN("Garden"),
	KITCHEN("Kitchen"),
	ELECTRIC("Electric"),
	GARAGE("Garage"),
	BATHROOM("Bathroom"),
	BEDROOM("Bedroom"),
	HOME("Home");
	
	private String type;

	ProjectPreferences(String theType) {
		type = theType;
	}
	
	public String type() {
		return type;
    }
	
}
