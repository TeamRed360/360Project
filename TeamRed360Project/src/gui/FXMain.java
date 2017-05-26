package gui;  
 
import javax.swing.JOptionPane; 

import connection.SQL;
import javafx.application.Application;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Item;
import model.Project;
import model.User;
 
/** 
 * JavaFx Application. 
 * @author Taylor Riccetti
 * edited by Amanda Aldrich
 */
public class FXMain extends Application {
  
	
	/**
	 * A list of the contributors' names.
	 */
	private static final String NAMES[] = {
			"Stan Hu", "Jimmy Best", "Amanda Aldrich", "Taylor Riccetti", "Joshua Lau"
	};
	
	
	private final int SCENE_WIDTH = 900;
	private final int SCENE_HEIGHT = 600;
	
	private final Font HEADER_FONT = Font.font("Arial", FontWeight.NORMAL, 20);
	private final Border BORDER = new Border(new BorderStroke(Color.DARKGRAY,
															  BorderStrokeStyle.SOLID, 
															  CornerRadii.EMPTY, 
															  BorderWidths.DEFAULT));
	private final Tab homeTab = new Tab("Home");
	private final Tab aboutTab = new Tab("About");
	private final Tab settingsTab = new Tab("Settings");
	
	private Text welcomeText = new Text();
	
	private User currentUser = null;
	

	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Nailed It!");
     
