package gui;  
 
import javax.swing.JOptionPane;

import connection.SQL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Project;
import model.Tag;
import model.User;
 
/** 
 * JavaFx Application. 
 * @author Taylor Riccetti
 *
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
	    contributors.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

		contributorGrid.add(contributors, 0, 0, 2, 1);

		for(int i = 1; i < NAMES.length + 1; i++) {
			Text currentName = new Text(NAMES[i - 1]);
			contributorGrid.add(currentName, 1, i);
		}
		
		
		Text credits = new Text("Credits:");
		credits.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		
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
	    loginMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
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
	    signUpMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
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
       	tabPane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
       	StackPane.setAlignment(tabPane, Pos.CENTER);
       	return loginContainer;
	    	
	}  

	private StackPane getHomeContent(String welcomeText) {
		StackPane homePane = new StackPane();
			
		GridPane homeGrid = new GridPane();
		homeGrid.setAlignment(Pos.TOP_LEFT);
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
		homeGrid.add(projectButton, 0, 1, 3, 1);
	    
	    
	    
		homePane.getChildren().add(homeGrid);

		aboutTab.disableProperty().set(false);
		settingsTab.disableProperty().set(false);
		return homePane;		
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

	    Button backButton = new Button("Back");
	    
	    backButton.setOnAction(new EventHandler<ActionEvent>() {
		    	 
	            @Override
	            public void handle(ActionEvent event) {  
	            	homeTab.setContent(getHomeContent(welcomeText.getText()));
	            }
	            
	    });
	    
	    projectGrid.add(projectMessage, 0, 0, 2, 1);
	    
	    Project testProject = new Project("Fence");
	    projectGrid.add(getProjectPane(testProject), 0, 1, 2, 1);
	    
	    projectGrid.add(backButton, 0, 10);
	    
	   	projectPane.setMaxHeight(SCENE_HEIGHT);
       	projectPane.setMaxWidth(SCENE_WIDTH);
       	projectPane.getChildren().add(projectGrid);
       	StackPane.setAlignment(projectPane, Pos.CENTER);
       	return projectPane;
	}

	private StackPane getSettingContent() {
		StackPane settingPane = new StackPane();
		
		TabPane tabPane = new TabPane();
	    Tab accountTab = new Tab("Account");
	    GridPane account = new GridPane();
	    account.setAlignment(Pos.TOP_CENTER);
	    account.setVgap(10);
	    account.setHgap(10);
	    account.setPadding(new Insets(25)); 
	    Text changePassMessage = new Text("Change Password");
	    changePassMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
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
	    changeEmailMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
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

	    Tab preferenceTab = new Tab("Preferences");
	    GridPane preference = new GridPane();
	    preference.setAlignment(Pos.TOP_CENTER);
	    preference.setVgap(10);
	    preference.setHgap(10);
	    preference.setPadding(new Insets(25)); 
	    Text typeOfProject = new Text("Type of Project");
	    typeOfProject.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
	    
	    VBox checkBoxContainer = new VBox(8);
 
	    for(Tag type : Tag.values()) {
	    	// Makes a check box from the enum value.
	    	CheckBox checkBox = new CheckBox(type.type());
	    	checkBox.setId(type.type());
	    	checkBox.setOnAction(new EventHandler<ActionEvent>() {
		    	 
	            @Override
	            public void handle(ActionEvent event) {       
	            	// gets id
	            	System.out.println(((CheckBox) event.getSource()).getId());
	            	// is selected or not
	            	System.out.println(((CheckBox) event.getSource()).isSelected());
	            }
	            
	    	});
	    	checkBoxContainer.getChildren().add(checkBox);
	    }
	    
	    
	    preference.add(typeOfProject, 0, 0, 2, 1);
	    preference.add(checkBoxContainer, 0, 1);
	    
	    
	    accountTab.closableProperty().set(false);
	    preferenceTab.closableProperty().set(false);
	    accountTab.setContent(account);
	    preferenceTab.setContent(preference);
	    tabPane.getTabs().add(accountTab);
	    tabPane.getTabs().add(preferenceTab);
	    tabPane.setMaxHeight(SCENE_HEIGHT - 50);
	    tabPane.setMaxWidth(SCENE_WIDTH - 50);
       	tabPane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

	    settingPane.getChildren().add(tabPane);
	    
		return settingPane;
	}
	
	private StackPane getProjectPane(Project project) {
		StackPane projectPane = new StackPane();

		
		
		return projectPane;		
	}
	
}