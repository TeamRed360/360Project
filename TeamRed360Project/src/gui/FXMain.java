package gui;  

 
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import connection.SQL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
/** 
 * JavaFx Application. 
 * @author Taylor Riccetti
 *
 */
public class FXMain extends Application {
  
	private final int SCENE_WIDTH = 800;
	private final int SCENE_HEIGHT = 600;
 
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Nailed It!");
                
        StackPane root = new StackPane();
        root.getChildren().add(getTabs());
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
    	Tab homeTab = new Tab("Home");
		Tab aboutTab = new Tab("About");
		homeTab.setContent(getUserInputPane());
		
    	homeTab.closableProperty().set(false);
    	aboutTab.closableProperty().set(false); 
    	
    	// Initially all other tabs are disabled until the user logs in.
    	aboutTab.disableProperty().set(true);
    	
    	
    	

    	tabPane.getTabs().add(homeTab);
    	tabPane.getTabs().add(aboutTab);
    	return tabPane;
    	
    }

	private StackPane getUserInputPane() {
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
            		loginActionText.setText("Welcome back, " + user.getFirstName() + " " + user.getLastName() + "!");
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
	    TextField confirmPass = new TextField();
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
            			signUpActionText.setText("Welcome, " + firstName.getText() + " " + lastName.getText() + "!");
	            	} else if (code == 1 | code == 2) {
	            		signUpActionText.setText("This email is already registered.");
	            	} else {
	            		signUpActionText.setText("Error - could not access the login server.");
	            	}
            	}
            }
	    	
	    });
	 
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
		signUp.add(signUpActionText, 3, 7);
	    signUpTab.setContent(signUp);

	    loginTab.closableProperty().set(false);
	    signUpTab.closableProperty().set(false);  
	    
	    
	    
	    
        tabPane.getTabs().add(loginTab);
       	tabPane.getTabs().add(signUpTab);
       	tabPane.setMaxWidth(SCENE_WIDTH / 2);
       	tabPane.setMaxHeight(SCENE_HEIGHT / 2);
       	loginContainer.getChildren().add(tabPane);
       	tabPane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
       	StackPane.setAlignment(tabPane, Pos.CENTER);
       	return loginContainer;
	    	
	} 
    
}