package gui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import connection.SQL;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Calculator;
import model.Item;
import model.Project;
import model.ProjectWriter;
import model.User;

/**
 * JavaFx Application.
 * 
 * @author Taylor Riccetti edited by Amanda Aldrich
 */
public class FXMain extends Application {

	/**
	 * A list of the contributors' names.
	 */
	private static final String NAMES[] = { "Stan Hu", "Jimmy Best", "Amanda Aldrich", "Taylor Riccetti",
			"Joshua Lau" };

	private ArrayList<Project> projects = new ArrayList<Project>();

	/** The scenes width. */
	private final int SCENE_WIDTH = 825;

	/** The scenes height. */
	private final int SCENE_HEIGHT = 600;

	/** A constant for the font of header text. */
	private final Font HEADER_FONT = Font.font("Century Gothic", FontWeight.NORMAL, 20);

	/** A constant for the borders. */
	private final Border BORDER = new Border(
			new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(3), BorderWidths.DEFAULT));
	private Stage mainScreen;
	private final TabPane tabPane = new TabPane();
	private final Tab homeTab = new Tab("Home");
	private final Tab aboutTab = new Tab("About");
	private final Tab settingsTab = new Tab("Settings");

	private Text welcomeText = new Text();

	private User currentUser = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage screen) {
		mainScreen = screen;
		mainScreen.setTitle("Nailed It!");

		// Moved to login, so if offline user can skip login
		// SQL.connect();

		StackPane root = new StackPane();
		root.getChildren().add(getTabs());
		// messing around with css effects
		root.getStylesheets().add("gui/GUICss.css");

		mainScreen.setMinHeight(SCENE_HEIGHT);
		mainScreen.setMinWidth(SCENE_WIDTH);
		mainScreen.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.MISTYROSE));
		mainScreen.show();
	}

	/**
	 * Private helper method that returns a TabPane full of all the GUI's tabs.
	 * 
	 * @return TabPane
	 * @author Taylor Riccetti
	 */
	private TabPane getTabs() {

		homeTab.setContent(getLoginPane());

		homeTab.closableProperty().set(false);
		settingsTab.closableProperty().set(false);
		aboutTab.closableProperty().set(false);

		// Moved to getLoginPane
		// Initially all other tabs are disabled until the user logs in.
		// aboutTab.disableProperty().set(true);
		// settingsTab.disableProperty().set(true);

		aboutTab.setContent(getAboutContent());
		settingsTab.setContent(getSettingContent());
		tabPane.getStylesheets().add("gui/GUICss.css");
		tabPane.getTabs().add(homeTab);
		tabPane.getTabs().add(settingsTab);
		tabPane.getTabs().add(aboutTab);
		return tabPane;

	}

	/**
	 * Creates the About Tab.
	 * 
	 * @author Taylor Riccetti
	 * @return aboutPane, the base for this page of the UI
	 */
	private StackPane getAboutContent() {
		StackPane aboutPane = new StackPane();

		// Adds all the contributors to a grid pane.
		GridPane contributorGrid = new GridPane();
		contributorGrid.setAlignment(Pos.TOP_LEFT);
		contributorGrid.setVgap(10);
		contributorGrid.setHgap(10);
		contributorGrid.setPadding(new Insets(25));

		aboutPane.setAlignment(Pos.TOP_LEFT);
		Text contributors = new Text("Contributors:");
		contributors.setFont(HEADER_FONT);

		contributorGrid.add(contributors, 0, 0, 2, 1);

		for (int i = 1; i < NAMES.length + 1; i++) {
			Text currentName = new Text(NAMES[i - 1]);
			contributorGrid.add(currentName, 1, i); // tried to make the weird
													// gap leave -Amanda
		} // fixed it on line 177 -Taylor

		Text credits = new Text("Credits:");
		credits.setFont(HEADER_FONT);

		Text lightBulb = new Text("Icons made by: http://www.flaticon.com/authors/vectors-market"
				+ "\nVectors Market http://www.flaticon.com is licensed by http://creativecommons.org/licenses/by/3.0/CC 3.0 BY");

		contributorGrid.add(credits, 4, 0, 2, 1);
		contributorGrid.add(lightBulb, 4, 1, 1, 2);
		aboutPane.getChildren().add(contributorGrid);
		return aboutPane;
	}

	/**
	 * Gets a pane that displays all the login and sign up content.
	 * 
	 * @return a login pane.
	 * @author Taylor Riccetti
	 */
	private StackPane getLoginPane() {
		// Tabs disabled when not logged in
		aboutTab.disableProperty().set(true);
		settingsTab.disableProperty().set(true);

		StackPane loginContainer = new StackPane();
		TabPane tabPane = new TabPane();
		Tab loginTab = new Tab("Login");
		GridPane login = new GridPane();
		login.setAlignment(Pos.CENTER);
		login.setVgap(10);
		login.setHgap(10);
		login.setPadding(new Insets(25));
		Text loginMessage = new Text("Welcome");
		loginMessage.setFont(HEADER_FONT);
		Label emailLabel = new Label("E-Mail:");
		TextField loginEmail = new TextField();
		Label passwordLabel = new Label("Password:");
		PasswordField loginPassword = new PasswordField();
		final Text loginActionText = new Text();
		Button continueButton = new Button("Continue");

		Button skipLoginButton = new Button("Skip");
		Button skipLoginButton2 = new Button("Skip");
		skipLoginButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				currentUser = new User("Guest", "Guest", "User", "guest@guest.com");
				welcomeText.setText("Logged in as Guest.");
				homeTab.setContent(getHomeContent(welcomeText.getText()));

			}
		});

		skipLoginButton2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				currentUser = new User("Guest", "Guest", "User", "guest@guest.com");
				welcomeText.setText("Logged in as Guest.");
				homeTab.setContent(getHomeContent(welcomeText.getText()));

			}
		});

		TextField email = new TextField();
		TextField password = new PasswordField();

		continueButton.defaultButtonProperty().bind(continueButton.focusedProperty());
		// if you tab over the button now, it will work on ENTER press -Amanda

		continueButton.setOnAction(new EventHandler<ActionEvent>() {
			/**
			 * Attempts to submit the information.
			 * 
			 * @param theEvent
			 *            The event reference.
			 */
			@Override
			public void handle(ActionEvent event) {

				SQL.connect();

				User user = new User("", "", loginEmail.getText(), loginPassword.getText());
				int code = SQL.login(user);
				user = SQL.getLastUser();
				loginActionText.setFill(Color.FIREBRICK);
				if (code == 0) {
					int reply = JOptionPane.showConfirmDialog(null,
							"Email does not exist. Would you like to register instead?", "Register instead?",
							JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						email.setText(loginEmail.getText());
						password.setText(loginPassword.getText());
						loginActionText.setText("Please use the sign up tab.");
					}
				} else if (code == 1) {
					currentUser = user;
					welcomeText.setText("Welcome back, " + user.getFirstName() + " " + user.getLastName() + "!");
					homeTab.setContent(getHomeContent(welcomeText.getText()));
				} else if (code == 2) {
					loginActionText.setText("Incorrect password - try again.");
				} else {
					loginActionText.setText("Error - could not access the login server.");
				}
			}

		});

		// Adding components to the login tab.
		login.add(loginMessage, 0, 0, 2, 1);
		login.add(emailLabel, 0, 1);
		login.add(loginEmail, 1, 1);
		login.add(passwordLabel, 0, 2);
		login.add(loginPassword, 1, 2);
		login.add(continueButton, 3, 7);
		login.add(skipLoginButton, 0, 7);
		login.add(loginActionText, 3, 6);

		loginTab.setContent(login);
		Tab signUpTab = new Tab("Sign Up");
		GridPane signUp = new GridPane();
		signUp.setAlignment(Pos.CENTER);
		signUp.setVgap(10);
		signUp.setHgap(10);
		signUp.setPadding(new Insets(25));
		Text signUpMessage = new Text("Sign Up");
		signUpMessage.setFont(HEADER_FONT);
		Label firstNameLabel = new Label("First Name:");
		TextField firstName = new TextField();
		Label lastNameLabel = new Label("Last Name:");
		TextField lastName = new TextField();
		Label confirmPassLabel = new Label("Confirm Password:");
		PasswordField confirmPass = new PasswordField();
		final Text signUpActionText = new Text();

		Button signUpButton = new Button("Sign Up");
		signUpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				SQL.connect();

				signUpActionText.setFill(Color.FIREBRICK);

				if (!password.getText().equals(confirmPass.getText())) {
					signUpActionText.setText("Passwords do not match.");
				} else {
					User user = new User(firstName.getText(), lastName.getText(), email.getText(), password.getText());
					int code = SQL.login(user);
					if (code == 0) {
						SQL.updateUser(user);
						welcomeText.setText("Welcome, " + firstName.getText() + " " + lastName.getText() + "!");
						homeTab.setContent(getHomeContent(welcomeText.getText()));
					} else if (code == 1 | code == 2) {
						signUpActionText.setText("This email is already registered.");
					} else {
						signUpActionText.setText("Error - could not access the login server.");
					}
				}
			}

		});

		// adding all sign up input boxes
		signUp.add(signUpMessage, 0, 0, 2, 1);
		signUp.add(firstNameLabel, 0, 1);
		signUp.add(firstName, 1, 1);
		signUp.add(lastNameLabel, 0, 2);
		signUp.add(lastName, 1, 2);
		signUp.add(new Label("E-Mail:"), 0, 3);
		signUp.add(email, 1, 3);
		signUp.add(new Label("Password:"), 0, 4);
		signUp.add(password, 1, 4);
		signUp.add(confirmPassLabel, 0, 5);
		signUp.add(confirmPass, 1, 5);
		signUp.add(signUpButton, 3, 6);
		signUp.add(skipLoginButton2, 0, 6);
		signUp.add(signUpActionText, 2, 7, 2, 1);
		signUpTab.setContent(signUp);

		loginTab.closableProperty().set(false);
		signUpTab.closableProperty().set(false);

		// adds the tabs to the page
		tabPane.getTabs().add(loginTab);
		tabPane.getTabs().add(signUpTab);
		tabPane.setMaxWidth(SCENE_WIDTH / 1.8);
		tabPane.setMaxHeight(SCENE_HEIGHT / 1.8);
		loginContainer.getChildren().add(tabPane);
		tabPane.setBorder(BORDER);
		StackPane.setAlignment(tabPane, Pos.CENTER);
		return loginContainer;
	}

	/**
	 * Gets a pane that displays all the home content.
	 * 
	 * @param welcomeText
	 * @return the home pane
	 * @author Taylor Riccetti
	 */
	private StackPane getHomeContent(String welcomeText) {
		StackPane homePane = new StackPane();
		GridPane homeGrid = new GridPane();
		homeGrid.setAlignment(Pos.CENTER);
		homeGrid.setVgap(10);
		homeGrid.setHgap(10);
		homeGrid.setPadding(new Insets(25));

		Text welcome = new Text(welcomeText);
		welcome.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		Button projectButton = new Button("Projects");
		BackgroundImage backgroundImage = new BackgroundImage(new Image("./saw.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		projectButton.setBackground(background);
		projectButton.setMinSize(128, 128);
		projectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				homeTab.setContent(getProjectView());
			}
		});

		// code and design for import button
		Button importButton = new Button("Import Projects");
		BackgroundImage importImage = new BackgroundImage(new Image("./upload.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background importBackground = new Background(importImage);
		importButton.setBackground(importBackground);
		importButton.setMinSize(128, 128);
		importButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Project File");
				fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.prj"));
				File selectedFile = fileChooser.showOpenDialog(mainScreen);
				if (selectedFile != null) {
					Project importedProject = ProjectWriter.importFile(selectedFile, currentUser.getId());
					if (importedProject != null) {
						projects.add(importedProject);
					} else {
						// error text
					}
				}
			}
		});

		// code and design for export button
		Button exportButton = new Button("Export Projects");
		BackgroundImage exportImage = new BackgroundImage(new Image("./save.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background exportBackground = new Background(exportImage);
		exportButton.setBackground(exportBackground);
		exportButton.setMinSize(128, 128);
		exportButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// display

				homeTab.setContent(getProjectList());
			}
		});

		Button signoutButton = new Button("Sign Out");
		BackgroundImage signoutImage = new BackgroundImage(new Image("./chainsaw.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background signout = new Background(signoutImage);
		signoutButton.setBackground(signout);
		signoutButton.setMinSize(64, 64);

		signoutButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// should select the tab at index zero.. not working..
				// user still has to navigate back to the home tab to log back
				// in
				currentUser = null;
				new JOptionPane();
				// update database if needed
				int doubleCheck = JOptionPane.showConfirmDialog(null, "Do you want to Log out?", "Log Out?",
						JOptionPane.YES_NO_OPTION);
				if (doubleCheck == JOptionPane.YES_OPTION) {
					homeTab.setContent(getLoginPane());
					currentUser = null;
				}
			}

		});

		homeGrid.add(welcome, 0, 0, 2, 1);
		homeGrid.add(projectButton, 1, 1);
		homeGrid.add(importButton, 1, 2);
		homeGrid.add(exportButton, 1, 3);
		homeGrid.add(getCalculatorPane(), 2, 1, 2, 4);
		homeGrid.add(signoutButton, 5, 3);
		homePane.getChildren().add(homeGrid);

		aboutTab.disableProperty().set(false);
		settingsTab.disableProperty().set(false);
		return homePane;
	}

	protected TilePane getProjectList() {
		TilePane projectPane = new TilePane();
		GridPane projectGrid = new GridPane();

		projectGrid.setAlignment(Pos.TOP_LEFT);
		projectGrid.setVgap(10);
		projectGrid.setHgap(10);
		projectGrid.setPadding(new Insets(25));
		Text projectMessage = new Text("Pick a project to export.");
		projectMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

		Text placeHolderText = new Text("No Projects");
		placeHolderText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

		final ObservableList<Project> projectObsList = FXCollections.observableArrayList(projects);

		ListView<Project> projectList = new ListView<Project>(projectObsList);
		projectList.setPlaceholder(placeHolderText);
		projectList.setMinHeight(350);
		projectList.setMinWidth(400);

		Button exportButton = new Button("Export Project");
		exportButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// calls the project export code... and exports the selected
				// project.
				Project currentProject = projectList.getSelectionModel().getSelectedItem();
				if (currentProject == null) {
					// displays error
					System.err.println("No project to export.");

				} else {
					int result = ProjectWriter.export(currentProject);
					if (result > 0) {
						// success text
						homeTab.setContent(getHomeContent(welcomeText.getText()));
					} else {
						System.err.println("Export Failed");
					}
				}
			}
		});
		exportButton.disableProperty().bind(Bindings.isEmpty(projectList.getItems()));

		Button backButton = new Button("Back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				homeTab.setContent(getHomeContent(welcomeText.getText()));
			}

		});

		projectGrid.add(projectMessage, 0, 0, 2, 1);
		projectGrid.add(exportButton, 0, 9, 2, 1);
		projectGrid.add(backButton, 0, 40);

		projectPane.setMaxHeight(SCENE_HEIGHT);
		projectPane.setMaxWidth(SCENE_WIDTH);
		projectPane.getChildren().add(projectGrid);
		projectPane.getChildren().add(projectList);
		StackPane.setAlignment(projectPane, Pos.CENTER);
		return projectPane;
	}

	/**
	 * Returns a pane displaying the calculator.
	 * 
	 * @return a grid pane
	 * @author Taylor Riccetti, eduted by Amanda Aldrich
	 */
	private GridPane getCalculatorPane() {
		GridPane calculatorGrid = new GridPane();
		calculatorGrid.setAlignment(Pos.CENTER);
		calculatorGrid.setMinHeight(400);
		calculatorGrid.setMinWidth(275);
		calculatorGrid.setAlignment(Pos.TOP_LEFT);
		calculatorGrid.setVgap(10);
		calculatorGrid.setHgap(10);
		calculatorGrid.setPadding(new Insets(25));
		TextField total = new TextField();
		total.setEditable(false);
		total.setPrefWidth(225);
		total.setAlignment(Pos.CENTER_RIGHT);

		int row = 3;
		int col = 0;
		for (int i = 0; i < 10; i++) {
			Button currButton = new Button("" + i);
			currButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					total.setText(total.getText() + ((Button) event.getSource()).getText());

				}
			});
			currButton.setMinSize(50, 50);
			if (i == 0) {
				calculatorGrid.add(currButton, 1, 4);
			} else {
				calculatorGrid.add(currButton, col, row);
				col++;
				if (col == 3) {
					col = 0;
					row--;
				}
			}
		}

		Button addButton = new Button("+");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				total.setText(total.getText() + ((Button) event.getSource()).getText());

			}
		});
		addButton.setMinSize(50, 50);

		Button subtractButton = new Button("-");
		subtractButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				total.setText(total.getText() + ((Button) event.getSource()).getText());

			}
		});
		subtractButton.setMinSize(50, 50);

		Button multiplyButton = new Button("*");
		multiplyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				total.setText(total.getText() + ((Button) event.getSource()).getText());

			}
		});
		multiplyButton.setMinSize(50, 50);

		Button divideButton = new Button("/");
		divideButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				total.setText(total.getText() + ((Button) event.getSource()).getText());

			}
		});
		divideButton.setMinSize(50, 50);

		Button carrotButton = new Button("^");
		carrotButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				total.setText(total.getText() + ((Button) event.getSource()).getText());
			}
		});
		carrotButton.setMinSize(50, 50);

		Button decimalButton = new Button(".");
		decimalButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				total.setText(total.getText() + ((Button) event.getSource()).getText());

			}
		});
		decimalButton.setMinSize(50, 50);

		Button equalButton = new Button("=");
		equalButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// total.setText(total.getText() + ((Button)
				// event.getSource()).getText());
				Calculator calc = new Calculator(total.getText());
				total.setText(calc.getResult() + "");

			}
		});
		equalButton.setMinSize(110, 50);

		Button clearButton = new Button("C/E");
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				total.setText("");
			}
		});
		clearButton.setMinSize(110, 50);

		calculatorGrid.add(total, 0, 0, 4, 1);
		calculatorGrid.add(addButton, 3, 4);
		calculatorGrid.add(subtractButton, 3, 3);
		calculatorGrid.add(divideButton, 3, 1);
		calculatorGrid.add(multiplyButton, 3, 2);
		calculatorGrid.add(carrotButton, 0, 4);
		calculatorGrid.add(decimalButton, 2, 4);
		calculatorGrid.add(equalButton, 2, 5, 2, 1);
		calculatorGrid.add(clearButton, 0, 5, 2, 1);

		calculatorGrid.setBorder(BORDER);
		return calculatorGrid;
	}

	/**
	 * Builds the Projects overview.
	 * 
	 * @author Taylor Riccetti, heavily modified Amanda Aldrich
	 * @return projectPane, the base the UI is on
	 */
	private TilePane getProjectView() {
		TilePane projectPane = new TilePane();

		GridPane projectGrid = new GridPane();

		projectGrid.setAlignment(Pos.TOP_LEFT);
		projectGrid.setVgap(10);
		projectGrid.setHgap(10);
		projectGrid.setPadding(new Insets(25));
		Text projectMessage = new Text("Your Projects");
		projectMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

		Text placeHolderText = new Text("This is where your project will live");
		placeHolderText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

		final ObservableList<Project> projectObsList = FXCollections.observableArrayList(projects);

		ListView<Project> projectList = new ListView<Project>(projectObsList);
		projectList.setPlaceholder(placeHolderText);
		projectList.setMinHeight(350);
		projectList.setMinWidth(400);

		Button addNewButton = new Button("Add New Project");
		addNewButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				homeTab.setContent(addNewProjectView());

			}
		});

		Button editButton = new Button("Edit Project");
		editButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				homeTab.setContent(editProjectView(projectList.getSelectionModel().getSelectedItem()));

			}
		});
		editButton.disableProperty().bind(Bindings.isEmpty(projectList.getItems()));

		Button removeButton = new Button("Remove Project");
		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int myIndex = projectList.getSelectionModel().getSelectedIndex();
				projectList.getItems().remove(myIndex);
				projects.remove(myIndex);
			}
		});

		removeButton.disableProperty().bind(Bindings.isEmpty(projectList.getItems()));

		Button backButton = new Button("Back");

		backButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				homeTab.setContent(getHomeContent(welcomeText.getText()));
			}

		});

		projectGrid.add(projectMessage, 0, 0, 2, 1);

		projectGrid.add(addNewButton, 0, 5, 2, 1);
		projectGrid.add(editButton, 0, 7, 2, 1);
		projectGrid.add(removeButton, 0, 9, 2, 1);
		projectGrid.add(backButton, 0, 40);

		projectPane.setMaxHeight(SCENE_HEIGHT);
		projectPane.setMaxWidth(SCENE_WIDTH);
		projectPane.getChildren().add(projectGrid);
		projectPane.getChildren().add(projectList);
		StackPane.setAlignment(projectPane, Pos.CENTER);
		return projectPane;
	}

	/**
	 * Returns a JavaFX TilePane containing all settings content.
	 * 
	 * @return TilePane
	 * @author Taylor Riccetti, minorly modified by Amanda Aldrich
	 */
	private GridPane getSettingContent() {
		GridPane account = new GridPane();
		account.setAlignment(Pos.TOP_CENTER);
		account.setVgap(10);
		account.setHgap(10);
		account.setPadding(new Insets(25));
		Text changePassMessage = new Text("Change Password");
		changePassMessage.setFont(HEADER_FONT);
		Label currPassLabel = new Label("Current Password:");
		PasswordField currPassword = new PasswordField();
		Label newPassLabel = new Label("New Password:");
		PasswordField newPassword = new PasswordField();
		Label confirmPassLabel = new Label("Confirm Password:");
		PasswordField confirmPassword = new PasswordField();
		Button changePassButton = new Button("Change Password");
		final Text changePassText = new Text();

		changePassButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currPassword.getText().equals(currentUser.getPassword())) {
					if (newPassword.getText().equals(confirmPassword.getText())) {
						currentUser.setPassword(newPassword.getText());
						changePassText.setFill(Color.LIMEGREEN);
						changePassText.setText("Password Changed!");
						SQL.updateUser(currentUser);
					} else {
						changePassText.setFill(Color.RED);
						changePassText.setText("Passwords do not match!");
					}
				} else {
					changePassText.setFill(Color.RED);
					changePassText.setText("Current password is invalid!");
				}

			}
		});

		account.add(changePassMessage, 0, 0, 2, 1);
		account.add(currPassLabel, 0, 1);
		account.add(currPassword, 1, 1);
		account.add(newPassLabel, 0, 2);
		account.add(newPassword, 1, 2);
		account.add(confirmPassLabel, 0, 3);
		account.add(confirmPassword, 1, 3);
		account.add(changePassButton, 1, 4);
		account.add(changePassText, 1, 5, 2, 1);

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		account.add(separator, 2, 0, 1, 5);

		Text changeEmailMessage = new Text("Change E-Mail");
		changeEmailMessage.setFont(HEADER_FONT);
		Label currEmailLabel = new Label("Current E-Mail:");
		PasswordField currEmail = new PasswordField();
		Label newEmailLabel = new Label("New E-Mail:");
		PasswordField newEmail = new PasswordField();
		Button changeEmailButton = new Button("Change E-Mail");
		final Text changeEmailText = new Text();

		changeEmailButton.setOnAction(new EventHandler<ActionEvent>() {
			// TO-DO action event handler for changing email
			@Override
			public void handle(ActionEvent event) {
				// TO DO
				changeEmailText.setFill(Color.LIMEGREEN);
				changeEmailText.setText("E-Mail Changed!");
			}
		});
		// adds change email components to the account tab.
		account.add(changeEmailMessage, 3, 0, 2, 1);
		account.add(currEmailLabel, 3, 1);
		account.add(currEmail, 4, 1);
		account.add(newEmailLabel, 3, 2);
		account.add(newEmail, 4, 2);
		account.add(changeEmailButton, 4, 3);
		account.add(changeEmailText, 4, 4, 2, 1);
		// account.add(signoutButton, 5, 5, 3, 2);
		return account;
	}

	/**
	 * Creates the Create a Project view.
	 * 
	 * @author Amanda Aldrich
	 * @return addProjectPane which is the pane we are building on.
	 */
	private StackPane addNewProjectView() {

		// project gets created to be customized by user
		Project tempProject = new Project(-1, " ", " ");

		// my pane
		StackPane addProjectPane = new StackPane();

		// the grid layout
		GridPane addProjectGrid = new GridPane();
		TilePane titlePane = new TilePane();
		titlePane.setHgap(20);
		titlePane.setVgap(20);
		addProjectGrid.setAlignment(Pos.TOP_LEFT);
		addProjectGrid.setVgap(10);
		addProjectGrid.setHgap(10);
		addProjectGrid.setPadding(new Insets(25));

		// my heading
		Text addProjectMessage = new Text("\n	Add new Project\n");
		addProjectMessage.setFont(HEADER_FONT);

		// project name setup
		Text projectName = new Text("Project Name:");
		projectName.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		TextField projectNameField = new TextField();
		projectNameField.setAlignment(Pos.BASELINE_LEFT);

		// project desc setup
		Text projectDesc = new Text("Project Description:");
		projectDesc.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		TextField projectDescField = new TextField();
		projectDescField.setAlignment(Pos.BASELINE_LEFT);

		Text errorSubmitMessage = new Text();
		errorSubmitMessage.setFill(Color.FIREBRICK);

		// my submit button
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (projectNameField.getText().length() > 0) {
					tempProject.changeName(projectNameField.getText());
					tempProject.changeDesc(projectDescField.getText());
					projects.add(tempProject);
					// System.out.println(projects.indexOf(tempProject));
					homeTab.setContent(getProjectView());
				} else {
					errorSubmitMessage.setText("Please enter a project name.");
				}
			}
		});

		// my back button
		Button backButton = new Button("Back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				homeTab.setContent(getProjectView());
			}
		});

		// my add button
		Button addButton = new Button("Add Row");
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// addRow(table);
			}

		});

		addButton.setFocusTraversable(false);
		BorderPane border = new BorderPane();

		border.setLeft(addProjectGrid);
		border.setCenter(makingTheList(tempProject));
		border.setTop(titlePane);
		// border.setRight();

		titlePane.getChildren().add(addProjectMessage);

		addProjectGrid.add(projectName, 0, 1, 2, 1);
		addProjectGrid.add(projectNameField, 0, 2, 2, 1);

		addProjectGrid.add(projectDesc, 0, 3, 2, 1);
		addProjectGrid.add(projectDescField, 0, 4, 2, 1);

		addProjectGrid.add(submitButton, 0, 5);
		addProjectGrid.add(errorSubmitMessage, 0, 6);
		addProjectGrid.add(backButton, 0, 30);

		addProjectPane.setMaxHeight(SCENE_HEIGHT);
		addProjectPane.setMaxWidth(SCENE_WIDTH);
		addProjectPane.getChildren().add(border);
		StackPane.setAlignment(border, Pos.CENTER);

		return addProjectPane;

	}

	/**
	 * This method creates the listview and its buttons
	 * 
	 * @author Amanda Aldrich, edited by Taylor Riccetti
	 * @return basePlate, the the pane everything was laid out on.
	 */
	private TilePane makingTheList(Project tempProject) {

		TilePane basePlate = new TilePane(); // I had legos on the brain
		basePlate.setHgap(10);
		basePlate.setVgap(10);
		GridPane listFormGrid = new GridPane();
		listFormGrid.setHgap(10);
		listFormGrid.setVgap(10);
		final ObservableList<String> strings = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<String>(strings);
		Text phText = new Text("Items will live here");
		listView.setPlaceholder(phText);
		listView.setMinWidth(300);
		if (tempProject.getListOfItems().length != 0) {
			for (int i = 0; i < tempProject.getListOfItems().length; i++) {
				strings.add(tempProject.getListOfItems()[i].toString());
			}
		}

		// item name setup
		Text itemName = new Text("Item Name:");
		itemName.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		TextField itemNameField = new TextField();
		itemNameField.setAlignment(Pos.BASELINE_LEFT);

		// item price setup
		Text itemPrice = new Text("Item Price:");
		itemPrice.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		TextField itemPriceField = new TextField();
		itemPriceField.setAlignment(Pos.BASELINE_LEFT);

		// item qty setup
		Text itemQty = new Text("Item Quantity:");
		itemQty.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		TextField itemQtyField = new TextField();
		itemQtyField.setAlignment(Pos.BASELINE_LEFT);

		Text totalPrice = new Text("Total Price: " + tempProject.getOverallPrice());
		totalPrice.setFont(Font.font("Arial", FontWeight.NORMAL, 15));

		Button addButton = new Button("Add Item");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Item myItem;
				try {
					myItem = new Item(itemNameField.getText(), Double.parseDouble(itemPriceField.getText()),
							Integer.parseUnsignedInt(itemQtyField.getText()));
					tempProject.add(myItem);

					strings.add(myItem.toString());
					totalPrice.setText("Total Price: " + tempProject.getOverallPrice());

				} catch (NumberFormatException e) {
					// does not add empty item
				}

				itemNameField.clear();
				itemPriceField.clear();
				itemQtyField.clear();

			}
		});

		Button removeButton = new Button("Remove Item");
		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				int myIndex = listView.getSelectionModel().getSelectedIndex();
				listView.getItems().remove(myIndex);
				tempProject.remove(tempProject.getListOfItems()[myIndex]);
				totalPrice.setText("Total Price: " + tempProject.getOverallPrice());

			}
		});
		removeButton.disableProperty().bind(Bindings.isEmpty(listView.getItems()));

		// adding create object form
		listFormGrid.add(itemName, 0, 1, 2, 1);
		listFormGrid.add(itemNameField, 0, 2, 2, 1);

		listFormGrid.add(itemPrice, 0, 4, 2, 1);
		listFormGrid.add(itemPriceField, 0, 5, 2, 1);

		listFormGrid.add(itemQty, 0, 7, 2, 1);
		listFormGrid.add(itemQtyField, 0, 8, 2, 1);

		listFormGrid.add(addButton, 0, 10);
		listFormGrid.add(removeButton, 1, 10);

		listFormGrid.add(totalPrice, 0, 12, 2, 1);

		basePlate.getChildren().add(listView);
		basePlate.getChildren().add(listFormGrid);

		return basePlate;
	}

	/**
	 * This allows you to edit an already created project.
	 * 
	 * @param tempProject,
	 * @return the project you want to change
	 */
	private StackPane editProjectView(Project tempProject) {

		// my pane
		StackPane editProjectPane = new StackPane();

		// the grid layout
		GridPane editProjectGrid = new GridPane();
		TilePane titlePane = new TilePane();
		titlePane.setHgap(20);
		titlePane.setVgap(20);
		editProjectGrid.setAlignment(Pos.TOP_LEFT);
		editProjectGrid.setVgap(10);
		editProjectGrid.setHgap(10);
		editProjectGrid.setPadding(new Insets(25));

		// my heading
		Text editProjectMessage = new Text("\n	Edit Your Project\n");
		editProjectMessage.setFont(HEADER_FONT);

		// project name setup
		Text projectName = new Text("Project Name:");
		projectName.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		TextField projectNameField = new TextField();
		if (tempProject.getName() != "") {
			projectNameField.setText(tempProject.getName());
		}
		projectNameField.setAlignment(Pos.BASELINE_LEFT);

		// project desc setup
		Text projectDesc = new Text("Project Description:");
		projectDesc.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		TextField projectDescField = new TextField();
		if (tempProject.getDesc() != "") {
			projectDescField.setText(tempProject.getDesc());
		}
		projectDescField.setAlignment(Pos.BASELINE_LEFT);

		// my submit button
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tempProject.changeName(projectNameField.getText());
				tempProject.changeDesc(projectDescField.getText());
				homeTab.setContent(getProjectView());
			}
		});

		// my back button
		Button backButton = new Button("Back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				homeTab.setContent(getProjectView());
			}

		});

		BorderPane border = new BorderPane();

		border.setLeft(editProjectGrid);
		border.setCenter(makingTheList(tempProject));
		border.setTop(titlePane);
		// border.setRight();

		titlePane.getChildren().add(editProjectMessage);

		editProjectGrid.add(projectName, 0, 1, 2, 1);
		editProjectGrid.add(projectNameField, 0, 2, 2, 1);

		editProjectGrid.add(projectDesc, 0, 3, 2, 1);
		editProjectGrid.add(projectDescField, 0, 4, 2, 1);

		editProjectGrid.add(submitButton, 0, 5);
		editProjectGrid.add(backButton, 0, 30);

		editProjectPane.setMaxHeight(SCENE_HEIGHT);
		editProjectPane.setMaxWidth(SCENE_WIDTH);
		editProjectPane.getChildren().add(border);
		StackPane.setAlignment(border, Pos.CENTER);

		return editProjectPane;

	}

}