        SQL.connect();
        StackPane root = new StackPane();
        root.getChildren().add(getTabs());
        primaryStage.setMinHeight(SCENE_HEIGHT);
        primaryStage.setMinWidth(SCENE_WIDTH);
        primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.MISTYROSE));
        primaryStage.show();
    }
    
    
    /** 
     * Private helper method that returns a TabPane full
     * of all the GUI's tabs.
     * @return TabPane
     * @author Taylor Riccetti
     */
    private TabPane getTabs() {
        TabPane tabPane = new TabPane(); 
		homeTab.setContent(getLoginPane());
		
    	homeTab.closableProperty().set(false);
    	aboutTab.closableProperty().set(false); 
    	settingsTab.closableProperty().set(false);
    	
    	// Initially all other tabs are disabled until the user logs in.
    	aboutTab.disableProperty().set(true);
    	settingsTab.disableProperty().set(true);
    	
    	aboutTab.setContent(getAboutContent());
    	settingsTab.setContent(getSettingContent());

    	tabPane.getTabs().add(homeTab);
    	tabPane.getTabs().add(aboutTab);
    	tabPane.getTabs().add(settingsTab);
    	return tabPane;
    	
    }

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

		for(int i = 1; i < NAMES.length + 1; i++) {
			Text currentName = new Text(NAMES[i - 1]);
			contributorGrid.add(currentName, 1, i);
		}
		
		
		Text credits = new Text("Credits:");
		credits.setFont(HEADER_FONT);
		
		Text lightBulb = new Text("Icons made by: http://www.flaticon.com/authors/vectors-market"
				+ "\nVectors Market http://www.flaticon.com is licensed by http://creativecommons.org/licenses/by/3.0/CC 3.0 BY");

	    
		contributorGrid.add(credits, 4, 0, 2, 1);
		contributorGrid.add(lightBulb, 4, 1);
		aboutPane.getChildren().add(contributorGrid);	

		GridPane Users = new GridPane();
		         
		
		return aboutPane;
	}
	
	private StackPane getLoginPane() {
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
		    
	    TextField email = new TextField();	
	    TextField password = new PasswordField();
	
	    continueButton.setOnAction(new EventHandler<ActionEvent>() {
	    	/**
    	 	* Attempts to submit the information.
 			* @param theEvent The event reference.
 			*/
            @Override
            public void handle(ActionEvent event) {            
            	User user = new User("", "",
            			loginEmail.getText(), loginPassword.getText());
            	int code = SQL.login(user);
            	user = SQL.getLastUser();
            	loginActionText.setFill(Color.FIREBRICK);
             	if (code == 0) {
            		int reply = JOptionPane.showConfirmDialog(null,
            			    "Email does not exist. Would you like to register instead?",
            			    "Register instead?",
            			    JOptionPane.YES_NO_OPTION);
            		if (reply == JOptionPane.YES_OPTION) {
            			email.setText(loginEmail.getText());
            			password.setText(loginPassword.getText()); 
            			loginActionText.setText("Please use the sign up tab.");
            		}
            	} else if (code == 1) { 
            	 	homeTab.setContent(getHomeContent("Welcome back, " + user.getFirstName() + " " + user.getLastName() + "!"));
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
	     
	    
	    HBox hbBtn = new HBox(10);
	    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	    hbBtn.getChildren().add(continueButton);
	    
	    // Adding components to the login tab.
	    login.add(loginMessage, 0, 0, 2, 1);
	    login.add(emailLabel, 0, 1);
	    login.add(loginEmail, 1, 1);
	    login.add(passwordLabel, 0, 2);
	    login.add(loginPassword, 1, 2);
	    login.add(hbBtn, 3, 6);
	    login.add(loginActionText, 3, 7);
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
	    HBox hbBtn2 = new HBox(10);
	    hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
	    hbBtn2.getChildren().add(signUpButton);
	    
	    signUpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				signUpActionText.setFill(Color.FIREBRICK);

				if (!password.getText().equals(confirmPass.getText())) {
					signUpActionText.setText("Passwords do not match.");
            	} else {
	            	User user = new User(firstName.getText(), lastName.getText(),
	            			email.getText(), password.getText());
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
		signUp.add(hbBtn2, 3, 6);
		signUp.add(signUpActionText, 2, 7, 2, 1);
	    signUpTab.setContent(signUp);

	    loginTab.closableProperty().set(false);
	    signUpTab.closableProperty().set(false);  
	    
	    
	    
	    
        tabPane.getTabs().add(loginTab);
       	tabPane.getTabs().add(signUpTab);
    
       	
       	tabPane.setMaxWidth(SCENE_WIDTH / 1.8);
       	tabPane.setMaxHeight(SCENE_HEIGHT / 1.8);
       	loginContainer.getChildren().add(tabPane);
       	tabPane.setBorder(BORDER);
       	StackPane.setAlignment(tabPane, Pos.CENTER);
       	return loginContainer;
	    	
	}  

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
	    BackgroundImage backgroundImage = new BackgroundImage(new Image("./fence-with-three-planks.png"), 
	    													  BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
	    													  BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage); 
        projectButton.setBackground(background);
        projectButton.setMinSize(128, 128);
        
        projectButton.setOnAction(new EventHandler<ActionEvent>() {
		    	 
	            @Override
	            public void handle(ActionEvent event) {  
	            	homeTab.setContent(addProjectView());
	            }
        
        });
	    
	    homeGrid.add(welcome, 0, 0, 2, 1); 
		homeGrid.add(projectButton, 0, 1, 3, 2); 
	    homeGrid.add(getCalculatorPane(), 4, 1, 10, 1); 
		homePane.getChildren().add(homeGrid);

		aboutTab.disableProperty().set(false);
		settingsTab.disableProperty().set(false);
		return homePane;		
	}
		

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
	    total.setPrefWidth(225);
		total.setAlignment(Pos.CENTER_RIGHT);
		
		int row = 3;
		int col = 0;
		for(int i = 0; i < 10; i++) {
			Button currButton = new Button("" + i);
			currButton.setOnAction(new EventHandler<ActionEvent>() {    	
	            @Override
	            public void handle(ActionEvent event) {    
	            	total.setText(total.getText() + ((Button) event.getSource()).getText());
	            }
            
			});
			currButton.setMinSize(50, 50);
			if(i == 0) {
				calculatorGrid.add(currButton, 1, 4);
			} else {
				calculatorGrid.add(currButton, col, row);
				col++;
				if(col == 3) {
					col = 0;
					row--;
				}					
			}		
		}
		
		Button addButton = new Button(" + ");
		addButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText(total.getText() + ((Button) event.getSource()).getText());
            }        
		});
		addButton.setMinSize(50, 50);
		Button subtractButton = new Button(" - ");
		subtractButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText(total.getText() + ((Button) event.getSource()).getText());
            }        
		});
		subtractButton.setMinSize(50, 50);
		Button multiplyButton = new Button(" * ");
		multiplyButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText(total.getText() + ((Button) event.getSource()).getText());
            }        
		});
		multiplyButton.setMinSize(50, 50);
		Button divideButton = new Button(" / ");
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
		Button openParenButton = new Button(" ( ");
		openParenButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText(total.getText() + ((Button) event.getSource()).getText());
            }        
		});
		openParenButton.setMinSize(50, 50);
		Button closeParenButton = new Button(" ) ");
		divideButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText(total.getText() + ((Button) event.getSource()).getText());
            }        
		});
		closeParenButton.setMinSize(50, 50);
		Button decimalButton = new Button(".");
		decimalButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText(total.getText() + ((Button) event.getSource()).getText());
            }        
		});
		decimalButton.setMinSize(50, 50);
		Button equalButton = new Button("=");
		decimalButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText(total.getText() + ((Button) event.getSource()).getText());
            }        
		});
		equalButton.setMinSize(50, 50);
		Button clearButton = new Button("C/E");
		clearButton.setOnAction(new EventHandler<ActionEvent>() {    	
            @Override
            public void handle(ActionEvent event) {    
            	total.setText("");
            }        
		});
		clearButton.setMinSize(50, 50);
		calculatorGrid.add(total, 0, 0, 4, 1);
	    calculatorGrid.add(addButton, 3, 4);
	    calculatorGrid.add(subtractButton, 3, 3);
	    calculatorGrid.add(divideButton, 3, 1);
	    calculatorGrid.add(multiplyButton, 3, 2);
	    calculatorGrid.add(carrotButton, 3, 5);
	    calculatorGrid.add(openParenButton, 0, 4);
	    calculatorGrid.add(closeParenButton, 2, 4);
	    calculatorGrid.add(decimalButton, 1, 5);
	    calculatorGrid.add(equalButton, 2, 5);
	    calculatorGrid.add(clearButton, 0, 5);
	    
	    
	    calculatorGrid.setBorder(BORDER);
		return calculatorGrid;
	}
 
	private StackPane addProjectView() {
		StackPane projectPane = new StackPane();
    
	    GridPane projectGrid = new GridPane();
	    projectGrid.setAlignment(Pos.TOP_LEFT);
	    projectGrid.setVgap(10);
	    projectGrid.setHgap(10);
	    projectGrid.setPadding(new Insets(25)); 
	    Text projectMessage = new Text("Your Projects");
	    projectMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

	    Button addNewButton = new Button("Add New Project...");
	    
	    addNewButton.setOnAction(new EventHandler<ActionEvent>(){
	    	@Override
            public void handle(ActionEvent event) {  
            	homeTab.setContent(addNewProjectView());

            }
	    });
	    
	    Button backButton = new Button("Back");
	    
	    backButton.setOnAction(new EventHandler<ActionEvent>() {
		    	 
	            @Override
	            public void handle(ActionEvent event) {  
	            	homeTab.setContent(getHomeContent(welcomeText.getText()));
	            }
	            
	    });
	    
	    projectGrid.add(projectMessage, 0, 0, 2, 1);
	    
	    Project testProject = new Project("Fence", "a fence");
	    projectGrid.add(getProjectPane(testProject), 0, 1, 2, 1);

	    projectGrid.add(addNewButton, 0, 5);
	    
	    projectGrid.add(backButton, 0, 10);
	    
	   	projectPane.setMaxHeight(SCENE_HEIGHT);
       	projectPane.setMaxWidth(SCENE_WIDTH);
       	projectPane.getChildren().add(projectGrid);
       	StackPane.setAlignment(projectPane, Pos.CENTER);
       	return projectPane;
	}

	/**
	 * Returns a JavaFX TilePane containing all settings content.
	 * @return TilePane
	 * @author Taylor Riccetti
	 */
	private TilePane getSettingContent() {
		TilePane settingPane = new TilePane();
		TabPane tabPane = new TabPane();
	    Tab accountTab = new Tab("Account");
	    GridPane account = new GridPane();
	    account.setAlignment(Pos.CENTER);
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
	    account.add(changePassText, 0, 4, 2, 1);
	    

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

            	changeEmailText.setFill(Color.LIMEGREEN);
            	changeEmailText.setText("E-Mail Changed!");
            }
	    });
	
	    account.add(changeEmailMessage, 3, 0, 2, 1);
	    account.add(currEmailLabel, 3, 1);
	    account.add(currEmail, 4, 1);
	    account.add(newEmailLabel, 3, 2);
	    account.add(newEmail, 4, 2);
	    account.add(changeEmailButton, 4, 3);
	    account.add(changeEmailText, 3, 3, 2, 1);
 
	    
	    
	    accountTab.closableProperty().set(false); 
	    accountTab.setContent(account); 
	    tabPane.getTabs().add(accountTab); 
	    tabPane.setMaxHeight(SCENE_HEIGHT - 50);
	    tabPane.setMaxWidth(SCENE_WIDTH - 50);
       	tabPane.setBorder(BORDER);

	    settingPane.getChildren().add(tabPane);
	    
		return settingPane;
	}
	
	private StackPane getProjectPane(Project project) {
		StackPane projectPane = new StackPane();

		
		
		return projectPane;		
	}
	
	/**
	 * Creates the Create a Project view.
	 * @author Amanda Aldrich
	 * @return addProjectPane which is the pane we are building on.
	 */
	private GridPane addNewProjectView() {
		
		//pproject gets created to be customized by user
		Project tempProject = new Project(" ", " ");
		
		//my pane
		StackPane addProjectPane = new StackPane();
    
		//the grid layout
	    GridPane addProjectGrid = new GridPane();
	    addProjectGrid.setAlignment(Pos.TOP_LEFT);
	    addProjectGrid.setVgap(10);
	    addProjectGrid.setHgap(10);
	    addProjectGrid.setPadding(new Insets(25)); 
	    
	    //my heading
	    Text addProjectMessage = new Text("Add new Project");
	    addProjectMessage.setFont(HEADER_FONT);

	    //project name setup
	    Text projectName = new Text("Project Name:");
	    projectName.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
	    TextField projectNameField = new TextField();
	    projectNameField.setAlignment(Pos.BASELINE_LEFT);
	    
	    //project desc setup
	    Text projectDesc = new Text("Project Description:");
	    projectDesc.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
	    TextField projectDescField = new TextField();
	    projectDescField.setAlignment(Pos.BASELINE_LEFT);
	    
	    itemPane(tempProject);
	    
	    //my submit button
	    Button submitButton = new Button("Submit");
	    submitButton.setOnAction(new EventHandler<ActionEvent>(){
	    	@Override
            public void handle(ActionEvent event) {  
            	tempProject.changeName(projectNameField.getText());
            	tempProject.changeDesc(projectDescField.getText());
            	homeTab.setContent(addProjectView());
            }
	    });
	    
	    //my back button
	    Button backButton = new Button("Back");
	    backButton.setOnAction(new EventHandler<ActionEvent>() {
		    	 
	            @Override
	            public void handle(ActionEvent event) {  
	            	homeTab.setContent(addProjectView());
	            }
	            
	    }); 

	    BorderPane border = new BorderPane();
	    
	    border.setLeft(addProjectGrid);
	    border.setRight(itemPane(tempProject));
	    border.setCenter(listPane(tempProject));
	    
	    addProjectGrid.add(addProjectMessage, 0, 0, 2, 1); 
	    
	    addProjectGrid.add(projectName, 0, 1, 2, 1);
	    addProjectGrid.add(projectNameField, 0, 2, 2, 1);
	    
	    addProjectGrid.add(projectDesc, 0, 3, 2, 1);
	    addProjectGrid.add(projectDescField, 0, 4, 2, 1);
	    
	    addProjectGrid.add(submitButton, 0, 5); 
	    addProjectGrid.add(backButton, 0, 10); 
	    
	    addProjectPane.setMaxHeight(SCENE_HEIGHT);
       	addProjectPane.setMaxWidth(SCENE_WIDTH);
       	addProjectPane.getChildren().add(border);
       	StackPane.setAlignment(border, Pos.CENTER);
		return addProjectGrid;
	    
	}   

	/**
	 * This should house the items
	 * @author Amanda Aldrich
	 * @param myProject
	 * @return itemsPane, which is the pane we are messing with
	 */
	private GridPane itemPane(Project myProject){
		GridPane itemsPane = new GridPane();
		itemsPane.setAlignment(Pos.TOP_RIGHT);
	    itemsPane.setVgap(10);
	    itemsPane.setHgap(10);
	    itemsPane.setPadding(new Insets(25)); 
		
		Text itemName = new Text("Item Name:");
	    itemName.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
	    TextField itemNameField = new TextField("Name");
	    itemNameField.setAlignment(Pos.BASELINE_LEFT);
		
	    Text itemQty = new Text("Item Quantity:");
	    itemQty.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
	    TextField itemQtyField = new TextField("0");
	    itemQtyField.setAlignment(Pos.BASELINE_LEFT);
	    
	    Text itemPrice = new Text("Item Price:");
	    itemPrice.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
	    TextField itemPriceField = new TextField("0.0");
	    itemPriceField.setAlignment(Pos.BASELINE_LEFT);
	    
		Button addButton = new Button("Add Item");
		addButton.setOnAction(new EventHandler<ActionEvent>(){
	    	@Override
            public void handle(ActionEvent event) {
	    		String theName = itemNameField.getText();
	    		double thePrice = Double.parseDouble(itemPriceField.getText());
	    		int theQty = Integer.parseInt(itemQtyField.getText());
	    		
	    		Item tempItem = new Item(theName, thePrice, theQty);
            	myProject.add(tempItem);
            	
            	itemNameField.clear();
            	itemPriceField.clear();
            	itemQtyField.clear();

            }
	    });
				
		itemsPane.add(itemName, 1, 1, 2, 1);
		itemsPane.add(itemNameField, 1, 2, 2, 1);
		
		itemsPane.add(itemQty, 1, 3, 2, 1);
		itemsPane.add(itemQtyField, 1, 4, 2, 1);
		
		itemsPane.add(itemPrice, 1, 5, 2, 1);
		itemsPane.add(itemPriceField, 1, 6, 2, 1);
		
		itemsPane.add(addButton, 1, 9);
				
		return itemsPane;
		
	}
	
	/**
	 * creates the list view.
	 * @author Amanda Aldrich
	 * @param myProject
	 * @return list, the list
	 */ 
	//this whole thing is janked up....but I gotta commit beofre I leave
	private TableView listPane(Project myProject){
		
		Item tempI = new Item("fake", 0.0, 0);
	 
		
	    TableView table = new TableView();
		TableColumn itemName = new TableColumn("Item Name");
        TableColumn price = new TableColumn("Price");
        TableColumn quantity = new TableColumn("Quantity");
        TableColumn totalPrice = new TableColumn("Total Price");
	       

        table.setEditable(true);
        table.getColumns().addAll(itemName, price, quantity, totalPrice);

         
	        
		
		
		return table;
		
	}
}