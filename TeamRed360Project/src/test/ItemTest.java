package test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

import model.Item;

/**
 * JUnit tests
 * 
 * @author Amanda Aldrich
 *
 */
public class ItemTest {

	Item tester;

	@Before
	public void setUp() {

		tester = new Item("a good name", 0.0, 0);
		tester.setId(-10);
		tester.setProjectId(-10);

	}

	@Test
	public void testName() {

		Item itemName = new Item("a good name", 0.0, 0);
		assertEquals(tester.getName(), itemName.getName());
	}

	@Test
	public void testPrice() {

		Item itemPrice = new Item("a good name", 0.0, 0);
		assertEquals(tester.getName(), itemPrice.getName());
	}

	@Test
	public void testQuantity() {

		Item itemQuantity = new Item("a good name", 0.0, 0);
		assertEquals(tester.getQuantity(), itemQuantity.getQuantity());
	}

	@Test
	public void testID() {

		Item itemID = new Item("a good name", 0.0, 0);
		itemID.setId(-10);

		assertEquals(tester.getId(), itemID.getId());
	}

	@Test
	public void testProjectID() {

		Item itemID = new Item("a good name", 0.0, 0);
		itemID.setProjectId(-10);

		assertEquals(tester.getProjectId(), itemID.getProjectId());
	}

	@Test
	public void testCopyConst() {
		Item testItem1 = new Item("name", 0.0, 0);
		testItem1.setId(-10);
		testItem1.setProjectId(-10);
		Item testItem2 = new Item(testItem1);

		String expected = testItem1.toString() + testItem1.getId() + testItem1.getProjectId();
		assertEquals(expected, testItem2.toString() + testItem2.getId() + testItem2.getProjectId());
	}

	@Test
	public void testPricePerUnit() {

		Item itemPrice = new Item("a good name", 0.0, 0);
		assertEquals(tester.getPricePerUnit(), itemPrice.getPricePerUnit());

	}

	@Test
	public void testPriceTotal() {

		Item itemPrice = new Item("a good name", 0.0, 0);
		Item item2Price = new Item("another item", 1.00, 5);
		assertEquals(tester.getTotalPrice(), itemPrice.getTotalPrice());
		assertEquals(new BigDecimal(5.00), item2Price.getTotalPrice());

	}

	@Test
	public void testtoString() {

		Item toStringItem = new Item("a good name", 0.0, 0);

		String expected = toStringItem.getName() + ": "
				+ NumberFormat.getCurrencyInstance().format(toStringItem.getPricePerUnit()) + " - "
				+ toStringItem.getQuantity() + " - "
				+ NumberFormat.getCurrencyInstance().format(toStringItem.getTotalPrice());
		assertEquals(expected, tester.toString());

	}

	@Test
	public void testCompareToName() {

		Item compareToItem1 = new Item("a good name", 0.0, 1);
		Item compareToItem2 = new Item("another good name", 0.0, 1);

		assertEquals(-78, compareToItem1.compareTo(compareToItem2));
		assertEquals(0, compareToItem1.compareTo(compareToItem1));
		assertEquals(78, compareToItem2.compareTo(compareToItem1));
	}

	@Test
	public void testCompareToPrice() {

		Item compareToItem1 = new Item("a good name", 0.0, 1);
		Item compareToItem2 = new Item("a good name", 1.0, 1);

		assertEquals(-1, compareToItem1.compareTo(compareToItem2));
		assertEquals(0, compareToItem1.compareTo(compareToItem1));
		assertEquals(1, compareToItem2.compareTo(compareToItem1));
	}

}
