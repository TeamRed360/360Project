package model;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * An item contains a project id, id, a name, quantity and a price.
 * 
 * @author Taylor Riccetti, Amanda Aldrich, Stan Hu
 *
 */
public class Item implements Comparable<Item> {

	private int projectId;

	private int id;

	private String name;

	private int quantity;

	private BigDecimal price;

	/**
	 * Copy constructor to be used by the SQL class.
	 */
	public Item(Item theItem) {
		projectId = theItem.getProjectId();
		id = theItem.getId();
		name = theItem.getName();
		quantity = theItem.getQuantity();
		price = theItem.getPricePerUnit();
	}

	/**
	 * Constructs the item with the given name, price and quantity.
	 * 
	 * @param theName
	 * @param thePrice
	 * @param theQuantity
	 * @author Taylor Riccetti, Stan Hu
	 */
	public Item(String theName, Double thePrice, int theQuantity) {
		projectId = -1;
		id = -1; // this is set later on, needs an SQL response to determine
		name = theName;
		price = new BigDecimal(thePrice);
		quantity = theQuantity;
	}

	/**
	 * Sets the project ID of the item.
	 * 
	 * @param id
	 *            The project id to set to.
	 * @author Stan Hu
	 */
	public void setProjectId(final int theId) {
		this.projectId = theId;
	}

	/**
	 * Returns the project ID of the item.
	 * 
	 * @author Stan Hu
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * Sets the ID of the item.
	 * 
	 * @param id
	 *            The id to set to.
	 * @author Stan Hu
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Returns the ID of the item.
	 * 
	 * @param id
	 *            The id of the item in the database.
	 * @author Stan Hu
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the item.
	 * 
	 * @return name.
	 * @author Taylor Riccetti
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the quantity of the item.
	 * 
	 * @return the quantity.
	 * @author Taylor Riccetti
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Returns the price per unit of the item.
	 * 
	 * @return the price.
	 * @author Taylor Riccetti
	 */
	public BigDecimal getPricePerUnit() {
		return price;
	}

	/**
	 * Returns the total price of the items price and quantity.
	 * 
	 * @return total price.
	 * @author Taylor Riccetti
	 */
	public BigDecimal getTotalPrice() {
		return price.multiply(new BigDecimal(quantity));
	}

	/**
	 * Returns a string representation of the item.
	 * 
	 * @author Taylor Riccetti, edited by Amanda Aldrich
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder bs = new StringBuilder();
		bs.append(name);
		bs.append(": ");
		bs.append(NumberFormat.getCurrencyInstance().format(price));
		bs.append(" - ");
		bs.append(quantity);
		bs.append(" - ");
		bs.append(NumberFormat.getCurrencyInstance().format(this.getTotalPrice()));
		return bs.toString();
	}

	@Override
	public int compareTo(Item theOther) {
		int result = this.name.compareTo(theOther.getName());
		if (result != 0) {
			return result;
		}

		result = this.price.compareTo(theOther.getPricePerUnit());
		if (result != 0) {
			return result;
		}
		return Integer.compare(this.quantity, theOther.getQuantity());
	}
}
