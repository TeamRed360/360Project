package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ProjectWriter { // implements Serializable {

	/**
	 * This function exports
	 * 
	 * @param theProject
	 * @author Taylor Riccetti
	 */
	public static int export(Project theProject) {

		PrintWriter writer = null;
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream("c:\\" + theProject.getName() + ".prj", true);
			writer = new PrintWriter(fout);
			writer.println(theProject.getName() + ":" + theProject.getDesc());
			Item[] items = theProject.getListOfItems();
			writer.println(">items:" + items.length);
			for (Item item : items) {
				writer.println(item.getName() + "," + item.getPricePerUnit() + "," + item.getQuantity());
			}
		} catch (Exception ex) {
			return -1; // to tell the GUI to display error text
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception ex) {
					return -1; // to tell the GUI to display error text
				}
			}
		}
		return 1; // it all worked so display success text
	}

	/**
	 * 
	 * @param theFile
	 * @param userId
	 * @return
	 */
	public static Project importFile(File theFile, int userId) {
		Scanner scanner;
		try {
			scanner = new Scanner(theFile);
			Set<Item> items = new HashSet<>();
			String[] projectName = new String[2];
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// first line should have name"project description
				if (line.contains(":") && !line.contains(">")) {
					projectName = line.split(":");
				} else if (line.contains(">items:")) {
					line = line.substring(7);
				} else {
					String[] readItem = line.split(",");
					items.add(new Item(readItem[0], Double.parseDouble(readItem[1]), Integer.parseInt(readItem[2])));
				}
			}
			Project readProject = new Project(userId, projectName[0], projectName[1]);
			readProject.setListOfItems(items);
			return readProject;
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}