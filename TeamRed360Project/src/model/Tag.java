package model;

/**
 * An enum class for the types of DIY
 * projects one can have.
 * 
 * @author Taylor Riccetti
 */
public enum Tag {
	
	GARDEN("Garden"),
	KITCHEN("Kitchen"),
	ELECTRIC("Electric"),
	GARAGE("Garage"),
	BATHROOM("Bathroom"),
	BEDROOM("Bedroom"),
	HOME("Home");
	
	private String type;

	Tag(String theType) {
		type = theType;
	}
	
	public String type() {
		return type;
    }
	
}
