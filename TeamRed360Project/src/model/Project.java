package model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Taylor 
 */
public class Project {

	private String name;
	
	private String desc;
	
	private Set<Item> contents;
	
	private BigDecimal totalPrice;
	 
	/**
	 * Contructs a project object.
	 */
	public Project(String name, String desc) {
		this.name = name;
		this.desc = desc;
		contents = new TreeSet<>();
		totalPrice = new BigDecimal(0.0); 
	}
	
	/**
	 * sets the name.
	 * 
	 * @author Amanda Aldrich
	 * @param newName, the new name....
	 */
	public void changeName(String newName){
		name = newName;
	}
	
	/**
	 * Getter for the name.
	 * 
	 * @author Amanda Aldrich
	 * @return name, the projects name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Getter for the description.
	 * 
	 * @author Amanda Aldrich
	 * @return desc, the projects description
	 */
	public String getDesc(){
		return desc;
	}
	
	/**
	 * sets the description.
	 * 
	 * @author Amanda Aldrich
	 * @param newDesc, the new description
	 */
	public void changeDesc(String newDesc){
		desc = newDesc;
	}
	
	/**
	 * Returns the totalPrice.
	 * 
	 * @author Amanda Aldrich
	 * @return theTotalPrice, the total price....
	 */
	public String getOverallPrice(){
		return NumberFormat.getCurrencyInstance().format(totalPrice);
	}
	
	/**
	 * Adds the item to the set and sets the total price.
	 * 
	 * @author Taylor Riccetti, modified by Amanda Aldrich
	 * @param theItem, item to be added
	 */
	public void add(Item theItem) {
		contents.add(theItem);
		this.totalPrice = updatePrice(theItem, true);
	}

	/**
	 * Removes the item to the set and sets the total price.
	 * 
	 * @author Taylor Riccetti, modified by Amanda Aldrich
	 * @param theItem, item to be removed
	 */
	public void remove(Item theItem) {
		contents.remove(theItem);
		this.totalPrice = updatePrice(theItem, false);
	}
	
	/**
	 * updates the price.
	 * 
	 * @author Taylor Riccetti, edited by Amanda Aldrich
	 * @param theItem, the item being priced
	 * @param plusMinus, whether we add or subtract
	 */
	private BigDecimal updatePrice(Item theItem, Boolean plusMinus) {
		
		BigDecimal result;
		
		if (plusMinus){
			result = totalPrice.add(theItem.getTotalPrice());
		}else{
			 result = totalPrice.subtract(theItem.getTotalPrice());
		}

		return result;
	}

	/**
	 * returns the items in an array format.
	 * 
	 * @author Taylor Riccetti, edited by Amanda Aldrich
	 * @return array of items
	 */
	public Item[] getListOfItems(){
		Item[] result = contents.toArray(new Item[contents.size()]);
		return result;
	}
	
	/**
	 * returns the project in string form.
	 * 
	 * @author Taylor Riccetti, edited my Amanda Aldrich
	 * @return a string builder that holds our project in string form
	 */
	@Override
	public String toString() {
		StringBuilder bs = new StringBuilder();
		bs.append(name + ", ");
		bs.append(desc + ", Items: ");
		bs.append(contents.size());
		bs.append(", Total Price: ");
		bs.append(NumberFormat.getCurrencyInstance().format(totalPrice));
		return bs.toString();
	}
}

