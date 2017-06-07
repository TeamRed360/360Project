package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import model.Item;
import model.Project;
import model.ProjectWriter;

/**
 * JUnit tests for ProjectWriter.java
 * 
 * @author Jimmy Best
 */
public class ProjectWriterTest {

	@Test
	public void testExport() {
		Project test = new Project(1, "test", "");
		Item a = new Item("a", 1.0, 1);
		Item b = new Item("b", 1.0, 1);
		test.add(a);
		test.add(b);
		//good
		assertEquals(1, ProjectWriter.export(test));
		assertEquals(1, ProjectWriter.export(new Project(1, "a", "")));
		//bad
		assertEquals(-1, ProjectWriter.export(null));
		//Not sure how to return -1 for IOException
	}

	@Test
	public void testImportFile() {
		File goodTest = new File(System.getProperty("user.dir") + "//" + "testFile.prj");
		File badTest = new File("");
		//good

		assertNotNull(ProjectWriter.importFile(goodTest, 1));
		//bad
		assertNull(ProjectWriter.importFile(badTest, 1));
		assertNull(ProjectWriter.importFile(null, 1));
	}

}
