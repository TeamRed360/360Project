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
	
	private Set<Item> contents;
	
	private BigDecimal totalPrice;
	
	private Set<Tag> tags;
	
	/**
	 * Contructs a project object.
	 */
	public Project(String name) {
		this.name = name;
		contents = new TreeSet<>();
		totalPrice = new BigDecimal(0.0);
		tags = new HashSet<>();
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
	
	public void setTag(Tag theTag) {
		tags.add(theTag);
	}
	
	public Item[] getListOfItems(){
		return (Item[]) contents.toArray();
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

