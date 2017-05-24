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
	
	public void changeName(String newName){
		name = newName;
	}
	
	public void changeDesc(String newDesc){
		desc = newDesc;
	}
	
	public void add(Item theItem) {
		contents.add(theItem);
		updatePrice(theItem);
	}

	public void remove(Item theItem) {
		contents.remove(theItem);
	}
	
	private void updatePrice(Item theItem) {
		totalPrice.add(theItem.getTotalPrice());
	}

	//modified by Amanda
	public Item[] getListOfItems(){
		Item[] result = contents.toArray(new Item[contents.size()]);
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder bs = new StringBuilder();
		bs.append("{");
		for(Item item : contents) {
			bs.append(item.toString());
			bs.append(", ");
		}
		bs.substring(0, bs.length() - 2);
		bs.append(", Total Price: ");
		bs.append(NumberFormat.getCurrencyInstance().format(totalPrice));
		bs.append("}");
		return bs.toString();
	}
}

