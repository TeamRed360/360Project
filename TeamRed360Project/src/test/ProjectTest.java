package test;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Item;
import model.Project;

/**
 * JUnit tests
 * 
 * @author Amanda Aldrich
 *
 */
public class ProjectTest {

	Project tester;

	@Before
	public void setUp() throws Exception {
		tester = new Project(-10, "A Fence", "a lovely fence");
		tester.setId(-10);

		Item item1 = new Item("A Nail", 1.00, 1);
		item1.setId(-1);
		item1.setProjectId(-10);

		Item item2 = new Item("A Board", 10.00, 1);
		item2.setId(-2);
		item2.setProjectId(-10);

		tester.add(item1);
		tester.add(item2);
	}

	@Test
	public void testProjectName() {
		Project nameProject = new Project(-10, "Fence", "a lovely fence");
		nameProject.changeName("A Fence");
		assertEquals(tester.getName(), nameProject.getName());
	}

	@Test
	public void testProjectDesc() {
		Project descProject = new Project(-10, "A Fence", "lovely fence");
		descProject.changeDesc("a lovely fence");
		assertEquals(tester.getDesc(), descProject.getDesc());
	}

	@Test
	public void testProjectUserID() {
		Project userIdProject = new Project(-1, "A Fence", "a lovely fence");
		userIdProject.setUserId(-10);
		assertEquals(tester.getUserId(), userIdProject.getUserId());
	}

	@Test
	public void testProjectID() {
		Project idProject = new Project(-1, "Fence", "lovely fence");
		idProject.setId(-10);
		assertEquals(tester.getId(), idProject.getId());
	}

	@Test
	public void testGetList() {
		Project itemProject = new Project(-10, "A Fence", "a lovely fence");

		Item item1 = new Item("A Nail", 1.00, 1);
		Item item2 = new Item("A Board", 10.00, 1);

		itemProject.add(item1);
		item1.setId(-1);
		item1.setProjectId(-10);

		itemProject.add(item2);
		item2.setId(-2);
		item2.setProjectId(-10);

		for (int i = 0; i < tester.getListOfItems().length; i++) {
			Assert.assertEquals(tester.getListOfItems()[i].getName(), itemProject.getListOfItems()[i].getName());
		}
	}

	@Test
	public void testRemove() {

		Project itemProject = new Project(-10, "A Fence", "a lovely fence");

		Item item1 = new Item("A Nail", 1.00, 1);
		Item item2 = new Item("A Board", 10.00, 1);
		Item item3 = new Item("A SurfBoard", 10.00, 1);

		itemProject.add(item1);
		item1.setId(-1);
		item1.setProjectId(-10);

		itemProject.add(item2);
		item2.setId(-2);
		item2.setProjectId(-10);

		itemProject.add(item3);
		item3.setId(-3);
		item3.setProjectId(-10);

		itemProject.remove(item3);
		item3.setId(-2);
		item3.setProjectId(-10);

		for (int i = 0; i < tester.getListOfItems().length; i++) {
			Assert.assertEquals(tester.getListOfItems()[i].getName(), itemProject.getListOfItems()[i].getName());
		}
	}

	@Test
	public void testSetList() {

		Project itemProject = new Project(-10, "A Fence", "a lovely fence");

		Item item1 = new Item("A Snail", 1.00, 1);
		Item item2 = new Item("A Board", 10.00, 1);
		Item item3 = new Item("A Nail", 1.00, 1);

		itemProject.add(item1);
		item1.setId(-1);
		item1.setProjectId(-10);

		itemProject.add(item2);
		item2.setId(-2);
		item2.setProjectId(-10);

		// itemProject.add(item3);
		item2.setId(-1);
		item2.setProjectId(-10);

		Set<Item> contents = new TreeSet<Item>();
		contents.add(item3);
		contents.add(item2);

		itemProject.setListOfItems(contents);

		for (int i = 0; i < tester.getListOfItems().length; i++) {
			Assert.assertEquals(tester.getListOfItems()[i].getName(), itemProject.getListOfItems()[i].getName());
		}
	}

	@Test
	public void testToString() {

		String expected = tester.getName() + ", " + tester.getDesc() + ", Items: " + tester.getListOfItems().length
				+ ", Total Price: " + tester.getOverallPrice();

		assertEquals(expected, tester.toString());
	}
	@Test
	public void testaddToDB() {

		String expected = tester.getName() + ", " + tester.getDesc() + ", Items: " + tester.getListOfItems().length
				+ ", Total Price: " + tester.getOverallPrice();

		assertEquals(expected, tester.toString());
	}

}
